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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {


    public TextField channelTimeField;
    public TextField treatmentIdField;
    public TextField appointmentStatusField;
    public DatePicker channelDateField;


    @FXML
    public TextField balanceToBePaidId;

    @FXML
    public TextField treatmentFeeId;
    @FXML
    public ComboBox<String> patientIdForSearch;

    @FXML
    private ComboBox<String> patientIdField;


    @FXML
    private ComboBox<String> treatmentFieldId;

    @FXML
    private ComboBox<String> regFeeStatusField;

    private final Map<String, Boolean> regFeeStatusMap = new HashMap<>();

    private final Map<String, Integer> treatmentMap = new HashMap<>();
    private final Map<String, Integer> patientMap = new HashMap<>();


    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, String> patientIdCol;

    @FXML
    private TableColumn<Appointment, LocalDate> channelDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> channelTimeCol;

    @FXML
    private TableColumn<Appointment, String> regFeeStatusCol;

    @FXML
    private TableColumn<Appointment, String> treatmentIdCol;

    @FXML
    private TableColumn<Appointment, String> appointmentStatusCol;


    // Other methods for handling actions, loading data, etc. will go here

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns and load data
        initializeTableColumns();
        loadAllAppointments();
        loadPatients();
        loadTreatment();
        loadRegFeeStatus();
    }

    private void initializeTableColumns() {
        // Set cell value factories for columns
        patientIdCol.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());
        channelDateCol.setCellValueFactory(cellData -> cellData.getValue().channelDateProperty());
        channelTimeCol.setCellValueFactory(cellData -> cellData.getValue().channelTimeProperty());
        regFeeStatusCol.setCellValueFactory(cellData -> cellData.getValue().registrationStatusStringProperty());
        treatmentIdCol.setCellValueFactory(cellData -> cellData.getValue().treatmentTypeProperty());
        appointmentStatusCol.setCellValueFactory(cellData -> cellData.getValue().appointmentStatusProperty());

        appointmentTableView.getColumns().setAll(
                patientIdCol, channelDateCol, channelTimeCol,
                regFeeStatusCol, treatmentIdCol, appointmentStatusCol
        );
    }


    private void loadAllAppointments() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT a.*, p.name AS patient_name, t.type AS treatment_type " +
                    "FROM appointment a " +
                    "INNER JOIN patient p ON a.patient_id = p.id " +
                    "INNER JOIN treatment t ON a.treatment_id = t.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId((resultSet.getInt("id")));
                appointment.setPatientName(resultSet.getString("patient_name"));
                appointment.setChannelDate(resultSet.getDate("channel_date").toLocalDate());
                appointment.setChannelTime(resultSet.getTime("channel_time").toLocalTime());
                appointment.setRegistrationStatusString(resultSet.getBoolean("reg_fee_status") ? "Not Paid":"Paid");
                appointment.setTreatmentType(resultSet.getString("treatment_type"));
                appointment.setAppointmentStatus(resultSet.getString("appointment_status"));

                appointmentList.add(appointment);
            }

            appointmentTableView.getItems().clear();
            appointmentTableView.getItems().addAll(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }


    @FXML
    void clickOnAppointmentAddBtn(ActionEvent event) {
        int patientId = patientMap.get(patientIdField.getValue());
        LocalDate channelDate = channelDateField.getValue();
        LocalTime channelTime = LocalTime.parse(channelTimeField.getCharacters());
        boolean regFeeStatus = regFeeStatusMap.get(regFeeStatusField.getValue());
        int treatmentId = treatmentMap.get(treatmentFieldId.getValue());
        String appointmentStatus = "Active";

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
        int patientId = patientMap.get(patientIdField.getValue());
        // Get other field values...

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "UPDATE appointment SET patient_id = ?, channel_date = ?," +
                    " channel_time = ?, reg_fee_status = ?, treatment_id = ? WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);

// Set parameters for the PreparedStatement
            pstm.setInt(1, patientId); // Set patient ID
            pstm.setDate(2, Date.valueOf(channelDateField.getValue())); // Set channel date
            pstm.setTime(3, Time.valueOf(LocalTime.parse(channelTimeField.getCharacters()))); // Set channel time
            pstm.setBoolean(4, regFeeStatusMap.get(regFeeStatusField.getValue())); // Set registration fee status
            pstm.setInt(5, treatmentMap.get(treatmentFieldId.getValue())); // Set treatment ID
            pstm.setInt(6, selectedAppointment.getId()); // Set appointment ID for the WHERE clause

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                new Alert(Alert.AlertType.WARNING, "Appointment updated successfully!").show();
                loadAllAppointments();
                // Additional handling if needed
            } else {
                // Update failed
                new Alert(Alert.AlertType.ERROR, "Failed to update appointment.").show();
                // Additional handling if needed
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void resetAppointmentDetail(ActionEvent actionEvent) {
        patientIdField.setValue("");
        channelTimeField.setText("");
        regFeeStatusField.setValue("");
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
            pstm.setInt(1, selectedAppointment.getId()); // Assuming appointment ID is obtained from the selected appointment

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
    void searchAppointmentByPatientId(ActionEvent event) {
        int selectedPatientId = patientMap.get(patientIdForSearch.getValue());
        filterAppointmentsByPatientId(selectedPatientId);
    }

    private void filterAppointmentsByPatientId(int patientId) {
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT a.id, p.name AS patient_name, a.channel_date, a.channel_time, a.reg_fee_status, t.type AS treatment_type, a.appointment_status " +
                    "FROM appointment a " +
                    "INNER JOIN patient p ON a.patient_id = p.id " +
                    "INNER JOIN treatment t ON a.treatment_id = t.id " +
                    "WHERE a.patient_id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, patientId);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPatientName(resultSet.getString("patient_name"));
                appointment.setChannelDate(resultSet.getDate("channel_date").toLocalDate());
                appointment.setChannelTime(resultSet.getTime("channel_time").toLocalTime());
                appointment.setRegistrationStatusString(resultSet.getBoolean("reg_fee_status") ? "Not Paid" : "Paid");
                appointment.setTreatmentType(resultSet.getString("treatment_type"));
                appointment.setAppointmentStatus(resultSet.getString("appointment_status"));
                filteredAppointments.add(appointment);
            }

            // Clear the current TableView items and update it with the filtered appointments
            appointmentTableView.getItems().clear();
            appointmentTableView.getItems().addAll(filteredAppointments);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }



    private void loadPatients() {
        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT id, name FROM patient";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                int patientId = resultSet.getInt("id");
                String patientName = resultSet.getString("name");
                patientIdField.getItems().add(patientName); // Populate ComboBox with patient names
                patientIdForSearch.getItems().add(patientName);
                // You can also store the IDs and names in a Map for future reference
                this.patientMap.put(patientName, patientId);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void loadTreatment() {
        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT id, type FROM treatment";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                int treatmentId = resultSet.getInt("id");
                String treatmentName = resultSet.getString("type");
                treatmentFieldId.getItems().add(treatmentName); // Populate ComboBox with patient names
                // You can also store the IDs and names in a Map for future reference
                this.treatmentMap.put(treatmentName, treatmentId);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadRegFeeStatus() {
        // Add paid and not paid options to the ComboBox and map
        regFeeStatusField.getItems().addAll("Paid", "Not Paid");
        regFeeStatusMap.put("Paid", true); // Set value as true for "Paid"
        regFeeStatusMap.put("Not Paid", false); // Set value as false for "Not Paid"
    }


    public void onChangeTreatmentType(ActionEvent actionEvent) {
        calculateBalanceToBePaid();
    }


    public void calculateBalanceToBePaid(){
        if(treatmentFieldId.getValue() == null){
            return;
        }
        try {
            Connection con = DbConnection.getInstance().getConnection();
            String sql = "SELECT price FROM treatment where id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, Integer.toString(treatmentMap.get(treatmentFieldId.getValue())));

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                balanceToBePaidId.setText("");
                float treatmentPrice = Float.parseFloat(resultSet.getString("price"));
                treatmentFeeId.setText(Float.toString(treatmentPrice));
                if (regFeeStatusMap.get(regFeeStatusField.getValue())) {
                    float calculatedBalanceToBePaid = (float) (treatmentPrice - 1000.00);
                    balanceToBePaidId.setText(Float.toString(calculatedBalanceToBePaid));
                } else {
                    balanceToBePaidId.setText(Float.toString(treatmentPrice));
                }

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public void onChangeRegisterFee(ActionEvent actionEvent) {
        calculateBalanceToBePaid();
    }

    public void onMouseClickOnTableBody(MouseEvent mouseEvent) {

        if (mouseEvent.getClickCount() == 1) {
            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
            if (appointment != null) {
                patientIdField.setValue(appointment.getPatientName());
                regFeeStatusField.setValue(appointment.getRegistrationStatusString());
                treatmentFieldId.setValue(appointment.getTreatmentType());
                channelDateField.setValue(appointment.channelDateProperty().getValue());
                channelTimeField.setText(appointment.channelTimeProperty().getValue().toString());
                calculateBalanceToBePaid();
            }
        }
    }
}
