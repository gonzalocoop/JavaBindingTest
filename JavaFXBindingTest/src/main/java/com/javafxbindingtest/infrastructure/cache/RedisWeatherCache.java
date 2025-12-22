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
    private static final String KEY = "weather:last";

    private final Jedis jedis = new Jedis("localhost", 6379);
    private final ObjectMapper mapper = new ObjectMapper();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public RedisWeatherCache() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                jedis.ping();
            } catch (Exception e) {
                System.out.println("⚠ Redis no disponible");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void save(WeatherInfo weather) {
        try {
            jedis.set(KEY, mapper.writeValueAsString(weather));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<WeatherInfo> getLast() {
        try {
            String json = jedis.get(KEY);
            if (json == null) return Optional.empty();
            return Optional.of(mapper.readValue(json, WeatherInfo.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void close() {
        scheduler.shutdown();
        jedis.close();
    }

    //Comando para que corra en docker
    //docker run -d --name redis-weather -p 6379:6379 redis
}
