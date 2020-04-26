package com.runetide.services.internal.character.server;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.runetide.common.Constants;
import com.runetide.common.Service;
import com.runetide.services.internal.account.client.AccountsClient;
import com.runetide.services.internal.character.client.CharactersClient;
import com.runetide.services.internal.entity.client.EntitiesClient;
import com.runetide.services.internal.multiverse.client.MultiversesClient;
import com.runetide.services.internal.resourcepool.client.ResourcePoolsClient;
import com.runetide.services.internal.xp.client.XPClient;

import java.util.List;

public class CharacterService extends Service<CharacterConfiguration> {
    public static void main(final String[] args) throws Exception {
        new CharacterService().run(args);
    }

    private CharacterService() {
        super(Constants.CHARACTER_SERVICE_NAME);
    }

    @Override
    protected GuiceBundle.Builder<CharacterConfiguration> addGuiceModules(GuiceBundle.Builder<CharacterConfiguration> builder) {
        return super.addGuiceModules(builder)
                .addModule(new CharacterGuiceModule());
    }

    @Override
    protected List<TypeCodec<?>> getCqlTypeCodecs() {
        final List<TypeCodec<?>> list = super.getCqlTypeCodecs();
        list.addAll(AccountsClient.getCqlTypeCodecs());
        list.addAll(CharactersClient.getCqlTypeCodecs());
        list.addAll(EntitiesClient.getCqlTypeCodecs());
        list.addAll(MultiversesClient.getCqlTypeCodecs());
        list.addAll(ResourcePoolsClient.getCqlTypeCodecs());
        list.addAll(XPClient.getCqlTypeCodecs());
        return list;
    }
}
