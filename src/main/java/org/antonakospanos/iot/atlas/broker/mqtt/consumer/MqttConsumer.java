package org.antonakospanos.iot.atlas.broker.mqtt.consumer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class MqttConsumer implements MqttCallback {

	@Value("${mqtt.service.quality}")
	private static final int qos = 1;

	@Override
	public void connectionLost(Throwable cause) {
		//Called when connection is lost
	}

	public abstract void deliveryComplete(IMqttDeliveryToken token);

	public abstract void messageArrived(String topic, MqttMessage message) throws Exception;

	public abstract String getTopic();

	public int getQoS() {
		return qos;
	};
}
