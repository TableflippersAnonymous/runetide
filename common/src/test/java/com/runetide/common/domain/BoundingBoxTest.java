package com.runetide.common.domain;

import com.runetide.common.domain.geometry.BoundingBox;
import com.runetide.common.domain.geometry.BoundingBoxSet;
import com.runetide.common.domain.geometry.Vec2D;
import com.runetide.common.domain.geometry.Vec3D;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

public class BoundingBoxTest {
    private final Vec3D vec1 = new Vec3D(0, 0, 0);
    private final Vec3D vec2 = new Vec3D(5, 5, 5);
    private final BoundingBox<Vec2D, Vec2D> sut2 = new BoundingBox<>(vec1.toVec2D(), vec2.toVec2D());
    private final BoundingBox<Vec3D, Vec3D> sut3 = new BoundingBox<>(vec1, vec2);


    @Test
    public void testDimensions() {
        Assert.assertEquals(sut3.getDimensions(), new Vec3D(5, 5, 5));
    }

    @Test
    public void testUnion() {
        final BoundingBox<Vec2D, Vec2D> add = new BoundingBox<>(new Vec2D(2, 2), new Vec2D(6, 6));
        final BoundingBoxSet<Vec2D, Vec2D> bbs = sut2.union(add);
        System.out.println(sut2 + " union " + add);
        System.out.println("= " + bbs);
        Assert.assertTrue(bbs.contains(new Vec2D(0, 0)));
        Assert.assertTrue(bbs.contains(new Vec2D(3, 3)));
        Assert.assertTrue(bbs.contains(new Vec2D(6, 6)));
        Assert.assertFalse(bbs.contains(new Vec2D(0, 6)));
        Assert.assertFalse(bbs.contains(new Vec2D(6, 0)));
    }

    @Test
    public void testSubtract() {
        final BoundingBox<Vec2D, Vec2D> sub1 = new BoundingBox<>(new Vec2D(2, 2), new Vec2D(3, 3));
        final Optional<BoundingBoxSet<Vec2D, Vec2D>> bbs1 = sut2.subtract(sub1);
        System.out.println(sut2 + " - " + sub1);
        System.out.println("= " + bbs1);
        Assert.assertTrue(bbs1.isPresent());
        Assert.assertTrue(bbs1.get().contains(new Vec2D(0, 0)));
        Assert.assertFalse(bbs1.get().contains(new Vec2D(3, 3)));
        Assert.assertTrue(bbs1.get().contains(new Vec2D(5, 5)));

        final BoundingBox<Vec2D, Vec2D> sub2 = new BoundingBox<>(new Vec2D(2, -1), new Vec2D(3, 6));
        final Optional<BoundingBoxSet<Vec2D, Vec2D>> bbs2 = sut2.subtract(sub2);
        System.out.println(sut2 + " - " + sub2);
        System.out.println("= " + bbs2);
        Assert.assertTrue(bbs2.isPresent());
        Assert.assertTrue(bbs2.get().contains(new Vec2D(0, 0)));
        Assert.assertFalse(bbs2.get().contains(new Vec2D(3, 3)));
        Assert.assertTrue(bbs2.get().contains(new Vec2D(5, 5)));

        final BoundingBox<Vec2D, Vec2D> sub3 = new BoundingBox<>(new Vec2D(6, 6), new Vec2D(7, 7));
        final Optional<BoundingBoxSet<Vec2D, Vec2D>> bbs3 = sut2.subtract(sub3);
        System.out.println(sut2 + " - " + sub3);
        System.out.println("= " + bbs3);
        Assert.assertTrue(bbs3.isPresent());
        Assert.assertTrue(bbs3.get().contains(new Vec2D(0, 0)));
        Assert.assertTrue(bbs3.get().contains(new Vec2D(3, 3)));
        Assert.assertTrue(bbs3.get().contains(new Vec2D(5, 5)));

        final Optional<BoundingBoxSet<Vec2D, Vec2D>> bbs4 = sut2.subtract(sut2);
        System.out.println(sut2 + " - " + sut2);
        System.out.println("= " + bbs4);
        Assert.assertTrue(bbs4.isEmpty());
    }
}
