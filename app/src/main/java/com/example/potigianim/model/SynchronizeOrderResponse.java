package com.example.potigianim.model;

import java.util.List;

public class SynchronizeOrderResponse {
    private String headerMessage;
    private int headerReturnCode;
    private List<SynchronizeOrderDetail> detailInfo;

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public int getHeaderReturnCode() {
        return headerReturnCode;
    }

    public void setHeaderReturnCode(int headerReturnCode) {
        this.headerReturnCode = headerReturnCode;
    }

    public List<SynchronizeOrderDetail> getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(List<SynchronizeOrderDetail> detailInfo) {
        this.detailInfo = detailInfo;
    }
}
