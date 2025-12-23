package com.javafxbindingtest.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherCache;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RedisWeatherCache implements WeatherCache {
    // Implementación concreta del cache usando Redis

    private static final String KEY = "weather:last";
    // Clave donde se guarda el último clima en Redis

    private final Jedis jedis = new Jedis("localhost", 6379);
    // Conexión a Redis local

    private final ObjectMapper mapper = new ObjectMapper();
    // Mapper JSON ↔ objeto Java

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    // Executor que ejecuta tareas periódicas en segundo plano

    public RedisWeatherCache() {
        // Constructor

        scheduler.scheduleAtFixedRate(() -> {
            // Tarea que se ejecuta cada 5 segundos

            try {
                jedis.ping();
                // Verifica si Redis sigue disponible
            } catch (Exception e) {
                System.out.println("Redis no disponible");
                // Mensaje si Redis se cae
            }
        }, 0, 5, TimeUnit.SECONDS);
        // Empieza inmediatamente y repite cada 5 segundos
    }

    @Override
    public void save(WeatherInfo weather) {
        // Guarda el clima en Redis
        try {
            jedis.set(KEY, mapper.writeValueAsString(weather));
            // Convierte WeatherInfo a JSON y lo guarda
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<WeatherInfo> getLast() {
        // Recupera el último clima
        try {
            String json = jedis.get(KEY);
            // Lee el JSON desde Redis

            if (json == null) return Optional.empty();
            // Si no hay nada guardado

            return Optional.of(mapper.readValue(json, WeatherInfo.class));
            // Convierte JSON → WeatherInfo
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void close() {
        // Cierra recursos
        scheduler.shutdown();
        // Detiene el scheduler
        jedis.close();
        // Cierra conexión Redis
    }

    //Comando para que corra en docker
    //docker run -d --name redis-weather -p 6379:6379 redis
}
