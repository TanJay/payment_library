package lk.connectbench.payment.Enums;

public enum  StringConfig {
    //TAG NAME
    DATE_FORMAT("yyyy-mm-dd HH:mm:ss"),
    PG_IDENTIFIER("pg_identifier"),
    STORE_NAME("store_name"),
    SECRET_CODE("secret_code"),
    CURRENCY("currency"),
    CALLBACK_URL("callback"),
    MERCHANT_IDENTIFIER("merchant_identifier"),
    TOKEN_STORE_NAME("token_store_name"),
    LANGUAGE("language"),
    MERCHANT_NAME("merchant_display"),
    BANNER_HIDE("banner_hide"),
    URL_CONTAIN_TRIGGER("url_trigger"),
    CARD_SAVE_TYPE("card_save_type");

    private final String value;

    StringConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

