module com.javafxbindingtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires jeromq;
    requires com.fasterxml.jackson.databind;
    requires com.google.protobuf;
    requires redis.clients.jedis;


    exports com.javafxbindingtest.app;


    opens com.javafxbindingtest.app to javafx.fxml;
    opens com.javafxbindingtest.presentation.control to javafx.fxml;
    opens com.javafxbindingtest.presentation.view to javafx.fxml;
    exports com.javafxbindingtest.domain.model;
    opens com.javafxbindingtest.presentation.controller to javafx.fxml;
}