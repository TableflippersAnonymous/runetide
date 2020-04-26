package com.runetide.services.internal.character.server;

import com.datastax.oss.driver.api.core.CqlSession;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.runetide.services.internal.character.server.dao.CharacterDao;

public class CharacterGuiceModule implements Module {
    @Override
    public void configure(Binder binder) {

    }

    @Provides @Singleton
    public CharacterMapper getCharacterMapper(final CqlSession cqlSession) {
        return new CharacterMapperBuilder(cqlSession).build();
    }

    @Provides @Singleton
    public CharacterDao getCharacterDao(final CharacterMapper characterMapper) {
        return characterMapper.dao();
    }
}
