package lk.connectbench.payment.Helpers;

import android.content.Context;
import android.webkit.WebView;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import lk.connectbench.payment.DTOs.CardSaveConfig;
import lk.connectbench.payment.DTOs.PaymentConfig;
import lk.connectbench.payment.DTOs.TransactionResponse;
import lk.connectbench.payment.Enums.AppURL;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Exceptions.LoadConfigException;

import static lk.connectbench.payment.Enums.AppURL.CARD_SAVE_ONLY;
import static lk.connectbench.payment.Enums.AppURL.CARD_SAVE_W_TRANSACTION;
import static lk.connectbench.payment.Enums.AppURL.PAYMENT_ONLY;

public class GeneralHelper {

    protected static <T> void nullOrEmptyCheck(T appConfig) {
        for (Method method : appConfig.getClass().getMethods()) {
            if (method.getName().contains("get")) {
                String value = null;
                try {
                    value = method.invoke(appConfig).toString();
                    if (value == null || value.isEmpty())
                        throw new LoadConfigException();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getURLTrigger(Context contex) {
        return TokenManifestLoadConfig.getMetaData(contex, getValue(StringConfig.URL_CONTAIN_TRIGGER));
    }

    public static TransactionResponse resultExtracter(String url) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> urlResponse = splitQuery(new URL(url));
        TransactionResponse response = new TransactionResponse();
        response.setTokenization(!urlResponse.containsKey("charge_total"));
        if (urlResponse.containsKey("charge_total")) {
            response.setInvoiceNumber(urlResponse.get("invoice_number"));
            response.setStatus(urlResponse.get("status").equals("YES"));
            response.setCardSaveType(false);
        } else {
            response.setCardSaveType(urlResponse.get("cardSaveType").equals("Y"));
            response.setInvoiceNumber(urlResponse.get("invoiceNumber"));
            response.setStatus(!urlResponse.get("status").equals("failure"));
            if (!response.getStatus() && response.isTokenization())
                response.setPreviousInvoice(urlResponse.get("previousInvoiceNumber"));

        }
        response.setStatusCode(urlResponse.get("code"));
        response.setMessage(urlResponse.get("message"));
        return response;
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }


    public static String generateOrderId(){
        return String.valueOf((int)(Math.random() * 9999999 + 1));
    }

    private static String generateToken(PaymentConfig paymentConfig, String dateTime) {
        String preToken = paymentConfig.getStoreName() + paymentConfig.getCurrency() + paymentConfig.getSecretCode() + dateTime + paymentConfig.getAmount();
        return Hashing.sha1()
                .hashString(preToken, Charsets.UTF_8)
                .toString();
    }

    private static String generateTokenizationToken(CardSaveConfig appConfig, String dateTime){
        String preToken = appConfig.getInvoiceNumber() + appConfig.getStoreName() + appConfig.getMerchantPgIdentifier() + appConfig.getAmount() + dateTime;
        return Hashing.sha256()
                .hashString(preToken, Charsets.UTF_8)
                .toString();
    }

    private static String getDateTime(){
        Date date = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat(getValue(StringConfig.DATE_FORMAT));
        String dateTime = dt1.format(date);
        return dateTime;
    }

    private static String generateGeniePayment(PaymentConfig paymentConfig) {
        String dateTime = getDateTime();
        String postData = null;
        try {
            postData = "merchantPgIdentifier=" + URLEncoder.encode(paymentConfig.getPGIdentifier(), "UTF-8")
                    + "&chargeTotal=" + URLEncoder.encode(paymentConfig.getAmount(), "UTF-8")
                    + "&currency=" + URLEncoder.encode(paymentConfig.getCurrency(), "UTF-8")
                    + "&paymentMethod=" + URLEncoder.encode("SALE", "UTF-8")
                    + "&orderId=" + URLEncoder.encode(paymentConfig.getOrderId(), "UTF-8")
                    + "&invoiceNumber=" + URLEncoder.encode(paymentConfig.getOrderId(), "UTF-8")
                    + "&successUrl=" + URLEncoder.encode(paymentConfig.getCallbackURL(), "UTF-8")
                    + "&errorUrl=" + URLEncoder.encode(paymentConfig.getCallbackURL(), "UTF-8")
                    + "&storeName=" + URLEncoder.encode(paymentConfig.getStoreName(), "UTF-8")
                    + "&transactionDateTime=" + URLEncoder.encode(dateTime, "UTF-8")
                    + "&token=" + URLEncoder.encode(generateToken(paymentConfig, dateTime), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public static <T> void initiateWebView(AppURL url, WebView webView, T config) {
        switch (url) {
            case PAYMENT_ONLY:
                webView.postUrl(PAYMENT_ONLY.getValue(), generateGeniePayment((PaymentConfig) config).getBytes());
                break;
            case CARD_SAVE_ONLY:
                webView.postUrl(CARD_SAVE_ONLY.getValue(), generateGenieCardSave((CardSaveConfig) config).getBytes());
                break;
            case CARD_SAVE_W_TRANSACTION:
                webView.postUrl(CARD_SAVE_W_TRANSACTION.getValue(), generateGenieCardSaveWithInitialTransaction((CardSaveConfig) config).getBytes());
                break;
        }
    }

    private static String generateGenieCardSaveWithInitialTransaction(CardSaveConfig appConfig) {
        String dateTime = getDateTime();
        String postData = null;
        try {
            postData =
                    "currency=" + URLEncoder.encode(appConfig.getCurrency(), "UTF-8")
                    + "&cardSaveType=" + URLEncoder.encode(appConfig.getCardSaveType(), "UTF-8")
                    + "&invoiceNumber=" + URLEncoder.encode(appConfig.getInvoiceNumber(), "UTF-8")
                    + "&merchantDisplayName=" + URLEncoder.encode(appConfig.getMerchantDisplayName(), "UTF-8")
                    + "&language=" + URLEncoder.encode("en", "UTF-8")
                    + "&token=" + URLEncoder.encode(generateTokenizationToken(appConfig, dateTime), "UTF-8")
                    + "&chargeTotal=" + URLEncoder.encode(appConfig.getAmount(), "UTF-8")
                    + "&transactionDateTime=" + URLEncoder.encode(dateTime, "UTF-8")
                    + "&merchantPgIdentifier=" + URLEncoder.encode(appConfig.getMerchantPgIdentifier(), "UTF-8")
                    + "&storeName=" + URLEncoder.encode(appConfig.getStoreName(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postData;
    }


    private static String generateGenieCardSave(CardSaveConfig appConfig) {
        String postData = null;
        try {
            postData = "cardSaveType=" + URLEncoder.encode(appConfig.getCardSaveType(), "UTF-8")
                    + "&invoiceNumber=" + URLEncoder.encode(appConfig.getInvoiceNumber(), "UTF-8")
                    + "&merchantDisplayName=" + URLEncoder.encode(appConfig.getMerchantDisplayName(), "UTF-8")
                    + "&language=" + URLEncoder.encode("en", "UTF-8")
                    + "&merchantPgIdentifier=" + URLEncoder.encode(appConfig.getMerchantPgIdentifier(), "UTF-8")
                    + "&storeName=" + URLEncoder.encode(appConfig.getStoreName(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public static String getValue(StringConfig config){
        return config.getValue();
    }
}
