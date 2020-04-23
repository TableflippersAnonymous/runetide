package com.runetide.services.internal.character.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.common.dto.EntityRef;
import com.runetide.common.services.cql.UUIDRefCodec;
import com.runetide.services.internal.character.client.CharactersClient;
import com.runetide.services.internal.resourcepool.client.ResourcePoolsClient;
import com.runetide.services.internal.xp.client.XPClient;

import java.util.Arrays;
import java.util.List;

public class CharacterService extends Service<CharacterConfiguration> {
    public static void main(final String[] args) throws Exception {
        new CharacterService().run(args);
    }

    private CharacterService() {
        super(Constants.CHARACTER_SERVICE_NAME);
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(ResourcePoolsClient.getCqlTypeCodecs());
        list.addAll(XPClient.getCqlTypeCodecs());
        list.addAll(CharactersClient.getCqlTypeCodecs());
        list.addAll(Arrays.asList(
                new UUIDRefCodec<>(EntityRef.class, EntityRef::new),
                new UUIDRefCodec<>(AccountRef.class, AccountRef::new),
                new UUIDRefCodec<>(MultiverseRef.class, MultiverseRef::new)
        ));
        return list;
    }
}
