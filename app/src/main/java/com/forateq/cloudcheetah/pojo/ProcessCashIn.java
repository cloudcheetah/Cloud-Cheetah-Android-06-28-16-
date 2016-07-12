package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.CashInOut;

import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
public class ProcessCashIn {

    List<CashInOut> cash_flow;

    public List<CashInOut> getCash_flow() {
        return cash_flow;
    }

    public void setCash_flow(List<CashInOut> cash_flow) {
        this.cash_flow = cash_flow;
    }
}
