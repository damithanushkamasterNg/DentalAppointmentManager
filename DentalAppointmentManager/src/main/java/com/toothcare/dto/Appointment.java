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

    // Other fields, constructors, getters, and setters...

    public IntegerProperty idProperty() {
        return id;
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
}
