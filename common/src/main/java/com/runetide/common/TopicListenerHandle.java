package com.runetide.common;

import java.io.Closeable;

public class TopicListenerHandle<T extends TopicMessage> implements Closeable {
    private final TopicManager topicManager;
    private final TopicListener<T> topicListener;
    private final String topic;
    private final int listenerId;
    private boolean closed;

    public TopicListenerHandle(final TopicManager topicManager, final TopicListener<T> topicListener,
                               final String topic, final int listenerId) {
        this.topicManager = topicManager;
        this.topicListener = topicListener;
        this.topic = topic;
        this.listenerId = listenerId;
    }

    public TopicManager getTopicManager() {
        return topicManager;
    }

    public TopicListener<T> getTopicListener() {
        return topicListener;
    }

    public String getTopic() {
        return topic;
    }

    public int getListenerId() {
        return listenerId;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if(closed)
            return;
        topicManager.removeListener(this);
        topicListener.onClose();
        closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
