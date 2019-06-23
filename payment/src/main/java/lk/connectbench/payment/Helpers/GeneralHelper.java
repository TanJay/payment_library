package lk.connectbench.payment.Helpers;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.connectbench.payment.DTOs.AppConfig;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Exceptions.LoadConfigException;

public class GeneralHelper {

    protected static void nullOrEmptyCheck(AppConfig appConfig) {
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

    public static String generateOrderId(){
        return String.valueOf((int)(Math.random() * 9999999 + 1));
    }

    private static String generateToken(AppConfig appConfig, String dateTime){
        String preToken = appConfig.getStoreName() + appConfig.getCurrency() + appConfig.getSecretCode() + dateTime + appConfig.getAmount();
        return Hashing.sha1()
                .hashString(preToken, Charsets.UTF_8)
                .toString();
    }

    private static String getDateTime(){
        Date date = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat(getValue(StringConfig.DATE_FORMAT));
        String dateTime = dt1.format(date);
        return dateTime;
    }

    public static String generateGeniePost(AppConfig appConfig){
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

    public static String getValue(StringConfig config){
        return config.getValue();
    }
}
