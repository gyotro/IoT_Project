package mqtt_Client

class UsefulParam 
{
	public static final String sDirInput = "C:\\Users\\InnovatesApp\\Desktop\\TestIoT\\"
	public static final String sDirOutput = "C:\\Users\\InnovatesApp\\Desktop\\TestIoT\\"
	//public static final String sFileName = "msg_alarms" + ".json"
	public static final String sFileName = "File_FE-660_2020-08-18-17-45-16-984" + ".json"
	//public static final String sFileName = "msg_alarms_2.json"
	//public static final String sFileName = "msg_4_G01_X1.json"
	public static final String sFileNameOut = "msg_alarms_out.json"
	public static final int iQoS = 0
	//public static final String sBrokerAddress = "tcp://192.168.10.2:61618"
	public static final String sBrokerAddress = "tcp://127.0.0.1:61618"
	public static final String sClientId = "GroovyClient_"
	public static final String[] sDeviceAlternateId = ["43ed21b6f-a113-3aaa-8bf4-b5b5f87e7bf7" , "400bf5925-9ae7-300e-99a8-e75fde46b4f2" ]
	public static final String sTopic = "660";
	public static final int iMinutes = 1;
	public static final long delay = 1000 * 60 * iMinutes;
	public static final long sleep = 2000;
	
	// Test Locale
}
