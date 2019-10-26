package com.example.potigianim.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RequestDetails {
    private int documentCode;
    private int documentPrefix;
    private int documentSuffix;
    private int articleCode;
    private String articleName;
    private int requestItem;
    private double originBasePrice;
    private double salePrice;
    private double baptized;
    private double manualPrice;
    private int familyCode;
    private Character specialSaleFactor;
    private Character salePerWeight;
    private int packagesGrams;
    private int piecesFactor;
    private double articleUnitaryPrice;
    private double finalArticleUnitaryPrice;
    private double IVA;
    private double articleTotal;
    private double totalArticleWeight;
    private int containerCode;
    private double totalContainerNet;
    private double requestGramsUnit;
    private double deliveredGramsUnit;
    private double pendingGramsUnit;
    private Character billDocumentLetter;
    private int billDocumentPrefix;
    private int billDocumentSuffix;
    private Date InsertDate;
    private double discountAppliedToSalePrice;
    private Character mobileFlag;
    private Character closedBoxFlag;
    private Character lineModifiedFlag;
    private Character fractionSaleFactorFlag;
    private Character modifiedPriceFlag;
    private String modifiedPriceUserCode;
    private Character specialBusinessFlag;
    private double specialBusinessSalePrice;
    private String dun14Code;
    private String eanCode;
    private String alternativeEanCode1;
    private String alternativeEanCode2;
    private String alternativeEanCode3;
    private String alternativeEanCode4;

    public int getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(int documentCode) {
        this.documentCode = documentCode;
    }

    public int getDocumentPrefix() {
        return documentPrefix;
    }

    public void setDocumentPrefix(int documentPrefix) {
        this.documentPrefix = documentPrefix;
    }

    public int getDocumentSuffix() {
        return documentSuffix;
    }

    public void setDocumentSuffix(int documentSuffix) {
        this.documentSuffix = documentSuffix;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(int requestItem) {
        this.requestItem = requestItem;
    }

    public double getOriginBasePrice() {
        return originBasePrice;
    }

    public void setOriginBasePrice(double originBasePrice) {
        this.originBasePrice = originBasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getBaptized() {
        return baptized;
    }

    public void setBaptized(double baptized) {
        this.baptized = baptized;
    }

    public double getManualPrice() {
        return manualPrice;
    }

    public void setManualPrice(double manualPrice) {
        this.manualPrice = manualPrice;
    }

    public int getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(int familyCode) {
        this.familyCode = familyCode;
    }

    public Character getSpecialSaleFactor() {
        return specialSaleFactor;
    }

    public void setSpecialSaleFactor(Character specialSaleFactor) {
        this.specialSaleFactor = specialSaleFactor;
    }

    public Character getSalePerWeight() {
        return salePerWeight;
    }

    public void setSalePerWeight(Character salePerWeight) {
        this.salePerWeight = salePerWeight;
    }

    public int getPackagesGrams() {
        return packagesGrams;
    }

    public void setPackagesGrams(int packagesGrams) {
        this.packagesGrams = packagesGrams;
    }

    public int getPiecesFactor() {
        return piecesFactor;
    }

    public void setPiecesFactor(int piecesFactor) {
        this.piecesFactor = piecesFactor;
    }

    public double getArticleUnitaryPrice() {
        return articleUnitaryPrice;
    }

    public void setArticleUnitaryPrice(double articleUnitaryPrice) {
        this.articleUnitaryPrice = articleUnitaryPrice;
    }

    public double getFinalArticleUnitaryPrice() {
        return finalArticleUnitaryPrice;
    }

    public void setFinalArticleUnitaryPrice(double finalArticleUnitaryPrice) {
        this.finalArticleUnitaryPrice = finalArticleUnitaryPrice;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getArticleTotal() {
        return articleTotal;
    }

    public void setArticleTotal(double articleTotal) {
        this.articleTotal = articleTotal;
    }

    public double getTotalArticleWeight() {
        return totalArticleWeight;
    }

    public void setTotalArticleWeight(double totalArticleWeight) {
        this.totalArticleWeight = totalArticleWeight;
    }

    public int getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(int containerCode) {
        this.containerCode = containerCode;
    }

    public double getTotalContainerNet() {
        return totalContainerNet;
    }

    public void setTotalContainerNet(double totalContainerNet) {
        this.totalContainerNet = totalContainerNet;
    }

    public double getRequestGramsUnit() {
        return requestGramsUnit;
    }

    public void setRequestGramsUnit(double requestGramsUnit) {
        this.requestGramsUnit = requestGramsUnit;
    }

    public double getDeliveredGramsUnit() {
        return deliveredGramsUnit;
    }

    public void setDeliveredGramsUnit(double deliveredGramsUnit) {
        this.deliveredGramsUnit = deliveredGramsUnit;
    }

    public double getPendingGramsUnit() {
        return pendingGramsUnit;
    }

    public void setPendingGramsUnit(double pendingGramsUnit) {
        this.pendingGramsUnit = pendingGramsUnit;
    }

    public Character getBillDocumentLetter() {
        return billDocumentLetter;
    }

    public void setBillDocumentLetter(Character billDocumentLetter) {
        this.billDocumentLetter = billDocumentLetter;
    }

    public int getBillDocumentPrefix() {
        return billDocumentPrefix;
    }

    public void setBillDocumentPrefix(int billDocumentPrefix) {
        this.billDocumentPrefix = billDocumentPrefix;
    }

    public int getBillDocumentSuffix() {
        return billDocumentSuffix;
    }

    public void setBillDocumentSuffix(int billDocumentSuffix) {
        this.billDocumentSuffix = billDocumentSuffix;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public double getDiscountAppliedToSalePrice() {
        return discountAppliedToSalePrice;
    }

    public void setDiscountAppliedToSalePrice(double discountAppliedToSalePrice) {
        this.discountAppliedToSalePrice = discountAppliedToSalePrice;
    }

    public Character getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Character mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

    public Character getClosedBoxFlag() {
        return closedBoxFlag;
    }

    public void setClosedBoxFlag(Character closedBoxFlag) {
        this.closedBoxFlag = closedBoxFlag;
    }

    public Character getLineModifiedFlag() {
        return lineModifiedFlag;
    }

    public void setLineModifiedFlag(Character lineModifiedFlag) {
        this.lineModifiedFlag = lineModifiedFlag;
    }

    public Character getFractionSaleFactorFlag() {
        return fractionSaleFactorFlag;
    }

    public void setFractionSaleFactorFlag(Character fractionSaleFactorFlag) {
        this.fractionSaleFactorFlag = fractionSaleFactorFlag;
    }

    public Character getModifiedPriceFlag() {
        return modifiedPriceFlag;
    }

    public void setModifiedPriceFlag(Character modifiedPriceFlag) {
        this.modifiedPriceFlag = modifiedPriceFlag;
    }

    public String getModifiedPriceUserCode() {
        return modifiedPriceUserCode;
    }

    public void setModifiedPriceUserCode(String modifiedPriceUserCode) {
        this.modifiedPriceUserCode = modifiedPriceUserCode;
    }

    public Character getSpecialBusinessFlag() {
        return specialBusinessFlag;
    }

    public void setSpecialBusinessFlag(Character specialBusinessFlag) {
        this.specialBusinessFlag = specialBusinessFlag;
    }

    public double getSpecialBusinessSalePrice() {
        return specialBusinessSalePrice;
    }

    public void setSpecialBusinessSalePrice(double specialBusinessSalePrice) {
        this.specialBusinessSalePrice = specialBusinessSalePrice;
    }

    public String getDun14Code() {
        return dun14Code;
    }

    public void setDun14Code(String dun14Code) {
        this.dun14Code = dun14Code;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
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

    public List<String> getBarcodeCodes() {
        return Arrays.asList(eanCode, dun14Code, alternativeEanCode1,
                alternativeEanCode2, alternativeEanCode3, alternativeEanCode4);
    }
}
