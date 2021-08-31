package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSet;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.locus.IterableLocus;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.dto.ContainerRef;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class InterpolatingGenerator2L<GenerationParent extends ContainerRef<GenerationParent, Vector2L, ?, ?, ?>,
        PointType extends ContainerRef<PointType, Vector2L, ?, ?, ?>>
        extends BaseGenerator2<GenerationParent, PointType, long[]> {
    private final BivariateGridInterpolator bivariateGridInterpolator;
    private final Generator2<PointType, long[]> generator;
    private final Vector2L interpolateBorder;
    private final Vector2L interpolateDistance;
    private final Class<GenerationParent> generationParentClass;
    private final Class<PointType> pointTypeClass;

    public InterpolatingGenerator2L(final Class<GenerationParent> generationParentClass,
                                    final Class<PointType> pointTypeClass,
                                    final BivariateGridInterpolator bivariateGridInterpolator,
                                    final Generator2<PointType, long[]> generator, final Vector2L interpolateBorder,
                                    final Vector2L interpolateDistance) {
        super(vec -> new long[vec.getX().intValue()][vec.getZ().intValue()], generationParentClass, pointTypeClass);
        this.bivariateGridInterpolator = bivariateGridInterpolator;
        this.generator = generator;
        this.interpolateBorder = interpolateBorder;
        this.interpolateDistance = interpolateDistance;
        this.generationParentClass = generationParentClass;
        this.pointTypeClass = pointTypeClass;
    }

    @Override
    protected void generateValues(final long[][] ret, final GenerationParent parent,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> boundingBox,
                                  final FixedBoundingBoxSingle<PointType, Vector2L> parentBox,
                                  final Vector2L retOffset, final Vector2L parentOffset) {
        /* Generate within the boundingBox */
        final long[][] generated = generator.generate(boundingBox);
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

        //TODO: Consider rewriting this to handle one box in borderBox at a time, rather than attempting to crunch
        //      them all at once.  This should be faster, and will solve the need for the .getContainingBox call below.
        final SortedSet<Long> generationXSet = new TreeSet<>();
        final SortedSet<Long> generationZSet = new TreeSet<>();
        final Map<FixedBoundingBoxSingle<PointType, Vector2L>, long[][]> generatedMap = new HashMap<>();
        for (final FixedBoundingBoxSingle<PointType, Vector2L> box : generationArea.get().getBoxes()) {
            final var offset = box.getStart().subtract(boundingBox.getStart());
            final Vector2L dimensions = box.getDimensions();
            generationXSet.addAll(LongStream.range(offset.getX(), dimensions.getX() + offset.getX())
                    .boxed().collect(Collectors.toSet()));
            generationZSet.addAll(LongStream.range(offset.getZ(), dimensions.getZ() + offset.getZ())
                    .boxed().collect(Collectors.toSet()));
            generatedMap.put(box, generator.generate(box));
        }

        final int[] generationX = generationXSet.stream().mapToInt(Long::intValue).toArray();
        final int[] generationZ = generationZSet.stream().mapToInt(Long::intValue).toArray();
        final double[][] generationValue = new double[generationX.length][generationZ.length];
        for (int x = 0; x < generationX.length; x++) {
            for (int z = 0; z < generationZ.length; z++) {
                final var box = generationArea.get()
                        .getContainingBox(boundingBox.getStart().add(Vector.of(generationX[x], generationZ[z])));
                if (box.isEmpty())
                    throw new IllegalStateException();
                final var offset = box.get().getStart().subtract(boundingBox.getStart());
                final long[][] data = generatedMap.get(box.get());
                final int xEffective = generationX[x] - offset.getX().intValue();
                final int zEffective = generationZ[z] - offset.getZ().intValue();
                generationValue[x][z] = data[xEffective][zEffective];
            }
        }

        final BivariateFunction interpolator = bivariateGridInterpolator.interpolate(
                generationXSet.stream().mapToDouble(Long::doubleValue).toArray(),
                generationZSet.stream().mapToDouble(Long::doubleValue).toArray(), generationValue);

        borderBox.get().intersect(boundingBox).stream().flatMap(IterableLocus::stream).forEach(point -> {
            final Vector2L offset = point.subtract(boundingBox.getStart());
            final int xOffset = retOffsetX + offset.getX().intValue();
            final int zOffset = retOffsetZ + offset.getZ().intValue();
            ret[xOffset][zOffset] = (long) interpolator.value(offset.getX(), offset.getZ());
        });
    }
}
