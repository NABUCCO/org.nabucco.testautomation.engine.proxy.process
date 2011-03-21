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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;

import org.nabucco.testautomation.facade.datatype.PropertyFactory;
import org.nabucco.testautomation.facade.datatype.property.FileProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DownloadCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DownloadCommand extends AbstractFTPCommand {

	public DownloadCommand(FTPClient client) {
		super(client);
	}
	
	@Override
	public PropertyList executeCallback(Metadata metadata, PropertyList properties)
			throws FTPException, IOException {
		
		if (isConnected()) {
			String filename = getFilename(metadata, properties);
			info("Download of file: " + filename);
			start();
			BufferedInputStream in = new BufferedInputStream(getFTPClient().retrieveFileStream(filename));
			FileProperty fileProperty = receiveFile(metadata, filename, in);
			getFTPClient().completePendingCommand();
			stop();
			
			PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
			add(fileProperty, returnProperties);
			return returnProperties;
		} else {
			throw new FTPException("Cannot download file. Cause: Not connected");
		}
	}

	/**
	 * @param in
	 * @return
	 */
	private FileProperty receiveFile(Metadata metadata, String filename, BufferedInputStream in) throws FTPException {
		
		try {
			int c;
			byte[] buf = new byte[1024];
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			while ((c = in.read(buf)) != -1) {
				out.write(buf, 0, c);
			}
			in.close();
			String content = new String(out.toByteArray());
			FileProperty file = (FileProperty) PropertyFactory.getInstance()
					.produceProperty(PropertyType.FILE);
			file.setName(metadata.getName());
			file.setFilename(filename);
			file.setContent(content);
			return file;
		} catch (IOException ex) {
			throw new FTPException(
					"Error while recieving file from FTP-Server", ex);
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
