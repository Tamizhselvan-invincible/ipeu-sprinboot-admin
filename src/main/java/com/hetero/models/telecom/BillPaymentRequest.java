package com.hetero.models.telecom;


public class BillPaymentRequest {
    private String providerId;
    private String optional1;
    private String optional2;
    private String optional3;
    private String optional4;
    private String amount;
    private String clientId;
    //    private String type;
    public BillPaymentRequest () {
    }

    public BillPaymentRequest (String providerId, String optional1, String optional2, String optional3, String optional4, String amount, String clientId) {
        this.providerId = providerId;
        this.optional1 = optional1;
        this.optional2 = optional2;
        this.optional3 = optional3;
        this.optional4 = optional4;
        this.amount = amount;
        this.clientId = clientId;
    }

    public String getProviderId () {
        return providerId;
    }

    public void setProviderId (String providerId) {
        this.providerId = providerId;
    }

    public String getOptional1 () {
        return optional1;
    }

    public void setOptional1 (String optional1) {
        this.optional1 = optional1;
    }

    public String getOptional2 () {
        return optional2;
    }

    public void setOptional2 (String optional2) {
        this.optional2 = optional2;
    }

    public String getOptional3 () {
        return optional3;
    }

    public void setOptional3 (String optional3) {
        this.optional3 = optional3;
    }

    public String getOptional4 () {
        return optional4;
    }

    public void setOptional4 (String optional4) {
        this.optional4 = optional4;
    }

    public String getAmount () {
        return amount;
    }

    public void setAmount (String amount) {
        this.amount = amount;
    }

    public String getClientId () {
        return clientId;
    }

    public void setClientId (String clientId) {
        this.clientId = clientId;
    }
}
