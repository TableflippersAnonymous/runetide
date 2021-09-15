package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.dto.ColumnRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.services.internal.worldgen.server.domain.SeedPurpose;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import org.apache.commons.math3.analysis.interpolation.BicubicInterpolator;

public class TemperatureGenerator extends DelegatingGenerator2<ColumnRef, long[]> {
    protected TemperatureGenerator(final WorldSeed worldSeed) {
        super(new InterpolatingGenerator2L<>(SectorRef.class, ColumnRef.class, new BicubicInterpolator(),
                new OpenSimplex2SGenerator2L<>(SectorRef.class, ColumnRef.class, worldSeed.sector(SeedPurpose.TEMPERATURE),
                        0.001, 0, 255), Vector.of(5, 5), Vector.of(3, 3)));
    }
}
