package lk.connectbench.payment.DTOs;

public class PaymentConfig {

    private String PGIdentifier;
    private String StoreName;
    private String SecretCode;
    private String Currency;
    private String callbackURL;
    private String orderId;
    private String amount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPGIdentifier() {
        return PGIdentifier;
    }

    public void setPGIdentifier(String PGIdentifier) {
        this.PGIdentifier = PGIdentifier;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getSecretCode() {
        return SecretCode;
    }

    public void setSecretCode(String secretCode) {
        SecretCode = secretCode;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
}