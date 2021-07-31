package com.runetide.common.domain;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.runetide.common.dto.Vec;
import com.runetide.common.dto.VectorLike;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoundingBoxSet<PointType extends VectorLike<PointType, VecType>, VecType extends Vec<VecType>>
        implements BoundingBoxLike<BoundingBoxSet<PointType, VecType>, PointType, VecType> {
    private final Set<BoundingBox<PointType, VecType>> boxes;

    public BoundingBoxSet(BoundingBox<PointType, VecType> box) {
        this(Set.of(box));
    }

    public BoundingBoxSet(final Set<BoundingBox<PointType, VecType>> boxes) {
        if(boxes.size() == 0)
            throw new IllegalArgumentException("boxes must have a positive cardinality.");
        this.boxes = compact(boxes);
    }

    @Override
    public Iterator<PointType> iterator() {
        return Iterators.concat(Iterators.transform(boxes.iterator(), BoundingBox::iterator));
    }

    @Override
    public boolean contains(final PointType element) {
        return boxes.stream().anyMatch(bb -> bb.contains(element));
    }

    @Override
    public boolean intersectsWith(final BoundingBox<PointType, VecType> other) {
        return boxes.stream().anyMatch(bb -> bb.intersectsWith(other));
    }

    @Override
    public Optional<BoundingBoxSet<PointType, VecType>> intersect(final BoundingBox<PointType, VecType> other) {
        final Set<BoundingBox<PointType, VecType>> newBoxes = boxes.stream()
                .filter(bb -> bb.intersectsWith(other))
                .collect(Collectors.toUnmodifiableSet());
        if(newBoxes.isEmpty())
            return Optional.empty();
        return Optional.of(new BoundingBoxSet<>(newBoxes));
    }

    @Override
    public BoundingBoxSet<PointType, VecType> union(final BoundingBox<PointType, VecType> other) {
        /* If we intersect with the new BoundingBox, there are two cases:
         * 1) we are already a superset of the new BoundingBox, in which case we can simply return ourselves.
         * 2) we have one or more BoundingBoxes that overlap, but do not fully encompass the new BoundingBox.
         *    In this case, we need to subtract out the intersection, and add the rest.
         * These can actually be combined into the same basic check, as if we are a full superset, the intersection
         * will be the whole new BoundingBox, and the subtraction will be nothing.
         */
        final Optional<BoundingBoxSet<PointType, VecType>> intersection = intersect(other);
        if(intersection.isEmpty())
            return new BoundingBoxSet<>(ImmutableSet.<BoundingBox<PointType, VecType>>builder()
                    .addAll(boxes.iterator()).add(other).build());
        Optional<BoundingBoxSet<PointType, VecType>> bbs = Optional.of(new BoundingBoxSet<>(Set.of(other)));
        for(final BoundingBox<PointType, VecType> bb : boxes)
            bbs = bbs.flatMap(e -> e.subtract(bb));
        if(bbs.isEmpty())
            return this;
        return new BoundingBoxSet<>(ImmutableSet.<BoundingBox<PointType, VecType>>builder()
                .addAll(boxes).addAll(bbs.get().boxes).build());
    }

    @Override
    public Optional<BoundingBoxSet<PointType, VecType>> subtract(final BoundingBox<PointType, VecType> other) {
        return of(boxes.stream()
                .map(box -> box.subtract(other))
                .flatMap(Optional::stream)
                .flatMap(bbs -> bbs.boxes.stream())
                .collect(Collectors.toUnmodifiableSet()));
    }

    public static <PointType extends VectorLike<PointType, VecType>, VecType extends Vec<VecType>>
    Optional<BoundingBoxSet<PointType, VecType>> of(final Set<BoundingBox<PointType, VecType>> boxes) {
        if(boxes.isEmpty())
            return Optional.empty();
        return Optional.of(new BoundingBoxSet<>(boxes));
    }

    @Override
    public VecType getDimensions() {
        return toBoundingBox().getDimensions();
    }

    @Override
    public <NewPointType extends VectorLike<NewPointType, NewVecType>, NewVecType extends Vec<NewVecType>>
    BoundingBoxSet<NewPointType, NewVecType> map(final Function<PointType, NewPointType> startMapper,
                                                 final Function<PointType, NewPointType> endMapper) {
        return new BoundingBoxSet<>(boxes.stream().map(e -> e.map(startMapper, endMapper))
                .collect(Collectors.toUnmodifiableSet()));
    }

    public BoundingBox<PointType, VecType> toBoundingBox() {
        final PointType start = boxes.stream().map(BoundingBox::getStart).reduce(VectorLike::minCoordinates)
                .orElseThrow(IllegalStateException::new);
        final PointType end = boxes.stream().map(BoundingBox::getEnd).reduce(VectorLike::maxCoordinates)
                .orElseThrow(IllegalStateException::new);
        return new BoundingBox<>(start, end);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BoundingBoxSet<?, ?> that = (BoundingBoxSet<?, ?>) o;
        return Objects.equals(boxes, that.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes);
    }

    private Set<BoundingBox<PointType, VecType>> compact(final Set<BoundingBox<PointType, VecType>> inputBoxes) {
        /* The idea behind this method is to go over boxes and see if any of them can be joined into a larger unified
         * bounding box.
         */
        Set<BoundingBox<PointType, VecType>> boxes = inputBoxes;
        for(;;) {
            final Optional<Set<BoundingBox<PointType, VecType>>> compacted = compactOnce(boxes);
            if(compacted.isEmpty())
                return boxes;
            boxes = compacted.get();
        }
    }

    private Optional<Set<BoundingBox<PointType, VecType>>> compactOnce(final Set<BoundingBox<PointType, VecType>> boxes) {
        final Set<BoundingBox<PointType, VecType>> compactedBoxes = new HashSet<>(boxes);
        for(final BoundingBox<PointType, VecType> box1 : boxes) {
            for(final BoundingBox<PointType, VecType> box2 : boxes) {
                /* We can only merge adjacent boxes, otherwise we get an empty optional.  So, try, and see if
                 * we could merge it. */
                final Optional<BoundingBox<PointType, VecType>> mergedBox = box1.merge(box2);
                if(mergedBox.isEmpty())
                    continue;
                compactedBoxes.add(mergedBox.get());
                compactedBoxes.remove(box1);
                compactedBoxes.remove(box2);
                return Optional.of(compactedBoxes);
            }
        }
        return Optional.empty();
    }
}
