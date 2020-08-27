package mqtt_Client

String[] sDeviceAlternateId = UsefulParam.sDeviceAlternateId

MQTT_RunClient MQTT_Run = new MQTT_RunClient()

MQTT_Run.runClient(sDeviceAlternateId)
