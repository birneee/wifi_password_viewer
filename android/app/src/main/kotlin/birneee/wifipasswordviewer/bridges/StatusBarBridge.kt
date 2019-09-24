package birneee.wifipasswordviewer.bridges

import android.os.Build
import android.view.View
import birneee.wifipasswordviewer.R
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel

private const val CHANNEL_NAME = "tk.birneee.wifipasswordviewer/systemtheme"
private const val METHOD_SET_DARK_THEME = "setDarkTheme"
private const val METHOD_SET_LIGHT_THEME = "setLightTheme"

fun setupSystemThemeBridge(flutterActivity: FlutterActivity){
    MethodChannel(flutterActivity.flutterView, CHANNEL_NAME).setMethodCallHandler { call, result ->
        when (call.method) {
            METHOD_SET_DARK_THEME -> {
                try {
                    flutterActivity.setDarkTheme()
                } catch (e: Exception) {
                    result.error("UNKNOWN", "An unknown error occurred", null)
                }
            }
            METHOD_SET_LIGHT_THEME -> {
                try {
                    flutterActivity.setLightTheme()
                } catch (e: Exception) {
                    result.error("UNKNOWN", "An unknown error occurred", null)
                }
            }
            else -> result.notImplemented()
        }
    }
}

private fun FlutterActivity.setDarkTheme(){
    var flags = this.flutterView.systemUiVisibility
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
    this.flutterView.systemUiVisibility = flags
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.window.statusBarColor = resources.getColor(R.color.transparent)
        this.window.navigationBarColor = resources.getColor(R.color.black)
    }
}

private fun FlutterActivity.setLightTheme(){
    var flags = this.flutterView.systemUiVisibility
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    this.flutterView.systemUiVisibility = flags
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.window.statusBarColor = resources.getColor(R.color.transparent)
        this.window.navigationBarColor = resources.getColor(R.color.white)
    }
}