package com.toothcare.dto;

public class Treatment {
    private String treatmentType;
    private double cost;

    public Treatment(String treatmentType, double cost) {
        this.treatmentType = treatmentType;
        this.cost = cost;
    }


    public Treatment(String treatmentType) {
        this.treatmentType = treatmentType;
        this.cost = cost;
    }


    // Getters and setters for treatmentType and cost
    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
