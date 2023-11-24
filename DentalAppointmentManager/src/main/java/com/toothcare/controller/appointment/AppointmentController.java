package com.toothcare.controller.appointment;

import com.toothcare.db.DbConnection;
import com.toothcare.dto.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    public TextField patientIdField;

    public TextField channelTimeField;
    public TextField regFeeStatusField;
    public TextField treatmentIdField;
    public TextField appointmentStatusField;
    public DatePicker channelDateField;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> patientIdCol;

    @FXML
    private TableColumn<Appointment, LocalDate> channelDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> channelTimeCol;

    @FXML
    private TableColumn<Appointment, Boolean> regFeeStatusCol;

    @FXML
    private TableColumn<Appointment, Integer> treatmentIdCol;

    @FXML
    private TableColumn<Appointment, String> appointmentStatusCol;

    // Other methods for handling actions, loading data, etc. will go here

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns and load data
        initializeTableColumns();
        loadAllAppointments();
    }

    private void initializeTableColumns() {
        // Set cell value factories for columns
        patientIdCol.setCellValueFactory(cellData -> cellData.getValue().patientIdProperty().asObject());
        channelDateCol.setCellValueFactory(cellData -> cellData.getValue().channelDateProperty());
        channelTimeCol.setCellValueFactory(cellData -> cellData.getValue().channelTimeProperty());
        regFeeStatusCol.setCellValueFactory(cellData -> cellData.getValue().regFeeStatusProperty().asObject());
        treatmentIdCol.setCellValueFactory(cellData -> cellData.getValue().treatmentIdProperty().asObject());
        appointmentStatusCol.setCellValueFactory(cellData -> cellData.getValue().appointmentStatusProperty());

        // Set column width or any other properties as needed
        // ...

        // Add columns to the TableView
        appointmentTableView.getColumns().setAll(
                patientIdCol, channelDateCol, channelTimeCol,
                regFeeStatusCol, treatmentIdCol, appointmentStatusCol
        );
    }


    private void loadAllAppointments() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM appointment";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.patientIdProperty().set(resultSet.getInt("patient_id"));
                appointment.channelDateProperty().set(resultSet.getDate("channel_date").toLocalDate());
                appointment.channelTimeProperty().set(resultSet.getTime("channel_time").toLocalTime());
                appointment.regFeeStatusProperty().set(resultSet.getBoolean("reg_fee_status"));
                appointment.treatmentIdProperty().set(resultSet.getInt("treatment_id"));
                appointment.appointmentStatusProperty().set(resultSet.getString("appointment_status"));

                appointmentList.add(appointment);
            }

            // Clear existing items and add the fetched data to the TableView
            appointmentTableView.getItems().clear();
            appointmentTableView.getItems().addAll(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    @FXML
    void clickOnAppointmentAddBtn(ActionEvent event) {
        int patientId = Integer.parseInt(patientIdField.getText());
        LocalDate channelDate = channelDateField.getValue();
        LocalTime channelTime = LocalTime.parse(channelTimeField.getCharacters());
        boolean regFeeStatus = Boolean.parseBoolean(regFeeStatusField.getText());
        int treatmentId = Integer.parseInt(treatmentIdField.getText());
        String appointmentStatus = appointmentStatusField.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            // SQL query with correct syntax and parameter placeholders
            String sql = "INSERT INTO appointment (patient_id, channel_date, channel_time, reg_fee_status, treatment_id, appointment_status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            pstm.setInt(1, patientId);
            pstm.setDate(2, Date.valueOf(channelDate));
            pstm.setTime(3, Time.valueOf(channelTime));
            pstm.setBoolean(4, regFeeStatus);
            pstm.setInt(5, treatmentId);
            pstm.setString(6, appointmentStatus);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Appointment saved successfully!").show();
                loadAllAppointments();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save appointment.").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    public void updateAppointment(ActionEvent actionEvent) {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an appointment to update.").show();
            return;
        }

        // Get data from UI components (e.g., patientIdField, channelDateField, etc.)
        int patientId = Integer.parseInt(patientIdField.getText());
        // Get other field values...

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "UPDATE appointment SET patient_id = ?, channel_date = ?, ... WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            pstm.setInt(1, patientId);
            // Set other parameters...

            pstm.setInt(6, selectedAppointment.getId().getValue()); // Assuming appointment ID is obtained from the selected appointment

            boolean isUpdated = pstm.executeUpdate() > 0;
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Appointment updated successfully!").show();
                loadAllAppointments();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    @FXML
    public void resetAppointmentDetail(ActionEvent actionEvent) {
        patientIdField.setText("");
        channelTimeField.setText("");
        regFeeStatusField.setText("");
        treatmentIdField.setText("");
        appointmentStatusField.setText("");
    }


    @FXML
    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an appointment to delete.").show();
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM appointment WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, selectedAppointment.getId().getValue()); // Assuming appointment ID is obtained from the selected appointment

            boolean isDeleted = pstm.executeUpdate() > 0;
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Appointment deleted successfully!").show();
                loadAllAppointments();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void searchAppointmentById(ActionEvent event) {
        String appointmentId = patientIdField.getText();
        if (appointmentId == null || appointmentId.isEmpty()) {
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM appointment WHERE patient_id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, appointmentId);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                int patientId = resultSet.getInt("patient_id");
                // Fetch other fields as needed from the appointment table

                // Set fetched fields or perform any operations
                // Example: setAppointmentFields(patientId, ...);
            } else {
                new Alert(Alert.AlertType.WARNING, "Appointment not found!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
