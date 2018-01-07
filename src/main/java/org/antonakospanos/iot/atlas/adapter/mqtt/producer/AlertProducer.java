package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertProducer extends MqttProducer {

	public String ALERTS_TOPIC = "accounts/+/alerts";

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	public void publish(String message) {
		byte[] payload = message.getBytes();
		mqttBrokerClient.publish(ALERTS_TOPIC, payload, getQoS(), isRetained());
	}
}
