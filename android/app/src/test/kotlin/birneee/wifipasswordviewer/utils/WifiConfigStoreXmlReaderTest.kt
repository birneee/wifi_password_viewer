package birneee.wifipasswordviewer.utils

import org.junit.Test

val exampleXml = """<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<WifiConfigStoreData>
<int name="Version" value="1" />
<NetworkList>
<Network>
<WifiConfiguration>
<string name="ConfigKey">&quot;wifi1&quot;WPA_PSK</string>
<string name="SSID">&quot;wifi1&quot;</string>
<null name="BSSID" />
<string name="PreSharedKey">&quot;pass1&quot;</string>
<null name="WEPKeys" />
<int name="WEPTxKeyIndex" value="0" />
<boolean name="HiddenSSID" value="false" />
<boolean name="RequirePMF" value="false" />
<byte-array name="AllowedKeyMgmt" num="1">02</byte-array>
<byte-array name="AllowedProtocols" num="1">03</byte-array>
<byte-array name="AllowedAuthAlgos" num="1">01</byte-array>
<byte-array name="AllowedGroupCiphers" num="1">0f</byte-array>
<byte-array name="AllowedPairwiseCiphers" num="1">06</byte-array>
<boolean name="Shared" value="true" />
<int name="Status" value="2" />
<null name="FQDN" />
<null name="ProviderFriendlyName" />
<null name="LinkedNetworksList" />
<null name="DefaultGwMacAddress" />
<boolean name="ValidatedInternetAccess" value="true" />
<boolean name="NoInternetAccessExpected" value="false" />
<int name="UserApproved" value="0" />
<boolean name="MeteredHint" value="false" />
<int name="MeteredOverride" value="0" />
<boolean name="UseExternalScores" value="false" />
<int name="NumAssociation" value="9" />
<int name="CreatorUid" value="1000" />
<string name="CreatorName">android.uid.system:1000</string>
<string name="CreationTime">time=02-17 18:11:52.167</string>
<int name="LastUpdateUid" value="1000" />
<string name="LastUpdateName">android.uid.system:1000</string>
<int name="LastConnectUid" value="1000" />
<boolean name="IsLegacyPasspointConfig" value="false" />
<long-array name="RoamingConsortiumOIs" num="0" />
<string name="RandomizedMacAddress">02:00:00:00:00:00</string>
</WifiConfiguration>
<NetworkStatus>
<string name="SelectionStatus">NETWORK_SELECTION_ENABLED</string>
<string name="DisableReason">NETWORK_SELECTION_ENABLE</string>
<null name="ConnectChoice" />
<long name="ConnectChoiceTimeStamp" value="-1" />
<boolean name="HasEverConnected" value="true" />
</NetworkStatus>
<IpConfiguration>
<string name="IpAssignment">DHCP</string>
<string name="ProxySettings">NONE</string>
</IpConfiguration>
</Network>
<Network>
<WifiConfiguration>
<string name="ConfigKey">&quot;wifi2&quot;WPA_PSK</string>
<string name="SSID">&quot;wifi2&quot;</string>
<null name="BSSID" />
<string name="PreSharedKey">&quot;pass2&quot;</string>
<null name="WEPKeys" />
<int name="WEPTxKeyIndex" value="0" />
<boolean name="HiddenSSID" value="false" />
<boolean name="RequirePMF" value="false" />
<byte-array name="AllowedKeyMgmt" num="1">02</byte-array>
<byte-array name="AllowedProtocols" num="1">03</byte-array>
<byte-array name="AllowedAuthAlgos" num="1">01</byte-array>
<byte-array name="AllowedGroupCiphers" num="1">0f</byte-array>
<byte-array name="AllowedPairwiseCiphers" num="1">06</byte-array>
<boolean name="Shared" value="true" />
<int name="Status" value="2" />
<null name="FQDN" />
<null name="ProviderFriendlyName" />
<null name="LinkedNetworksList" />
<null name="DefaultGwMacAddress" />
<boolean name="ValidatedInternetAccess" value="true" />
<boolean name="NoInternetAccessExpected" value="false" />
<int name="UserApproved" value="0" />
<boolean name="MeteredHint" value="false" />
<int name="MeteredOverride" value="0" />
<boolean name="UseExternalScores" value="false" />
<int name="NumAssociation" value="11" />
<int name="CreatorUid" value="1000" />
<string name="CreatorName">android.uid.system:1000</string>
<string name="CreationTime">time=02-17 23:08:49.242</string>
<int name="LastUpdateUid" value="1000" />
<string name="LastUpdateName">android.uid.system:1000</string>
<int name="LastConnectUid" value="1000" />
<boolean name="IsLegacyPasspointConfig" value="false" />
<long-array name="RoamingConsortiumOIs" num="0" />
<string name="RandomizedMacAddress">02:00:00:00:00:00</string>
</WifiConfiguration>
<NetworkStatus>
<string name="SelectionStatus">NETWORK_SELECTION_ENABLED</string>
<string name="DisableReason">NETWORK_SELECTION_ENABLE</string>
<null name="ConnectChoice" />
<long name="ConnectChoiceTimeStamp" value="-1" />
<boolean name="HasEverConnected" value="true" />
</NetworkStatus>
<IpConfiguration>
<string name="IpAssignment">DHCP</string>
<string name="ProxySettings">NONE</string>
</IpConfiguration>
</Network>
</NetworkList>
<PasspointConfigData>
<long name="ProviderIndex" value="0" />
</PasspointConfigData>
</WifiConfigStoreData>"""

class WifiConfigStoreXmlReaderTest {

    @Test
    fun parseXml() {
        val reader = WifiConfigStoreXmlReader()
        val result = reader.parseXml(exampleXml)
        println(result)
        assert(result.size == 2)
    }
}