package com.kit.widget.update;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 解析Xml文件 ParseXmlUtils.java
 * 
 * @author Cloay 2011-11-7
 */
public class ParseXmlUtils {
	/**
	 * 通过InputStream 解析文件
	 * 
	 * @param in
	 * @return
	 */
	public static UpdateInfo parseXml(InputStream in) {
		UpdateInfo updateInfo = new UpdateInfo();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(in);
			Element root = doc.getDocumentElement();
			NodeList resultNode = root.getElementsByTagName("info");
			for (int i = 0; i < resultNode.getLength(); i++) {
				Element res = (Element) resultNode.item(i);
				updateInfo.serverVersionName = res
						.getElementsByTagName("version").item(0)
						.getFirstChild().getNodeValue();
				updateInfo.url = res.getElementsByTagName("url").item(0)
						.getFirstChild().getNodeValue();
				updateInfo.description = res
						.getElementsByTagName("description").item(0)
						.getFirstChild().getNodeValue();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updateInfo;
	}
}
