package com.toothcare.dto;

import java.util.HashMap;
import java.util.Map;

public class Treatment {
    private String treatmentType;
    private double cost;

    private Map<String, Double> treatmentPrices;
    public Treatment(String treatmentType, double cost) {
        this.treatmentType = treatmentType;
        this.cost = cost;
    }


    public Treatment(String treatmentType) {
        this.treatmentType = treatmentType;
    }


    public Treatment() {
        // Initialize treatment types with respective prices
        treatmentPrices = new HashMap<>();
        treatmentPrices.put("Cleanings", 50.0); // Replace with appropriate prices
        treatmentPrices.put("Whitening", 100.0);
        treatmentPrices.put("Filling", 80.0);
        treatmentPrices.put("Nerve Filling", 120.0);
        treatmentPrices.put("Root Canal Therapy", 200.0);
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

    public Map<String, Double> getTreatmentPrices() {
        return treatmentPrices;
    }
}
