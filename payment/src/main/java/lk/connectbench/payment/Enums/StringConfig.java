package lk.connectbench.payment.Enums;

public enum  StringConfig {
    //TAG NAME
    DATE_FORMAT("yyyy-mm-dd HH:mm:ss"),
    PG_IDENTIFIER("pg_identifier"),
    STORE_NAME("store_name"),
    SECRET_CODE("secret_code"),
    CURRENCY("currency"),
    PUB_SERVER("pub_server"),
    PUB_PORT("pub_port"),
    PUB_USERNAME("pub_username"),
    PUB_PASSWORD("pub_password"),
    CALLBACK_URL("callback");

    private final String value;

    StringConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

