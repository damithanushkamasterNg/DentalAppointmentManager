package com.toothcare.dto;

import javafx.beans.property.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
public class Appointment {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty patientId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> channelDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> channelTime = new SimpleObjectProperty<>();
    private final BooleanProperty regFeeStatus = new SimpleBooleanProperty();
    private final IntegerProperty treatmentId = new SimpleIntegerProperty();
    private final StringProperty appointmentStatus = new SimpleStringProperty();
    private final StringProperty patientName = new SimpleStringProperty();
    private final StringProperty treatmentType = new SimpleStringProperty();
    private final StringProperty registrationStatusString = new SimpleStringProperty();
    // Other fields, constructors, getters, and setters...

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public ObjectProperty<LocalDate> channelDateProperty() {
        return channelDate;
    }

    public ObjectProperty<LocalTime> channelTimeProperty() {
        return channelTime;
    }

    public BooleanProperty regFeeStatusProperty() {
        return regFeeStatus;
    }

    public IntegerProperty treatmentIdProperty() {
        return treatmentId;
    }

    public StringProperty appointmentStatusProperty() {
        return appointmentStatus;
    }

    public String getPatientName() {
        return patientName.get();
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName.set(patientName);
    }

    public String getTreatmentType() {
        return treatmentType.get();
    }

    public StringProperty treatmentTypeProperty() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType.set(treatmentType);
    }

    public LocalDate getChannelDate() {
        return channelDate.get();
    }

    public void setChannelDate(LocalDate channelDate) {
        this.channelDate.set(channelDate);
    }

    public LocalTime getChannelTime() {
        return channelTime.get();
    }

    public void setChannelTime(LocalTime channelTime) {
        this.channelTime.set(channelTime);
    }

    public boolean isRegFeeStatus() {
        return regFeeStatus.get();
    }

    public void setRegFeeStatus(boolean regFeeStatus) {
        this.regFeeStatus.set(regFeeStatus);
    }

    public String getAppointmentStatus() {
        return appointmentStatus.get();
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus.set(appointmentStatus);
    }

    public String getRegistrationStatusString() {
        return registrationStatusString.get();
    }

    public StringProperty registrationStatusStringProperty() {
        return registrationStatusString;
    }

    public void setRegistrationStatusString(String registrationStatusString) {
        this.registrationStatusString.set(registrationStatusString);
    }
}
