package com.javafxbindingtest.app;

import com.javafxbindingtest.infrastructure.cache.RedisWeatherCache;
import com.javafxbindingtest.infrastructure.messaging.zmq.ZmqWeatherClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WeatherApp extends Application {

    //Dependencias con ciclo de vida
    private final ZmqWeatherClient zmqWeatherClient = new ZmqWeatherClient();
    private final RedisWeatherCache redisWeatherCache = new RedisWeatherCache();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafxbindingtest/presentation/view/DashboardView.fxml"));
        stage.setWidth(400);
        stage.setHeight(400);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Weather Dashboard");
        stage.show();
    }

    //Apagar
    @Override
    public void stop() {
        zmqWeatherClient.stop();
        redisWeatherCache.close();
    }

    public static void main(String[] args) {

        launch();
    }
}