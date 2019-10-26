package com.example.potigianim.model;

import java.util.Date;

public class OrderAdditionalInfo {
    private int billType;
    private double prefixOc;
    private double suffixOc;
    private String observations;
    private Date date;
    private String user;
    private String terminal;
    private double branch;

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public double getPrefixOc() {
        return prefixOc;
    }

    public void setPrefixOc(double prefixOc) {
        this.prefixOc = prefixOc;
    }

    public double getSuffixOc() {
        return suffixOc;
    }

    public void setSuffixOc(double suffixOc) {
        this.suffixOc = suffixOc;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public double getBranch() {
        return branch;
    }

    public void setBranch(double branch) {
        this.branch = branch;
    }
}
