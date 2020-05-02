package com.runetide.common.services.topics;

public interface TopicManager {
    void publish(String topic, TopicMessage topicMessage);
    <T extends TopicMessage> TopicListenerHandle<T> addListener(String topic, TopicListener<T> topicListener, Class<T> clazz);
    void removeListener(TopicListenerHandle<?> listenerHandle);
}
