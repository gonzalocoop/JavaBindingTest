package com.javafxbindingtest.domain.ports;

import com.javafxbindingtest.domain.model.WeatherInfo;

import java.util.function.Consumer;

public interface WeatherDataSource {
    // Puerto del dominio para fuentes de datos
    // Define cómo el dominio RECIBE datos del clima

    void subscribe(Consumer<WeatherInfo> onData);
    // Permite suscribirse a cambios de clima
    // Cada vez que hay datos nuevos, se llama al Consumer

    void stop();
    // Detiene la fuente de datos (cerrar conexiones, hilos, etc.)
}
