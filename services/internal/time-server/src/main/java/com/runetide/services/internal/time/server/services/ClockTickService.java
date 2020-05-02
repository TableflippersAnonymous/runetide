package com.runetide.services.internal.time.server.services;

import com.runetide.common.Constants;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.time.common.TimeTickMessage;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class ClockTickService implements LeaderSelectorListener {
    private final ScheduledExecutorService executorService;
    private final TopicManager topicManager;
    private final LeaderSelector leaderSelector;
    private final RAtomicLong clockTime;
    private ScheduledFuture<?> tickTask;

    @Inject
    public ClockTickService(final ScheduledExecutorService executorService, final RedissonClient redissonClient,
                            final CuratorFramework curatorFramework, final TopicManager topicManager) {
        this.executorService = executorService;
        this.topicManager = topicManager;
        clockTime = redissonClient.getAtomicLong(Constants.TIME_REDIS);
        leaderSelector = new LeaderSelector(curatorFramework, Constants.ZK_LEADERS + "time",
                executorService, this);
    }

    public void start() {
        leaderSelector.start();
    }

    private void tick() {
        final long currentTick = clockTime.addAndGet(1);
        topicManager.publish(Constants.TIME_TOPIC, new TimeTickMessage(currentTick));
    }

    @Override
    public void takeLeadership(final CuratorFramework client) throws Exception {
        tickTask = executorService.scheduleAtFixedRate(this::tick, 0, Constants.TIME_TICK_RATE_MS, TimeUnit.MILLISECONDS);
        try {
            tickTask.get();
        } catch (final Exception e) {
            tickTask.cancel(true);
        }
        tickTask = null;
    }

    @Override
    public void stateChanged(final CuratorFramework client, final ConnectionState newState) {
        if(tickTask != null)
            tickTask.cancel(true);
    }

    public long getCurrentTick() {
        return clockTime.get();
    }

    public void setCurrentTick(final long newTime) {
        clockTime.set(newTime);
    }
}
