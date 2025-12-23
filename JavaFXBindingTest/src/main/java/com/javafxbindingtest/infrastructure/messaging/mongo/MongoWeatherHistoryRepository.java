package com.javafxbindingtest.infrastructure.messaging.mongo;

import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.domain.ports.WeatherHistoryRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class MongoWeatherHistoryRepository implements WeatherHistoryRepository {
    // Implementación concreta del puerto WeatherHistoryRepository
    // Esta clase pertenece a infraestructura y usa MongoDB

    private final MongoCollection<Document> collection;
    // Referencia a la colección donde se guardará el historial del clima

    public MongoWeatherHistoryRepository() {
        // Constructor: se ejecuta cuando se crea el repositorio

        MongoClient client =
                MongoClients.create("mongodb://localhost:27017");
        // Crea un cliente MongoDB conectado al servidor local
        // El servidor está levantado con Docker en el puerto 27017

        MongoDatabase db = client.getDatabase("weather_db");
        // Obtiene (o crea si no existe) la base de datos llamada "weather_db"

        this.collection = db.getCollection("weather_history");
        // Obtiene (o crea si no existe) la colección "weather_history"
        // Aquí se guardarán todos los registros históricos del clima
    }

    @Override
    public void save(WeatherInfo weather) {
        // Implementación del método definido en el puerto
        // Guarda un registro de clima en MongoDB

        Document doc = new Document()
                // Se crea un documento BSON (similar a un JSON)

                .append("temperature", weather.getTemperature())
                // Guarda la temperatura

                .append("windSpeed", weather.getWindSpeed())
                // Guarda la velocidad del viento

                .append("humidity", weather.getHumidity())
                // Guarda la humedad

                .append("pressure", weather.getPressure())
                // Guarda la presión atmosférica

                .append("condition", weather.getCondition())
                // Guarda la condición climática (ej: SUNNY, CLOUDY)

                .append("alert", weather.isAlert())
                // Guarda si hay alerta meteorológica

                .append("latitude", weather.getLatitude())
                // Guarda la latitud

                .append("longitude", weather.getLongitude());
        // Guarda la longitud

        collection.insertOne(doc);
        // Inserta el documento en la colección de MongoDB
        // Cada llamada crea un nuevo registro histórico
    }

    @Override
    public List<WeatherInfo> findAll() {
        List<WeatherInfo> result = new ArrayList<>();

        for (Document doc : collection.find()) {
            WeatherInfo weather = new WeatherInfo(
                    doc.getDouble("temperature"),
                    doc.getDouble("windSpeed"),
                    doc.getDouble("humidity"),
                    doc.getDouble("pressure"),
                    doc.getString("condition"),
                    doc.getBoolean("alert"),
                    doc.getDouble("latitude"),
                    doc.getDouble("longitude")
            );

            result.add(weather);
        }

        return result;
    }

    // Comando para levantar MongoDB en Docker:
    // docker run -d --name mongo-weather -p 27017:27017 mongo
}
