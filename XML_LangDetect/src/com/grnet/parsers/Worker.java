/**
 * 
 */
package com.grnet.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
import com.grnet.constants.Constants;

/**
 * @author vogias
 * 
 */
public class Worker {

	public static void main(String[] args) throws JDOMException, IOException,
			LangDetectException {

		SAXBuilder builder = new SAXBuilder();
		File xml = new File(
				"C:\\Users\\vogias\\Desktop\\Destination\\Identification\\TEST\\ODS_TEST__oai_dc_znam_2765079488_enc_1.xml");
		Document document = (Document) builder.build(xml);
		Element rootNode = document.getRootElement();
		Record record = new Record();

		record.setMetadata(rootNode);

		Properties properties = new Properties();
		properties.load(new FileInputStream("configure.properties"));

		Element title = JDomUtils
				.getXpathNode(
						properties
								.getProperty(com.grnet.constants.Constants.elements),
						Namespace.getNamespace(
								properties
										.getProperty(com.grnet.constants.Constants.prefix),
								properties
										.getProperty(com.grnet.constants.Constants.uri)),
						record.getMetadata());

		if (title != null) {
			String titleText = title.getText();

			if (!titleText.equals("")) {
				System.out.println("Title content:" + titleText);

				DetectorFactory.loadProfile(properties
						.getProperty(Constants.profiles));
				Detector detector = DetectorFactory.create();
				detector.append(titleText);

				String lang = detector.detect();
				Attribute attribute = new Attribute(
						properties.getProperty(Constants.attName), lang);
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
