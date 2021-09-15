package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;
import com.runetide.services.internal.worldgen.server.vendor.OpenSimplex2S;

import java.util.function.Function;

public class OpenSimplex2SGenerator2L<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>> extends Generator2FTo2L<PointType> {
    protected OpenSimplex2SGenerator2L(final Class<GenerationParent> generationParentClass,
                                       final Class<PointType> pointTypeClass,
                                       final Function<GenerationParent, Long> seedProvider,
                                       final OpenSimplex2S.GenerateContext2D context) {
        super(new OpenSimplex2SGenerator2F<>(generationParentClass, pointTypeClass, seedProvider, context));
    }

    protected OpenSimplex2SGenerator2L(final Class<GenerationParent> generationParentClass,
                                       final Class<PointType> pointTypeClass,
                                       final Function<GenerationParent, Long> seedProvider,
                                       final OpenSimplex2S.LatticeOrientation2D latticeOrientation2D,
                                       final double xFreq, final double zFreq, final long low, final long high) {
        //noinspection SuspiciousNameCombination
        super(new OpenSimplex2SGenerator2F<>(generationParentClass, pointTypeClass, seedProvider,
                new OpenSimplex2S.GenerateContext2D(latticeOrientation2D, zFreq, xFreq, 1)),
                -1, 1, low, high);
    }

    protected OpenSimplex2SGenerator2L(final Class<GenerationParent> generationParentClass,
                                       final Class<PointType> pointTypeClass,
                                       final Function<GenerationParent, Long> seedProvider,
                                       final double freq, final long low, final long high) {
        super(new OpenSimplex2SGenerator2F<>(generationParentClass, pointTypeClass, seedProvider,
                        new OpenSimplex2S.GenerateContext2D(OpenSimplex2S.LatticeOrientation2D.Standard,
                                freq, freq, 1)),
                -1, 1, low, high);
    }
}
