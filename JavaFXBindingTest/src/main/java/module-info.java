module com.javafxbindingtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires jeromq;
    requires com.fasterxml.jackson.databind;
    requires com.google.protobuf;


    exports com.javafxbindingtest.app;
    exports com.javafxbindingtest.domain;


    opens com.javafxbindingtest.app to javafx.fxml;
    opens com.javafxbindingtest.presentation.control to javafx.fxml;
    opens com.javafxbindingtest.presentation.view to javafx.fxml;
}