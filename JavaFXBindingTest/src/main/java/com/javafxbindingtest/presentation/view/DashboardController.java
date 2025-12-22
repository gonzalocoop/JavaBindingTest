package com.javafxbindingtest.presentation.view;

import com.javafxbindingtest.domain.WeatherInfo;
import com.javafxbindingtest.infrastructure.messaging.ZmqWeatherClient;
import com.javafxbindingtest.presentation.control.WeatherControl;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;

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
            //Platform.runLater(() -> currentWeather.set(info));
            Platform.runLater(() -> {
                // Convertimos de protobuf a dominio, osea este si es tipo WeatherProtobuf domain, para poder guardarlo en el WeatherInfo ObjectProperty
                WeatherInfo domainInfo = new WeatherInfo(
                        info.getTemperature(),
                        info.getWindSpeed(),
                        info.getHumidity(),
                        info.getPressure(),
                        info.getCondition(),
                        info.getAlert(),
                        info.getLatitude(),
                        info.getLongitude()
                );
                currentWeather.set(domainInfo);
            });
        });

    }
}
