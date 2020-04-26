package com.runetide.services.internal.character.server.dto;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.runetide.services.internal.account.common.AccountRef;
import com.runetide.services.internal.character.common.CharacterRef;
import com.runetide.services.internal.character.common.ClassType;
import com.runetide.services.internal.character.common.RaceType;
import com.runetide.services.internal.multiverse.common.MultiverseRef;

@Entity
@CqlName("characters_by_id")
public class CharacterByAccount {
    @PartitionKey
    @CqlName("account_id")
    private AccountRef accountId;
    @ClusteringColumn
    @CqlName("id")
    private CharacterRef id;
    @CqlName("multiverse_id")
    private MultiverseRef multiverseId;
    @CqlName("name")
    private String name;
    @CqlName("race")
    private RaceType race;
    @CqlName("class")
    private ClassType classType;

    public CharacterByAccount() {
    }

    public CharacterByAccount(AccountRef accountId, CharacterRef id, MultiverseRef multiverseId, String name,
                              RaceType race, ClassType classType) {
        this.accountId = accountId;
        this.id = id;
        this.multiverseId = multiverseId;
        this.name = name;
        this.race = race;
        this.classType = classType;
    }

    public AccountRef getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountRef accountId) {
        this.accountId = accountId;
    }

    public CharacterRef getId() {
        return id;
    }

    public void setId(CharacterRef id) {
        this.id = id;
    }

    public MultiverseRef getMultiverseId() {
        return multiverseId;
    }

    public void setMultiverseId(MultiverseRef multiverseId) {
        this.multiverseId = multiverseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RaceType getRace() {
        return race;
    }

    public void setRace(RaceType race) {
        this.race = race;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }
}
