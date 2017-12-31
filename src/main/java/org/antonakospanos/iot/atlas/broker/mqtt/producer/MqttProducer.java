package org.antonakospanos.iot.atlas.broker.mqtt.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class MqttProducer {

	@Value("${mqtt.service.quality}")
	private static final int qos = 1;

	@Value("${mqtt.service.retained}")
	private static final boolean retained = false;

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
}
