package birneee.wifipasswordviewer.utils

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

fun String.parseXml(): Node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.byteInputStream())

fun NodeList.toList(): List<Node> = (0 until length).map { i -> this.item(i) }

val Node.childNodeList: List<Node>
    get() = childNodes.toList()

fun List<Node>.flatMapChildNodes() : List<Node>{
    return this.flatMap { it.childNodeList }
}

fun List<Node>.filterByTag(tagName: String) : List<Node>{
    return this.filter { it is Element &&  it.tagName == tagName }
}

fun Node.firstChildByTag(tagName: String) : Node?{
    return childNodeList.filterByTag(tagName).firstOrNull()
}

fun List<Node>.filterByAttribute(attributeName: String, attributeValue: String) : List<Node>{
    return this.filter { it is Element && it.getAttribute(attributeName) == attributeValue }
}

fun Node.firstChildByAttribute(attributeName: String, attributeValue: String) : Node?{
    return this.childNodeList.filterByAttribute(attributeName, attributeValue).firstOrNull()
}

fun Node.xpath(xpath: String): List<Node>{
    return (XPathFactory.newInstance().newXPath().compile(xpath).evaluate(this, XPathConstants.NODESET) as NodeList).toList()
}

fun String.xpath(xpath: String): List<Node>{
    return this.parseXml().xpath(xpath)
}