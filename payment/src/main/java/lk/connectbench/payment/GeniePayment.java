package lk.connectbench.payment;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GeniePayment extends Activity {

    private static final String TAG = "Genie Payment";
    static Dialog dialog;
    static MqttAndroidClient client;

    private static boolean nullOrEmptyCheck(String...set){
        for (String str:set) {
            if(str == null || str.isEmpty()) return true;
        }
        return false;
    }

    public static void Process(final Activity activity, String amount) throws Exception {
        final ResultListnerModel resultListnerModel = ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
        String PGIdentifier = getMetaData(activity.getApplicationContext(), "pg_identifier");
        String StoreName = getMetaData(activity.getApplicationContext(), "store_name");
        String SecretCode = getMetaData(activity.getApplicationContext(), "secret_code");
        String Currency = getMetaData(activity.getApplicationContext(), "currency");
        String cBenchPubServer = getMetaData(activity.getApplicationContext(), "pub_server");
        String cBenchPubPort = getMetaData(activity.getApplicationContext(),"pub_port");
        String cBenchPubServerUserName = getMetaData(activity.getApplicationContext(), "pub_username");
        String cBenchPubServerPassword = getMetaData(activity.getApplicationContext(), "pub_password");
        String callbackURL = getMetaData(activity.getApplicationContext(), "callback");
        if(nullOrEmptyCheck(PGIdentifier, StoreName, SecretCode, Currency, cBenchPubPort, cBenchPubServer, cBenchPubServerPassword, cBenchPubServerUserName, callbackURL)){
            throw new Exception("Missing Config");
        }
        final String orderId = String.valueOf((int)(Math.random() * 9999999 + 1));
        showDialog(activity, PGIdentifier, StoreName,  SecretCode, Currency, amount, orderId, callbackURL);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(activity.getApplicationContext(), String.format("tcp://%s:%s", cBenchPubServer, cBenchPubPort),
                clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(cBenchPubServerUserName);
            options.setPassword(cBenchPubServerPassword.toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    String topic = String.format("payment/%s", orderId);
                    Log.d(TAG, topic);

                    int qos = 1;
                    IMqttToken subToken = null;
                    try {
                        subToken = client.subscribe(topic, qos);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    if (subToken != null) {
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                                Log.d(TAG, "onSuccess1");
                                client.setCallback(new MqttCallback() {
                                    @Override
                                    public void connectionLost(Throwable cause) {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                                        Log.d(TAG, message.toString());
                                        resultListnerModel.getCurrentName().postValue(message.toString());
                                    }

                                    @Override
                                    public void deliveryComplete(IMqttDeliveryToken token) {

                                    }
                                });

                            }



                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                GeniePayment.dismiss();
                            }
                        });
                    }
                }


                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                    GeniePayment.dismiss();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static ResultListnerModel getListner(Activity activity){
        return ViewModelProviders.of((AppCompatActivity) activity).get(ResultListnerModel.class);
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

    private static void showDialog(final Activity activity, String pgIdentifier, String storeName, String scretCode, String currency, String amount, String orderId, String callbackURL){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog);
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        WebView webView = dialog.findViewById(R.id.webView);
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
        String url = "https://apps.genie.lk/merchant";
        Date date = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String dateString = dt1.format(date);
        String hs = storeName + currency + scretCode + dateString + amount;
        String token = Hashing.sha1()
                .hashString(hs, Charsets.UTF_8)
                .toString();
        String postData = null;
        try {
            postData = "merchantPgIdentifier=" + URLEncoder.encode(pgIdentifier, "UTF-8")
                    + "&chargeTotal=" + URLEncoder.encode(amount, "UTF-8")
                    + "&currency=" + URLEncoder.encode(currency, "UTF-8")
                    + "&paymentMethod=" + URLEncoder.encode("SALE", "UTF-8")
                    + "&orderId=" + URLEncoder.encode(orderId, "UTF-8")
                    + "&invoiceNumber=" + URLEncoder.encode(orderId, "UTF-8")
                    + "&successUrl=" + URLEncoder.encode(callbackURL, "UTF-8")
                    + "&errorUrl=" + URLEncoder.encode(callbackURL, "UTF-8")
                    + "&storeName=" + URLEncoder.encode(storeName, "UTF-8")
                    + "&transactionDateTime=" + URLEncoder.encode(dateString, "UTF-8")
                    + "&token=" + URLEncoder.encode(token, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webView.postUrl(url,postData.getBytes());
        Button dialogButton = dialog.findViewById(R.id.dismiss);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeniePayment.dismiss();
            }
        });

        dialog.show();

    }

    public static void dismiss(){
        try {
            if(client != null) client.disconnect();
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
