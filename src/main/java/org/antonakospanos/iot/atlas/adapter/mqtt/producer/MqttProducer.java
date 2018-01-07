package org.antonakospanos.iot.atlas.adapter.mqtt.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class MqttProducer {

	@Value("${mqtt.service.quality}")
	private int qos = 1;

	@Value("${mqtt.service.retained}")
	private boolean retained = false;

	@Autowired
	ObjectMapper jsonSerializer;

	private final static Logger logger = LoggerFactory.getLogger(MqttProducer.class);


	public int getQoS() {
		return qos;
	};

	/**
	 * Normally if a publisher publishes a message to a topic, and no one is subscribed to that topic the message is simply discarded by the broker.
	 * However the publisher can tell the broker to keep the last message on that topic by setting the retained message flag.
	 * This can be very useful if the producer publishes messages only only when status changed,
	 * cause the subscriber would have to wait for the status to change before it received the first status message.
	 * With retained = true the last, only the last, message will be retained by the broker and it will be eligible for consumption by topic's new subscribers.
	 *
	 * @return
	 */
	public boolean isRetained() {
		return retained;
	}

	public byte[] serialize(Object object) {
		byte[] payload = null;
		try {
			payload = jsonSerializer.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			logger.error("Could not serialize: " + object);
		}

		return payload;
	}
}
