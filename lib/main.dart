import 'dart:async';
import 'dart:core';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:math';
import 'package:qr_flutter/qr_flutter.dart';
import 'wifibridge.dart' as wifi;
import 'systemthemebridge.dart' as systemtheme;
import 'wifiqrcodebuilder.dart' as wifiqrcode;

const PROPERTY_DARKMODE = "darkmode";
const PROPERTY_TESTDATA = "testdata";

const Color ACCENT_COLOR = Color(0xFF2196F3);
const Color CONNECTED_WIFI_COLOR = Color(0xFF4CAF50);

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        fontFamily: 'Manrope',
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'WiFi'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List<wifi.Wifi> _wifis = [];
  String _connectedWifi;
  ScrollController _scrollController;
  double _appBarElevation = 0.0;
  bool _darkmode = false;
  bool _testdata = false;
  Offset _latestTapPosition;
  int _showPasswordAtPos = -1;

  Color get _backgroundColor => _darkmode ? Colors.black : Colors.white;

  Color get _textColor => _darkmode ? Colors.white : Colors.black;

  Future<void> _updateWifis() async {
    try {
      var wifis = _testdata ? await wifi.getTestWifis() : await wifi.getWifis();
      var connectedWifi = await wifi.getConnectedWifiSSID();
      setState(() {
        _wifis = wifis;
        _connectedWifi = connectedWifi;
      });
    } on PlatformException catch (e) {}
  }

  Future<void> _updateProperties() async {
    var darkmode =
        (await SharedPreferences.getInstance()).getBool(PROPERTY_DARKMODE);
    var testdata =
        (await SharedPreferences.getInstance()).getBool(PROPERTY_TESTDATA);

    _updateSystemTheme();

    setState(() {
      _darkmode = darkmode;
      _testdata = testdata;
    });
  }

  Future<void> _updateSystemTheme() {
    if (_darkmode) {
      systemtheme.setDarkTheme();
    } else {
      systemtheme.setLightTheme();
    }
  }

  Future<bool> setDarkmode(bool darkmode) {
    setState(() {
      _darkmode = darkmode;
    });
    _updateSystemTheme();
    return SharedPreferences.getInstance().then(
        (SharedPreferences pref) => pref.setBool(PROPERTY_DARKMODE, darkmode));
  }

  Future<bool> setTestdata(bool testdata) {
    setState(() {
      _testdata = testdata;
    });
    _updateWifis();
    return SharedPreferences.getInstance().then(
        (SharedPreferences pref) => pref.setBool(PROPERTY_TESTDATA, testdata));
  }

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_scrollListener);
    _updateProperties()
        .then((_) => _updateWifis())
        .then((_) => _updateSystemTheme());
    WidgetsBinding.instance.addPostFrameCallback((_) => _updateSystemTheme());
  }

  _scrollListener() {
    double elevation =
        (_scrollController.offset - _scrollController.position.minScrollExtent)
                .abs() /
            10.0;
    elevation = elevation - elevation % 0.1;
    elevation = min(elevation, 2.0);
    if (elevation != _appBarElevation) {
      setState(() {
        _appBarElevation = elevation;
      });
    }
  }

  void _onListTap(DragDownDetails details) {
    _latestTapPosition = details.globalPosition;
    if (_showPasswordAtPos != -1) {
      setState(() {
        _showPasswordAtPos = -1;
      });
    }
  }

  static const CONTEXT_MENU_COPY_PASSWORD = 0;
  static const CONTEXT_MENU_SHOW_PASSWORD = 1;
  static const CONTEXT_MENU_SHOW_QR_CODE = 2;

  _showContextMenu(BuildContext context, int wifiIndex) {
    final RenderBox overlay = Overlay.of(context).context.findRenderObject();

    showMenu(
            context: context,
            items: <PopupMenuEntry<int>>[
              PopupMenuItem(
                  value: CONTEXT_MENU_COPY_PASSWORD,
                  child: Text("Copy Password")),
              PopupMenuItem(
                  value: CONTEXT_MENU_SHOW_PASSWORD,
                  child: Text("Show Password")),
              PopupMenuItem(
                  value: CONTEXT_MENU_SHOW_QR_CODE,
                  child: Text("Show QR-Code")),
            ],
            position: RelativeRect.fromRect(
                _latestTapPosition & Size(40, 40), Offset.zero & overlay.size))
        .then<void>((int item) {
      switch (item) {
        case CONTEXT_MENU_COPY_PASSWORD:
          Clipboard.setData(
              new ClipboardData(text: _wifis[wifiIndex].password));
          Scaffold.of(context).showSnackBar(new SnackBar(
            content: new Text("Copied Password to Clipboard"),
          ));
          break;
        case CONTEXT_MENU_SHOW_PASSWORD:
          setState(() {
            _showPasswordAtPos = wifiIndex;
          });
          break;
        case CONTEXT_MENU_SHOW_QR_CODE:
          showDialog(
              context: context,
              builder: (builderContext) {
                return AlertDialog(
                    content: Container(
                        width: 300.0,
                        height: 300.0,
                        child: QrImage(
                          data: wifiqrcode.build(_wifis[wifiIndex].ssid, _wifis[wifiIndex].password, wifiqrcode.AUTHENTICATION_WPA),
                          size: 300.0,
                        )));
              });
          break;
      }
    });
  }

  _selectMenuItem(String item) {
    if (item == PROPERTY_TESTDATA) {
      setTestdata(!_testdata);
    } else if (item == PROPERTY_DARKMODE) {
      setDarkmode(!_darkmode);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: _backgroundColor,
      appBar: AppBar(
        title: Text(widget.title, style: TextStyle(color: Colors.grey)),
        centerTitle: true,
        backgroundColor: _backgroundColor,
        iconTheme: IconThemeData(color: Colors.grey),
        elevation: _appBarElevation,
        actions: <Widget>[
          PopupMenuButton<String>(
              offset: Offset(0.0, 10.0),
              onSelected: _selectMenuItem,
              itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
                    PopupMenuItem<String>(
                        value: PROPERTY_TESTDATA,
                        child: _testdata
                            ? Text("Use Real Data")
                            : Text("Use Test Data")),
                    /*PopupMenuItem<String>(
                            value: PROPERTY_DARKMODE,
                            child: _darkmode
                                ? Text("Disable Darkmode")
                                : Text("Enable Darkmode")),*/
                  ]),
        ],
      ),
      body: RefreshIndicator(
          color: ACCENT_COLOR,
          child: GestureDetector(
            child: Scrollbar(
                child: ListView.builder(
              physics: AlwaysScrollableScrollPhysics(
                  parent: BouncingScrollPhysics()),
              controller: _scrollController,
              itemCount: _wifis.length,
              itemBuilder: (context, index) => ListTile(
                    contentPadding:
                        EdgeInsets.symmetric(horizontal: 30.0, vertical: 4.0),
                    leading: Icon(
                      Icons.network_wifi,
                      color: _connectedWifi == _wifis[index].ssid
                          ? CONNECTED_WIFI_COLOR
                          : ACCENT_COLOR,
                    ),
                    title: Text(_wifis[index].ssid),
                    subtitle: Text(
                        _showPasswordAtPos == index
                            ? _wifis[index].password
                            : mask(_wifis[index].password),
                        style: TextStyle(color: Colors.grey)),
                    onTap: () => _showContextMenu(context, index),
                    onLongPress: () {
                      Clipboard.setData(
                          new ClipboardData(text: _wifis[index].password));
                      Scaffold.of(context).showSnackBar(new SnackBar(
                        content: new Text("Copied Password to Clipboard"),
                      ));
                    },
                  ),
            )),
            onPanDown: _onListTap,
          ),
          onRefresh: _updateWifis),
    );
  }
}

mask(String string) {
  return string.replaceAll(RegExp("."), "●");
}
