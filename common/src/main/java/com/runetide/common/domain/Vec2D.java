package com.runetide.common.domain;

import java.util.Objects;

public class Vec2D {
    public static final Vec2D IDENTITY = new Vec2D(0, 0);
    public static final Vec2D UNIT_X = new Vec2D(1, 0);
    public static final Vec2D UNIT_Z = new Vec2D(0, 1);
    public static final Vec2D UNIT_XZ = new Vec2D(1, 1);

    protected final long x;
    protected final long z;

    public Vec2D(final long x, final long z) {
        this.x = x;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vec2D vec2D = (Vec2D) o;
        return x == vec2D.x && z == vec2D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    public Vec2D divide(final Vec2D vec) {
        return new Vec2D(x / vec.x, z / vec.z);
    }

    public Vec2D modulo(final Vec2D vec) {
        return new Vec2D(x % vec.x, z % vec.z);
    }

    public Vec2D scale(final Vec2D vec) {
        return new Vec2D(x * vec.x, z * vec.z);
    }

    public Vec2D add(final Vec2D vec) {
        return new Vec2D(x + vec.x, z + vec.z);
    }

    public Vec3D add(final Vec3D vec) {
        return new Vec3D(x + vec.x, vec.y, z + vec.z);
    }

    public Vec2D negate() {
        return new Vec2D(-x, -z);
    }
}
