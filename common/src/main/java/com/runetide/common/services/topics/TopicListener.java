package com.runetide.common.services.topics;

import java.util.EventListener;

public interface TopicListener<T extends TopicMessage> extends EventListener {
    void onMessage(T message);
    void onClose();
}
