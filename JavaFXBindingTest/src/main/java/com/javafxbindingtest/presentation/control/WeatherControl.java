package com.javafxbindingtest.presentation.control;

import com.javafxbindingtest.domain.WeatherInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WeatherControl extends AnchorPane {
    @FXML private Label lblTemperature;
    @FXML private Label lblWind;
    @FXML private Label lblHumidity;
    @FXML private Label lblPressure;
    @FXML private Label lblCondition;
    @FXML private Label lblAlert;

    private final ObjectProperty<WeatherInfo> weatherInfo = new SimpleObjectProperty<>();

    public WeatherControl() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafxbindingtest/presentation/control/WeatherControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {

        weatherInfo.addListener((observable, oldVariable, newVariable) -> {
            if (newVariable != null) {
                bindWeatherInfo(newVariable);
            }
        });
    }

    public void bindWeatherInfo(WeatherInfo info) {
        lblTemperature.textProperty().bind(info.temperatureProperty().asString());
        lblWind.textProperty().bind(info.windSpeedProperty().asString());
        lblHumidity.textProperty().bind(info.humidityProperty().asString());
        lblPressure.textProperty().bind(info.pressureProperty().asString());
        lblCondition.textProperty().bind(info.conditionProperty());
        lblAlert.textProperty().bind(info.alertProperty().asString());
    }


}
