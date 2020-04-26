package com.runetide.services.internal.character.server.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.BaseDao;
import com.runetide.services.internal.account.common.AccountRef;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.server.dto.Character;
import com.runetide.services.internal.character.server.dto.CharacterAttributeAssignment;
import com.runetide.services.internal.character.server.dto.CharacterByAccount;

import java.util.UUID;

@Dao
public interface CharacterDao extends BaseDao<Character> {
    @Select
    Character getCharacter(final CharacterRef characterRef);

    @Select
    PagingIterable<CharacterByAccount> getCharactersByAccount(final AccountRef accountRef);

    @Select
    PagingIterable<CharacterAttributeAssignment> getCharacterAttributeAssignments(final CharacterRef characterRef);
}
