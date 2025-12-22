package com.javafxbindingtest.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WeatherApp extends Application {

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

    public static void main(String[] args) {
        launch();
    }
}