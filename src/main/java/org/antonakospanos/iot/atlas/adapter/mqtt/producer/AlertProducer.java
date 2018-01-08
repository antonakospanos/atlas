package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.antonakospanos.iot.atlas.web.dto.AccountAlertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AlertProducer extends MqttProducer {

	public String ALERTS_TOPIC = "accounts/${id}/alerts";

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	/**
	 * Publishes alerts to MQTT Broker
	 *
	 * @param accountAlerts
	 */
	public void publish(List<AccountAlertDto> accountAlerts) {
		for (AccountAlertDto accountAlert : accountAlerts) {
			UUID accountId = accountAlert.getAccount().getId();
			String alertsTopic = ALERTS_TOPIC.replace("${id}", accountId.toString());

			byte[] payload = serialize(accountAlert.getAlert().getCondition());
			if (payload != null) {
				mqttBrokerClient.publish(alertsTopic, payload, getQoS(), isRetained());
			}
		}
	}
}
