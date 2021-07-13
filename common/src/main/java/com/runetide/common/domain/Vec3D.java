package com.runetide.common.domain;

import java.util.Objects;

public class Vec3D extends Vec2D {
    public static final Vec3D IDENTITY = new Vec3D(0, 0, 0);
    public static final Vec3D UNIT_X = new Vec3D(1, 0, 0);
    public static final Vec3D UNIT_Y = new Vec3D(0, 1, 0);
    public static final Vec3D UNIT_Z = new Vec3D(0, 0, 1);
    public static final Vec3D UNIT_XZ = new Vec3D(1, 0, 1);

    protected final long y;

    public Vec3D(final long x, final long y, final long z) {
        super(x, z);
        this.y = y;
    }

    public long getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final Vec3D vec3D = (Vec3D) o;
        return y == vec3D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), y);
    }

    public Vec3D divide(final Vec3D vec) {
        return new Vec3D(x / vec.x, y / vec.y, z / vec.z);
    }

    public Vec3D modulo(final Vec3D vec) {
        return new Vec3D(x % vec.x, y % vec.y, z % vec.z);
    }

    public Vec3D scale(final Vec3D vec) {
        return new Vec3D(x * vec.x, y * vec.y, z * vec.z);
    }

    @Override
    public Vec3D add(final Vec3D vec) {
        return new Vec3D(x + vec.x, y + vec.y, z + vec.z);
    }

    @Override
    public Vec3D negate() {
        return new Vec3D(-x, -y, -z);
    }
}
