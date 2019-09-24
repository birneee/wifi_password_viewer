package birneee.wifipasswordviewer.utils

import org.junit.Test
import java.io.File

class WifiConfigStoreXmlReaderTest {

    @Test
    fun parseXml() {
        val xml = File(javaClass.classLoader.getResource("WifiConfigStore.xml").toURI()).readText(Charsets.UTF_8)
        val reader = WifiConfigStoreXmlReader()
        val result = reader.parseXml(xml)
        println(result)
        assert(result.size == 2)
    }
}