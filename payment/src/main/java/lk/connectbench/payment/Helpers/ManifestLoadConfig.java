package lk.connectbench.payment.Helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import dagger.Component;
import lk.connectbench.payment.DTOs.AppConfig;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Exceptions.LoadConfigException;

import static lk.connectbench.payment.Helpers.GeneralHelper.getValue;


public class ManifestLoadConfig implements ILoadConfig {

    private static final String TAG = ManifestLoadConfig.class.getClass().getSimpleName();

    @Override
    public AppConfig load(Context context, String amount) throws LoadConfigException {
        AppConfig config = new AppConfig();
        config.setPGIdentifier(getMetaData(context, getValue(StringConfig.PG_IDENTIFIER)));
        config.setStoreName(getMetaData(context,  getValue(StringConfig.STORE_NAME)));
        config.setSecretCode(getMetaData(context, getValue(StringConfig.SECRET_CODE)));
        config.setCurrency(getMetaData(context, getValue(StringConfig.CURRENCY)));
        config.setcBenchPubServer(getMetaData(context, getValue(StringConfig.PUB_SERVER)));
        config.setcBenchPubPort(getMetaData(context,getValue(StringConfig.PUB_PORT)));
        config.setcBenchPubServerUserName(getMetaData(context, getValue(StringConfig.PUB_USERNAME)));
        config.setcBenchPubServerPassword(getMetaData(context, getValue(StringConfig.PUB_PASSWORD)));
        config.setCallbackURL(getMetaData(context, getValue(StringConfig.CALLBACK_URL)));

        //General Config Load
        config.setOrderId(GeneralHelper.generateOrderId());
        config.setAmount(amount);

//        GeneralHelper.nullOrEmptyCheck(config);
        return config;

    }




    private static String getMetaData(Context context, String name) {
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

