package com.runetide.services.internal.time.client;

import com.runetide.common.*;
import com.runetide.common.clients.StatelessClient;
import com.runetide.common.services.servicediscovery.ServiceRegistry;
import com.runetide.common.services.topics.TopicListener;
import com.runetide.common.services.topics.TopicListenerHandle;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.time.common.Time;
import com.runetide.services.internal.time.common.TimeTickMessage;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

@Singleton
public class TimeClient extends StatelessClient {
    @Inject
    public TimeClient(final ServiceRegistry serviceRegistry, final TopicManager topicManager) {
        super(serviceRegistry, topicManager, Constants.TIME_SERVICE_NAME, "time");
    }

    public Time getTime() {
        return getTarget().request(ACCEPT).get(Time.class);
    }

    public Time setTime(final Time time) {
        return getTarget().request(ACCEPT).post(Entity.entity(time, MediaType.APPLICATION_JSON), Time.class);
    }

    public TopicListenerHandle<TimeTickMessage> listen(final TopicListener<TimeTickMessage> listener) {
        return topicManager.addListener(Constants.TIME_TOPIC, listener, TimeTickMessage.class);
    }
}
