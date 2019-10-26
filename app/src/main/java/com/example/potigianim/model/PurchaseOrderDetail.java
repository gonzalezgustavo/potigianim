package com.example.potigianim.model;

import java.util.Arrays;
import java.util.List;

public class PurchaseOrderDetail {
    private double ocCode;
    private double prefixOcCode;
    private double suffixOcCode;
    private double articleCode;
    private double requestedPackages;
    private double providerFactor;
    private String articleName;
    private String eanCode;
    private String dun14Code;
    private String alternativeEanCode1;
    private String alternativeEanCode2;
    private String alternativeEanCode3;
    private String alternativeEanCode4;

    public double getOcCode() {
        return ocCode;
    }

    public void setOcCode(double ocCode) {
        this.ocCode = ocCode;
    }

    public double getPrefixOcCode() {
        return prefixOcCode;
    }

    public void setPrefixOcCode(double prefixOcCode) {
        this.prefixOcCode = prefixOcCode;
    }

    public double getSuffixOcCode() {
        return suffixOcCode;
    }

    public void setSuffixOcCode(double suffixOcCode) {
        this.suffixOcCode = suffixOcCode;
    }

    public double getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(double articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getDun14Code() {
        return dun14Code;
    }

    public void setDun14Code(String dun14Code) {
        this.dun14Code = dun14Code;
    }

    public String getAlternativeEanCode1() {
        return alternativeEanCode1;
    }

    public void setAlternativeEanCode1(String alternativeEanCode1) {
        this.alternativeEanCode1 = alternativeEanCode1;
    }

    public String getAlternativeEanCode2() {
        return alternativeEanCode2;
    }

    public void setAlternativeEanCode2(String alternativeEanCode2) {
        this.alternativeEanCode2 = alternativeEanCode2;
    }

    public String getAlternativeEanCode3() {
        return alternativeEanCode3;
    }

    public void setAlternativeEanCode3(String alternativeEanCode3) {
        this.alternativeEanCode3 = alternativeEanCode3;
    }

    public String getAlternativeEanCode4() {
        return alternativeEanCode4;
    }

    public void setAlternativeEanCode4(String alternativeEanCode4) {
        this.alternativeEanCode4 = alternativeEanCode4;
    }

    public double getRequestedPackages() {
        return requestedPackages;
    }

    public void setRequestedPackages(double requestedPackages) {
        this.requestedPackages = requestedPackages;
    }

    public double getProviderFactor() {
        return providerFactor;
    }

    public void setProviderFactor(double providerFactor) {
        this.providerFactor = providerFactor;
    }

    public List<String> getBarcodeCodes() {
        return Arrays.asList(eanCode, dun14Code, alternativeEanCode1,
                alternativeEanCode2, alternativeEanCode3, alternativeEanCode4);
    }
}
