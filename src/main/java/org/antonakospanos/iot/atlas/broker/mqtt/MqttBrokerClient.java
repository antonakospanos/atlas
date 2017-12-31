package org.antonakospanos.iot.atlas.broker.mqtt;

import org.antonakospanos.iot.atlas.broker.mqtt.consumer.MqttConsumer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttBrokerClient {

	private final static Logger logger = LoggerFactory.getLogger(MqttBrokerClient.class);

	private MqttClient mqttClient;

	@Value("${mqtt.broker.url}")
	private static String brokerUrl = "tcp://broker.mqttdashboard.com:1883";
	@Value("${mqtt.client.id}")
	private static String clientId = "org.iotac.atlas";

	@PostConstruct
	private void create() {
		try {
			mqttClient = new MqttClient(brokerUrl, clientId);
		} catch (MqttException e) {
			logger.error("Creating MQTT Client "+clientId+" to "+brokerUrl+": " + e);
		}
	}

	/**
	 * Wrapper method for MQTT topic subscriptions
	 *
	 * @param consumer
	 */
	public void subscribe(MqttConsumer consumer) {
		mqttClient.setCallback(consumer);
		connect();
		subscribe(consumer.getTopic(), consumer.getQoS());
	}

	private void connect() {

		if (!mqttClient.isConnected()) {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true); // clean or persistent session
			options.setKeepAliveInterval(180);
			options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT); // Tries with 3.1.1, else falls back to 3.1.0

			// Authentication
			// options.setUserName("username");
			// options.setPassword("mypw".toCharArray());

			// A Last Will and Testament (LWT) message can be specified by an MQTT client when connecting to the MQTT broker.
			// If that client does not disconnect gracefully, the broker sends out the LWT message on behalf of the client when connection loss is detected
			// options.setWill("will/topic", "message".getBytes(),	1, true);

			try {
				mqttClient.connect(options);
			} catch (MqttException e) {
				logger.error("Connecting to MQTT Broker "+brokerUrl+": " + e);
			}
		}
	}

	private void subscribe(String topic, int qos) {
		try {
			mqttClient.subscribe(topic, qos);
		} catch (MqttException e) {
			logger.error("Subscribing to MQTT Broker's Topic "+topic+": " + e);
		}
	}

	public void publish(String topic, byte[] message, int qos, boolean retained) {
		try {
			mqttClient.publish(topic, message, qos, retained);
		} catch (MqttException e) {
			logger.error("Publishing message "+message+" to MQTT Broker "+brokerUrl+": " + e);
		}
	}

	private void disconnect() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			logger.error("Disconnecting from MQTT Broker "+brokerUrl+": " + e);
		}
	}
}
