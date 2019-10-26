package com.example.potigianim.model;

public class SynchronizeOrderDetail {
    private Double articleCode;
    private String detailMessage;
    private int detailReturnCode;

    public Double getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(Double articleCode) {
        this.articleCode = articleCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public int getDetailReturnCode() {
        return detailReturnCode;
    }

    public void setDetailReturnCode(int detailReturnCode) {
        this.detailReturnCode = detailReturnCode;
    }
}
