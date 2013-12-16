/**
 * 
 */
package com.grnet.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.grnet.constants.Constants;

/**
 * @author vogias
 * 
 */
public class CheckConfig {
	Properties props;

	public CheckConfig() {
		props = new Properties();

	}

	public boolean checkAttributes() {

		try {
			props.load(new FileInputStream("configure.properties"));

			if (checkInputClass() && checkElements() && checkAttributeName()
					&& checkThreadPoolSize() && checkLogging() && checkReport())
				return true;
			else
				return false;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private boolean checkInputClass() {
		String inputClass = props.getProperty(Constants.inputClass);

		if (inputClass == null) {
			System.err.println("detect.input.class attribute is not used...");
			return false;

		} else if (inputClass.equals("")) {
			System.err.println("detect.input.class attribute has no value...");
			return false;
		} else {
			return true;
		}
	}

	private boolean checkElements() {
		String elements = props.getProperty(Constants.elements);

		if (elements == null) {
			System.err.println("detect.elements attribute is not used...");
			return false;

		} else if (elements.equals("")) {
			System.err.println("detect.elements attribute has no value...");
			return false;
		} else {
			return true;
		}
	}

	private boolean checkAttributeName() {
		String att = props.getProperty(Constants.attName);

		if (att == null) {
			System.err
					.println("detect.attribute.name attribute is not used...");
			return false;

		} else if (att.equals("")) {
			System.err
					.println("detect.attribute.name attribute has no value...");
			return false;
		} else {
			return true;
		}
	}

	private boolean checkThreadPoolSize() {
		String tps = props.getProperty(Constants.tPoolSize);

		if (tps == null) {
			System.err
					.println("detect.threadPool.size attribute is not used...");
			return false;

		} else if (tps.equals("")) {
			System.err
					.println("detect.threadPool.size attribute has no value...");
			return false;
		} else {
			try {
				Integer size = Integer.valueOf(tps);
				if (size == 0) {
					System.err
							.println("detect.threadPool.size attribute should be greater than 0...");
					return false;
				} else if (size < 0) {
					System.err
							.println("detect.threadPool.size attribute should be a positive integer...");
					return false;
				} else {
					return true;
				}
			} catch (NumberFormatException ex) {
				System.err
						.println("detect.threadPool.size attribute is not a number...");
				return false;
			}
		}
	}

	private boolean checkReport() {
		String report = props.getProperty(Constants.report);

		if (report == null) {
			System.err.println("detection.report attribute is not used...");
			return false;

		} else if (report.equals("")) {
			System.err.println("detection.report attribute has no value...");
			return false;
		} else {
			return true;
		}
	}

	private boolean checkLogging() {
		String logPath = props.getProperty(Constants.logPath);
		String logFileName = props.getProperty(Constants.logfileName);

		if (logFileName == null || logPath == null) {
			System.err
					.println("Either log.file.path or log.file.name attribute is not used ...");
			return false;

		} else if (logFileName.equals("") || logPath.equals("")) {
			System.err
					.println("Either log.file.path or log.file.name attribute has no value...");
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		CheckConfig config=new CheckConfig();
		
		if(config.checkAttributes())
			System.out.println("Ook");
		else
			System.out.println("Shit");
	}
}
