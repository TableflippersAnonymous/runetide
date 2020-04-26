package com.runetide.services.internal.account.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.ServiceRegistry;
import com.runetide.common.StatelessClient;
import com.runetide.common.TopicManager;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.account.common.AccountRef;

import java.util.Arrays;
import java.util.List;

@Singleton
public class AccountsClient extends StatelessClient {
    @Inject
    public AccountsClient(ServiceRegistry serviceRegistry, TopicManager topicManager) {
        super(serviceRegistry, topicManager, Constants.ACCOUNT_LOADING_NAMESPACE, "accounts");
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new UUIDRefCodec<>(AccountRef.class, AccountRef::new)
        );
    }
}
