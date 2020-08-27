package mqtt_Client

import groovy.json.JsonSlurper
import java.util.function.Consumer
import groovy.json.JsonOutput
import groovy.json.JsonBuilder

//import org.json.JSONArray
//import org.json.JSONObject

def filePath = UsefulParam.sDirInput + UsefulParam.sFileName
def file = new File(filePath)
def jsonString = file.getText()

// test commit 2

HashMap p1, p2

HashMap jsonIn = new JsonSlurper().parseText(jsonString)

ArrayList<HashMap<String, ?>> telemetryObject = jsonIn.telemetryDataList

// Faccio il Sort dell'Array, ordinando dal varId minore al maggiore

//ArrayList<HashMap<String, ?>> SortedList = telemetryObject.sort( { m1 , m2 -> m1.get("varId") <=> (m2.get("varId"))  } )

ArrayList<HashMap<String, ?>> SortedList = telemetryObject.sort( { it.varId  } )

// <=> stessa cosa che fare il compareTo
//ArrayList<HashMap<String, ?>> SortedList = telemetryObject.sort( { m1 , m2 -> m1.get("varId").compareTo(m2.get("varId"))  } )

//jsonIn.replace("telemetryDataList", SortedList)

LinkedHashMap mapOut = new LinkedHashMap()


mapOut.put("telemetryDataList", SortedList)
mapOut.put("devSn", jsonIn.devSn)
mapOut.put("onTime", jsonIn.onTime)
mapOut.put("ontTimeMillisUTC", jsonIn.ontTimeMillisUTC)

long dateMillis = jsonIn.ontTimeMillisUTC

def date = JsonUtil.convertEpoch_to_date(dateMillis)

def jsout = new JsonOutput()

String sJout = JsonOutput.prettyPrint( (String) jsout.toJson(mapOut) )

//println sJout

File newFile = new File(UsefulParam.sDirOutput + UsefulParam.sFileNameOut)
newFile.write(sJout, "utf-8")
