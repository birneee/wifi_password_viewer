package birneee.wifipasswordviewer.bridges

import birneee.wifipasswordviewer.utils.Wifi
import birneee.wifipasswordviewer.utils.WifiStoreReader
import birneee.wifipasswordviewer.utils.getCurrentSsid
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterView

private const val CHANNEL_WIFI = "tk.birneee.wifipasswordviewer/wifi"
private const val METHOD_GET_WIFIS = "getWifis"
private const val METHOD_GET_TEST_WIFIS = "getTestWifis"
private const val METHOD_GET_CONNECTED_WIFI = "getConnectedWifi"

fun setupWifiBridge(flutterView: FlutterView){
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
                val ssid = getCurrentSsid(flutterView.getContext())
                result.success(ssid)
            }
            else -> result.notImplemented()
        }
    }
}

private fun getRandomWifis(count: Int): List<Wifi> {
    return (1..count).map { Wifi(randomString(10), randomString(20)) }.sortedBy { it.ssid }
}

private fun getWifis(): List<Wifi> {
    return WifiStoreReader.getInstance().read().sortedBy { it.ssid }
}

private fun <T> List<T>.toJson(): String = jacksonObjectMapper().writeValueAsString(this)

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun randomString(length: Int) = (1..length)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")