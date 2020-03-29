package lk.connectbench.payment.DTOs;

public class CardSaveConfig {

    private String cardSaveType;
    private String invoiceNumber;
    private String merchantDisplayName;
    private String language;
    private String merchantPgIdentifier;
    private String storeName;
    private String contextPath;
    private String currency;
    private String amount;

    public CardSaveConfig() {

    }

    public CardSaveConfig(String invoiceNumber, String merchantDisplayName, String merchantPgIdentifier, String storeName) {
        this.invoiceNumber = invoiceNumber;
        this.merchantDisplayName = merchantDisplayName;
        this.merchantPgIdentifier = merchantPgIdentifier;
        this.storeName = storeName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardSaveType() {
        return cardSaveType;
    }

    public void setCardSaveType(String cardSaveType) {
        this.cardSaveType = cardSaveType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMerchantDisplayName() {
        return merchantDisplayName;
    }

    public void setMerchantDisplayName(String merchantDisplayName) {
        this.merchantDisplayName = merchantDisplayName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMerchantPgIdentifier() {
        return merchantPgIdentifier;
    }

    public void setMerchantPgIdentifier(String merchantPgIdentifier) {
        this.merchantPgIdentifier = merchantPgIdentifier;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    String reference;
}
