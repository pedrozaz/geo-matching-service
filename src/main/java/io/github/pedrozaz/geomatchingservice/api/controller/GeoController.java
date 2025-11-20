package io.github.pedrozaz.geomatchingservice.api.controller;

import io.github.pedrozaz.geomatchingservice.api.messaging.LocationProducer;
import io.github.pedrozaz.geomatchingservice.core.GeoEngineService;
import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    private final GeoEngineService engineService;
    private final LocationProducer producer;

    @Autowired
    public GeoController(GeoEngineService engineService,  LocationProducer producer) {
        this.engineService = engineService;
        this.producer = producer;
    }

    public record DriverInput(String id, double lat, double lon) {}

    @PostMapping("/drivers")
    public ResponseEntity<Void> updateDriverLocation(@RequestBody DriverInput input) {
        producer.sendLocation(input.id, input.lat, input.lon);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DriverLocation>> searchDrivers(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5.0") double radiusKm) {

        GeoPoint center = new GeoPoint(lat, lon);
        List<DriverLocation> results = engineService.searchNearby(center, radiusKm);

        return ResponseEntity.ok(results);
    }
}
