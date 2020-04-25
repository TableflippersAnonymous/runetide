package com.runetide.services.internal.character.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.ServiceRegistry;
import com.runetide.common.TopicManager;
import com.runetide.common.UniqueLoadingClient;
import com.runetide.common.services.cql.EnumIndexedCodec;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.character.common.*;
import org.apache.curator.framework.CuratorFramework;

import java.util.Arrays;
import java.util.List;

@Singleton
public class CharactersClient extends UniqueLoadingClient<CharacterRef> {
    @Inject
    public CharactersClient(ServiceRegistry serviceRegistry, TopicManager topicManager,
                            CuratorFramework curatorFramework) {
        super(serviceRegistry, topicManager, Constants.CHARACTER_LOADING_NAMESPACE, "characters",
                curatorFramework);
    }

    public static List<TypeCodec<?>> getCqlTypeCodecs() {
        return Arrays.asList(
                new EnumIndexedCodec<>(RaceType.class),
                new EnumIndexedCodec<>(Size.class),
                new EnumIndexedCodec<>(Language.class),
                new EnumIndexedCodec<>(Attribute.class),
                new EnumIndexedCodec<>(ClassType.class),
                new EnumIndexedCodec<>(EquipmentSlot.class),
                new EnumIndexedCodec<>(SpecialAbilityType.class),
                new UUIDRefCodec<>(CharacterRef.class, CharacterRef::new)
        );
    }
}
