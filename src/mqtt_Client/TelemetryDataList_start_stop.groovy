package mqtt_Client

import groovy.json.JsonSlurper

import java.util.Map.Entry
import java.util.function.Consumer
import java.util.stream.Stream
import groovy.json.JsonOutput
import groovy.json.JsonBuilder
import java.lang.Thread
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


//import org.json.JSONArray
//import org.json.JSONObject

def filePath = UsefulParam.sDirInput + UsefulParam.sFileName
def file = new File(filePath)
def jsonString = file.getText()
long date
//def jsonarray = new org.json.JSONArray(jsonStr)
//for (int i = 0; i < jsonarray.length(); i++) {
//	def jsonobject = jsonarray.getjsonbject(i)
//	
//	}	

def TelemetryJson = new JsonSlurper().parseText( jsonString )


boolean bStart = true

/*
 *  per ogni oggetto dell'array recuperiamo il millisUTC
 * e lo cambiamo con il nostro calcolato
 */

Thread.start { 	println("Press Enter to stop...");
				Scanner scanner = new Scanner(System.in);
				scanner.nextLine();
				bStart = false;
			}
	while (bStart)
		{

			
		/* vecchia versione 
		LinkedHashMap listaJsonOut_bk;
		LinkedHashMap msgOut = new LinkedHashMap()
		def listaJson = TelemetryJson.get("telemetryDataList")
		ArrayList<LinkedHashMap> listaJson2 = TelemetryJson.telemetryDataList
		ArrayList<LinkedHashMap<String, Object>> listaJsonOut = new ArrayList<LinkedHashMap<String, Object>>()
		ArrayList<LinkedHashMap<String, String> > listaJsonOutVector_bk = new ArrayList<LinkedHashMap>()
			
		date = ( System.currentTimeMillis() - UsefulParam.delay )
		for (LinkedHashMap currentJson : listaJson)
				{
				listaJsonOut_bk = new LinkedHashMap()
				currentJson.each { entry -> listaJsonOut_bk.put(entry.key, (( ( (String) entry.key).equalsIgnoreCase("millisUTC") ) ? date + "" : entry.value)) }
				listaJsonOut.add(listaJsonOut_bk)
				date = date + 100
			}
	
		def devSn= TelemetryJson.get("devSn")
		def onTime= TelemetryJson.get("onTime")
		def ontTimeMillisUTC = TelemetryJson.get("millisUTC")
		
		/*
		 * recuperare gli altri dati: ontTimeMillisUTC, onTime, devSn e costruire il JSON Finale
		
		
		def date1= System.currentTimeMillis() + ""
		
		LinkedHashMap mapOut= new LinkedHashMap()
		
		mapOut.put("telemetryDataList", listaJsonOut)
		mapOut.put("devSn", devSn)
		mapOut.put("onTime", onTime)
		mapOut.put("ontTimeMillisUTC", date1)
		
		def jsout = new JsonOutput()
		//def js = jsout.toJson(mapOut) + ""
		 * 
		 */

			List listaJson = TelemetryJson.get("telemetryDataList")
			ArrayList<LinkedHashMap> listaJson2 = TelemetryJson.telemetryDataList
			ArrayList<LinkedHashMap<String, Object>> listaJsonOut = new ArrayList<LinkedHashMap<String, Object>>()
			ArrayList<LinkedHashMap<String, String> > listaJsonOutVector_bk = new ArrayList<LinkedHashMap>()
			
			LinkedHashMap listaJsonOut_bk;
			LinkedHashMap msgOut = new LinkedHashMap()
			
			def builder = new JsonBuilder()
			
			builder {
				'telemetryDataList'( listaJson.collect {
					[
						/*'date' : ( new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.ENGLISH).format( new Date() ) ),*/
						"date" : ( sGetTimeExtended() ),
						'millisUTC': (System.currentTimeMillis() - UsefulParam.delay),
						"devId": it.devId,
						"varId": it.varId,
						"value": it.value,
						"quality": it.quality
					]
	
				} )
	
		'devSn' (TelemetryJson.devSn)
		"onTime" ( sGetTimeExtended() )
		"ontTimeMillisUTC" ( System.currentTimeMillis() - UsefulParam.delay )
	}
	
	def jsout = new JsonOutput()
	//def js = jsout.toJson(mapOut) + ""
	
	String sJout = builder.toPrettyString()
		
		//String sJout = JsonOutput.prettyPrint((String) jsout.toJson(mapOut))
		
		//JsonOutput jsOutAux = new JsonOutput(mapOut)
		
		//JsonBuilder jsOut= new JsonBuilder()
		
		//File newFile = new File("C:/Users/InnovatesApp/Desktop/msg_2.json")
		//newFile.write(sJout, "utf-8")
		
				MQTT_IngectMessages.MQTT_ClientSend(UsefulParam.sTopic + "/telemetry", sJout)
				System.sleep(UsefulParam.sleep);
			}
	println "Application Stopped"
	System.exit(0)
	
	String sGetTimeExtended()
	{
		DateTimeFormatter dateTimeFormatterOut = DateTimeFormatter.ofPattern( "MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH )
		LocalDateTime localDateTime = LocalDateTime.now().minusSeconds( ( UsefulParam.delay / 1000 ).toLong() )
		return localDateTime.format(dateTimeFormatterOut)
	}

	



