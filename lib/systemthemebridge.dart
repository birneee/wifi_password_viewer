import 'dart:convert';

import 'package:flutter/services.dart';

const _METHOD_CHANNEL_NAME = 'tk.birneee.wifipasswordviewer/systemtheme';
const _PLATFORM = const MethodChannel(_METHOD_CHANNEL_NAME);
const _METHOD_SET_DARK_THEME = "setDarkTheme";
const _METHOD_SET_LIGHT_THEME = "setLightTheme";

Future<void> setDarkTheme() async {
  return _PLATFORM.invokeMethod(_METHOD_SET_DARK_THEME);
}

Future<void> setLightTheme() async {
  return _PLATFORM.invokeMethod(_METHOD_SET_LIGHT_THEME);
}
