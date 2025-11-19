package io.github.pedrozaz.geomatchingservice.core.impl;

import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;

public class GeoUtils {

    private static final double EARTH_RADIUS = 6378.137;

    private GeoUtils() {}

    public static double distanceKm(GeoPoint p1, GeoPoint p2) {

        double dLat = Math.toRadians(p2.latitude() - p1.latitude());
        double dLon = Math.toRadians(p2.longitude() - p1.longitude());

        double lat1 = Math.toRadians(p1.latitude());
        double lat2 = Math.toRadians(p2.latitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
