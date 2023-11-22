package com.toothcare.dto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;




public class Appointment {
    private SimpleStringProperty appointmentId;
    private SimpleStringProperty patientName;
    private SimpleStringProperty appointmentDate;
    private SimpleStringProperty appointmentTime;
    private SimpleStringProperty treatmentType;
    private SimpleDoubleProperty treatmentCost;

    private SimpleStringProperty patientAddress;
    private SimpleStringProperty patientPhone;

    private SimpleDoubleProperty registrationAmount;
    private SimpleDoubleProperty remainingBalance;


    private double registrationFee;


    public Appointment(String appointmentId, String patientName, String appointmentDate,
                       String appointmentTime, String treatmentType, double treatmentCost, String patientAddress, String patientPhone,
                       double registrationAmount, double remainingBalance) {
        this.appointmentId = new SimpleStringProperty(appointmentId);
        this.patientName = new SimpleStringProperty(patientName);
        this.appointmentDate = new SimpleStringProperty(appointmentDate);
        this.appointmentTime = new SimpleStringProperty(appointmentTime);
        this.treatmentType = new SimpleStringProperty(treatmentType);
        this.treatmentCost = new SimpleDoubleProperty(treatmentCost);
        this.patientAddress = new SimpleStringProperty(patientAddress);
        this.patientPhone = new SimpleStringProperty(patientPhone);
        this.registrationAmount = new SimpleDoubleProperty(registrationAmount);
        this.remainingBalance = new SimpleDoubleProperty(remainingBalance);

    }

    public Appointment(){
        this.appointmentId = new SimpleStringProperty("");
        this.patientName = new SimpleStringProperty("");
        this.appointmentDate = new SimpleStringProperty("");
        this.appointmentTime = new SimpleStringProperty("");
        this.treatmentType = new SimpleStringProperty("");
        this.treatmentCost = new SimpleDoubleProperty(0.0);
        this.patientAddress = new SimpleStringProperty("");
        this.patientPhone = new SimpleStringProperty("");
        this.registrationAmount = new SimpleDoubleProperty(0.0);
        this.remainingBalance = new SimpleDoubleProperty(0.0);
    }

    public SimpleStringProperty appointmentIdProperty() {
        return appointmentId;
    }

    public SimpleStringProperty patientNameProperty() {
        return patientName;
    }

    public SimpleStringProperty appointmentDateProperty() {
        return appointmentDate;
    }

    public SimpleStringProperty appointmentTimeProperty() {
        return appointmentTime;
    }

    public SimpleStringProperty treatmentTypeProperty() {
        return treatmentType;
    }

    public SimpleDoubleProperty treatmentCostProperty() {
        return treatmentCost;
    }

    public SimpleStringProperty patientAddressProperty() {
        return patientAddress;
    }

    public SimpleStringProperty patientPhoneProperty() {
        return patientPhone;
    }

    public double getRegistrationAmount() {
        return registrationAmount.get();
    }

    public SimpleDoubleProperty registrationAmountProperty() {
        return registrationAmount;
    }

    public void setRegistrationAmount(double registrationAmount) {
        this.registrationAmount.set(registrationAmount);
    }

    public double getRemainingBalance() {
        return remainingBalance.get();
    }

    public SimpleDoubleProperty remainingBalanceProperty() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance.set(remainingBalance);
    }

    public String getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public String getPatientName() {
        return patientName.get();
    }

    public void setPatientName(String patientName) {
        this.patientName.set(patientName);
    }

    public String getAppointmentDate() {
        return appointmentDate.get();
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate.set(appointmentDate);
    }

    public String getAppointmentTime() {
        return appointmentTime.get();
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime.set(appointmentTime);
    }

    public String getTreatmentType() {
        return treatmentType.get();
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType.set(treatmentType);
    }

    public double getTreatmentCost() {
        return treatmentCost.get();
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost.set(treatmentCost);
    }


    public String getPatientAddress() {
        return patientAddress.get();
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress.set(patientAddress);
    }

    public String getPatientPhone() {
        return patientPhone.get();
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone.set(patientPhone);
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    // Getters and setters for individual properties if needed
}
