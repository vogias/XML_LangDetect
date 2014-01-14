/**
 * 
 */
package com.grnet.info;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.grnet.stats.Stats;

/**
 * @author vogias
 * 
 */
public class report {

	int filesLD = 0;
	int elementsLD = 0;
	String repo;
	int threadPoolSize, cores;
	long duration;
	File ldReport;
	BufferedWriter writer;
	Stats stats;

	public report(String repo, long duration, int threadPoolSize, int cores,
			File destFolder, Stats stats) throws IOException {
		this.repo = repo;
		this.duration = duration;
		this.threadPoolSize = threadPoolSize;
		this.cores = cores;
		ldReport = new File(destFolder, "langDetect_" + repo + "_Report"
				+ ".txt");
		writer = new BufferedWriter(new FileWriter(ldReport));
		this.stats = stats;
	}

	private String getDate() {
		return new Date().toString();
	}

	private void addGeneralInfo() throws IOException {
		writer.append("=========== General Info ==================");
		writer.newLine();
		writer.append("Repository:" + repo);
		writer.newLine();
		writer.append("Date:" + getDate());
		writer.newLine();
		writer.append("Duration:" + duration + " msec");
		writer.newLine();
		writer.append("Thread Pool Size:" + threadPoolSize);
		writer.newLine();
		writer.append("CPU Cores:" + cores);
		writer.newLine();
		writer.append("===========================================");

	}

	private void addSpecificInfo() throws IOException {
		writer.append("=========== Lang Detection Info ===========");
		writer.newLine();
		writer.append("Number of files lang detected:"
				+ stats.getFilesLangDetected());
		writer.newLine();
		writer.append("=========== Elements Lang Detected=========");
		writer.newLine();

		HashMap<String, Integer> elementsD = stats.getElementsD();
		Set<String> keySet = elementsD.keySet();
		Iterator<String> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String next = iterator.next();
			Integer integer = elementsD.get(next);
			writer.append("Element:" + next + ", times lang detected:"
					+ integer);
			writer.newLine();
		}

	}

	public void createReport() throws IOException {
		addSpecificInfo();
		addGeneralInfo();
		writer.close();

	}
}
