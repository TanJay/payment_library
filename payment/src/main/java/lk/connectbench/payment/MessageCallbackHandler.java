package lk.connectbench.payment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MessageCallbackHandler implements MqttCallback {


    @Override
    public void connectionLost(Throwable cause) {
        GeniePayment.dialog.dismiss();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        GeniePayment.sendUpdate(message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
