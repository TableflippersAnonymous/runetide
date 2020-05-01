package com.runetide.services.internal.character.server.domain;

import com.runetide.common.LoadingToken;
import com.runetide.common.TopicManager;
import com.runetide.services.internal.character.server.dao.CharacterDao;
import com.runetide.services.internal.character.server.dto.Character;
import com.runetide.services.internal.character.server.dto.CharacterAttributeAssignment;
import com.runetide.services.internal.entity.client.EntitiesClient;
import com.runetide.services.internal.entity.common.dto.EntityRef;
import com.runetide.services.internal.resourcepool.client.ResourcePoolsClient;
import com.runetide.services.internal.resourcepool.common.ResourcePoolRef;
import com.runetide.services.internal.resourcepool.common.ResourceType;
import com.runetide.services.internal.xp.client.XPClient;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LoadedCharacter {
    private final Character character;
    private final CharacterDao dao;
    private final TopicManager topicManager;
    private final EntitiesClient entitiesClient;
    private final ResourcePoolsClient resourcePoolsClient;
    private final XPClient xpClient;

    private final LoadingToken<EntityRef> entity;
    private final LoadingToken<XPRef> xp;
    private final Map<ResourceType, LoadingToken<ResourcePoolRef>> resources;
    private final Map<XPType, LoadingToken<XPRef>> skills;

    public LoadedCharacter(Character character, CharacterDao dao, TopicManager topicManager,
                           EntitiesClient entitiesClient, ResourcePoolsClient resourcePoolsClient, XPClient xpClient) {
        this.character = character;
        this.dao = dao;
        this.topicManager = topicManager;
        this.entitiesClient = entitiesClient;
        this.resourcePoolsClient = resourcePoolsClient;
        this.xpClient = xpClient;

        entity = entitiesClient.requestLoad(character.getEntityId());
        xp = xpClient.requestLoad(character.getXp());

        resources = character.getResources().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> resourcePoolsClient.requestLoad(e.getValue())));
        skills = character.getSkills().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> xpClient.requestLoad(e.getValue())));
    }

    public void save() {
        dao.save(character);
    }

    public void unload() {
        save();
        reset();
    }

    public void reset() {
        entity.close();
        xp.close();
        resources.values().forEach(LoadingToken::close);
        resources.clear();
        skills.values().forEach(LoadingToken::close);
        skills.clear();
    }

    public com.runetide.services.internal.character.common.Character toClient() {
        return new com.runetide.services.internal.character.common.Character(
                character.getId(),
                character.getEntityId(),
                character.getAccountId(),
                character.getMultiverseId(),
                character.getName(),
                character.getRace(),
                character.getClassType(),
                character.getSpawnInstance(),
                xpClient.get(xp),
                resources.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> resourcePoolsClient.get(e.getValue()))),
                character.getEquipment(),
                skills.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> xpClient.get(e.getValue()))),
                character.getSpecialAbilities(),
                StreamSupport.stream(dao.getCharacterAttributeAssignments(character.getId()).spliterator(), false)
                        .collect(Collectors.groupingBy(CharacterAttributeAssignment::getAttribute, Collectors.counting()))
        );
    }
}
