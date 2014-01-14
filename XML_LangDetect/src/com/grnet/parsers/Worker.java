/**
 * 
 */
package com.grnet.parsers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import ch.qos.logback.core.status.Status;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.grnet.constants.Constants;
import com.grnet.stats.Stats;

/**
 * @author vogias
 * 
 */
public class Worker implements Runnable {

	Properties properties;
	File xml;
	SAXBuilder builder;
	String outputPath;
	Stats stats;
	boolean flag;

	public Worker(SAXBuilder builder, File xml, Properties properties,
			String outputPath, Stats stats) {
		this.xml = xml;
		this.properties = properties;
		this.builder = builder;
		this.outputPath = outputPath;
		this.stats = stats;
		flag = false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out
				.println("-----------------------------------------------------");
		System.out.println("Worker thread for file:" + xml.getName()
				+ " is started.");
		Document document;
		try {
			document = (Document) builder.build(xml);

			Element rootNode = document.getRootElement();
			Record record = new Record();

			record.setMetadata(rootNode);

			String elementsString = properties.getProperty(Constants.elements);
			String[] elements = elementsString.split(",");

			for (int i = 0; i < elements.length; i++) {

				// System.out.println("Checking element:" + elements[i]);
				List<Element> elementList = JDomUtils.getXpathList(elements[i],
						Namespace.getNamespace(
								properties.getProperty(Constants.prefix),
								properties.getProperty(Constants.uri)), record
								.getMetadata());

				if (elementList != null) {

					for (int j = 0; j < elementList.size(); j++) {
						Element elmt = elementList.get(j);
						String titleText = elmt.getText();

						// System.out.println("Element number:" + j);
						if (!titleText.equals("")) {
							// System.out.println("Element content:" +
							// titleText);

							try {

								Attribute langAtt = elmt
										.getAttribute(properties
												.getProperty(Constants.attName));

								String chosenLangAtt = properties
										.getProperty(Constants.attName);

								if (langAtt == null) {
									Detector detector = DetectorFactory
											.create();
									detector.append(titleText);
									String lang = detector.detect();

									Attribute attribute = new Attribute(
											chosenLangAtt, lang);
									elmt.setAttribute(attribute);

									stats.raiseElementsLangDetected();
									flag = true;
								}
								// else
								// System.out.println(chosenLangAtt
								// + " attribute exists.");

							} catch (LangDetectException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// else
						// System.err.println("No element content.");
					}

				}
				// else
				// System.err.println("No elements.");

				// System.out.println("Done");
			}
			String xmlString = JDomUtils.parseXml2string(record.getMetadata()
					.getDocument(), null);

			OaiUtils.writeStringToFileInEncodingUTF8(xmlString, outputPath
					+ File.separator + xml.getName());
			if (flag)
				stats.raiseFilesLangDetected();

			// System.out.println(xmlString);

		} catch (JDOMException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Worker thread for file:" + xml.getName()
				+ " is done.");
		System.out.println("-------------------------------------------------");
	}
}
