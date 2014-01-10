/**
 * 
 */
package com.grnet.parsers;

import java.io.File;
import java.io.IOException;

import org.ariadne.util.JDomUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 * @author vogias
 * 
 */
public class XMLHandler {

	public static void main(String[] args) throws JDOMException, IOException,
			LangDetectException {
		

		// InputStream inS = null;
		//
		// try {
		// inS = new FileInputStream(
		// "C:\\Users\\vogias\\Desktop\\COSMOS\\http_.s..s.portal.discoverthecosmos.eu.s.node.s.104968.xml");
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		SAXBuilder builder = new SAXBuilder();
		File xml = new File(
				"C:\\Users\\vogias\\Desktop\\COSMOS\\http_.s..s.portal.discoverthecosmos.eu.s.node.s.104968.xml");
		Document document = (Document) builder.build(xml);
		Element rootNode = document.getRootElement();
		Record record = new Record();

		record.setMetadata(rootNode);

		Element title = JDomUtils.getXpathNode(
				"//ods:general/ods:title/ods:string",
				Namespace.getNamespace("ods", "http://ltsc.ieee.org/xsd/LOM"),
				record.getMetadata());

		if (title != null) {
			String titleText = title.getText();

			if (!titleText.equals("")) {
				System.out.println("Title content:" + titleText);

				DetectorFactory.loadProfile("profiles");
				Detector detector = DetectorFactory.create();
				detector.append(titleText);

				String lang = detector.detect();
				Attribute attribute = new Attribute("language", lang);
				title.setAttribute(attribute);
				String xmlString = JDomUtils.parseXml2string(record
						.getMetadata().getDocument(), null);
				System.out.println(xmlString);
			} else
				System.err.println("No title content.");

		} else
			System.err.println("No title element.");

	}
}
