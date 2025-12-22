package com.javafxbindingtest.presentation.controller;

import com.javafxbindingtest.application.usecase.ObserveWeatherUseCase;
import com.javafxbindingtest.domain.model.WeatherInfo;
import com.javafxbindingtest.infrastructure.cache.RedisWeatherCache;
import com.javafxbindingtest.infrastructure.messaging.zmq.ZmqWeatherClient;
import com.javafxbindingtest.presentation.view.WeatherControl;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;



public class DashboardController {
    @FXML
    private WeatherControl weatherControl;

    private final ObjectProperty<WeatherInfo> weather = new SimpleObjectProperty<>();
    private ObserveWeatherUseCase useCase;

    public void initialize() {
        weatherControl.weatherInfoProperty().bind(weather);

        useCase = new ObserveWeatherUseCase( new ZmqWeatherClient(),
                new RedisWeatherCache());

        useCase.start(info ->
                Platform.runLater(() -> weather.set(info))
        );
    }
}
