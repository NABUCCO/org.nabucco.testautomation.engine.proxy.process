/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.nabucco.testautomation.engine.proxy.process.command.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DeleteFileCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DeleteFileCommand extends AbstractFTPCommand {

	public DeleteFileCommand(FTPClient client) {
		super(client);
	}
	
	@Override
	public PropertyList executeCallback(Metadata metadata, PropertyList properties)
			throws FTPException, IOException {

		if (isConnected()) {
			String filename = getFilename(metadata, properties);
			info("Delete remote file: " + filename);
			start();
			boolean success = getFTPClient().deleteFile(filename);
			stop();
			
			if (!success) {
				throw new FTPException("Deletion of file failed. Cause: " + getResponse());
			}
			return null;
		} else {
			throw new FTPException("Deletion of file failed. Cause: Not connected");
		}
	}

	/**
	 * @param metadata
	 * @param properties
	 * @return
	 * @throws FTPException 
	 */
	private String getFilename(Metadata metadata, PropertyList properties) throws FTPException {
		
		// First, check PropertyList from Action
		Property userProperty = PropertyHelper.getFromList(properties,
				"FILENAME");

		if (userProperty != null && userProperty.getType() == PropertyType.STRING) {
			return ((StringProperty) userProperty).getValue().getValue();
		}
		
		// Second, check PropertyList from Metadata
		userProperty = PropertyHelper.getFromList(metadata.getPropertyList(),
				"FILENAME");
		
		if (userProperty != null && userProperty.getType() == PropertyType.STRING) {
			return ((StringProperty) userProperty).getValue().getValue();
		}
		
		throw new FTPException("FILENAME not found in "
				+ properties.getName().getValue() + " and "
				+ metadata.getPropertyList().getName().getValue());
	}

}