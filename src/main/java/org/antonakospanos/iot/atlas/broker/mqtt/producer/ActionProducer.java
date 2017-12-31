package org.antonakospanos.iot.atlas.broker.mqtt.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antonakospanos.iot.atlas.broker.mqtt.MqttBrokerClient;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionProducer extends MqttProducer {

	public static final String ACTIONS_TOPIC = "devices/+/actions";

	@Autowired
	MqttBrokerClient mqttBrokerClient;

	@Autowired
	ObjectMapper jsonSerializer;

	public void publishAction(List<ModuleActionDto> actions) throws JsonProcessingException {
		for (ModuleActionDto action: actions) {
			byte[] payload = jsonSerializer.writeValueAsBytes(action);
			mqttBrokerClient.publish(ACTIONS_TOPIC, payload, getQoS(), isRetained());
		}
	}
}
