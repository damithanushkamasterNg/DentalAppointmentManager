package com.toothcare.controller;
import com.toothcare.dto.Appointment;
import com.toothcare.dto.Treatment;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class AppointmentController {

    public Button viewAppointmentsButton;
    public Button searchAppointmentButton;
    public Button makeAppointmentButton;
    public Button updateAppointmentButton;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Treatment> treatments = FXCollections.observableArrayList();

    @FXML
    private TextField appointmentIdField;
    @FXML
    private TextField patientNameField;
    @FXML
    private TextField patientAddressField;
    @FXML
    private TextField patientPhoneField;
    @FXML
    private ComboBox<String> appointmentDateField;
    @FXML
    private ComboBox<String> appointmentTimeField;
    @FXML
    private TextField treatmentTypeField;
    @FXML
    private TextField treatmentCostField;
    @FXML
    private Label totalFeeLabel;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTimeColumn;
    @FXML
    private TableColumn<Appointment, String> treatmentTypeColumn;
    @FXML
    private TableColumn<Appointment, Double> treatmentCostColumn;

    public void initialize() {
        // Initialize appointment date and time fields
        appointmentDateField.setItems(FXCollections.observableArrayList("Monday", "Wednesday", "Saturday", "Sunday"));
        appointmentTimeField.setItems(FXCollections.observableArrayList("06:00 PM - 09:00 PM", "03:00 PM - 10:00 PM"));

        // Initialize appointments table
        appointmentsTable.setItems(appointments);
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty());
        patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());
        appointmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentDateProperty());
        appointmentTimeColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeProperty());
        treatmentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().treatmentTypeProperty());
        treatmentCostColumn.setCellValueFactory(cellData -> {
            DoubleProperty value = cellData.getValue().treatmentCostProperty();
            return Bindings.createObjectBinding(() -> value.get());
        });

    }

    @FXML
    private void makeAppointmentButtonAction(ActionEvent event) {
        // Validate patient information
        if (patientNameField.getText().isEmpty() || patientAddressField.getText().isEmpty() || patientPhoneField.getText().isEmpty()) {
            showAlert("Please enter all patient information.");
            return;
        }

        // Validate appointment date and time
        if (appointmentDateField.getSelectionModel().getSelectedItem() == null || appointmentTimeField.getSelectionModel().getSelectedItem() == null) {
            showAlert("Please select an appointment date and time.");
            return;
        }

        // Validate treatment type
        if (treatmentTypeField.getText().isEmpty()) {
            showAlert("Please enter the treatment type.");
            return;
        }

        // Create new appointment
        Appointment appointment = new Appointment();
        appointment.setPatientName(patientNameField.getText());
        appointment.setPatientAddress(patientAddressField.getText());
        appointment.setPatientPhone(patientPhoneField.getText());
        appointment.setAppointmentDate(appointmentDateField.getSelectionModel().getSelectedItem());
        appointment.setAppointmentTime(appointmentTimeField.getSelectionModel().getSelectedItem());
        appointment.setTreatmentType(treatmentTypeField.getText());

        // Calculate treatment cost
        Treatment treatment = new Treatment(appointment.getTreatmentType());
        double treatmentCost = treatment.getCost();
        treatmentCostField.setText(String.valueOf(treatmentCost));

        // Accept registration fee
        TextInputDialog registrationFeeDialog = new TextInputDialog();
        registrationFeeDialog.setHeaderText("Enter registration fee");
        registrationFeeDialog.setContentText("Please enter the registration fee (LKR):");
        Optional<String> registrationFeeOption = registrationFeeDialog.showAndWait();
        if (registrationFeeOption.isPresent()) {
            double registrationFee = Double.parseDouble(registrationFeeOption.get());
            appointment.setRegistrationFee(registrationFee);

            // Add appointment to list
            appointments.add(appointment);

            // Clear fields
            clearFields();

            // Show confirmation message
            showAlert("Appointment created successfully.");
        } else {
            showAlert("Registration fee is required.");
        }
    }

    @FXML
    private void updateAppointmentButtonAction(ActionEvent event) {
        // Search for appointment by appointment ID
        String appointmentId = appointmentIdField.getText();
        if (appointmentId.isEmpty()) {
            showAlert("Please enter an appointment ID.");
            return;
        }

        Appointment appointment = null;
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId)) {
                appointment = appt;
                break;
            }
        }

        if (appointment == null) {
            showAlert("Appointment with ID " + appointmentId + " not found.");
            return;
        }

        // Update appointment details
        appointment.setPatientName(patientNameField.getText());
        appointment.setPatientAddress(patientAddressField.getText());
        appointment.setPatientPhone(patientPhoneField.getText());
        appointment.setAppointmentDate(appointmentDateField.getSelectionModel().getSelectedItem());
        appointment.setAppointmentTime(appointmentTimeField.getSelectionModel().getSelectedItem());
        appointment.setTreatmentType(treatmentTypeField.getText());

        // Calculate treatment cost
        Treatment treatment = new Treatment(appointment.getTreatmentType());
        double treatmentCost = treatment.getCost();
        treatmentCostField.setText(String.valueOf(treatmentCost));

        // Update appointment in list
        appointmentsTable.refresh();

        // Clear fields
        clearFields();

        // Show confirmation message
        showAlert("Appointment updated successfully.");
    }

    @FXML
    private void viewAppointmentsButtonAction(ActionEvent event) {
        // Filter appointments by date
        String selectedDate = appointmentDateField.getSelectionModel().getSelectedItem();
        if (selectedDate != null) {
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
            for (Appointment appointment : appointments) {
                if (appointment.getAppointmentDate().equals(selectedDate)) {
                    filteredAppointments.add(appointment);
                }
            }

            appointmentsTable.setItems(filteredAppointments);
        } else {
            appointmentsTable.setItems(appointments);
        }
    }

    @FXML
    private void searchAppointmentButtonAction(ActionEvent event) {
        // Search for appointment by appointment ID
        String appointmentId = appointmentIdField.getText();
        if (appointmentId.isEmpty()) {
            showAlert("Please enter an appointment ID.");
            return;
        }

        Appointment appointment = null;
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId)) {
                appointment = appt;
                break;
            }
        }

        if (appointment == null) {
            showAlert("Appointment with ID " + appointmentId + " not found.");
            return;
        }

        // Display appointment details
        patientNameField.setText(appointment.getPatientName());
        patientAddressField.setText(appointment.getPatientAddress());
        patientPhoneField.setText(appointment.getPatientPhone());
        appointmentDateField.getSelectionModel().select(appointment.getAppointmentDate());
        appointmentTimeField.getSelectionModel().select(appointment.getAppointmentTime());
        treatmentTypeField.setText(appointment.getTreatmentType());
        treatmentCostField.setText(String.valueOf(appointment.getTreatmentCost()));
    }

    @FXML
    private void clearFieldsButtonAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        appointmentIdField.clear();
        patientNameField.clear();
        patientAddressField.clear();
        patientPhoneField.clear();
        appointmentDateField.getSelectionModel().clearSelection();
        appointmentTimeField.getSelectionModel().clearSelection();
        treatmentTypeField.clear();
        treatmentCostField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ... (Previous methods)

    @FXML
    private void acceptPaymentButtonAction(ActionEvent event) {
        // Calculate total fee for treatments after the appointment
        double totalFee = calculateTotalFee();

        // View invoice of payment
        viewInvoice(totalFee);

        // Clear fields after payment
        clearFields();

        // Show confirmation message
        showAlert("Payment accepted successfully.");
    }

    private double calculateTotalFee() {
        // Calculate total fee for treatments after the appointment
        double totalFee = 0.0;
        for (Appointment appointment : appointments) {
            totalFee += appointment.getTreatmentCost();
        }
        totalFeeLabel.setText(String.valueOf(totalFee)); // Display total fee in UI if needed
        return totalFee;
    }

    private void viewInvoice(double totalFee) {
        // View invoice of payment
        // This method could open a new window or display an alert showing the invoice details
        StringBuilder invoice = new StringBuilder();
        invoice.append("Invoice Details:\n");
        invoice.append("Total Fee: LKR ").append(totalFee).append("\n");
        // Add more details to the invoice as needed

        showAlert(invoice.toString()); // Display invoice details in an alert
    }
}


