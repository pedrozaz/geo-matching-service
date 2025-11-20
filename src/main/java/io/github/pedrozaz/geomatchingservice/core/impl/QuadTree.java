package io.github.pedrozaz.geomatchingservice.core.impl;

import io.github.pedrozaz.geomatchingservice.core.model.BoundingBox;
import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private static final int DEFAULT_CAPACITY = 4;
    private static final int MAX_DEPTH = 20;

    private final BoundingBox boundary;
    private final int capacity;
    private final int currentDepth;
    private final List<DriverLocation> points;

    @Getter
    private boolean divided = false;

    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;

    public QuadTree(BoundingBox boundary) {
        this(boundary, DEFAULT_CAPACITY, 0);
    }

    public QuadTree(BoundingBox boundary, int capacity, int currentDepth) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.points = new ArrayList<>(capacity);
        this.currentDepth = currentDepth;
    }

    public boolean insert(DriverLocation driver) {
        if (!this.boundary.contains(driver.location())) {
            return false;
        }

        if (this.points.size() < capacity || this.currentDepth >= MAX_DEPTH) {
            if (!this.divided) {
                this.points.add(driver);
                return true;
            }
        }

        if (!this.divided) {
            subdivide();
        }

        if (this.northWest.insert(driver)) return true;
        if (this.northEast.insert(driver)) return true;
        if (this.southWest.insert(driver)) return true;
        if (this.southEast.insert(driver)) return true;

        return false;
    }

    private void subdivide() {
        GeoPoint center = this.boundary.getCenter();
        double cLat = center.latitude();
        double cLon = center.longitude();

        int nextDepth = this.currentDepth + 1;

        this.northWest = new QuadTree(new BoundingBox(cLat, this.boundary.minLon(), this.boundary.maxLat(), cLon), capacity, nextDepth);
        this.northEast = new QuadTree(new BoundingBox(cLat, cLon, this.boundary.maxLat(), this.boundary.maxLon()), capacity, nextDepth);
        this.southWest = new QuadTree(new BoundingBox(this.boundary.minLat(), this.boundary.minLon(), cLat, cLon), capacity, nextDepth);
        this.southEast = new QuadTree(new BoundingBox(this.boundary.minLat(), cLon, cLat, this.boundary.maxLon()), capacity, nextDepth);

        this.divided = true;

        for (DriverLocation p : this.points) {
            boolean inserted = this.northWest.insert(p) ||
                    this.northEast.insert(p) ||
                    this.southWest.insert(p) ||
                    this.southEast.insert(p);

            if (!inserted) {
                throw new IllegalStateException("Failed to redistribute point: " + p);
            }
        }
        this.points.clear();
    }

    public void search(BoundingBox searchRegion, List<DriverLocation> results) {
        if (!this.boundary.intersects(searchRegion)) {
            return;
        }

        if (!this.points.isEmpty()) {
           for (DriverLocation driver : this.points) {
               if (searchRegion.contains(driver.location())) {
                   results.add(driver);
               }
           }
        }

        if (this.divided) {
            this.northWest.search(searchRegion, results);
            this.northEast.search(searchRegion, results);
            this.southWest.search(searchRegion, results);
            this.southEast.search(searchRegion, results);
        }
    }

    public int getPointCount() { return points.size(); }
    public List<DriverLocation> getPoints() { return new ArrayList<>(points); }
}
