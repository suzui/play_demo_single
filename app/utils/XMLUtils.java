package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class XMLUtils {
	/**
	 * @param xml
	 *            只返回1层结构的键值，如果有嵌套，则合并子节点的text值（使用getStringValue()）
	 * @return
	 */
	public static Map<String, String> parseXML(String xml) {
		Map<String, String> params = new HashMap();
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Iterator it = doc.getRootElement().elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				String name = element.getName();
				String text = element.getStringValue();
				params.put(name, text);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	public static String convert2XML(Map<String, String> params) {
		StringBuffer sb = new StringBuffer("<xml>");
		try {
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				sb.append("<" + entry.getKey() + ">");
				// sb.append("<![CDATA[" + entry.getValue() + "]]");
				sb.append(entry.getValue());
				sb.append("</" + entry.getKey() + ">");
			}
			sb.append("</xml>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String convert2XML_(Map<String, String> params) {
		StringBuffer sb = new StringBuffer("<xml>");
		try {
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				sb.append("<![CDATA[" + entry.getValue() + "]]");
				sb.append(entry.getValue());
				sb.append("</" + entry.getKey() + ">");
			}
			sb.append("<CreateTime>").append(new Date().getTime())
					.append("</CreateTime>");
			sb.append("</xml>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String inputStream2String(InputStream is) {
		StringBuffer buffer = new StringBuffer("");
		try {
			if (is.available() > 0) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(is));
				String line = "";
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

}
