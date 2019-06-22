package lk.connectbench.payment;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lk.connectbench.payment.DTOs.AppConfig;


public class MQTTServiceHandler {

    private final String TAG = this.getClass().getSimpleName();

    static MqttAndroidClient client;

    public static MqttAndroidClient handler(Activity activity, final AppConfig appConfig) {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(activity.getApplicationContext(), String.format("tcp://%s:%s", appConfig.getcBenchPubServer(), appConfig.getcBenchPubPort()),
                clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(appConfig.getcBenchPubServerUserName());
            options.setPassword(appConfig.getcBenchPubServerPassword().toCharArray());
            IMqttToken token = client.connect(options);
            MQTTConnectionListner listener = new MQTTConnectionListner(client, appConfig);
            token.setActionCallback(listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return client;
    }


}
