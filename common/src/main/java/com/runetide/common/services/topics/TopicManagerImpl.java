package com.runetide.common.services.topics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Singleton
public class TopicManagerImpl implements TopicManager {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TOPIC_PREFIX = "topic:";

    private final RedissonClient redissonClient;
    private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    @Inject
    public TopicManagerImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void publish(final String topic, final TopicMessage topicMessage) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic(TOPIC_PREFIX + topic, ByteArrayCodec.INSTANCE);
        try {
            rTopic.publish(mapper.writeValueAsBytes(topicMessage));
        } catch (final JsonProcessingException e) {
            LOG.error("Got exception sending topic message on topic {}", topic, e);
        }
    }

    @Override
    public <T extends TopicMessage> TopicListenerHandle<T> addListener(final String topic,
                                                                       final TopicListener<T> topicListener,
                                                                       final Class<T> clazz) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic(TOPIC_PREFIX + topic, ByteArrayCodec.INSTANCE);
        return new TopicListenerHandle<>(this, topicListener, topic, rTopic.addListener((channel, msg) -> {
            try {
                topicListener.onMessage(mapper.readValue(msg, clazz));
            } catch (final IOException e) {
                LOG.error("Got exception parsing topic message on topic {}", topic, e);
            }
        }));
    }

    @Override
    public void removeListener(final TopicListenerHandle<?> listenerHandle) {
        listenerHandle.close();
    }

    void removeListener(final String topic, final int listenerId) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic(TOPIC_PREFIX + topic, ByteArrayCodec.INSTANCE);
        rTopic.removeListener(listenerId);
    }
}
