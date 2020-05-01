package com.runetide.services.internal.character.server.resources;

import com.google.inject.Inject;
import com.runetide.services.internal.character.common.Character;
import com.runetide.services.internal.character.server.domain.LoadedCharacter;
import com.runetide.services.internal.character.server.services.CharacterManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/characters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CharactersResource {
    private final CharacterManager manager;

    @Inject
    public CharactersResource(CharacterManager manager) {
        this.manager = manager;
    }

    @GET
    public List<Character> getLoadedCharacters() {
        return manager.getLoaded().stream()
                .map(LoadedCharacter::toClient)
                .collect(Collectors.toList());
    }
}
