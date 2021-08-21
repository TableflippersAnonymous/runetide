package com.runetide.services.internal.worldgen.server.generation;

import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.FixedVector;
import com.runetide.common.dto.ContainerRef;

import java.util.Optional;
import java.util.function.Function;

public abstract class BaseGenerator<
        GenerationParent extends ContainerRef<GenerationParent, GenerationParentVecType, ?, ?, ?>,
        GenerationParentVecType extends FixedVector<GenerationParentVecType>,
        PointType extends ContainerRef<PointType, VecType, ?, ?, ?>,
        VecType extends FixedVector<VecType>, ReturnType>
        implements Generator<PointType, VecType, ReturnType> {
    private final Function<VecType, ReturnType> allocateArray;
    private final Class<GenerationParent> generationParentClass;
    private final Class<PointType> pointTypeClass;

    protected BaseGenerator(final Function<VecType, ReturnType> allocateArray,
                            final Class<GenerationParent> generationParentClass,
                            final Class<PointType> pointTypeClass) {
        this.allocateArray = allocateArray;
        this.generationParentClass = generationParentClass;
        this.pointTypeClass = pointTypeClass;
    }

    public ReturnType generate(final FixedBoundingBoxSingle<PointType, VecType> boundingBox) {
        final ReturnType ret = allocateArray.apply(boundingBox.getDimensions());
        for(final GenerationParent parent : boundingBox.map(point -> point.getOffsetBasis(generationParentClass))) {
            final FixedBoundingBoxSingle<PointType, VecType> parentBox = parent.asBoundingBox(pointTypeClass);
            final Optional<FixedBoundingBoxSingle<PointType, VecType>> intersection = boundingBox.intersect(parentBox);
            if(intersection.isPresent()) {
                final PointType intersectionStart = intersection.get().getStart();
                final VecType retOffset = intersectionStart.subtract(boundingBox.getStart());
                generateValues(ret, parent, intersection.get(), parentBox, retOffset,
                        intersectionStart.offsetTo(parent));
            }
        }
        return ret;
    }

    protected abstract void generateValues(final ReturnType ret, final GenerationParent parent,
                                           final FixedBoundingBoxSingle<PointType, VecType> boundingBox,
                                           final FixedBoundingBoxSingle<PointType, VecType> parentBox,
                                           final VecType retOffset, final VecType parentOffset);
}
