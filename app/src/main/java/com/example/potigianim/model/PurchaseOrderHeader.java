package com.example.potigianim.model;

import java.util.Date;

public class PurchaseOrderHeader {
    private int orderCode;
    private int orderPrefix;
    private int orderSuffix;
    private int providerCode;
    private int situation;
    private Date emissionDate;
    private int buyerCode;
    private int items;

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderPrefix() {
        return orderPrefix;
    }

    public void setOrderPrefix(int orderPrefix) {
        this.orderPrefix = orderPrefix;
    }

    public int getOrderSuffix() {
        return orderSuffix;
    }

    public void setOrderSuffix(int orderSuffix) {
        this.orderSuffix = orderSuffix;
    }

    public int getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(int providerCode) {
        this.providerCode = providerCode;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date insertDate) {
        this.emissionDate = insertDate;
    }

    public int getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(int buyerCode) {
        this.buyerCode = buyerCode;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
