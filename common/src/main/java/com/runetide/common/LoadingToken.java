package com.runetide.common;

import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.nodes.GroupMember;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.io.Closeable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LoadingToken<K> implements Closeable {
    private final GroupMember groupMember;
    private final K key;
    private final UniqueLoadingClient<K> client;
    private final RMap<String, String> serviceStates;
    private final Condition updateCondition = new ReentrantLock().newCondition();
    private TopicListenerHandle<StateTransitionMessage> topicListenerHandle;

    public LoadingToken(final CuratorFramework curatorFramework, final String objectName, final K key,
                        final UniqueLoadingClient<K> client, final RedissonClient redissonClient) {
        groupMember = new GroupMember(curatorFramework,
                Constants.ZK_SVC_INTEREST + objectName + "/" + key.toString(), UUID.randomUUID().toString());
        this.key = key;
        this.client = client;
        this.serviceStates = redissonClient.getMap("services:" + objectName + ":state", StringCodec.INSTANCE);
    }

    public void start() {
        groupMember.start();
        topicListenerHandle = client.listenStateTransition(key, new TopicListener<StateTransitionMessage>() {
            @Override
            public void onMessage(StateTransitionMessage message) {
                updateCondition.signalAll();
            }

            @Override
            public void onClose() {

            }
        });
    }

    public void awaitServiceState(final ServiceState desiredServiceState) {
        while(getCurrentState() != desiredServiceState)
            //noinspection UnstableApiUsage
            Uninterruptibles.awaitUninterruptibly(updateCondition, 1, TimeUnit.SECONDS);
    }

    public ServiceState getCurrentState() {
        return Optional.ofNullable(serviceStates.get(key.toString()))
                .map(ServiceState::valueOf).orElse(null);
    }

    @Override
    public void close() {
        groupMember.close();
        if(topicListenerHandle != null)
            topicListenerHandle.close();
    }

    public K getKey() {
        return key;
    }
}
