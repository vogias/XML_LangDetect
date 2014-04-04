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
package com.grnet.parsers;

import org.jdom.Element;

public class Record {

	protected Element metadata;

	public Element getMetadata() {
		return metadata;
	}

	public void setMetadata(Element metadata) {
		this.metadata = metadata;
	}

}
