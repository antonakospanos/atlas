package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antonakospanos.iot.atlas.adapter.mqtt.MqttBrokerClient;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionProducer extends MqttProducer {

	public static final String ACTIONS_TOPIC = "devices/+/actions";

	private final static Logger logger = LoggerFactory.getLogger(ActionProducer.class);

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	@Autowired
	ObjectMapper jsonSerializer;

	public void publishAction(List<ModuleActionDto> actions) {
		for (ModuleActionDto action: actions) {

			byte[] payload = null;
			try {
				payload = jsonSerializer.writeValueAsBytes(action);
			} catch (JsonProcessingException e) {
				logger.error("Could not serialize publishing action to MQTT Broker: " + action);
			}

			mqttBrokerClient.publish(ACTIONS_TOPIC, payload, getQoS(), isRetained());
		}
	}
}
