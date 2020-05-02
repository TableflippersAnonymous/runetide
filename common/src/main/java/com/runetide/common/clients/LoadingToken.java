package com.runetide.common.clients;

import com.google.common.util.concurrent.Uninterruptibles;
import com.runetide.common.*;
import com.runetide.common.loading.ServiceState;
import com.runetide.common.loading.StateTransitionMessage;
import com.runetide.common.services.topics.TopicListener;
import com.runetide.common.services.topics.TopicListenerHandle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.nodes.GroupMember;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.lang.invoke.MethodHandles;
import java.lang.ref.Cleaner;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LoadingToken<K> implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Cleaner CLEANER = Cleaner.create();

    private static class CleanAction<K> implements Runnable {
        private final String objectName;
        final K key;
        final GroupMember groupMember;
        TopicListenerHandle<StateTransitionMessage> topicListenerHandle;
        volatile boolean opened = false;
        volatile boolean closed = false;

        public CleanAction(String objectName, K key, GroupMember groupMember) {
            this.objectName = objectName;
            this.key = key;
            this.groupMember = groupMember;
        }

        @Override
        public void run() {
            closed = true;
            try {
                groupMember.close();
            } catch(Exception e) {
                LOG.error("Caught error closing LoadingToken({}, {}) groupMember", objectName, key, e);
            }
            if(topicListenerHandle != null)
                try {
                    topicListenerHandle.close();
                } catch(Exception e) {
                    LOG.error("Caught error closing LoadingToken({}, {}) topicListener", objectName, key, e);
                }
        }
    }

    private final CleanAction<K> cleanAction;
    private final Cleaner.Cleanable cleanable;
    private final UniqueLoadingClient<K> client;
    private final RMap<String, String> serviceStates;
    private final Condition updateCondition = new ReentrantLock().newCondition();

    public LoadingToken(final CuratorFramework curatorFramework, final String objectName, final K key,
                        final UniqueLoadingClient<K> client, final RedissonClient redissonClient) {
        cleanAction = new CleanAction<>(objectName, key, new GroupMember(curatorFramework,
                Constants.ZK_SVC_INTEREST + objectName + "/" + key.toString(), UUID.randomUUID().toString()));
        this.client = client;
        this.serviceStates = redissonClient.getMap("services:" + objectName + ":state", StringCodec.INSTANCE);
        cleanable = CLEANER.register(this, cleanAction);
    }

    public void start() {
        if(cleanAction.opened)
            throw new IllegalStateException("Already started");
        cleanAction.groupMember.start();
        cleanAction.topicListenerHandle = client.listenStateTransition(cleanAction.key, new TopicListener<StateTransitionMessage>() {
            @Override
            public void onMessage(StateTransitionMessage message) {
                updateCondition.signalAll();
            }

            @Override
            public void onClose() {

            }
        });
        cleanAction.opened = true;
    }

    public void awaitServiceState(final ServiceState desiredServiceState) {
        if(!cleanAction.opened || cleanAction.closed)
            throw new IllegalStateException("Not started");
        while(getCurrentState() != desiredServiceState)
            //noinspection UnstableApiUsage
            Uninterruptibles.awaitUninterruptibly(updateCondition, 1, TimeUnit.SECONDS);
    }

    public ServiceState getCurrentState() {
        return Optional.ofNullable(serviceStates.get(cleanAction.key.toString()))
                .map(ServiceState::valueOf).orElse(null);
    }

    @Override
    public synchronized void close() {
        if(!cleanAction.opened || cleanAction.closed)
            return;
        cleanable.clean();
    }

    public K getKey() {
        if(!cleanAction.opened || cleanAction.closed)
            throw new IllegalStateException("Not started");
        return cleanAction.key;
    }

}
