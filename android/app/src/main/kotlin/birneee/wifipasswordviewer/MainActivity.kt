package birneee.wifipasswordviewer

import android.os.Build
import android.os.Bundle
import android.view.View
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


private const val CHANNEL_WIFI = "tk.birneee.wifipasswordviewer/wifi"
private const val METHOD_GET_WIFIS = "getWifis"

class MainActivity : FlutterActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.transparent)
            var flags = flutterView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            flutterView.systemUiVisibility = flags
        };

        MethodChannel(flutterView, CHANNEL_WIFI).setMethodCallHandler { call, result ->
            when (call.method) {
                METHOD_GET_WIFIS -> {
                    try {
                        val wifis = getRandomWifis(100).toJson()
                        result.success(wifis)
                    } catch (e: Exception) {
                        result.error("UNKNOWN", "An unknown error occured", null)
                    }
                }
                else -> result.notImplemented()
            }
        }
    }
}

private fun getRandomWifis(count: Int): List<Wifi> {
    return (1..count).map { Wifi(randomString(10), randomString(20)) }
}

private fun getWifis(): List<Wifi> {
    return listOf(
            Wifi("wifi1", "blub"),
            Wifi("wifi2", "123")
    )
}

data class Wifi(val ssid: String, val password: String)

fun <T> List<T>.toArrayList() = ArrayList<T>(this)

fun <T> List<T>.toJson(): String = jacksonObjectMapper().writeValueAsString(this)

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun randomString(length: Int) = (1..length)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")