package lk.connectbench.payment;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import lk.connectbench.payment.DTOs.CardSaveConfig;
import lk.connectbench.payment.DTOs.PaymentConfig;
import lk.connectbench.payment.DTOs.TransactionResponse;
import lk.connectbench.payment.Enums.AppURL;
import lk.connectbench.payment.Enums.StringConfig;
import lk.connectbench.payment.Helpers.GeneralHelper;
import lk.connectbench.payment.Helpers.ILoadConfig;
import lk.connectbench.payment.Helpers.ManifestLoadConfig;
import lk.connectbench.payment.Helpers.TokenManifestLoadConfig;

import static lk.connectbench.payment.Enums.AppURL.CARD_SAVE_ONLY;
import static lk.connectbench.payment.Enums.AppURL.CARD_SAVE_W_TRANSACTION;
import static lk.connectbench.payment.Enums.AppURL.PAYMENT_ONLY;


public class GeniePayment extends Activity {
    private static final String TAG = GeniePayment.class.getName();
    static Dialog dialog;
    private static ResultListnerModel resultListnerModel;


    public static void processPayment(final Activity activity, String amount) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<PaymentConfig> loadConfig = new ManifestLoadConfig();
        PaymentConfig appConfig = loadConfig.load(activity.getApplicationContext(), amount);
        processGateway(activity, appConfig, PAYMENT_ONLY);
    }

    public static void processSaveCard(final Activity activity) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<CardSaveConfig> loadConfig = new TokenManifestLoadConfig();
        CardSaveConfig appConfig = loadConfig.load(activity.getApplicationContext(), "0.00");
        processGateway(activity, appConfig, CARD_SAVE_ONLY);
    }

    public static void processSaveCardWithInitialTransaction(final Activity activity, String amount) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig<CardSaveConfig> loadConfig = new TokenManifestLoadConfig();
        CardSaveConfig appConfig = loadConfig.load(activity.getApplicationContext(), amount);
        processGateway(activity, appConfig, CARD_SAVE_W_TRANSACTION);
    }

    public static ResultListnerModel getListner(Activity activity){
        return ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
    }

    private static <T> void processGateway(final Activity activity, T appConfig, AppURL url) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog);
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        WebView webView = dialog.findViewById(R.id.webView);
        setupWebview(activity.getApplicationContext(), webView);
        GeneralHelper.initiateWebView(url, webView, appConfig);
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

    public static void sendUpdate(TransactionResponse message){
        resultListnerModel.getCurrentName().postValue(message);
    }

    private static void setupWebview(final Context context, WebView webView) {
        webView.setWebViewClient( new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains(GeneralHelper.getURLTrigger(context))) {
                    try {
                        sendUpdate(GeneralHelper.resultExtracter(url));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setMinimumFontSize(1);
        webView.getSettings().setMinimumLogicalFontSize(1);
    }



    public static void dismiss(){
        dialog.cancel();
    }

    private static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }



}
