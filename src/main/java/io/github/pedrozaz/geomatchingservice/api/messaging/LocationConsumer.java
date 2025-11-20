package io.github.pedrozaz.geomatchingservice.api.messaging;

import io.github.pedrozaz.geomatchingservice.core.GeoEngineService;
import io.github.pedrozaz.geomatchingservice.core.model.DriverLocation;
import io.github.pedrozaz.geomatchingservice.core.model.GeoPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationConsumer {

    private final GeoEngineService geoEngineService;

    public LocationConsumer(GeoEngineService geoEngineService) {
        this.geoEngineService = geoEngineService;
    }

    @KafkaListener(topics = "driver-locations", groupId = "geo-engine-group")
    public void consumeLocationEvent(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 3) return;

            String id = parts[0];
            double lat = Double.parseDouble(parts[1]);
            double lon = Double.parseDouble(parts[2]);

            DriverLocation location = new DriverLocation(id, new GeoPoint(lat, lon));
            geoEngineService.addDriver(location);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
