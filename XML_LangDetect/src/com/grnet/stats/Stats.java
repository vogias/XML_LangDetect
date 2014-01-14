/**
 * 
 */
package com.grnet.stats;

/**
 * @author vogias
 * 
 */
public class Stats {

	private int filesLangDetected, elementsLangDetected;
	int sumFiles;

	public Stats(int sumFiles) {
		this.sumFiles = sumFiles;
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
