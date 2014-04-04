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
	int unrecognisedFiles;

	public Stats(int sumFiles) {
		this.sumFiles = sumFiles;
		elementsDetected = new HashMap<>();
		unrecognisedFiles = 0;
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

	public synchronized void raiseFilessLangNotDetected() {
		unrecognisedFiles++;
	}

	public int getFilesLangNotDetected() {
		return unrecognisedFiles;
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
