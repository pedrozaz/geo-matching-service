package io.github.pedrozaz.geomatchingservice.core;

import io.github.pedrozaz.geomatchingservice.core.impl.GeoUtils;
import io.github.pedrozaz.geomatchingservice.core.impl.QuadTree;
import io.github.pedrozaz.geomatchingservice.core.model.BoundingBox;
import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class GeoEngineService {

    private final QuadTree root;

    public GeoEngineService() {
        this.root = new QuadTree(new BoundingBox(-90, -180, 90, 180));
    }

    public void addDriver(DriverLocation driver) {
        this.root.insert(driver);
    }

    public List<DriverLocation> searchNearby(GeoPoint center, double radiusKm) {
        List<DriverLocation> candidates = new ArrayList<>();

        double latOffset = radiusKm / 111;
        double lonOffset = radiusKm / (111 * Math.cos(Math.toRadians(center.latitude())));

        double minLat = center.latitude() - latOffset;
        double maxLat = center.latitude() + latOffset;
        double minLon = center.longitude() - lonOffset;
        double maxLon = center.longitude() + lonOffset;

        BoundingBox searchBox = new BoundingBox(minLat, minLon, maxLat, maxLon);

        this.root.search(searchBox, candidates);

        List<DriverLocation> finalResult = new ArrayList<>();
        for (DriverLocation driver : candidates) {
            double exactDistance = GeoUtils.distanceKm(center, driver.location());
            if (exactDistance <= radiusKm) {
                finalResult.add(driver);
            }
        }

        return finalResult;
    }
}
