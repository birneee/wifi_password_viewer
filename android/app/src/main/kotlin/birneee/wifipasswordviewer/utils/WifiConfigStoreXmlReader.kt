package birneee.wifipasswordviewer.utils

import eu.chainfire.libsuperuser.Shell

private val FILE_PATH = "/data/misc/wifi/WifiConfigStore.xml"

class WifiConfigStoreXmlReader : WifiStoreReader {

    override fun read(): List<Wifi> {
        if(Shell.SU.available()){
            val xml = Shell.SU.run("cat $FILE_PATH").joinToString("\n")
            return parseXml(xml)
        }
        else {
            throw Exception("failed to read file")
        }
    }

    fun parseXml(xml: String) : List<Wifi>{
        return xml.xpath("/WifiConfigStoreData/NetworkList/Network/WifiConfiguration")
                .map {
                    Wifi(
                            it.firstChildByAttribute("name","SSID")?.textContent?.trim('\"').orEmpty(),
                            it.firstChildByAttribute("name","PreSharedKey")?.textContent?.trim('\"').orEmpty()
                    )
                }
    }

}

