package org.antonakospanos.iot.atlas.broker.mqtt.producer;

import org.antonakospanos.iot.atlas.broker.mqtt.MqttBrokerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertProducer extends MqttProducer {

	public static final String ALERTS_TOPIC = "accounts/+/alerts";

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	public void publish(String message) {
		byte[] payload = message.getBytes();
		mqttBrokerClient.publish(ALERTS_TOPIC, payload, getQoS(), isRetained());
	}
}
