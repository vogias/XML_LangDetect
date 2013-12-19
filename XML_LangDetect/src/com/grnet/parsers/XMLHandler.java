/**
 * 
 */
package com.grnet.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author vogias
 * 
 */
public class XMLHandler extends DefaultHandler {

	String[] elements;
	String branche;
	Stack<String> xPaths;

	public XMLHandler() {
		// TODO Auto-generated constructor stub

		branche = "";
		xPaths = new Stack<>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		branche += qName.toLowerCase();
		xPaths.push(branche);

		branche += ".";
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		String elmt = "";
		qName = qName.toLowerCase();

		if (branche.endsWith(qName + "" + ".")) {
			branche = branche.substring(0, branche.length() - qName.length()
					- 1);
			// branche = branche.toLowerCase();
			elmt = xPaths.elementAt(xPaths.size() - 1);
			xPaths.removeElementAt(xPaths.size() - 1);
			// System.out.println("--------End element-----");
			 System.out.println(elmt);
		}
		
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void parseDocument(InputStream is) throws SAXException, IOException,
			ParserConfigurationException {
		// TODO Auto-generated method stub
		SAXParserFactory spf = SAXParserFactory.newInstance();

		Reader reader = new InputStreamReader(is, "UTF-8");

		InputSource inputStream = new InputSource(reader);
		inputStream.setEncoding("UTF-8");

		SAXParser parser = spf.newSAXParser();

		parser.parse(inputStream, this);

	}

	public static void main(String[] args) {
		XMLHandler handler = new XMLHandler();

		InputStream inS = null;

		try {
			inS = new FileInputStream(
					"C:\\Users\\vogias\\Desktop\\COSMOS\\http_.s..s.portal.discoverthecosmos.eu.s.node.s.104968.xml");
			handler.parseDocument(inS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
