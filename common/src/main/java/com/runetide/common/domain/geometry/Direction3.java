package com.runetide.common.domain.geometry;

import com.runetide.common.domain.geometry.vector.Vector3F;
import com.runetide.common.domain.geometry.vector.Vector3L;

import java.util.Arrays;
import java.util.Comparator;

public enum Direction3 implements Direction<Vector3L, Vector3F> {
    NORTH(Face.NORTH),
    EAST(Face.EAST),
    SOUTH(Face.SOUTH),
    WEST(Face.WEST),
    UP(Face.UP),
    DOWN(Face.DOWN),

    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),

    NORTH_UP(NORTH, UP),
    NORTH_DOWN(NORTH, DOWN),
    SOUTH_UP(SOUTH, UP),
    SOUTH_DOWN(SOUTH, DOWN),

    EAST_UP(EAST, UP),
    EAST_DOWN(EAST, DOWN),
    WEST_UP(WEST, UP),
    WEST_DOWN(WEST, DOWN),

    NORTH_EAST_UP(NORTH_EAST, UP),
    NORTH_EAST_DOWN(NORTH_EAST, DOWN),
    NORTH_WEST_UP(NORTH_WEST, UP),
    NORTH_WEST_DOWN(NORTH_WEST, DOWN),

    SOUTH_EAST_UP(SOUTH_EAST, UP),
    SOUTH_EAST_DOWN(SOUTH_EAST, DOWN),
    SOUTH_WEST_UP(SOUTH_WEST, UP),
    SOUTH_WEST_DOWN(SOUTH_WEST, DOWN);

    public static Direction3 closest(final Vector3L vec) {
        return closest(vec.toFloat());
    }

    public static Direction3 closest(final Vector3F vec) {
        final Vector3F normalized = vec.normalize();
        return Arrays.stream(values())
                .max(Comparator.comparing(dir -> dir.normalized.dot(normalized)))
                .orElse(NORTH);
    }

    private final Vector3L vec;
    private final Vector3F normalized;
    private Direction2 direction2;

    Direction3(final Vector3L vec) {
        this.vec = vec;
        this.normalized = vec.normalize();
    }

    Direction3(final Face face) {
        this(face.asVectorL());
    }

    Direction3(final Direction3 dir1, final Direction3 dir2) {
        this(dir1.vec.add(dir2.vec));
    }

    @Override
    public Vector3L asVectorL() {
        return vec;
    }

    @Override
    public Vector3F asVectorF() {
        return normalized;
    }

    @Override
    public Direction2 asDirection2() {
        if(direction2 == null)
            direction2 = Direction2.closest(vec.toVec2D());
        return direction2;
    }

    @Override
    public Direction3 asDirection3() {
        return this;
    }
}
