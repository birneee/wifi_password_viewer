package birneee.wifipasswordviewer.utils

import android.os.Build

interface WifiStoreReader{
    companion object {
        fun getInstance() : WifiStoreReader{
            return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                TODO("implement")
            } else{
                return WifiConfigStoreXmlReader()
            }
        }
    }

    fun read() : List<Wifi>
}

data class Wifi(val ssid: String, val password: String)
