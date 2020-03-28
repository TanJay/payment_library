package lk.connectbench.payment;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import lk.connectbench.payment.DTOs.AppConfig;
import lk.connectbench.payment.DTOs.CardSaveConfig;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Helpers.GeneralHelper;
import lk.connectbench.payment.Helpers.ILoadConfig;
import lk.connectbench.payment.Helpers.ManifestLoadConfig;
import lk.connectbench.payment.Helpers.TokenManifestLoadConfig;


public class GeniePayment extends Activity {
    private static final String TAG = GeniePayment.class.getName();
    static Dialog dialog;
    private static ResultListnerModel resultListnerModel;
    static MqttAndroidClient client;
    private static final String CHECKOUT_URL = "https://apps.genie.lk/merchant";
    private static final String CARDSAVE_URL = "https://apps.axis.dialog.lk/tokenization?type=cardsave&automation=yes";
    private static final String CARDSAVEWTRANS_URL = "https://apps.axis.dialog.lk/tokenization";


    public static void processPayment(final Activity activity, String amount) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<AppConfig> loadConfig = new ManifestLoadConfig();
        AppConfig appConfig = loadConfig.load(activity.getApplicationContext(), amount);
        InitPaymentScreen(activity, appConfig);
        MQTTServiceHandler.handler(activity, appConfig);
    }

    public static void processSaveCard(final Activity activity) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<CardSaveConfig> loadConfig = new TokenManifestLoadConfig();
        CardSaveConfig appConfig = loadConfig.load(activity.getApplicationContext(), "0.00");
        SaveCreditCard(activity, appConfig);
    }

    public static void processSaveCardWithInitialTransaction(final Activity activity, String amount) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<CardSaveConfig> loadConfig = new TokenManifestLoadConfig();
        CardSaveConfig appConfig = loadConfig.load(activity.getApplicationContext(), amount);
        SaveCreditCardWithInitialCharge(activity, appConfig);
    }

    public static ResultListnerModel getListner(Activity activity){
        return ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
    }

    private static void SaveCreditCard(final Activity activity, CardSaveConfig appConfig){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog);
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        WebView webView = dialog.findViewById(R.id.webView);
        setupWebview(webView);
        webView.postUrl(CARDSAVE_URL, GeneralHelper.generateGenieCardSave(appConfig).getBytes());
        Button dialogButton = dialog.findViewById(R.id.dismiss);
        GeniePayment.bannerVisibility(activity, dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeniePayment.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private static void SaveCreditCardWithInitialCharge(final Activity activity, CardSaveConfig appConfig){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog);
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        WebView webView = dialog.findViewById(R.id.webView);
        setupWebview(webView);
        webView.postUrl(CARDSAVEWTRANS_URL, GeneralHelper.generateGenieCardSaveWithInitialTransaction(appConfig).getBytes());
        Button dialogButton = dialog.findViewById(R.id.dismiss);
        GeniePayment.bannerVisibility(activity, dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeniePayment.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private static void InitPaymentScreen(final Activity activity, AppConfig appConfig){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog);
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        WebView webView = dialog.findViewById(R.id.webView);
        setupWebview(webView);
        webView.postUrl(CHECKOUT_URL, GeneralHelper.generateGeniePayment(appConfig).getBytes());
        Button dialogButton = dialog.findViewById(R.id.dismiss);
        GeniePayment.bannerVisibility(activity, dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeniePayment.dismiss();
            }
        });
        dialog.show();

    }

    private static void bannerVisibility(Activity activity, Dialog dialog){
        LinearLayout layout = dialog.findViewById(R.id.genie_banner);
        if(TokenManifestLoadConfig.getMetaData(activity.getApplicationContext(), StringConfig.BANNER_HIDE.getValue()).equals("true"))
            layout.setVisibility(View.GONE);
    }

    public static void sendUpdate(String message){
        resultListnerModel.getCurrentName().postValue(message);
    }

    private static void setupWebview(WebView webView) {
        webView.setWebViewClient( new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setMinimumFontSize(1);
        webView.getSettings().setMinimumLogicalFontSize(1);
    }

    public static void dismiss(){
        try {
            if(MQTTSubscribeListner.client != null) MQTTSubscribeListner.client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }



}
