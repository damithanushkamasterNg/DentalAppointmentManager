package com.toothcare.controller.payment;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PaymentController {

    @FXML
    private ComboBox<?> appointmentId;

    @FXML
    private TextField balanceToBePaidID;

    @FXML
    private TextField channelDateID;

    @FXML
    private TextField channelTimeID;

    @FXML
    private TextField patientAddressID;

    @FXML
    private TextField patientNameID;

    @FXML
    private TextField patientPhoneNoID;

    @FXML
    private DatePicker paymentDateID;

    @FXML
    private TextField registrationFeeStatusID;

    @FXML
    private TextField treatmentPriceID;

    @FXML
    private TextField treatmentTypeID;
}
