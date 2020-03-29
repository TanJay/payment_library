package lk.connectbench.payment.Enums;

public enum AppURL {

    CARD_SAVE_ONLY("https://apps.axis.dialog.lk/tokenization?type=cardsave&automation=yes"),
    CARD_SAVE_W_TRANSACTION("https://apps.axis.dialog.lk/tokenization"),
    PAYMENT_ONLY("https://apps.genie.lk/merchant");

    private final String value;

    AppURL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
