/*******************************************************************************
 * Copyright (c) 2014 Kostas Vogias.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Kostas Vogias - initial API and implementation
 ******************************************************************************/
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

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.grnet.constants.Constants;
import com.grnet.stats.Stats;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author vogias
 * 
 */
public class Worker implements Runnable {

	Properties properties;
	File xml;

	String outputPath, bad, strict, queue;
	Stats stats;
	boolean flag, recon;
	private Logger slf4jLogger;
	ConnectionFactory factory;

	public Worker(File xml, Properties properties, String outputPath,
			String bad, Stats stats, Logger slf4jLogger, String strict,
			String queue, ConnectionFactory factory) {
		this.xml = xml;
		this.properties = properties;

		this.outputPath = outputPath;
		this.bad = bad;
		this.stats = stats;
		flag = false;
		recon = true;
		this.slf4jLogger = slf4jLogger;
		this.strict = strict;
		this.factory = factory;
		this.queue = queue;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

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

				List<Element> elementList = JDomUtils.getXpathList(elements[i],
						Namespace.getNamespace(
								properties.getProperty(Constants.prefix),
								properties.getProperty(Constants.uri)), record
								.getMetadata());

				if (elementList != null) {

					for (int j = 0; j < elementList.size(); j++) {
						Element elmt = elementList.get(j);
						String titleText = elmt.getText();

						if (!titleText.equals("")) {

							Attribute langAtt = elmt.getAttribute(properties
									.getProperty(Constants.attName));

							String chosenLangAtt = properties
									.getProperty(Constants.attName);

							if (langAtt == null
									|| langAtt.getValue().equals("")
									|| langAtt.getValue().equals("none")) {
								StringBuffer logstring = new StringBuffer();
								try {
									Detector detector = DetectorFactory
											.create();
									detector.append(titleText);
									String lang = detector.detect();

									Attribute attribute = new Attribute(
											chosenLangAtt, lang);
									elmt.setAttribute(attribute);

									stats.raiseElementsLangDetected();

									logstring.append(xml.getParentFile()
											.getName());
									logstring.append(" "
											+ name.substring(0,
													name.lastIndexOf(".")));

									logstring.append(" " + elements[i]);
									logstring.append(" " + lang);

									slf4jLogger.info(logstring.toString());

									Connection connection = this.factory
											.newConnection();
									Channel channel = connection
											.createChannel();
									channel.queueDeclare(this.queue, false,
											false, false, null);

									channel.basicPublish("", this.queue, null,
											logstring.toString().getBytes());

									channel.close();
									connection.close();

									stats.addElementD(elements[i]);
									flag = true;
								} catch (LangDetectException e) {
									// TODO Auto-generated catch block
									// e.printStackTrace();
									logstring.append(xml.getParentFile()
											.getName());
									logstring.append(" "
											+ name.substring(0,
													name.lastIndexOf(".")));
									logstring.append(" " + "NoLangDetected");
									slf4jLogger.info(logstring.toString());

									Connection connection = this.factory
											.newConnection();
									Channel channel = connection
											.createChannel();
									channel.queueDeclare(this.queue, false,
											false, false, null);

									channel.basicPublish("", this.queue, null,
											logstring.toString().getBytes());

									channel.close();
									connection.close();

									if (strict.equals("true"))
										recon = false;
									else {
										recon = true;
										continue;
									}
								}
							}

						}

					}

				}

			}

			if (recon) {
				String xmlString = JDomUtils.parseXml2string(record
						.getMetadata().getDocument(), null);

				OaiUtils.writeStringToFileInEncodingUTF8(xmlString, outputPath
						+ File.separator + name);
			} else {
				String xmlString = JDomUtils.parseXml2string(record
						.getMetadata().getDocument(), null);

				OaiUtils.writeStringToFileInEncodingUTF8(xmlString, bad
						+ File.separator + name);
			}
			if (flag)
				stats.raiseFilesLangDetected();

			if (recon == false)
				stats.raiseFilessLangNotDetected();

		} catch (JDOMException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
