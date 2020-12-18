package edge_util


import groovy.io.*
import groovy.json.*
import org.apache.groovy.*

import groovy.json.JsonBuilder
import groovy.json.JsonOutput


String sPath = 'C:\\IoT_Gateway_Test\\gateway-4.50.0_NEW\\config\\devices'

def totalFileSize = 0
def groovyFileCount = 0
def sumFileSize = {
    totalFileSize += it.size()
    groovyFileCount++
}

File groovySrcDir = new File(sPath)

def filterGroovyFiles = ~/.*\.json$/
// def filterGroovyFiles = '.json'

// groovySrcDir.traverse( type: FILES, visit: sumFileSize, nameFilter: filterGroovyFiles )
// println "Total file size for $groovyFileCount Json source files is: $totalFileSize"

File[] fileArray = groovySrcDir.listFiles()
List<File> jsonFile = fileArray.toList().findAll {it.getName().matches(filterGroovyFiles)}

for (File fjson in jsonFile)
{
    List<String> yAx = []
    List<String> xAx = []
    List<String> zAx = []
    String fName = fjson.getName().replace('.json', '_v2.json')
    JsonSlurper jsonSlurper = new JsonSlurper()
    Map<String, List<String>> mapJson = jsonSlurper.parse(fjson)
    if (mapJson.containsKey('Y_LINEAR_AXIS_FMECA') )
    {
        yAx = mapJson.Y_LINEAR_AXIS_FMECA
        mapJson.remove('Y_LINEAR_AXIS_FMECA')
    }
    else
        continue
    if (mapJson.containsKey('X_LINEAR_AXIS_FMECA') )
    {
        xAx= mapJson.X_LINEAR_AXIS_FMECA
        mapJson.remove('X_LINEAR_AXIS_FMECA')
    }
    if (mapJson.containsKey('Z_LINEAR_AXIS_FMECA') )
    {
        zAx= mapJson.Z_LINEAR_AXIS_FMECA
        mapJson.remove('Z_LINEAR_AXIS_FMECA')
    }
    List<String> total = []
    total.addAll(xAx)
    total.addAll(yAx)
    total.addAll(zAx)
    mapJson.LINEAR_AXIS_FMECA = total
    // def jsonOut = JsonOutput.toJson(mapJson).toString()
    String jsonOut = JsonOutput.prettyPrint(JsonOutput.toJson(mapJson) as String)
    File file = new File(sPath + '\\' + fName )
    file.write(jsonOut)

}