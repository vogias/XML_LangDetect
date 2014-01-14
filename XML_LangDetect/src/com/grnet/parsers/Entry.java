/**
 * 
 */
package com.grnet.parsers;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.grnet.config.CheckConfig;
import com.grnet.constants.Constants;
import com.grnet.input.Input;
import com.grnet.stats.Stats;

/**
 * @author vogias
 * 
 */
public class Entry {
	private static final Logger slf4jLogger = LoggerFactory
			.getLogger(Entry.class);

	public static void main(String[] args) throws InterruptedException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, LangDetectException {

		if (args.length != 2) {
			System.err.println("Usage : ");
			System.err
					.println("java -jar com.grnet.parsers.Entry <input folder> <output folder>");
			System.exit(-1);
		}

		File input = new File(args[0]);

		File output = new File(args[1]);

		if (!input.exists() || !output.isDirectory()) {
			System.err
					.println("Input folder does not exist or it is not a folder.");
			System.exit(-1);
		}

		if (!output.exists() || !output.isDirectory()) {
			System.err
					.println("Output folder does not exist or it is not a folder.");
			System.exit(-1);
		}

		CheckConfig config = new CheckConfig();

		if (config.checkAttributes()) {

			StringBuffer logstring = new StringBuffer();

			System.out.println("Starting lang detection on folder:"
					+ input.getName());

			logstring.append(input.getName());

			System.out.println("--------------------------------");
			SAXBuilder builder = new SAXBuilder();

			String idClass = config.getProps()
					.getProperty(Constants.inputClass);
			ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
			Class myClass = myClassLoader.loadClass(idClass);
			Object whatInstance = myClass.newInstance();
			Input inpt = (Input) whatInstance;

			Collection<File> data = (Collection<File>) inpt.getData(input);

			Stats stats = new Stats(data.size());

			int threadPoolSize = Integer.parseInt(config.getProps()
					.getProperty(Constants.tPoolSize));
			int availableProcessors = Runtime.getRuntime()
					.availableProcessors();
			System.out.println("Available cores:" + availableProcessors);
			System.out.println("Thread Pool size:" + threadPoolSize);
			ExecutorService executor = Executors
					.newFixedThreadPool(threadPoolSize);

			long start = System.currentTimeMillis();
			Iterator<File> iterator = data.iterator();

			DetectorFactory.loadProfile(config.getProps().getProperty(
					Constants.profiles));

			while (iterator.hasNext()) {

				Worker worker = new Worker(builder, iterator.next(),
						config.getProps(), output.getPath(), stats, slf4jLogger);
				executor.execute(worker);
			}

			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("Duration:" + diff + "ms");
			System.out.println("Done");

			logstring.append(" " + stats.getSumFiles());
			logstring.append(" " + stats.getFilesLangDetected());
			logstring.append(" " + stats.getElementsLangDetected());

		} else
			System.err
					.println("Please correct configuration.properties file attribute values");

	}
}
