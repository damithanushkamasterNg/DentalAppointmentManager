package com.toothcare.controller.patient;

import com.toothcare.db.DbConnection;
import com.toothcare.dto.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    public TextField nameId;
    public TextField addressId;
    public TextField phoneId;
    public TextField searchPhoneId;

    @FXML
    private TableView<Patient> patientTableView;

    @FXML
    private TableColumn<Patient, String> nameCol;

    @FXML
    private TableColumn<Patient, String> addressCol;

    @FXML
    private TableColumn<Patient, String> phoneCol;


    /**
     * Handles the event when the 'Add Patient' button is clicked.
     * Retrieves name, address, and phone number from the input fields.
     * Executes an SQL query to insert a new patient into the database using prepared statements.
     * Displays an alert message upon successful or failed patient addition.
     * Loads all patient data into the TableView after a successful addition.
     *
     * @param event The event triggered when the 'Add Patient' button is clicked.
     */
    @FXML
    void clickOnPatientAddBtn(ActionEvent event) {
        String name = nameId.getText();
        String address = addressId.getText();
        String phoneNo = phoneId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            // SQL query with correct syntax and parameter placeholders
            String sql = "INSERT INTO patient (name, phone, address) VALUES (?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            pstm.setString(1, name);
            pstm.setString(2, phoneNo);
            pstm.setString(3, address);

            boolean isSaved = pstm.executeUpdate() > 0;

            if (isSaved) {
                // Display confirmation message upon successful patient addition
                new Alert(Alert.AlertType.CONFIRMATION, "Patient saved successfully!").show();
                // Load all patient data into the TableView after a successful addition
                loadAllPatient();
            } else {
                // Display an error message if patient addition fails
                new Alert(Alert.AlertType.ERROR, "Failed to save patient.").show();
            }

        } catch (SQLException e) {
            // Display an error message for any SQL exceptions that occur
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    /**
     * This method will be triggered when clicked on the search patient button.
     * It invokes the searchPatientByPhoneNo method.
     *
     * @param event The event caused by the button click
     */
    @FXML
    void searchPatient(ActionEvent event) {
        searchPatientByPhoneNo();
    }

    /**
     * This method searches for a patient by phone number.
     * If found, it populates the fields with patient details.
     */
    void searchPatientByPhoneNo() {
        String phoneNo = searchPhoneId.getText();
        if (phoneNo == null) {
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM patient WHERE phone=?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, phoneNo);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString(2);
                String phone = resultSet.getString(4);
                String address = resultSet.getString(3);

                setFields(name, phone, address);
            } else {
                new Alert(Alert.AlertType.WARNING, "Patient not found!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    /**
     * This method is triggered when updating patient details.
     * It updates the patient information in the database based on the provided phone number.
     *
     * @param actionEvent The event caused by the update action
     * @throws SQLException if there's an error with SQL execution
     */
    @FXML
    public void updatePatient(ActionEvent actionEvent) throws SQLException {
        String name = nameId.getText();
        String address = addressId.getText();
        String phoneNo = phoneId.getText();

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "UPDATE patient SET name = ?, phone = ?, address = ? WHERE phone = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(3, address);
            pstm.setString(2, phoneNo);
            pstm.setString(4, searchPhoneId.getText());

            boolean isUpdated = pstm.executeUpdate() > 0;
            if (isUpdated) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Patient updated successfully!").show();
                loadAllPatient();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    /**
     * This method is triggered when deleting a patient's record.
     * It deletes the patient's information from the database based on the provided phone number.
     *
     * @param actionEvent The event caused by the delete action
     */
    @FXML
    public void deletePatient(ActionEvent actionEvent) {
        String phoneNo = searchPhoneId.getText();
        if (phoneNo == null) {
            return;
        }
        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "delete from patient WHERE phone = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, phoneNo);

            boolean isDeleted = pstm.executeUpdate() > 0;
            if (isDeleted) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Patient deleted successfully!").show();
                loadAllPatient();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    /**
     * This method retrieves all patient records from the database and populates them into the TableView.
     * It fetches patient details including ID, name, address, and phone number, then displays them in the TableView.
     * Handles exceptions occurring during the database interaction.
     */
    private void loadAllPatient() {
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM patient";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Patient> patientList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setName(resultSet.getString("name"));
                patient.setAddress(resultSet.getString("address"));
                patient.setPhone(resultSet.getString("phone"));

                patientList.add(patient);
            }

            // Clear existing items and add the fetched data to the TableView
            patientTableView.getItems().clear();
            patientTableView.getItems().addAll(patientList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    /**
     * Sets the text fields with the provided name, phone, and address values.
     * It updates the corresponding TextField elements with the given data.
     *
     * @param name    the name to be set in the nameId TextField
     * @param phone   the phone number to be set in the phoneId TextField
     * @param address the address to be set in the addressId TextField
     */
    private void setFields(String name, String phone, String address) {
        this.nameId.setText(name);
        this.phoneId.setText(phone);
        this.addressId.setText(address);
    }

    /**
     * Clears the content of all text fields for name, phone, and address.
     * It sets the content of each corresponding TextField element to an empty string.
     */
    private void clearFields() {
        this.nameId.setText("");
        this.phoneId.setText("");
        this.addressId.setText("");
    }


    /**
     * Initializes the TableView columns and loads all patient data into the TableView.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the table columns
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // Add the columns to the TableView
        patientTableView.getColumns().setAll(nameCol, addressCol, phoneCol);

        // Load all patient data into the TableView when the application starts
        loadAllPatient();
    }


    /**
     * Handles the mouse click event on the TableView.
     * If a patient is selected, retrieves the phone number from the selected patient's data, performs a search operation,
     * and populates the search field with the selected patient's phone number.
     * Then triggers a search operation based on the selected patient's phone number.
     *
     * @param mouseEvent The event triggered when the mouse is clicked.
     */
    public void onMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                String phone = selectedPatient.getPhone();

                // Set the phone number in the search field
                searchPhoneId.setText(phone);

                // Trigger search operation for the selected patient's phone number
                searchPatientByPhoneNo();

                // Clear the search field after the search operation is completed
                searchPhoneId.setText("");
            }
        }
    }

    /**
     * Resets the patient details by clearing the input fields for name, phone, and address.
     *
     * @param actionEvent The event triggered to reset patient details.
     */
    @FXML
    public void resetPatientDetail(ActionEvent actionEvent) {
        clearFields();
    }

}
