package org.antonakospanos.iot.atlas.adapter.mqtt;

import org.antonakospanos.iot.atlas.adapter.mqtt.consumer.MqttConsumer;
import org.antonakospanos.iot.atlas.service.HashService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttBrokerClient {

	private final static Logger logger = LoggerFactory.getLogger(MqttBrokerClient.class);

	private MqttClient mqttClient;

	@Value("${mqtt.broker.url}")
	private String brokerUrl = "tcp://broker.mqttdashboard.com:1883";
	@Value("${mqtt.client.id}")
	private String clientId = "org.iotac.atlas";
	@Value("${mqtt.broker.auth.username}")
	private String username;
	@Value("${mqtt.broker.auth.password}")
	private String password;
	@Value("${mqtt.broker.auth.hash-algorithm}")
	private String hashAlgorithm;
	@Value("${mqtt.broker.auth.hash-salt}")
	private String salt;

	@Autowired
	HashService hashService;

	@PostConstruct
	private void create() {
		try {
			logger.info("Trying to create client to MQTT broker "+brokerUrl);
			mqttClient = new MqttClient(brokerUrl, clientId);
			logger.info("Successfully created client to MQTT broker "+brokerUrl);
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
			options.setUserName(username);
			String passwd = password;
			if (hashAlgorithm != null) {
				passwd = hashService.hash(hashAlgorithm, salt, password );
			}
			logger.warn("Username':"+ username);
			logger.warn("Password':"+ passwd);
			options.setPassword(passwd.toCharArray());

			// A Last Will and Testament (LWT) message can be specified by an MQTT client when connecting to the MQTT broker.
			// If that client does not disconnect gracefully, the broker sends out the LWT message on behalf of the client when connection loss is detected
			// options.setWill("will/topic", "message".getBytes(),	1, true);

			try {
				logger.info("Trying to connect to MQTT broker "+brokerUrl);
				mqttClient.connect(options);
				logger.info("Successfully connected to MQTT broker "+brokerUrl);
			} catch (MqttException e) {
				logger.error("Connecting to MQTT Broker "+brokerUrl+": " + e);
			}
		}
	}

	private void subscribe(String topic, int qos) {
		try {
			logger.info("Trying to subscribe to MQTT topic "+topic);
			mqttClient.subscribe(topic, qos);
			logger.info("Successfully subscribed to MQTT topic "+topic);
		} catch (MqttException e) {
			logger.error("Subscribing to MQTT Broker's Topic "+topic+": " + e);
		}
	}

	public void publish(String topic, byte[] message, int qos, boolean retained) {
		connect();

		try {
			logger.info("Trying to publish message "+message+" to MQTT topic "+topic);
			mqttClient.publish(topic, message, qos, retained);
			logger.info("Successfully published message "+message+" to MQTT topic "+topic);
		} catch (MqttException e) {
			logger.error("Publishing message "+message+" to MQTT topic "+topic+": " + e);
		}

		// disconnect();
	}

	private void disconnect() {
		try {
			logger.info("Trying to disconnect from MQTT broker "+brokerUrl);
			mqttClient.disconnect();
			logger.info("Successfully to disconnect from MQTT broker "+brokerUrl);
		} catch (MqttException e) {
			logger.error("Disconnecting from MQTT Broker "+brokerUrl+": " + e);
		}
	}
}
