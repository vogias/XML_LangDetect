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
package com.grnet.input;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class FSXMLInput extends Input {

	@Override
	public Collection<?> getData(Object var) {
		// TODO Auto-generated method stub

		return getXMLs((File) var);
	}

	private Collection<File> getXMLs(File dataProviderDir) {

		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(dataProviderDir, extensions,
				true);

		return files;

	}

}
