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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import lk.connectbench.payment.DTOs.AppConfig;
import lk.connectbench.payment.Helpers.GeneralHelper;
import lk.connectbench.payment.Helpers.ILoadConfig;
import lk.connectbench.payment.Helpers.ManifestLoadConfig;


public class GeniePayment extends Activity {
    private static final String TAG = GeniePayment.class.getName();
    static Dialog dialog;
    private static ResultListnerModel resultListnerModel;
    static MqttAndroidClient client;


    public static void Process(final Activity activity, String amount) throws Exception {
        resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        ILoadConfig loadConfig = new ManifestLoadConfig();
        AppConfig appConfig = loadConfig.load(activity.getApplicationContext(), amount);
        InitPaymentScreen(activity, appConfig);
        MQTTServiceHandler.handler(activity, appConfig);
    }

    public static ResultListnerModel getListner(Activity activity){
        return ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
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
        String url = "https://apps.genie.lk/merchant";
        webView.postUrl(url, GeneralHelper.generateGeniePost(appConfig).getBytes());
        Button dialogButton = dialog.findViewById(R.id.dismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeniePayment.dismiss();
            }
        });
        dialog.show();

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
