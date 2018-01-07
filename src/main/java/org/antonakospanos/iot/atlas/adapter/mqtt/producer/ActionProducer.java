package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionProducer extends MqttProducer {

	public static final String ACTIONS_TOPIC = "devices/${id}/actions";

	private final static Logger logger = LoggerFactory.getLogger(ActionProducer.class);

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	/**
	 * Publishes actions to MQTT Broker for specific device (mostly triggered by device's heartbeat)
	 *
	 * @param actions
	 * @param deviceId
	 */
	public void publish(List<ModuleActionDto> actions, String deviceId) {
		if (actions != null && !actions.isEmpty()) {
			String actionsTopic = ACTIONS_TOPIC.replace("${id}", deviceId);

			for (ModuleActionDto action: actions) {
				byte[] payload = serialize(action);

				if (payload != null) {
					mqttBrokerClient.publish(actionsTopic, payload, getQoS(), isRetained());
				}
			}
		}
	}
}
