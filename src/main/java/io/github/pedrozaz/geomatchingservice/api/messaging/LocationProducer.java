package io.github.pedrozaz.geomatchingservice.api.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationProducer {
    private static final String TOPIC = "driver-locations";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public LocationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLocation(String driverId, double lat, double lon) {
        String payload = String.format("%s,%f,%f", driverId, lat, lon);
        kafkaTemplate.send(TOPIC, driverId, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error while sending location for driver: {}", driverId, ex);
                    }
                });
    }
}
