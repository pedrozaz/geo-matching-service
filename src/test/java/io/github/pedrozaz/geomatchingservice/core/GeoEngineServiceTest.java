package io.github.pedrozaz.geomatchingservice.core;

import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoEngineServiceTest {

    @Test
    void shouldFindDriversWithinRadius() {
        GeoEngineService service = new GeoEngineService();

        GeoPoint center = new GeoPoint(0, 0);

        service.addDriver(new DriverLocation("A", new GeoPoint(0.009, 0)));
        service.addDriver(new DriverLocation("B", new GeoPoint(1.0, 0)));

        List<DriverLocation> results = service.searchNearby(center, 10);

        assertEquals(1, results.size());
        assertEquals("A", results.getFirst().driverId());
    }

    @Test
    void shouldHandleEmptyResults() {
        GeoEngineService service = new GeoEngineService();
        service.addDriver(new DriverLocation("C", new GeoPoint(10, 10)));

        List<DriverLocation> results = service.searchNearby(new GeoPoint(0, 0), 5);
        assertTrue(results.isEmpty());
    }
}
