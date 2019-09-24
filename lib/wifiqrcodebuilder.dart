const _SEPARATOR_VALUE = ':';
const _SEPARATOR_FIELD = ';';

const _QR_WIFI = "WIFI";
const _QR_WIFI_AUTHENTICATION_TYP = "T";
const _QR_WIFI_SSID = "S";
const _QR_WIFI_PASSWORD = "P";

const AUTHENTICATION_WPA = "WPA";
const AUTHENTICATION_WEP = "WEP";
const AUTHENTICATION_NOPASS = "nopass";

String build(String ssid, String password, String authenticationMode) {
  if (password == null) {
    password = "";
  }

  return _QR_WIFI +
      _SEPARATOR_VALUE +
      _QR_WIFI_AUTHENTICATION_TYP +
      _SEPARATOR_VALUE +
      authenticationMode +
      _SEPARATOR_FIELD +
      _QR_WIFI_SSID +
      _SEPARATOR_VALUE +
      ssid +
      _SEPARATOR_FIELD +
      _QR_WIFI_PASSWORD +
      _SEPARATOR_VALUE +
      password +
      _SEPARATOR_FIELD +
      _SEPARATOR_FIELD;
}
