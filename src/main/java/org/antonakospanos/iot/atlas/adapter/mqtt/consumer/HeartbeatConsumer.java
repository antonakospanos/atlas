package org.antonakospanos.iot.atlas.adapter.mqtt.consumer;

import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class HeartbeatConsumer extends MqttConsumer {

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	public static final String HEARTBEAT_TOPIC = "devices/+/heartbeats";

	@PostConstruct
	public void subscribe() {
			mqttBrokerClient.subscribe(this);
	}

	@Override
	public String getTopic() {
		return HEARTBEAT_TOPIC;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Heartbeat from topic: " + topic);

		// HeartbeatRequest hb = new String(message. getPayload());
		// TODO: Update Device's Modules in DB
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// When message delivery was complete
	}
}