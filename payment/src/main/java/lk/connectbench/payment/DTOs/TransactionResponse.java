package lk.connectbench.payment.DTOs;

public class TransactionResponse {

    private boolean isTokenization;
    private String statusCode;
    private String message;
    private boolean status;
    private String invoiceNumber;
    private boolean cardSaveType;
    private String previousInvoice;

    public boolean isTokenization() {
        return isTokenization;
    }

    public void setTokenization(boolean tokenization) {
        isTokenization = tokenization;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isCardSaveType() {
        return cardSaveType;
    }

    public void setCardSaveType(boolean cardSaveType) {
        this.cardSaveType = cardSaveType;
    }

    public String getPreviousInvoice() {
        return previousInvoice;
    }

    public void setPreviousInvoice(String previousInvoice) {
        this.previousInvoice = previousInvoice;
    }
}
