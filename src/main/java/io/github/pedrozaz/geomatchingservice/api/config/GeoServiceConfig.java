package io.github.pedrozaz.geomatchingservice.api.config;

import io.github.pedrozaz.geomatchingservice.core.GeoEngineService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoServiceConfig {

    @Bean
    public GeoEngineService geoEngineService() {
        return new GeoEngineService();
    }
}
