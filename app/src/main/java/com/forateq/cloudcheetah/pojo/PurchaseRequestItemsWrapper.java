package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
public class PurchaseRequestItemsWrapper {

    List<PurchaseRequestItems> purchaseRequestsList;
    double totalPurchaseRequestPrice;

    public List<PurchaseRequestItems> getPurchaseRequestsList() {
        return purchaseRequestsList;
    }

    public void setPurchaseRequestsList(List<PurchaseRequestItems> purchaseRequestsList) {
        this.purchaseRequestsList = purchaseRequestsList;
    }

    public double getTotalPurchaseRequestPrice() {
        return totalPurchaseRequestPrice;
    }

    public void setTotalPurchaseRequestPrice(double totalPurchaseRequestPrice) {
        this.totalPurchaseRequestPrice = totalPurchaseRequestPrice;
    }
}
