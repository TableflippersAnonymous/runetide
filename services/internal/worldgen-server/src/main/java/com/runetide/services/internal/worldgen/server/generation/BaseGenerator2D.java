package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.Vec2D;
import com.runetide.common.dto.SectorRef;

public abstract class BaseGenerator2D {

    protected abstract void generateValues(final SectorRef sectorRef, final Vec2D start, int[][] out);

    protected abstract long seed(final SectorRef sectorRef);
}
