package com.javafxbindingtest.presentation.view;

import com.javafxbindingtest.domain.WeatherInfo;
import com.javafxbindingtest.presentation.control.WeatherControl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.util.Random;

public class DashboardController {
    @FXML
    private WeatherControl weatherControl;

    private final Random random = new Random();
    private WeatherInfo currentWeather;

    public void initialize() {

        currentWeather = generateRandomWeather();

        weatherControl.bindWeatherInfo(currentWeather);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    updateWeather();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); //que el timeline se repita infinitamente
        timeline.play();
    }

    private WeatherInfo generateRandomWeather() {
        return new WeatherInfo(
                //random.nextDouble() es de 0 a 1
                10 + random.nextDouble() * 20,
                random.nextDouble() * 15,
                20 + random.nextDouble() * 60,
                950 + random.nextDouble() * 100,
                random.nextBoolean() ? "Sunny" : "Cloudy",
                random.nextBoolean()
        );
    }

    private void updateWeather() {
        currentWeather.setTemperature(10 + random.nextDouble() * 20);
        currentWeather.setWindSpeed(random.nextDouble() * 15);
        currentWeather.setHumidity(20 + random.nextDouble() * 60);
        currentWeather.setPressure(950 + random.nextDouble() * 100);
        currentWeather.setCondition(random.nextBoolean() ? "Sunny" : "Cloudy");
        currentWeather.setAlert(random.nextBoolean());
    }
}
