package io.github.pedrozaz.geomatchingservice.core.model;

public record BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {

    public BoundingBox {
        if (minLat > maxLat || minLon > maxLon) {
            throw new IllegalArgumentException("Min coordinates be greater than Max coordinates");
        }
    }

    public boolean contains(GeoPoint point) {
        return point.latitude() >= minLat &&
                point.latitude() <= maxLat &&
                point.longitude() >= minLon &&
                point.longitude() <= maxLon;
    }

    public boolean intersects(BoundingBox other) {
        boolean isDisjoint = other.minLon > this.maxLon ||
                other.maxLon < this.minLon ||
                other.minLat > this.maxLat ||
                other.maxLat < this.minLat;

        return !isDisjoint;
    }

    public GeoPoint getCenter() {
        return new GeoPoint(
                (minLat + maxLat) / 2,
                (minLon + maxLon) / 2
        );
    }
}
