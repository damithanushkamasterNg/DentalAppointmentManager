package com.toothcare.dto;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    private int id;
    private int appointmentId;
    private BigDecimal totalFee;
    private Date paymentDate;
    private String paymentStatus;

    // Constructors
    public Payment() {
    }

    public Payment(int appointmentId, BigDecimal totalFee, Date paymentDate, String paymentStatus) {
        this.appointmentId = appointmentId;
        this.totalFee = totalFee;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Override toString() method for debugging
    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", totalFee=" + totalFee +
                ", paymentDate=" + paymentDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
