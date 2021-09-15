package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.dto.RegionRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.services.internal.worldgen.server.domain.SeedPurpose;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;

public class DifficultyGenerator extends DelegatingGenerator2<RegionRef, long[]> {
    private static final int MAX_DIFFICULTY = 150;

    public DifficultyGenerator(final WorldSeed worldSeed) {
        super(new InterpolatingGenerator2L<>(SectorRef.class, RegionRef.class, new BicubicInterpolator(),
                new OpenSimplex2SGenerator2L<>(SectorRef.class, RegionRef.class,
                        worldSeed.sector(SeedPurpose.DIFFICULTY),
                        .01,0, MAX_DIFFICULTY),
                Vector.of(5, 5), Vector.of(3, 3)));
    }

    public int getDifficulty(final RegionRef regionRef) {
        return (int) generate(BoundingBox.of(regionRef, regionRef))[0][0];
    }
}
