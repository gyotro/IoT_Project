package mqtt_Client

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.UUID;

class MQTT_IngectMessages {

	public static MQTT_ClientSend(String sTopic, String sMessage) 
	{
		int qos             = UsefulParam.iQoS
		UUID uuid			= UUID.randomUUID();
		String broker       = UsefulParam.sBrokerAddress
		String clientId     = UsefulParam.sClientId + uuid.toString()
		
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			println("Connecting to broker: "+broker);
			sampleClient.connect(connOpts);
			println("Connected");
			println("Publishing message: "+sMessage);
			MqttMessage message = new MqttMessage(sMessage.getBytes());
			message.setQos(qos);
			sampleClient.publish( sTopic, message);
			println("Message published");
			sampleClient.disconnect();
			println("Disconnected");
			//System.exit(0); //--> commentato per il caso con while
		} catch(MqttException me) {
			println("reason "+me.getReasonCode());
			println("msg "+me.getMessage());
			println("loc "+me.getLocalizedMessage());
			println("cause "+me.getCause());
			println("excep "+me);
			me.printStackTrace();
		}
	}
}
