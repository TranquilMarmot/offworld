package com.bitwaffle.moguts.util;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLHelper {
	/**
	 * Get a node list from an XML file
	 * @param xmlFile Stream from file to get list from
	 * @return List of nodes in file, null if there's an error
	 */
	public static NodeList getNodeList(InputStream xmlFile){
		NodeList nodes = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;
		// initialize the DocumentBuilder and parse the file to get the NodeList
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(xmlFile);
			Element docEle = doc.getDocumentElement();
			nodes = docEle.getChildNodes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nodes;
	}
	
	/**
	 * Get a string value from an element
	 * @param ele Element to get value from
	 * @param tagName Name of value to get
	 * @return Value of tagName in ele, null if tagName doesn't exist in ele
	 */
	public static String getString(Element ele, String tagName) {
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0)
			return nl.item(0).getFirstChild().getNodeValue();

        return null;
	}
	
	public static int getInt(Element ele, String tagName){
		return Integer.parseInt(getString(ele, tagName));
	}
	
	public static float getFloat(Element ele, String tagName){
		return Float.parseFloat(getString(ele, tagName));
	}
}
