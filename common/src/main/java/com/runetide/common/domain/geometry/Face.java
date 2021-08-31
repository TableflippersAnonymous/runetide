package com.runetide.common.domain.geometry;

import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector3F;
import com.runetide.common.domain.geometry.vector.Vector3L;

public enum Face implements Direction<Vector3L, Vector3F> {
    NORTH(Vector.of(0, 0, 1)),
    EAST(Vector.of(1, 0, 0)),
    SOUTH(Vector.of(0, 0, -1)),
    WEST(Vector.of(-1, 0, 0)),
    UP(Vector.of(0, 1, 0)),
    DOWN(Vector.of(0, -1, 0));

    private final Vector3L vecL;
    private final Vector3F vecF;
    private Direction2 direction2;
    private Direction3 direction3;

    Face(final Vector3L vecL) {
        this.vecL = vecL;
        this.vecF = vecL.toFloat();
    }

    @Override
    public Vector3L asVectorL() {
        return vecL;
    }

    @Override
    public Vector3F asVectorF() {
        return vecF;
    }

    @Override
    public Direction2 asDirection2() {
        if(direction2 == null)
            direction2 = Direction2.closest(vecL.toVec2D());
        return direction2;
    }

    @Override
    public Direction3 asDirection3() {
        if(direction3 == null)
            direction3 = Direction3.closest(vecL);
        return direction3;
    }
}
