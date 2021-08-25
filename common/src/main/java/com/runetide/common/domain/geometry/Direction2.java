package com.runetide.common.domain.geometry;

import java.util.Arrays;
import java.util.Comparator;

public enum Direction2 implements Direction<Vector2L, Vector2F> {
    NORTH(Face.NORTH),
    EAST(Face.EAST),
    SOUTH(Face.SOUTH),
    WEST(Face.WEST),

    NORTH_EAST(NORTH, EAST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    NORTH_WEST(NORTH, WEST);

    public static Direction2 closest(final Vector2L vec) {
        return closest(vec.toFloat());
    }

    public static Direction2 closest(final Vector2F vec) {
        final Vector2F normalized = vec.normalize();
        return Arrays.stream(values())
                .max(Comparator.comparing(dir -> dir.normalized.dot(normalized)))
                .orElseThrow();
    }

    private final Vector2L vec;
    private final Vector2F normalized;
    private Direction3 direction3;

    Direction2(final Vector2L vec) {
        this.vec = vec;
        this.normalized = vec.normalize();
    }

    Direction2(final Face face) {
        this(face.asVectorL().toVec2D());
    }

    Direction2(final Direction2 dir1, final Direction2 dir2) {
        this(dir1.vec.add(dir2.vec));
    }

    @Override
    public Vector2L asVectorL() {
        return vec;
    }

    @Override
    public Vector2F asVectorF() {
        return normalized;
    }

    @Override
    public Direction2 asDirection2() {
        return this;
    }

    @Override
    public Direction3 asDirection3() {
        if(direction3 == null)
            direction3 = Direction3.closest(vec.toVec3D(0));
        return direction3;
    }
}
