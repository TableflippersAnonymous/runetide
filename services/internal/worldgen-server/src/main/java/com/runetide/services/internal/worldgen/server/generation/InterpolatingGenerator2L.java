package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedBoundingBoxSet;
import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.dto.ContainerRef;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import java.util.Optional;
import java.util.stream.Collectors;

public class InterpolatingGenerator2L<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        extends BaseGenerator2<GenerationParent, PointType, int[]> {
    private final UnivariateInterpolator interpolator;
    private final Generator2<PointType, int[]> generator;
    private final Vector2L interpolateBorder;
    private final Vector2L interpolateDistance;
    private final Class<GenerationParent> generationParentClass;
    private final Class<PointType> pointTypeClass;

    public InterpolatingGenerator2L(final Class<GenerationParent> generationParentClass,
                                    final Class<PointType> pointTypeClass, final UnivariateInterpolator interpolator,
                                    final Generator2<PointType, int[]> generator, final Vector2L interpolateBorder,
                                    final Vector2L interpolateDistance) {
        super(vec -> new int[vec.getX().intValue()][vec.getZ().intValue()], generationParentClass, pointTypeClass);
        this.interpolator = interpolator;
        this.generator = generator;
        this.interpolateBorder = interpolateBorder;
        this.interpolateDistance = interpolateDistance;
        this.generationParentClass = generationParentClass;
        this.pointTypeClass = pointTypeClass;
    }

    @Override
    protected void generateValues(final int[][] ret, final GenerationParent parent,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> parentBox,
                                  final Vector2L retOffset, final Vector2L parentOffset) {
        /* Generate the boundingBox */
        final int[][] generated = generator.generate(boundingBox);
        final int retOffsetX = retOffset.getX().intValue();
        final int retOffsetZ = retOffset.getZ().intValue();
        for(int x = 0; x < generated.length; x++)
            System.arraycopy(generated[x], 0, ret[x + retOffsetX], retOffsetZ, generated[x].length);

        /* Check if we need to interpolate at all. */
        final FixedBoundingBoxSingle<PointType, Vector2L> nonInterpolateBox = parentBox
                .shrink(interpolateBorder);
        final Optional<FixedBoundingBoxSet<PointType, Vector2L>> borderBox = parentBox
                .grow(interpolateBorder)
                .subtract(nonInterpolateBox);
        if(borderBox.isEmpty() || !borderBox.get().intersectsWith(boundingBox))
            return;

        /* If we got here, we need to interpolate.  Specifically, borderBox is the parts we need to interpolate over
         * and nonInterpolateBox is the part that can be taken from the generator verbatim.
         */
        final FixedBoundingBoxSet<PointType, Vector2L> totalInterpolatableArea = borderBox.get()
                .map(point -> point.add(interpolateDistance.negate()), point -> point.add(interpolateDistance));
        final FixedBoundingBoxSingle<GenerationParent, Vector2L> generationParents = totalInterpolatableArea
                .toBoundingBox().map(point -> point.getOffsetBasis(generationParentClass));
        final FixedBoundingBoxSet<PointType, Vector2L> generationArea = FixedBoundingBoxSet.of(generationParents
                .stream()
                .map(p -> p.asBoundingBox(pointTypeClass)
                        .shrink(interpolateBorder))
                .collect(Collectors.toUnmodifiableSet()));

        //TODO: We need to generate points for these points:
        generationArea.intersect(boundingBox.grow(interpolateBorder).grow(interpolateBorder)
                .grow(interpolateDistance));

        //TODO: We need to then interpolate between those points and nonInterpolateBox, filling in any points in
        //      borderBox.intersect(boundingBox)
    }

    @Override
    protected void generateValues(final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox, final int[][] out) {
        generator.generateValues(boundingBox, out);

        /* At this point, out contains the filled output from the child generator.  We need to check if any of the
         * elements of out are within interpolateBorder from the edge of sectorRef.
         */

        if(!needInterpolation(sectorRef, start))
            return;

        interpolator.interpolate()
        sectorRef
    }

    @Override
    protected long seed(final SeedType point) {
        return generator.seed(point);
    }
}
