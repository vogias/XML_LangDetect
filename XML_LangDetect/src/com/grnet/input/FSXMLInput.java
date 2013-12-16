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
		String path = (String) var;
		File source = new File(path);

		return getXMLs(source);
	}

	private Collection<File> getXMLs(File dataProviderDir) {

		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(dataProviderDir, extensions,
				true);

		return files;

	}

}
