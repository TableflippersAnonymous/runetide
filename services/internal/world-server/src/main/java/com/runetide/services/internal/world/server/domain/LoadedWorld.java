package com.runetide.services.internal.world.server.domain;

import com.runetide.common.clients.LoadingToken;
import com.runetide.common.loading.ServiceState;
import com.runetide.services.internal.multiverse.client.MultiversesClient;
import com.runetide.services.internal.multiverse.common.MultiverseRef;
import com.runetide.services.internal.world.common.World;

public class LoadedWorld {
    private final MultiversesClient multiversesClient;
    private final World world;
    private final LoadingToken<MultiverseRef> multiverseToken;

    public LoadedWorld(MultiversesClient multiversesClient, World world) {
        this.multiversesClient = multiversesClient;
        this.world = world;
        this.multiverseToken = multiversesClient.requestLoad(world.getMultiverse())
                .awaitServiceState(ServiceState.LOADED);
    }

    public void close() {
        multiverseToken.close();
    }

    public World getWorld() {
        return world;
    }
}
