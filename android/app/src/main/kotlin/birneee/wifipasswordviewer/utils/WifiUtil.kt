package birneee.wifipasswordviewer.utils

import android.content.Context
import android.net.wifi.WifiManager

fun getCurrentSsid(context: Context): String? {
    return getWifiManager(context).connectionInfo?.ssid?.trim('\"')
}

fun getWifiManager(context: Context): WifiManager{
    return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
}