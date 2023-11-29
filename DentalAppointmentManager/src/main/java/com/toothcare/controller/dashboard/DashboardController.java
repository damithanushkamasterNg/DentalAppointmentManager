package com.toothcare.controller.dashboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashboardController {

    public Button paymentButtonId;

    @FXML
    void clickPatientManagementBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/patient/patient_form.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setWidth(768.0);
        stage.setHeight(600);
        stage.setScene(scene);

        stage.setTitle("Patient Management");
        stage.show();
    }

    public void clickAppointmentManagementBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/appointment/appointment_form.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setWidth(768.0);
        stage.setHeight(600);
        stage.setScene(scene);

        stage.setTitle("Appointment Management");
        stage.show();
    }

    @FXML
    public void clickPaymentManagementBtn(ActionEvent actionEvent) throws IOException {
        System.out.println("call");
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/payment/payment.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setWidth(768.0);
        stage.setHeight(600);
        stage.setScene(scene);

        stage.setTitle("Payment Management");
        stage.show();
    }
}