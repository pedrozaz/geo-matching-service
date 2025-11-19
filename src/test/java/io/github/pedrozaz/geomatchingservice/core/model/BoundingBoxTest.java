package io.github.pedrozaz.geomatchingservice.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoundingBoxTest {

    @Test
    void shouldIntersectOverlappingBoxes() {
        BoundingBox boxA = new BoundingBox(0, 0, 10, 10);
        BoundingBox boxB = new BoundingBox(5, 5, 15, 15);

        assertTrue(boxA.intersects(boxB));
        assertTrue(boxB.intersects(boxA));
    }

    @Test
    void shouldNotIntersectDisjointBoxes() {
        BoundingBox boxA = new BoundingBox(0, 0, 10, 10);
        BoundingBox boxB = new BoundingBox(20, 20, 30, 30);

        assertFalse(boxA.intersects(boxB));
    }

    @Test
    void shouldContainPointInside() {
        BoundingBox box = new BoundingBox(0, 0, 10, 10);
        GeoPoint inside = new GeoPoint(5, 5);

        assertTrue(box.contains(inside));
    }
}
