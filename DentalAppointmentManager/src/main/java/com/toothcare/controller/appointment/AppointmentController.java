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

import java.math.BigDecimal;
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
    public Button paymentButtonId;
    @FXML
    public TextField paymentAmountId;
    public DatePicker paymentDateId;

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

    @FXML
    private TableColumn<Appointment, String> paymentStatus;

    public int appointmentId;


    /**
     * Initializes various components and loads necessary data when the associated FXML file is loaded.
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns and load data
        initializeTableColumns();
        loadAllAppointments();
        loadPatients();
        loadTreatment();
        loadRegFeeStatus();
        if (paymentAmountId.getText() != null && !paymentAmountId.getText().isEmpty() && paymentDateId.getValue() != null) {
            paymentButtonId.setDisable(false); // Enable the button if the text field is not empty
        } else {
            paymentButtonId.setDisable(true); // Disable the button if the text field is empty
            paymentButtonId.setStyle("");
        }
        // Add a listener to the paymentAmountId TextField
        paymentAmountId.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the text field has a value
            if (newValue != null && !newValue.isEmpty() && paymentDateId.getValue() != null) {
                paymentButtonId.setDisable(false); // Enable the button if the text field is not empty
            } else {
                paymentButtonId.setDisable(true); // Disable the button if the text field is empty
                paymentButtonId.setStyle("");
            }
        });

        // Assuming paymentDateId is a DatePicker
        paymentDateId.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the date picker has a value and if it's a valid date
            if (newValue != null && paymentAmountId.getText() != null && !paymentAmountId.getText().isEmpty()) {
                paymentButtonId.setDisable(false); // Enable the button if a date is selected
            } else {
                paymentButtonId.setDisable(true); // Disable the button if no date is selected
                paymentButtonId.setStyle(""); // Reset any button styles if needed
            }
        });

    }

    /**
     * Initializes the TableView columns by setting cell value factories for each column
     * and adding columns to the TableView.
     * This method is used to define how the data will be displayed in each column of the TableView.
     */
    private void initializeTableColumns() {
        // Set cell value factories for columns
        patientIdCol.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());
        channelDateCol.setCellValueFactory(cellData -> cellData.getValue().channelDateProperty());
        channelTimeCol.setCellValueFactory(cellData -> cellData.getValue().channelTimeProperty());
        regFeeStatusCol.setCellValueFactory(cellData -> cellData.getValue().registrationStatusStringProperty());
        treatmentIdCol.setCellValueFactory(cellData -> cellData.getValue().treatmentTypeProperty());
        appointmentStatusCol.setCellValueFactory(cellData -> cellData.getValue().appointmentStatusProperty());
        paymentStatus.setCellValueFactory(cellData -> cellData.getValue().paymentStatusProperty());

        appointmentTableView.getColumns().setAll(
                patientIdCol, channelDateCol, channelTimeCol,
                regFeeStatusCol, treatmentIdCol, appointmentStatusCol, paymentStatus
        );
    }


    /**
     * Loads all appointments from the database and populates the TableView.
     * This method retrieves appointment details including patient name, treatment type, and payment information
     * and adds them to the TableView.
     */
    private void loadAllAppointments() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT a.*, p.name AS patient_name, t.type AS treatment_type, pm.total_fee AS payment_total_fee, pm.payment_status AS payment_status " +
                    "FROM appointment a " +
                    "INNER JOIN patient p ON a.patient_id = p.id " +
                    "INNER JOIN treatment t ON a.treatment_id = t.id " +
                    "LEFT JOIN payment pm ON pm.appointment_id = a.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPatientName(resultSet.getString("patient_name"));
                appointment.setChannelDate(resultSet.getDate("channel_date").toLocalDate());
                appointment.setChannelTime(resultSet.getTime("channel_time").toLocalTime());
                appointment.setRegistrationStatusString(resultSet.getBoolean("reg_fee_status") ? "Not Paid" : "Paid");
                appointment.setTreatmentType(resultSet.getString("treatment_type"));
                appointment.setAppointmentStatus(resultSet.getString("appointment_status"));
                appointment.setPaymentStatus(resultSet.getString("payment_status"));


                appointmentList.add(appointment);
            }

            appointmentTableView.getItems().clear();
            appointmentTableView.getItems().addAll(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }


    /**
     * Handles the event when the "Add Appointment" button is clicked.
     * This method retrieves input data from various UI components,
     * constructs an SQL INSERT query, and saves a new appointment into the database.
     * If successful, it triggers the reloading of all appointments in the TableView.
     *
     * @param event The ActionEvent generated by clicking the "Add Appointment" button.
     */
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


    /**
     * Handles the event when the "Update Appointment" button is clicked.
     * Retrieves the selected appointment from the TableView, validates its selection,
     * gathers input data from various UI components, constructs an SQL UPDATE query,
     * and updates the appointment in the database based on the changes.
     * If successful, it triggers the reloading of all appointments in the TableView.
     * Displays appropriate alerts for successful or failed updates.
     *
     * @param actionEvent The ActionEvent generated by clicking the "Update Appointment" button.
     */
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

    /**
     * Resets the input fields related to appointment details to their initial state.
     * This method clears the values present in the patientIdField, channelTimeField,
     * regFeeStatusField, treatmentIdField, and appointmentStatusField.
     *
     * @param actionEvent The ActionEvent generated by clicking the "Reset Appointment Detail" button.
     */
    @FXML
    public void resetAppointmentDetail(ActionEvent actionEvent) {
        patientIdField.setValue("");
        channelTimeField.setText("");
        regFeeStatusField.setValue("");
        treatmentIdField.setText("");
        appointmentStatusField.setText("");
    }


    /**
     * Deletes the selected appointment from the database.
     * It first checks if any appointment is selected in the TableView.
     * Then, it constructs an SQL DELETE query to remove the appointment based on its ID.
     * If the deletion is successful, it triggers the reloading of all appointments in the TableView.
     *
     * @param actionEvent The ActionEvent generated by clicking the "Delete Appointment" button.
     */
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

    /**
     * Searches appointments based on the selected patient ID.
     * It retrieves the selected patient ID from the patientIdForSearch ComboBox,
     * then filters appointments by that patient ID using the filterAppointmentsByPatientId method.
     *
     * @param event The ActionEvent generated by selecting a patient ID in the ComboBox and clicking the search button.
     */
    @FXML
    void searchAppointmentByPatientId(ActionEvent event) {
        int selectedPatientId = patientMap.get(patientIdForSearch.getValue());
        filterAppointmentsByPatientId(selectedPatientId);
    }

    /**
     * Filters appointments based on the provided patient ID.
     * Retrieves appointments associated with the given patient ID from the database and updates the TableView with the filtered appointments.
     *
     * @param patientId The ID of the patient used to filter appointments.
     */
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


    /**
     * Loads patient data from the database into ComboBoxes used for patient selection and search.
     * Retrieves patient IDs and names from the 'patient' table in the database and populates two ComboBoxes with patient names.
     * Additionally, it stores patient names and their corresponding IDs in a HashMap for future reference.
     */
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


    /**
     * Loads treatment data from the database into a ComboBox used for treatment selection.
     * Retrieves treatment IDs and types from the 'treatment' table in the database and populates a ComboBox with treatment types.
     * Additionally, it stores treatment types and their corresponding IDs in a HashMap for future reference.
     */
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

    /**
     * Loads registration fee status options into a ComboBox and maps the statuses to boolean values in a HashMap.
     * Adds "Paid" and "Not Paid" options to the ComboBox for registration fee status selection.
     * Additionally, maps the statuses to boolean values (true for "Paid" and false for "Not Paid") in a HashMap for reference.
     */
    private void loadRegFeeStatus() {
        // Add paid and not paid options to the ComboBox and map
        regFeeStatusField.getItems().addAll("Paid", "Not Paid");
        regFeeStatusMap.put("Paid", true); // Set value as true for "Paid"
        regFeeStatusMap.put("Not Paid", false); // Set value as false for "Not Paid"
    }


    public void onChangeTreatmentType(ActionEvent actionEvent) {
        calculateBalanceToBePaid();
    }

    /**
     * Calculates the balance to be paid based on the selected treatment type and registration fee status.
     * This method is triggered on changes to the treatment type.
     * Retrieves the price of the selected treatment from the database and computes the balance to be paid
     * considering the registration fee status. Updates the corresponding UI components.
     */

    public void calculateBalanceToBePaid() {
        if (treatmentFieldId.getValue() == null) {
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

    /**
     * Handles the mouse click event on the appointment table body.
     * When a row is clicked once, it retrieves the appointment details and sets them in the corresponding UI components.
     * It also triggers the calculation of the balance to be paid based on the selected appointment.
     *
     * @param mouseEvent The MouseEvent captured from the mouse click
     */
    public void onMouseClickOnTableBody(MouseEvent mouseEvent) {

        if (mouseEvent.getClickCount() == 1) {
            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
            if (appointment != null) {
                patientIdField.setValue(appointment.getPatientName());
                regFeeStatusField.setValue(appointment.getRegistrationStatusString());
                treatmentFieldId.setValue(appointment.getTreatmentType());
                channelDateField.setValue(appointment.channelDateProperty().getValue());
                channelTimeField.setText(appointment.channelTimeProperty().getValue().toString());
                appointmentId = appointment.getId();
                calculateBalanceToBePaid();
            }
        }
    }

    /**
     * Handles the action event triggered to create a payment.
     * Validates the payment amount and saves payment details into the database.
     * Displays appropriate alerts based on the success or failure of payment creation.
     * @param actionEvent The ActionEvent generated by the user action
     */
    @FXML
    public void createPayment(ActionEvent actionEvent) {
        try {
            Connection con = DbConnection.getInstance().getConnection();

            // SQL query with correct syntax and parameter placeholders
            String sql = "INSERT INTO payment (appointment_id, total_fee, payment_date, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            pstm.setInt(1, appointmentId);

            if (!paymentAmountId.getText().isEmpty()) {
                BigDecimal paymentAmount = new BigDecimal(paymentAmountId.getText());

                // Set the BigDecimal value in the PreparedStatement
                pstm.setBigDecimal(2, paymentAmount);
            } else {
                // Handle the case where the TextField is empty
                // For example, setting a default value or handling the absence of payment amount
                pstm.setBigDecimal(2, BigDecimal.ZERO); // Set a default value as zero or handle accordingly
            }

            pstm.setDate(3, Date.valueOf(paymentDateId.getValue()));

            try {
                BigDecimal balanceToBePaid = new BigDecimal(balanceToBePaidId.getText());
                BigDecimal enteredPaymentAmount = new BigDecimal(paymentAmountId.getText());

                int comparisonResult = balanceToBePaid.compareTo(enteredPaymentAmount);

                if (comparisonResult < 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Payment amount should not be greater than balance to be paid!").show();
                    return;
                } else if (comparisonResult > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Entered payment amount should not equal to payment amount!").show();
                    return;
                } else if (comparisonResult == 0) {
                    pstm.setString(4, "Paid");
                    // Continue with your PreparedStatement operations here
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                new Alert(Alert.AlertType.ERROR, "Invalid input! Please enter a valid number.").show();
            }

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment saved successfully!").show();
                loadAllAppointments();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save payment.").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
