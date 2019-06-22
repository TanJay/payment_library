package lk.connectbench.payment.DTOs;

public class AppConfig {

    private String PGIdentifier;
    private String StoreName;
    private String SecretCode;
    private String Currency;
    private String cBenchPubServer;
    private String cBenchPubPort;
    private String cBenchPubServerUserName;
    private String cBenchPubServerPassword;
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

    public String getcBenchPubServer() {
        return cBenchPubServer;
    }

    public void setcBenchPubServer(String cBenchPubServer) {
        this.cBenchPubServer = cBenchPubServer;
    }

    public String getcBenchPubPort() {
        return cBenchPubPort;
    }

    public void setcBenchPubPort(String cBenchPubPort) {
        this.cBenchPubPort = cBenchPubPort;
    }

    public String getcBenchPubServerUserName() {
        return cBenchPubServerUserName;
    }

    public void setcBenchPubServerUserName(String cBenchPubServerUserName) {
        this.cBenchPubServerUserName = cBenchPubServerUserName;
    }

    public String getcBenchPubServerPassword() {
        return cBenchPubServerPassword;
    }

    public void setcBenchPubServerPassword(String cBenchPubServerPassword) {
        this.cBenchPubServerPassword = cBenchPubServerPassword;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
}

//    String PGIdentifier = getMetaData(activity.getApplicationContext(), "pg_identifier");
//    String StoreName = getMetaData(activity.getApplicationContext(), "store_name");
//    String SecretCode = getMetaData(activity.getApplicationContext(), "secret_code");
//    String Currency = getMetaData(activity.getApplicationContext(), "currency");
//    String cBenchPubServer = getMetaData(activity.getApplicationContext(), "pub_server");
//    String cBenchPubPort = getMetaData(activity.getApplicationContext(),"pub_port");
//    String cBenchPubServerUserName = getMetaData(activity.getApplicationContext(), "pub_username");
//    String cBenchPubServerPassword = getMetaData(activity.getApplicationContext(), "pub_password");
//    String callbackURL = getMetaData(activity.getApplicationContext(), "callback");