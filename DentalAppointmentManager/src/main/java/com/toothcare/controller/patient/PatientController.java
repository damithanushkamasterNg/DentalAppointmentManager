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
import java.util.ArrayList;
import java.util.List;
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
                new Alert(Alert.AlertType.CONFIRMATION, "Patient saved successfully!").show();
                loadAllPatient();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save patient.").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void searchPatient(ActionEvent event) {
        searchPatientByPhoneNo();
    }

    void searchPatientByPhoneNo(){
        String phoneNo = searchPhoneId.getText();
        if(phoneNo == null){
            return;
        }

        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM patient WHERE phone=?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, phoneNo);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
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
            if(isUpdated) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Patient updated successfully!").show();
                loadAllPatient();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    public void deletePatient(ActionEvent actionEvent) {
        String phoneNo = searchPhoneId.getText();
        if(phoneNo == null){
            return;
        }
        try {
            Connection con = DbConnection.getInstance().getConnection();

            String sql = "delete from patient WHERE phone = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, phoneNo);

            boolean isDeleted = pstm.executeUpdate() > 0;
            if(isDeleted) {
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION, "Patient deleted successfully!").show();
                loadAllPatient();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


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

    private void setFields(String name, String phone, String address) {
        this.nameId.setText(name);
        this.phoneId.setText(phone);
        this.addressId.setText(address);
    }


    private void clearFields() {
        this.nameId.setText("");
        this.phoneId.setText("");
        this.addressId.setText("");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the table columns
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // Add the columns to the TableView
        patientTableView.getColumns().setAll(nameCol, addressCol, phoneCol);
        loadAllPatient();
    }

    public void onMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                String phone = selectedPatient.getPhone();

                searchPhoneId.setText(phone);
                searchPatientByPhoneNo();
                searchPhoneId.setText("");
            }
        }
    }

    @FXML
    public void resetPatientDetail(ActionEvent actionEvent) {
        clearFields();
    }
}
