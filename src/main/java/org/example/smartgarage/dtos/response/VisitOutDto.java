package org.example.smartgarage.dtos.response;

import org.example.smartgarage.models.ServiceType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class VisitOutDto {

    private long id;
    private String bookedDate;
    private String clientName;
    private String employeeName;
    private VehicleOutDTO vehicle;
    private List<ServiceType> services;
    private String status;
    private List<String> history;
    private BigDecimal totalCost;
    private String currency = "BGN";
    private double exchangeRate;

    public VisitOutDto() {
    }

    public VisitOutDto(String bookedDate, String clientName, String employeeName, VehicleOutDTO vehicle,
                       List<ServiceType> services, String status, List<String> history, BigDecimal totalCost, String currency) {
        this.bookedDate = bookedDate;
        this.clientName = clientName;
        this.employeeName = employeeName;
        this.vehicle = vehicle;
        this.services = services;
        this.status = status;
        this.history = history;
        this.totalCost = totalCost;
        this.currency = currency;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public VehicleOutDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleOutDTO vehicle) {
        this.vehicle = vehicle;
    }

    public List<ServiceType> getServices() {
        return services;
    }

    public void setServices(List<ServiceType> services) {
        this.services = services;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
