package lk.connectbench.payment.Helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import lk.connectbench.payment.DTOs.CardSaveConfig;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Exceptions.LoadConfigException;

import static lk.connectbench.payment.Helpers.GeneralHelper.getValue;


public class TokenManifestLoadConfig implements ILoadConfig<CardSaveConfig> {

    private static final String TAG = TokenManifestLoadConfig.class.getClass().getSimpleName();

    @Override
    public CardSaveConfig load(Context context, String amount) throws LoadConfigException {
        CardSaveConfig config = new CardSaveConfig();
        config.setCardSaveType(getMetaData(context, getValue(StringConfig.CARD_SAVE_TYPE)));
        config.setMerchantDisplayName(getMetaData(context,  getValue(StringConfig.MERCHANT_NAME)));
        config.setLanguage(getMetaData(context, getValue(StringConfig.LANGUAGE)));
        config.setMerchantPgIdentifier(getMetaData(context, getValue(StringConfig.MERCHANT_IDENTIFIER)));
        config.setStoreName(getMetaData(context, getValue(StringConfig.TOKEN_STORE_NAME)));
        config.setCurrency(getMetaData(context, getValue(StringConfig.CURRENCY)));
        //General Config Load
        config.setInvoiceNumber(GeneralHelper.generateOrderId());
        config.setAmount(amount);
//        GeneralHelper.<CardSaveConfig>nullOrEmptyCheck(config);
        return config;
    }




    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String result = "";
            try {
                result = bundle.getString(name);
                if(result == null){
                    result = String.valueOf(bundle.getInt(name));
                }
                return result;
            }catch (ClassCastException w){
                result = String.valueOf(bundle.getInt(name));
                return result;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }

}

