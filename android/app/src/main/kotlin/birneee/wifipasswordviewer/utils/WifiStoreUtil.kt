package birneee.wifipasswordviewer.utils

import android.os.Build
import eu.chainfire.libsuperuser.Shell

private val FILE_WIFI_SUPPLICANT = "/data/misc/wifi/wpa_supplicant.conf"
private val FILE_WIFI_CONFIG_STORE = "/data/misc/wifi/WifiConfigStore.xml"

private fun getFile() : String{
    return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        FILE_WIFI_SUPPLICANT
    } else{
        FILE_WIFI_CONFIG_STORE
    }
}

private fun readWifiFile(){
    if(Shell.SU.available()){
        val result = Shell.SU.run("cat wifiFile").joinToString("\n")

    }
}