package lk.connectbench.payment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTSubscribeListner implements IMqttActionListener {


    static MqttAndroidClient client;

    MQTTSubscribeListner(MqttAndroidClient client) {
        this.client = client;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        this.client.setCallback(new MessageCallbackHandler());
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        GeniePayment.dialog.dismiss();
    }
}
