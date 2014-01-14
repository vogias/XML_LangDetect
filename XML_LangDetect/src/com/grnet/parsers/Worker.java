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
import org.slf4j.Logger;

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

	String outputPath;
	Stats stats;
	boolean flag;
	private Logger slf4jLogger;

	public Worker(File xml, Properties properties, String outputPath,
			Stats stats, Logger slf4jLogger) {
		this.xml = xml;
		this.properties = properties;

		this.outputPath = outputPath;
		this.stats = stats;
		flag = false;
		this.slf4jLogger = slf4jLogger;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// System.out
		// .println("-----------------------------------------------------");
		// System.out.println("Worker thread for file:" + xml.getName()
		// + " is started.");
		String name = xml.getName();
		Document document;
		try {
			SAXBuilder builder = new SAXBuilder();
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
									StringBuffer logstring = new StringBuffer();

									logstring.append(xml.getParentFile()
											.getName());
									logstring.append(" "
											+ name.substring(0,
													name.lastIndexOf(".")));

									logstring.append(" " + elements[i]);
									logstring.append(" " + lang);
									slf4jLogger.info(logstring.toString());
									stats.addElementD(elements[i]);
									flag = true;
								}
								// else
								// System.out.println(chosenLangAtt
								// + " attribute exists.");

							} catch (LangDetectException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else
							System.err.println("No element content.");
					}

				} else
					System.err.println("No elements.");

				// System.out.println("Done");

			}
			String xmlString = JDomUtils.parseXml2string(record.getMetadata()
					.getDocument(), null);

			OaiUtils.writeStringToFileInEncodingUTF8(xmlString, outputPath
					+ File.separator + name);
			if (flag)
				stats.raiseFilesLangDetected();

			// System.out.println(xmlString);

		} catch (JDOMException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println("Worker thread for file:" + xml.getName()
		// + " is done.");
		// System.out.println("-------------------------------------------------");

	}
}
