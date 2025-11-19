package io.github.pedrozaz.geomatchingservice.core.model;

import java.util.Objects;

public record DriverLocation(String driverId, GeoPoint location) {

    public DriverLocation {
        Objects.requireNonNull(driverId, "driverId cannot be null");
        Objects.requireNonNull(location, "location cannot be null");
    }
}
