/**
 * 
 */
package com.grnet.stats;

import java.util.HashMap;

/**
 * @author vogias
 * 
 */
public class Stats {

	private int filesLangDetected, elementsLangDetected;
	int sumFiles;
	private HashMap<String, Integer> elementsDetected;

	public Stats(int sumFiles) {
		this.sumFiles = sumFiles;
		elementsDetected = new HashMap<>();
	}

	public synchronized void addElementD(String element) {

		if (elementsDetected.containsKey(element)) {
			Integer cnt = elementsDetected.get(element);
			elementsDetected.put(element, cnt + 1);
		} else
			elementsDetected.put(element, 1);

	}

	public HashMap<String, Integer> getElementsD() {
		return elementsDetected;
	}

	/**
	 * @return the elementsLangDetected
	 */

	public int getElementsLangDetected() {
		return elementsLangDetected;
	}

	public synchronized void raiseElementsLangDetected() {
		elementsLangDetected++;
	}

	/**
	 * @return the filesLangDetected
	 */
	public int getFilesLangDetected() {
		return filesLangDetected;
	}

	/**
	 * @return the sumFiles
	 */
	public int getSumFiles() {
		return sumFiles;
	}

	/**
	 * @param filesLangDetected
	 *            the filesLangDetected to set
	 */
	public synchronized void raiseFilesLangDetected() {
		filesLangDetected++;
	}

	/**
	 * @param sumFiles
	 *            the sumFiles to set
	 */
	public void setSumFiles(int sumFiles) {
		this.sumFiles = sumFiles;
	}

}
