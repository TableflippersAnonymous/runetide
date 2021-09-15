package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.Constants;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.services.internal.worldgen.server.domain.SeedPurpose;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;

public class ElevationGenerator extends DelegatingGenerator2<RegionRef, long[]> {
    protected ElevationGenerator(final WorldSeed worldSeed) {
        super(new InterpolatingGenerator2L<>(SectorRef.class, RegionRef.class, new BicubicInterpolator(),
                new OpenSimplex2SGenerator2L<>(SectorRef.class, RegionRef.class, worldSeed.sector(SeedPurpose.ELEVATION),
                        0.1, Constants.BLOCKS_PER_CHUNK_Y / 2, 3 * Constants.BLOCKS_PER_CHUNK_Y / 4),
                Vector.of(5, 5), Vector.of(3, 3)));
    }
}
