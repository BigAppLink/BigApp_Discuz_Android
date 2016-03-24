package com.kit.widget.update;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

import com.kit.utils.AppUtils;
import com.kit.utils.JsonUtils;

/**
 * 解析Xml文件 ParseXmlUtils.java
 * 
 * @author Cloay 2011-11-7
 */
public class ParseJsonUtils {
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
//				updateInfo.versionName(res
//						.getElementsByTagName("version").item(0)
//						.getFirstChild().getNodeValue());
//				updateInfo.setUrl(res.getElementsByTagName("url").item(0)
//						.getFirstChild().getNodeValue());
//				updateInfo.setDescription(res
//						.getElementsByTagName("description").item(0)
//						.getFirstChild().getNodeValue());
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

	/**
	 * 通过InputStream 解析文件
	 * 
	 * @return
	 */
	public static UpdateInfo parseString(Context context, String str) {
		UpdateInfo updateInfo = null;

		JSONObject jsonObj = JsonUtils.str2JSONObj(str);

		try {

			// System.out.println("jsonObj:"+jsonObj);
			String serverVersionName = jsonObj.getString("versionName");
			int serverVersionCode = jsonObj.getInt("versionCode");
			boolean isForce = jsonObj.getBoolean("isForce");
			String url = jsonObj.getString("url");
			String description = jsonObj.getString("description");
			String belong = "pantomobile";
			try {
				belong = jsonObj.getString("belong");
			} catch (Exception e) {

			}
			
			String localVersionName = AppUtils.getAppVersion(context);
			int localVersionCode = AppUtils.getAppCode(context);

			updateInfo = new UpdateInfo();
			updateInfo.serverVersionName = serverVersionName;
			updateInfo.serverVersionCode = serverVersionCode;
			updateInfo.isForce = isForce;
			updateInfo.url = url;

			updateInfo.description = description;
			updateInfo.belong = belong;
			updateInfo.versionName = localVersionName;
			updateInfo.versionCode = localVersionCode;


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return updateInfo;
	}
}
