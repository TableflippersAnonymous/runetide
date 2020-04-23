package com.runetide.services.internal.character.client;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.ServiceRegistry;
import com.runetide.common.TopicManager;
import com.runetide.common.UniqueLoadingClient;
import com.runetide.common.services.cql.EnumOrdinalCodec;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.common.EquipmentSlot;
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
                new EnumOrdinalCodec<>(RaceType.class),
                new EnumOrdinalCodec<>(ClassType.class),
                new EnumOrdinalCodec<>(EquipmentSlot.class),
                new EnumOrdinalCodec<>(SpecialAbilityType.class),
                new UUIDRefCodec<>(CharacterRef.class, CharacterRef::new)
        );
    }
}
