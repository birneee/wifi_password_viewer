package birneee.wifipasswordviewer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import birneee.wifipasswordviewer.utils.Wifi
import birneee.wifipasswordviewer.utils.WifiStoreReader
import birneee.wifipasswordviewer.utils.getCurrentSsid
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


private const val CHANNEL_WIFI = "tk.birneee.wifipasswordviewer/wifi"
private const val METHOD_GET_WIFIS = "getWifis"
private const val METHOD_GET_TEST_WIFIS = "getTestWifis"
private const val METHOD_GET_CONNECTED_WIFI = "getConnectedWifi"

class MainActivity : FlutterActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
            }
        }

        GeneratedPluginRegistrant.registerWith(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.transparent)
            /*var flags = flutterView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            flutterView.systemUiVisibility = flags*/
        }

        MethodChannel(flutterView, CHANNEL_WIFI).setMethodCallHandler { call, result ->
            when (call.method) {
                METHOD_GET_WIFIS -> {
                    try {
                        val wifis = getWifis().toJson()
                        result.success(wifis)
                    } catch (e: Exception) {
                        result.error("UNKNOWN", "An unknown error occurred", null)
                    }
                }
                METHOD_GET_TEST_WIFIS -> {
                    try {
                        val wifis = getRandomWifis(20).toJson()
                        result.success(wifis)
                    } catch (e: Exception) {
                        result.error("UNKNOWN", "An unknown error occurred", null)
                    }
                }
                METHOD_GET_CONNECTED_WIFI -> {
                    val ssid = getCurrentSsid(this)
                    result.success(ssid)
                }
                else -> result.notImplemented()
            }
        }
    }
}

private fun getRandomWifis(count: Int): List<Wifi> {
    return (1..count).map { Wifi(randomString(10), randomString(20)) }.sortedBy { it.ssid }
}

private fun getWifis(): List<Wifi> {
    return WifiStoreReader.getInstance().read().sortedBy { it.ssid }
}

fun <T> List<T>.toJson(): String = jacksonObjectMapper().writeValueAsString(this)

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun randomString(length: Int) = (1..length)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")