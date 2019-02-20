package birneee.wifipasswordviewer.utils

import eu.chainfire.libsuperuser.Shell

private val FILE_WIFI_CONFIG_STORE = "/data/misc/wifi/WifiConfigStore.xml"

class WifiConfigStoreXmlReader : WifiStoreReader {

    override fun read(): List<Wifi> {
        if(Shell.SU.available()){
            val xml = Shell.SU.run("cat $FILE_WIFI_CONFIG_STORE").joinToString("\n")
            return parseXml(xml)
        }
        else {
            throw Exception("failed to read file")
        }
    }

    fun parseXml(xml: String) : List<Wifi>{
        return xml.parseXml()
                .firstChildByTag("WifiConfigStoreData")!!
                .firstChildByTag("NetworkList")!!
                .childNodeList
                .filterByTag("Network")
                .flatMapChildNodes()
                .filterByTag("WifiConfiguration")
                .map {

                    Wifi(
                            it.firstChildByAttribute("name","SSID")?.textContent?.trim('\"').orEmpty(),
                            it.firstChildByAttribute("name","PreSharedKey")?.textContent?.trim('\"').orEmpty()
                    )
                }
    }

}

