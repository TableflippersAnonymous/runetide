package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSet;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

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
        /* Generate within the boundingBox */
        final int[][] generated = generator.generate(boundingBox);
        final int retOffsetX = retOffset.getX().intValue();
        final int retOffsetZ = retOffset.getZ().intValue();
        for(int x = 0; x < generated.length; x++)
            System.arraycopy(generated[x], 0, ret[x + retOffsetX], retOffsetZ, generated[x].length);

        /* Check if we need to interpolate at all.
         *
         * +----------------------------+<-- borderBox (does not include parentBox.shrink(interpolateBorder) below)
         * |      interpolateBorder     |
         * |   +--------------------+<--|--- parentBox
         * |   |  interpolateBorder |   |
         * |   |   +------------+<--|---|--- parentBox.shrink(interpolateBorder)
         * |   |   |////////////|   |   |
         * |   |   |////////////|   |   |
         * |   |   |////////////|   |   |
         * |   |   |////////////|   |   |
         * |   |   +------------+   |   |
         * |   |                    |   |
         * |   +--------------------+   |
         * |                            |
         * +----------------------------+
         *
         * parentBox is the BB containing all of the points in the GenerationParent.
         * borderBox is the BB containing just the interpolateBorder on the outside and on the inside of parentBox.
         *
         * We need to interpolate only if borderBox intersects with boundingBox (our generation area).
         */
        final var borderBox = parentBox.outset(interpolateBorder)
                .subtract(parentBox.inset(interpolateBorder));
        if(borderBox.isEmpty() || !borderBox.get().intersectsWith(boundingBox))
            return;

        /* If we got here, we need to interpolate.  Specifically, borderBox is the parts we need to interpolate over
         * and generationBox is the part that can be taken from the generator verbatim.
         *
         * totalInterpolatableArea is like borderBox, but expanded to include interpolateDistance.  This represents
         *     what we care about for interpolation.
         * generationParents is the BB containing all of the GenerationParents in the totalInterpolatableArea.
         * generationArea is the generatable areas (not within interpolateBorder from the edges) for each
         *     GenerationParent in generationParents, intersected with boundingBox.  This is what we need to generate.
         */
        final var totalInterpolatableArea = borderBox.get()
                .map(start -> start.add(interpolateDistance.negate()), end -> end.add(interpolateDistance));
        final var generationParents = totalInterpolatableArea
                .toBoundingBox().map(point -> point.getOffsetBasis(generationParentClass));
        final var generationArea = FixedBoundingBoxSet.of(generationParents
                .stream()
                .map(p -> p.asBoundingBox(pointTypeClass)
                        .inset(interpolateBorder))
                .collect(Collectors.toUnmodifiableSet()))
                .intersect(boundingBox.outset(interpolateBorder.scale(2L).add(interpolateDistance)));

        // No interpolation necessary.
        if(generationArea.isEmpty())
            return;

        for(final FixedBoundingBoxSingle<PointType, Vector2L> box : generationArea.get().getBoxes()) {

        }

        //TODO: We need to then interpolate between those points and generationBox, filling in any points in
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
