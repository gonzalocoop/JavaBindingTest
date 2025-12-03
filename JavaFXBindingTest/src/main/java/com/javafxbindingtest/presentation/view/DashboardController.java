package com.javafxbindingtest.presentation.view;

import com.javafxbindingtest.domain.WeatherInfo;
import com.javafxbindingtest.infrastructure.messaging.ZmqWeatherClient;
import com.javafxbindingtest.presentation.control.WeatherControl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.util.Random;

public class DashboardController {
    @FXML
    private WeatherControl weatherControl;

    private final Random random = new Random();
    private ObjectProperty<WeatherInfo> currentWeather;
    private ZmqWeatherClient zmqClient;

    public void initialize() {

        currentWeather = new SimpleObjectProperty<>(); // generar Object Property

        weatherControl.weatherInfoProperty().bind(currentWeather);
        zmqClient = new ZmqWeatherClient();
        // Escuchar ZeroMQ
        zmqClient.startListening("tcp://localhost:5555", info -> {
            Platform.runLater(() -> currentWeather.set(info));
        });
        /*
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    updateWeather();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); //que el timeline se repita infinitamente
        timeline.play();

         */
    }

    /*private ObjectProperty<WeatherInfo> generateRandomWeather() {
        return new WeatherInfo(
                //random.nextDouble() es de 0 a 1
                10 + random.nextDouble() * 20,
                random.nextDouble() * 15,
                20 + random.nextDouble() * 60,
                950 + random.nextDouble() * 100,
                random.nextBoolean() ? "Sunny" : "Cloudy",
                random.nextBoolean()
        );
    }*/
    WeatherInfo info;
    private void updateWeather() {
        info = new WeatherInfo();
        info.setTemperature(10 + random.nextDouble() * 20);
        info.setWindSpeed(random.nextDouble() * 15);
        info.setHumidity(20 + random.nextDouble() * 60);
        info.setPressure(950 + random.nextDouble() * 100);
        info.setCondition(random.nextBoolean() ? "Sunny" : "Cloudy");
        info.setAlert(random.nextBoolean());

        currentWeather.set(info);
    }
}
