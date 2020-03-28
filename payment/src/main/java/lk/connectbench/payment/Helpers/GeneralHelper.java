package lk.connectbench.payment.Helpers;

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

import lk.connectbench.payment.DTOs.AppConfig;
import lk.connectbench.payment.DTOs.CardSaveConfig;
import lk.connectbench.payment.DTOs.TransactionResponse;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Exceptions.LoadConfigException;

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

    public static TransactionResponse resultExtracter(String url) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> urlResponse = splitQuery(new URL(url));
        TransactionResponse response = new TransactionResponse();
        response.setCardSaveType(urlResponse.get("cardSaveType").equals("Y"));
        response.setStatusCode(urlResponse.get("code"));
        response.setMessage(urlResponse.get("message"));
        response.setStatus(urlResponse.get("status").equals("failure"));
        response.setInvoiceNumber(urlResponse.get("invoiceNumber"));
        if(!response.getStatus()) response.setPreviousInvoice(urlResponse.get("previousInvoiceNumber"));
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

    private static String generateToken(AppConfig appConfig, String dateTime){
        String preToken = appConfig.getStoreName() + appConfig.getCurrency() + appConfig.getSecretCode() + dateTime + appConfig.getAmount();
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

    public static String generateGeniePayment(AppConfig appConfig){
        String dateTime = getDateTime();
        String postData = null;
        try {
            postData = "merchantPgIdentifier=" + URLEncoder.encode(appConfig.getPGIdentifier(), "UTF-8")
                    + "&chargeTotal=" + URLEncoder.encode(appConfig.getAmount(), "UTF-8")
                    + "&currency=" + URLEncoder.encode(appConfig.getCurrency(), "UTF-8")
                    + "&paymentMethod=" + URLEncoder.encode("SALE", "UTF-8")
                    + "&orderId=" + URLEncoder.encode(appConfig.getOrderId(), "UTF-8")
                    + "&invoiceNumber=" + URLEncoder.encode(appConfig.getOrderId(), "UTF-8")
                    + "&successUrl=" + URLEncoder.encode(appConfig.getCallbackURL(), "UTF-8")
                    + "&errorUrl=" + URLEncoder.encode(appConfig.getCallbackURL(), "UTF-8")
                    + "&storeName=" + URLEncoder.encode(appConfig.getStoreName(), "UTF-8")
                    + "&transactionDateTime=" + URLEncoder.encode(dateTime, "UTF-8")
                    + "&token=" + URLEncoder.encode(generateToken(appConfig, dateTime), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public static String generateGenieCardSaveWithInitialTransaction(CardSaveConfig appConfig){
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


    public static String generateGenieCardSave(CardSaveConfig appConfig){
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
