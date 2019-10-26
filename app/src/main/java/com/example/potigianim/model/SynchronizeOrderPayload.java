package com.example.potigianim.model;

import java.util.Map;

public class SynchronizeOrderPayload {
    private OrderAdditionalInfo orderAdditionalInfo;
    private Map<Integer, Integer> articleCount;

    public OrderAdditionalInfo getOrderAdditionalInfo() {
        return orderAdditionalInfo;
    }

    public void setOrderAdditionalInfo(OrderAdditionalInfo orderAdditionalInfo) {
        this.orderAdditionalInfo = orderAdditionalInfo;
    }

    public Map<Integer, Integer> getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Map<Integer, Integer> articleCount) {
        this.articleCount = articleCount;
    }
}
