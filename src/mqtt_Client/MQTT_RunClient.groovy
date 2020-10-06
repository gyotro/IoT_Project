package mqtt_Client

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.text.ParseException
import java.text.SimpleDateFormat
import mqtt_Client.UsefulParam

import java.util.ArrayList;
import java.util.Arrays;

class MQTT_RunClient implements MqttCallback{

	private MqttClient mqttForwardClient;
	private MqttConnectOptions connOpts;

	String brokerAddress = UsefulParam.sBrokerAddress

	@Override
	public void connectionLost(Throwable arg0) {
		try {
			this.runClient();
		} catch (Exception e) {
			println e.printStackTrace()
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	public void messageArrived(String topic, MqttMessage message)
	{
		String sAlarmSufixf = ''
		String arrivedMsg = null;
		String sEnergySuffixf = ''
		//HashMap msgOut = new HashMap()
		LinkedHashMap mapOut= new LinkedHashMap()
		HashMap msgIn

		if (message.getPayload() != null && !message.getPayload().toString().isEmpty() && !message.isRetained())
		{
			arrivedMsg = new String(message.getPayload())

			if( UsefulParam.bOnlyAlarms )
			{
				JsonSlurper jsonIn = new JsonSlurper()
				Map jmapIn = jsonIn.parseText(arrivedMsg)
				List telemetryDataList = jmapIn.telemetryDataList
				if(telemetryDataList.any { it.varId == 6100 })
				{
					sAlarmSufixf = '_isAlarm'
				}
				else
					return
			}
			if( UsefulParam.bOnlyEnergetics )
			{
				JsonSlurper jsonIn = new JsonSlurper()
				Map jmapIn = jsonIn.parseText(arrivedMsg)
				List telemetryDataList = jmapIn.telemetryDataList
				if(telemetryDataList.any { it.varId  <= 15 && it.quality == true })
				{
					sEnergySuffixf = '_isEnergy'
				}
				else
					return
			}
		} else {
			return;
		}

		/* non si applicano filtri

		println("-------------------------------------------------");
		println("| Topic:" + topic);
//		println("| Message: " + arrivedMsg);
		println("-------------------------------------------------");

		JsonSlurper arrivedJObj = new JsonSlurper()
		try {
				msgIn = arrivedJObj.parseText(arrivedMsg)

				// se non c'� il telemetry data list si esce
				if(!msgIn.containsKey("telemetryDataList"))
				{
					println "No telemetryDataList in the message"
					return;
				}

				ArrayList<HashMap<String , ?>> telemetryJArr = msgIn.telemetryDataList
				ArrayList<HashMap<String , ?>> listaJsonOutVector_bk = new ArrayList<HashMap<String , ?>>()

		//		for (HashMap currentJson : telemetryJArr)
		//		{
		//			currentJson.eachWithIndex{ entry, index -> (entry.getKey("varId") ) ? date : entry.value)) }
		//			listaJsonOut.add(listaJsonOut_bk)
		//		}
				listaJsonOutVector_bk.add( telemetryJArr.find{it.varId == 5005} )

//				println listaJsonOutVector_bk.get(0)
//				println listaJsonOutVector_bk.size

				if(listaJsonOutVector_bk.isEmpty() || listaJsonOutVector_bk.size == 0 || listaJsonOutVector_bk.get(0) == null)
				{
					println "No varId 5005 in the message"
					return
				}
				mapOut.put("telemetryDataList", listaJsonOutVector_bk)
				mapOut.put("devSn", msgIn.devSn)
				mapOut.put("onTime", msgIn.onTime)
				mapOut.put("ontTimeMillisUTC", msgIn.ontTimeMillisUTC)

				def jsout = new JsonOutput()

				String sJout = JsonOutput.prettyPrint((String) jsout.toJson(mapOut))

				*/

		String sDevice;

		if(topic.contains( "a84d49d0b62f" ))
			sDevice = "FE-242";
		else if(topic.contains( "b1f0db4de45d" ))
			sDevice = "FE-243";
		else if(topic.contains( "d4973290b3e3" ))
			sDevice = "FE-244";
		else if(topic.contains( "0f56e905001d" ))
			sDevice = "FE-148";
		else if(topic.contains( "4543b7b528ba" ))
			sDevice = "FE-128";
		else if(topic.contains( "48ce0bcb2f09" ))
			sDevice = "FE-164";
		else if(topic.contains( "7552eb14f64d" ))
			sDevice = "FE-660";
		else if(topic.contains( "ac01896953fb" ))
			sDevice = "FE-666";
		else if(topic.contains( "4543b7b528ba" ))
			sDevice = "FE-128";
		else if(topic.contains( "d319931283e0" ))
			sDevice = "FE-127";
		else if(topic.contains( "b5b5f87e7bf7" ))
			sDevice = "FE-198";
		else if(topic.contains( "8253341621c7" ))
			sDevice = "FE-197";
		else if(topic.contains( "a860cce90327" ))
			sDevice = "FE-145";


		String sFileName = "File_" + sDevice + "_" + MQTT_RunClient.convertEpoch_to_date( System.currentTimeMillis() ) + sAlarmSufixf + sEnergySuffixf + ".json"
//				String sFileName = "File_" + sDevice + "_" + MQTT_RunClient.convertEpoch_to_date( System.currentTimeMillis() ) + ".json"
		println "Writing File $sFileName"

		File newFile = new File(UsefulParam.sDirOutput + sFileName)
		newFile.write(arrivedMsg, "utf-8")


//		} catch (Exception e1) {
//			println e1.printStackTrace()
//		}

	}

	public void runClient(String[] sDeviceAlternateId) throws InterruptedException {

		// Setting up the MQTT client
		connOpts = new MqttConnectOptions();

		connOpts.setCleanSession(true);
		connOpts.setKeepAliveInterval(30);
		connOpts.setAutomaticReconnect(true);

		println "Trying to connect to broker..."
		// Connect to broker
		while (true)
			try {

				MemoryPersistence persistence1 = new MemoryPersistence();

				mqttForwardClient = new MqttClient(brokerAddress, MqttClient.generateClientId(), persistence1);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				connOpts.setKeepAliveInterval(30);
				connOpts.setAutomaticReconnect(true);
				connOpts.setMaxInflight(1000);
				mqttForwardClient.setCallback(this);
				mqttForwardClient.connect(connOpts);

				println "Custom MQTT Client connected to " + brokerAddress;

				loadSubscriptions(sDeviceAlternateId)

				break;

			} catch (MqttException e) {
				println(e.getCause().toString());
				Thread.sleep(5000);
			}
	}

	public void mqttForwardMessage(MqttMessage message, String topic) {

		try {
			// Publish message to broker
			mqttForwardClient.publish(topic, message);
		} catch (MqttException e) {
			println e.printStackTrace();
		}
	}

	public void disconnectClient() {
		try {
			mqttForwardClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadSubscriptions(String[] sTopicToSubscribe) {

		println "Starting Subscription....."

//				String configJsonStr = InterceptorActivator.configJson.toString();
//				JSONObject configJson = null;
//				JSONArray topicsToSubscribe = null;
//				try {
//					configJson = (JSONObject) new JSONParser().parse(configJsonStr);
//					topicsToSubscribe = (JSONArray) new JSONParser().parse(configJson.get("devicesTopics").toString());
//				} catch (ParseException e1) {
//					log.error("Error while parsing json", e1);
//				}
//		
		ArrayList<String> topicFiltersAL = new ArrayList<String>();
		ArrayList<Integer> QoSAL = new ArrayList<Integer>();

//				for (int i = 0; i < topicsToSubscribe.toArray().length; i++) {
//					// i topic in uscita da alleantia avranno i topic devideAlternateId/telemetry (in cui ci saranno sia quelli di telemetria, sia gli allarm)
//					String topicToSubscribe = topicsToSubscribe.get(i) + "/telemetry";
//					log.info(topicToSubscribe);
//					topicFiltersAL.add(topicToSubscribe);
//					QoSAL.add(0);
//				}

		for( String s : sTopicToSubscribe )
		{
			String sTopic = s + "/telemetry"
			topicFiltersAL.add(sTopic)
			println "Adding topic $sTopic"
			QoSAL.add(0)
		}
		// si converte da Lista ad array di oggetti e poi da array di oggetti in array di stringhe
		Object[] objectTopicList = topicFiltersAL.toArray();
		String[] topicFilters = Arrays.copyOf(objectTopicList, objectTopicList.length, String[].class);

		int[] qosFilters = new int[QoSAL.size()];
		// si crea un array di int per la QoS
		for (int i = 0; i < QoSAL.size(); i++) {
			qosFilters[i] = QoSAL.get(i);
		}

		try {
			mqttForwardClient.subscribe(topicFilters, qosFilters);
			println "Subscribed to $topicFilters"
		} catch (MqttException e)
		{
			println "Error Subscribing to $topicFilters"
			e.printStackTrace();
		}
	}
	public static String convertEpoch_to_date(long timeInMillis)
	{
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Calendar calendar = new GregorianCalendar();
		format.setTimeZone(calendar.getTimeZone());
		return format.format(timeInMillis);
	}

}
