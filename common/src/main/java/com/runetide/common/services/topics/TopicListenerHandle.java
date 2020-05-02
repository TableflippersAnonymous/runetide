package com.runetide.common.services.topics;

import java.io.Closeable;
import java.lang.ref.Cleaner;

public class TopicListenerHandle<T extends TopicMessage> implements Closeable {
    private static final Cleaner CLEANER = Cleaner.create();

    private static class CleanAction<T extends TopicMessage> implements Runnable {
        final TopicManagerImpl topicManager;
        final TopicListener<T> topicListener;
        final String topic;
        final int listenerId;
        volatile boolean closed = false;

        public CleanAction(final TopicManagerImpl topicManager, final TopicListener<T> topicListener,
                           final String topic, final int listenerId) {
            this.topicManager = topicManager;
            this.topicListener = topicListener;
            this.topic = topic;
            this.listenerId = listenerId;
        }

        @Override
        public void run() {
            topicManager.removeListener(topic, listenerId);
            topicListener.onClose();
            closed = true;
        }
    }

    private final CleanAction<T> cleanAction;
    private final Cleaner.Cleanable cleanable;

    public TopicListenerHandle(final TopicManagerImpl topicManager, final TopicListener<T> topicListener,
                               final String topic, final int listenerId) {
        this.cleanAction = new CleanAction<>(topicManager, topicListener, topic, listenerId);
        this.cleanable = CLEANER.register(this, cleanAction);
    }

    public TopicManager getTopicManager() {
        return cleanAction.topicManager;
    }

    public TopicListener<T> getTopicListener() {
        return cleanAction.topicListener;
    }

    public String getTopic() {
        return cleanAction.topic;
    }

    public int getListenerId() {
        return cleanAction.listenerId;
    }

    public boolean isClosed() {
        return cleanAction.closed;
    }

    @Override
    public void close() {
        if(cleanAction.closed)
            return;
        cleanable.clean();
    }
}
