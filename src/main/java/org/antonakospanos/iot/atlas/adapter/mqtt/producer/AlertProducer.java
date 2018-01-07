package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertProducer extends MqttProducer {

	public String ALERTS_TOPIC = "accounts/${id}/alerts";

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	public void publish(Object alert, String accountId) {
		String alertsTopic = ALERTS_TOPIC.replace("${id}", accountId);

		byte[] payload = serialize(alert);
		if (payload != null) {
			mqttBrokerClient.publish(alertsTopic, payload, getQoS(), isRetained());
		}
	}
}
