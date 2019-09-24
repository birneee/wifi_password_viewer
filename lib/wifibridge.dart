import 'dart:convert';

import 'package:flutter/services.dart';

const _METHOD_CHANNEL_NAME = 'tk.birneee.wifipasswordviewer/wifi';
const _PLATFORM = const MethodChannel(_METHOD_CHANNEL_NAME);
const _METHOD_GET_WIFIS = "getWifis";
const _METHOD_GET_TEST_WIFIS = "getTestWifis";
const _METHOD_GET_CONNECTED_WIFI_SSID = "getConnectedWifi";

/// Requires root access
Future<List<Wifi>> getWifis() async {
  var json = await _PLATFORM.invokeMethod(_METHOD_GET_WIFIS);
  return _parseJson(json);
}

Future<List<Wifi>> getTestWifis() async {
  var json = await _PLATFORM.invokeMethod(_METHOD_GET_TEST_WIFIS);
  return _parseJson(json);
}

Future<String> getConnectedWifiSSID() async {
  return await _PLATFORM.invokeMethod(_METHOD_GET_CONNECTED_WIFI_SSID);
}

List<Wifi> _parseJson(String jsonWifiList) {
  return List<Wifi>.from(json
      .decode(jsonWifiList)
      .map((i) => new Wifi(i["ssid"], i["password"]))
      .toList());
}

class Wifi {
  final String ssid;
  final String password;

  Wifi(this.ssid, this.password);

  @override
  String toString() {
    return '$ssid $password';
  }
}
