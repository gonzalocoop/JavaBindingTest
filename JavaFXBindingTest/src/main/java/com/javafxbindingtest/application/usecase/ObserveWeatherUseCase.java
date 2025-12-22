package com.javafxbindingtest.application.usecase;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherCache;
import com.javafxbindingtest.domain.ports.WeatherDataSource;

import java.util.function.Consumer;

public class ObserveWeatherUseCase {

    private final WeatherDataSource dataSource;
    private final WeatherCache cache;

    public ObserveWeatherUseCase(
            WeatherDataSource dataSource,
            WeatherCache cache
    ) {
        this.dataSource = dataSource;
        this.cache = cache;
    }

    public void start(Consumer<WeatherInfo> onWeatherChanged) {

        //Emitir último valor cacheado (si existe)
        cache.getLast().ifPresent(onWeatherChanged);

        //Escuchar nuevos datos
        dataSource.subscribe(weather -> {
            cache.save(weather);
            onWeatherChanged.accept(weather);
        });
    }

    public void stop() {
        dataSource.stop();
    }
}
