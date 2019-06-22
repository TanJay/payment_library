package lk.connectbench.payment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import lk.connectbench.payment.DTOs.AppConfig;

public class MQTTConnectionListner implements IMqttActionListener {

    private MqttAndroidClient client;
    private AppConfig appConfig;

    MQTTConnectionListner(MqttAndroidClient client, AppConfig appConfig) {
        this.client = client;
        this.appConfig = appConfig;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        String topic = String.format("payment/%s", appConfig.getOrderId());
        int qos = 1;
        IMqttToken subToken = null;

        try {
            subToken = client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        if (subToken != null) {
            MQTTSubscribeListner mqttSubscribeListner = new MQTTSubscribeListner(client);
            subToken.setActionCallback(mqttSubscribeListner);
        }
    }


    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }
}
