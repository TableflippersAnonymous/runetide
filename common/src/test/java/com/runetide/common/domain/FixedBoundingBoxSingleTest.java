package com.runetide.common.domain;

import com.runetide.common.domain.geometry.FixedBoundingBoxSet;
import com.runetide.common.domain.geometry.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.BoundingBoxSet;
import com.runetide.common.domain.geometry.Vector2L;
import com.runetide.common.domain.geometry.Vector3L;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

public class FixedBoundingBoxSingleTest {
    private final Vector3L vec1 = Vector3L.of(0, 0, 0);
    private final Vector3L vec2 = Vector3L.of(5, 5, 5);
    private final FixedBoundingBoxSingle<Vector2L, Vector2L> sut2 = FixedBoundingBoxSingle.of(vec1.toVec2D(), vec2.toVec2D());
    private final FixedBoundingBoxSingle<Vector3L, Vector3L> sut3 = FixedBoundingBoxSingle.of(vec1, vec2);


    @Test
    public void testDimensions() {
        Assert.assertEquals(sut3.getDimensions(), Vector3L.of(6, 6, 6));
    }

    @Test
    public void testUnion() {
        final FixedBoundingBoxSingle<Vector2L, Vector2L> add = FixedBoundingBoxSingle.of(Vector2L.of(2, 2), Vector2L.of(6, 6));
        final FixedBoundingBoxSet<Vector2L, Vector2L> bbs = sut2.union(add);
        System.out.println(sut2 + " union " + add);
        System.out.println("= " + bbs);
        Assert.assertTrue(bbs.contains(Vector2L.of(0, 0)));
        Assert.assertTrue(bbs.contains(Vector2L.of(3, 3)));
        Assert.assertTrue(bbs.contains(Vector2L.of(6, 6)));
        Assert.assertFalse(bbs.contains(Vector2L.of(0, 6)));
        Assert.assertFalse(bbs.contains(Vector2L.of(6, 0)));
    }

    @Test
    public void testSubtract() {
        final FixedBoundingBoxSingle<Vector2L, Vector2L> sub1 = FixedBoundingBoxSingle.of(Vector2L.of(2, 2), Vector2L.of(3, 3));
        final Optional<FixedBoundingBoxSet<Vector2L, Vector2L>> bbs1 = sut2.subtract(sub1);
        System.out.println(sut2 + " - " + sub1);
        System.out.println("= " + bbs1);
        Assert.assertTrue(bbs1.isPresent());
        Assert.assertTrue(bbs1.get().contains(Vector2L.of(0, 0)));
        Assert.assertFalse(bbs1.get().contains(Vector2L.of(3, 3)));
        Assert.assertTrue(bbs1.get().contains(Vector2L.of(5, 5)));

        final FixedBoundingBoxSingle<Vector2L, Vector2L> sub2 = FixedBoundingBoxSingle.of(Vector2L.of(2, -1), Vector2L.of(3, 6));
        final Optional<FixedBoundingBoxSet<Vector2L, Vector2L>> bbs2 = sut2.subtract(sub2);
        System.out.println(sut2 + " - " + sub2);
        System.out.println("= " + bbs2);
        Assert.assertTrue(bbs2.isPresent());
        Assert.assertTrue(bbs2.get().contains(Vector2L.of(0, 0)));
        Assert.assertFalse(bbs2.get().contains(Vector2L.of(3, 3)));
        Assert.assertTrue(bbs2.get().contains(Vector2L.of(5, 5)));

        final FixedBoundingBoxSingle<Vector2L, Vector2L> sub3 = FixedBoundingBoxSingle.of(Vector2L.of(6, 6), Vector2L.of(7, 7));
        final Optional<FixedBoundingBoxSet<Vector2L, Vector2L>> bbs3 = sut2.subtract(sub3);
        System.out.println(sut2 + " - " + sub3);
        System.out.println("= " + bbs3);
        Assert.assertTrue(bbs3.isPresent());
        Assert.assertTrue(bbs3.get().contains(Vector2L.of(0, 0)));
        Assert.assertTrue(bbs3.get().contains(Vector2L.of(3, 3)));
        Assert.assertTrue(bbs3.get().contains(Vector2L.of(5, 5)));

        final Optional<FixedBoundingBoxSet<Vector2L, Vector2L>> bbs4 = sut2.subtract(sut2);
        System.out.println(sut2 + " - " + sut2);
        System.out.println("= " + bbs4);
        Assert.assertTrue(bbs4.isEmpty());
    }
}
