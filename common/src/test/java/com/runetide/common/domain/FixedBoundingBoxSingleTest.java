package com.runetide.common.domain;

import com.runetide.common.domain.geometry.Direction3;
import com.runetide.common.domain.geometry.locus.BoundingBox;
import com.runetide.common.domain.geometry.locus.FixedBoundingBoxSingle;
import com.runetide.common.domain.geometry.vector.Vector;
import com.runetide.common.domain.geometry.vector.Vector2L;
import com.runetide.common.domain.geometry.vector.Vector3L;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FixedBoundingBoxSingleTest {
    private final Vector3L vec1 = Vector.of(0, 0, 0);
    private final Vector3L vec2 = Vector.of(5, 5, 5);
    private final FixedBoundingBoxSingle<Vector2L, Vector2L> sut2 = BoundingBox.of(vec1.toVec2D(), vec2.toVec2D());
    private final FixedBoundingBoxSingle<Vector3L, Vector3L> sut3 = BoundingBox.of(vec1, vec2);


    @Test
    public void testDimensions() {
        Assert.assertEquals(sut3.getDimensions(), Vector3L.of(6, 6, 6));
    }

    @Test
    public void testUnion() {
        final var add = BoundingBox.of(Vector.of(2, 2), Vector.of(6, 6));
        final var bbs = sut2.union(add);
        System.out.println(sut2 + " union " + add);
        System.out.println("= " + bbs);
        Assert.assertTrue(bbs.contains(Vector.of(0, 0)));
        Assert.assertTrue(bbs.contains(Vector.of(3, 3)));
        Assert.assertTrue(bbs.contains(Vector.of(6, 6)));
        Assert.assertFalse(bbs.contains(Vector.of(0, 6)));
        Assert.assertFalse(bbs.contains(Vector.of(6, 0)));
    }

    @Test
    public void testSubtract() {
        final var sub1 = BoundingBox.of(Vector.of(2, 2), Vector.of(3, 3));
        final var bbs1 = sut2.subtract(sub1);
        System.out.println(sut2 + " - " + sub1);
        System.out.println("= " + bbs1);
        Assert.assertTrue(bbs1.isPresent());
        Assert.assertTrue(bbs1.get().contains(Vector.of(0, 0)));
        Assert.assertFalse(bbs1.get().contains(Vector.of(3, 3)));
        Assert.assertTrue(bbs1.get().contains(Vector.of(5, 5)));

        final var sub2 = BoundingBox.of(Vector.of(2, -1), Vector.of(3, 6));
        final var bbs2 = sut2.subtract(sub2);
        System.out.println(sut2 + " - " + sub2);
        System.out.println("= " + bbs2);
        Assert.assertTrue(bbs2.isPresent());
        Assert.assertTrue(bbs2.get().contains(Vector.of(0, 0)));
        Assert.assertFalse(bbs2.get().contains(Vector.of(3, 3)));
        Assert.assertTrue(bbs2.get().contains(Vector.of(5, 5)));

        final var sub3 = BoundingBox.of(Vector.of(6, 6), Vector.of(7, 7));
        final var bbs3 = sut2.subtract(sub3);
        System.out.println(sut2 + " - " + sub3);
        System.out.println("= " + bbs3);
        Assert.assertTrue(bbs3.isPresent());
        Assert.assertTrue(bbs3.get().contains(Vector.of(0, 0)));
        Assert.assertTrue(bbs3.get().contains(Vector.of(3, 3)));
        Assert.assertTrue(bbs3.get().contains(Vector.of(5, 5)));

        final var bbs4 = sut2.subtract(sut2);
        System.out.println(sut2 + " - " + sut2);
        System.out.println("= " + bbs4);
        Assert.assertTrue(bbs4.isEmpty());
    }

    @Test
    public void testExpandContract() {
        final var expanded = sut3.expand(Direction3.NORTH_WEST.scale(5L));
        System.out.println(sut3 + " expand " + Direction3.NORTH_EAST.asVectorL());
        System.out.println("= " + expanded);
        Assert.assertEquals(expanded, BoundingBox.of(Vector.of(-5, 0, 0), Vector.of(5, 5, 10)));

        final var contracted = expanded.contract(Direction3.NORTH_WEST.scale(5L));
        System.out.println(expanded + " contract " + Direction3.NORTH_EAST.asVectorL());
        System.out.println("= " + contracted);
        Assert.assertEquals(contracted, sut3);

        final var outset = sut3.outset(Vector.of(1, 1, 1));
        System.out.println(sut3 + " outset " + Vector.of(1, 1, 1));
        System.out.println("= " + outset);
        Assert.assertEquals(outset, BoundingBox.of(Vector.of(-1, -1, -1), Vector.of(6, 6, 6)));
    }
}
