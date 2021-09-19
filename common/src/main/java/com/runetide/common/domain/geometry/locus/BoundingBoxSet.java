package com.runetide.common.domain.geometry.locus;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.runetide.common.domain.geometry.point.FixedPoint;
import com.runetide.common.domain.geometry.point.Point;
import com.runetide.common.domain.geometry.vector.Vector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BoundingBoxSet<BBSet extends BoundingBoxSet<BBSet, BBType, PointType, VecType, NumberType>,
        BBType extends BoundingBoxSingle<BBType, BBSet, PointType, VecType, NumberType>,
        PointType extends Point<PointType, VecType, NumberType>, VecType extends Vector<VecType, NumberType>,
        NumberType extends Number>
        implements BoundingBox<BBSet, PointType, VecType, NumberType> {
    private final Function<Set<BBType>, BBSet> constructor;

    protected final Set<BBType> boxes;

    protected BoundingBoxSet(final Function<Set<BBType>, BBSet> constructor, final Set<BBType> boxes) {
        this.constructor = constructor;
        if(boxes.size() == 0)
            throw new IllegalArgumentException("boxes must have a positive cardinality.");
        this.boxes = compact(boxes);
    }

    @Override
    public boolean contains(final PointType element) {
        return boxes.stream().anyMatch(bb -> bb.contains(element));
    }

    public Optional<BBType> getContainingBox(final PointType element) {
        return boxes.stream().filter(bb -> bb.contains(element)).findFirst();
    }

    public boolean intersectsWith(final BBType other) {
        return boxes.stream().anyMatch(bb -> bb.intersectsWith(other));
    }

    @Override
    public boolean intersectsWith(final BBSet other) {
        return other.boxes.stream().anyMatch(this::intersectsWith);
    }

    public Optional<BBSet> intersect(final BBType other) {
        return intersect(constructor.apply(Set.of(other)));
    }

    @Override
    public Optional<BBSet> intersect(final BBSet other) {
        final Set<BBType> newBoxes = boxes.stream()
                .flatMap(box1 -> other.boxes.stream().map(box1::intersect))
                .flatMap(Optional::stream)
                .collect(Collectors.toUnmodifiableSet());
        if(newBoxes.isEmpty())
            return Optional.empty();
        return Optional.of(constructor.apply(newBoxes));
    }

    public BBSet union(final BBType other) {
        return constructor.apply(ImmutableSet.<BBType>builder().addAll(boxes).add(other).build());
    }

    @Override
    public BBSet union(final BBSet other) {
        /* If we intersect with the new BoundingBox, there are two cases:
         * 1) we are already a superset of the new BoundingBox, in which case we can simply return ourselves.
         * 2) we have one or more BoundingBoxes that overlap, but do not fully encompass the new BoundingBox.
         *    In this case, we need to subtract out the intersection, and add the rest.
         * These can actually be combined into the same basic check, as if we are a full superset, the intersection
         * will be the whole new BoundingBox, and the subtraction will be nothing.
         *
         * This is actually implemented below in compactOnce()
         */
        return constructor.apply(ImmutableSet.<BBType>builder()
                .addAll(boxes).addAll(other.boxes).build());
    }

    public Optional<BBSet> subtract(final BBType other) {
        return subtract(constructor.apply(Set.of(other)));
    }

    @Override
    public Optional<BBSet> subtract(final BBSet other) {
        return of(boxes.stream()
                .flatMap(box1 -> other.boxes.stream().map(box1::subtract))
                .flatMap(Optional::stream)
                .flatMap(bbs -> bbs.boxes.stream())
                .collect(Collectors.toUnmodifiableSet()));
    }

    private Optional<BBSet> of(final Set<BBType> boxes) {
        if(boxes.isEmpty())
            return Optional.empty();
        return Optional.of(constructor.apply(boxes));
    }

    @Override
    public VecType getDimensions() {
        return toBoundingBox().getDimensions();
    }

    @Override
    public BBSet move(final VecType direction) {
        return constructor.apply(boxes.stream().map(box -> box.move(direction))
                .collect(Collectors.toUnmodifiableSet()));
    }

    public Set<BBType> getBoxes() {
        return boxes.stream().collect(Collectors.toUnmodifiableSet());
    }

    public abstract BBType toBoundingBox();

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BoundingBoxSet<?, ?, ?, ?, ?> that = (BoundingBoxSet<?, ?, ?, ?, ?>) o;
        return Objects.equals(boxes, that.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes);
    }

    @Override
    public String toString() {
        return "<BBSet:" + Joiner.on(",").join(boxes) + ">";
    }

    private Set<BBType> compact(final Set<BBType> inputBoxes) {
        final Queue<BBType> notProcessed = new ArrayDeque<>(inputBoxes);
        final Set<BBType> processed = new HashSet<>();
        BBType box1;
        nextBox: while((box1 = notProcessed.poll()) != null) {
            for(final Iterator<BBType> it = processed.iterator(); it.hasNext();) {
                final BBType box2 = it.next();
                if(box1.intersectsWith(box2)) {
                    box1.subtract(box2).ifPresent(bbSet -> notProcessed.addAll(bbSet.boxes));
                    continue nextBox;
                }
                final Optional<BBType> mergedBox = box1.merge(box2);
                if(mergedBox.isPresent()) {
                    notProcessed.add(mergedBox.get());
                    it.remove();
                    continue nextBox;
                }
            }
            processed.add(box1);
        }
        return processed;
    }
}
