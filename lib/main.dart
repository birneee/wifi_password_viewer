import 'dart:async';
import 'dart:convert';
import 'dart:core';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:math';

const accentColor = Colors.red;

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        fontFamily: 'Manrope',
        primarySwatch: accentColor,
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
  static const platform =
      const MethodChannel('tk.birneee.wifipasswordviewer/wifi');
  List<Wifi> _wifis = [];
  String _connectedWifi;
  ScrollController _scrollController;
  double _appBarElevation = 0.0;

  Future<void> _updateWifis() async {
    try {
      var result = (await platform.invokeMethod("getWifis"));
      var wifis = List<Wifi>.from(json
          .decode(result)
          .map((i) => new Wifi(i["ssid"], i["password"]))
          .toList());
      var connectedWifi = (await platform.invokeMethod("getConnectedWifi"));
      setState(() {
        _wifis = wifis;
        _connectedWifi = connectedWifi;
      });
    } on PlatformException catch (e) {}
  }

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_scrollListener);
    WidgetsBinding.instance
        .addPostFrameCallback((_) => _updateWifis());
  }

  _scrollListener(){
    double elevation = (_scrollController.offset - _scrollController.position.minScrollExtent).abs() / 10.0;
    elevation = elevation - elevation % 0.1;
    elevation = min(elevation, 2.0);
    if(elevation != _appBarElevation) {
      setState(() {
        _appBarElevation = elevation;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Center(child: Text(widget.title, style: TextStyle(color: Colors.grey))),
        backgroundColor: Colors.white,
        elevation: _appBarElevation,
      ),
      body: RefreshIndicator(
          color: accentColor,
          child: Scrollbar(
            child:
              ListView.builder(
            physics: BouncingScrollPhysics(),
            controller: _scrollController,
            itemCount: _wifis.length,
            itemBuilder: (context, index) => ListTile(
                  contentPadding:
                      EdgeInsets.symmetric(horizontal: 30.0, vertical: 4.0),
                  leading: Icon(
                    Icons.network_wifi,
                    color: _connectedWifi == _wifis[index].ssid ? Colors.orange : accentColor,
                  ),
                  title: Text(_wifis[index].ssid),
                  subtitle: Text(_wifis[index].password),
                ),
          )),
          onRefresh: _updateWifis),
    );
  }
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
