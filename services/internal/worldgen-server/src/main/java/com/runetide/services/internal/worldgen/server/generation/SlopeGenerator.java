package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ChunkRef;
import com.runetide.common.dto.SectorRef;
import com.runetide.services.internal.worldgen.server.domain.SeedPurpose;
import com.runetide.services.internal.worldgen.server.domain.WorldSeed;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

public class SlopeGenerator extends DelegatingGenerator2<ChunkRef, double[]> {
    protected SlopeGenerator(final WorldSeed worldSeed) {
        super(new OpenSimplex2SGenerator2F<>(SectorRef.class, ChunkRef.class, worldSeed.sector(SeedPurpose.SLOPE),
                new OpenSimplex2S.GenerateContext2D(OpenSimplex2S.LatticeOrientation2D.Standard,
                        0.1, 0.1, 8)));
    }

    @Override
    public double[][] generate(final FixedBoundingBoxSingle<ChunkRef, Vector2L> boundingBox) {
        final double[][] ret = super.generate(boundingBox);
        for(int x = 0; x < ret.length; x++)
            for(int z = 0; z < ret[x].length; z++)
                ret[x][z] = Math.abs(ret[x][z]);
        return ret;
    }
}
