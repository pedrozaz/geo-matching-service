package io.github.pedrozaz.geomatchingservice.core.impl;

import io.github.pedrozaz.geomatchingservice.core.model.BoundingBox;
import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuadTreeTest {

    @Test
    void shouldInsertPointsWithoutSubdividing() {
        BoundingBox world = new BoundingBox(-90, -180, 90, 180);
        QuadTree tree = new QuadTree(world, 4);

        tree.insert(new DriverLocation("d1", new GeoPoint(10, 10)));
        tree.insert(new DriverLocation("d2", new GeoPoint(20, 20)));

        assertFalse(tree.isDivided());
        assertEquals(2, tree.getPointCount());
    }

    @Test
    void shouldSubdivideWhenCapacityExceeded() {
        BoundingBox box = new BoundingBox(0, 0, 10, 10);
        QuadTree tree = new QuadTree(box, 4);

        tree.insert(new DriverLocation("d1", new GeoPoint(1, 1)));
        tree.insert(new DriverLocation("d2", new GeoPoint(2, 2)));
        tree.insert(new DriverLocation("d3", new GeoPoint(8, 8)));
        tree.insert(new DriverLocation("d4", new GeoPoint(9, 9)));

        assertFalse(tree.isDivided(), "Should not divide at exact capacity.");
        tree.insert(new DriverLocation("d5", new GeoPoint(5, 5)));

        assertTrue(tree.isDivided(), "Should divide at exact capacity.");
        assertEquals(0, tree.getPointCount(), "Parent node should be empty after subdivision.");
    }

    @Test
    void shouldIgnorePointsOutsideBoundary() {
        BoundingBox box = new BoundingBox(0, 0, 10, 10);
        QuadTree tree = new QuadTree(box);

        boolean accepted = tree.insert(new DriverLocation("d1", new GeoPoint(50, 50)));

        assertFalse(accepted);
        assertEquals(0, tree.getPointCount());
    }
}
