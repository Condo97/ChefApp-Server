package com.pantrypro.networking.server.request;

public class RegisterTransactionRequest extends AuthRequest {

    private Long transactionId;

    // LEGACY
    private String receiptString;

    public RegisterTransactionRequest() {

    }

    public RegisterTransactionRequest(String authToken, Long transactionId, String receiptString) {
        super(authToken);
        this.transactionId = transactionId;
        this.receiptString = receiptString;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    // LEGACY
    public String getReceiptString() {
        return receiptString;
    }

}
