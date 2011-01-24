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
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DisconnectCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DisconnectCommand extends AbstractFTPCommand {

	public DisconnectCommand(FTPClient client) {
		super(client);
	}
	
	@Override
	public PropertyList executeCallback(Metadata metadata, PropertyList properties)
			throws FTPException, IOException {
		
		if (isConnected()) {
			info("Disconnecting from FTP-Server");
			start();
			getFTPClient().disconnect();
			stop();
		} else {
			info("Already disconnected");
		}
		return null;
	}

}
