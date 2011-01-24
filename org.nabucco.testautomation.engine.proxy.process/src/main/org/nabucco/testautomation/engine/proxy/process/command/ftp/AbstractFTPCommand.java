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

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyCommand;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * AbstractFTPCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public abstract class AbstractFTPCommand extends AbstractProxyCommand implements FTPCommand, ProtocolCommandListener {

	private static final NBCTestLogger logger = NBCTestLoggingFactory
			.getInstance().getLogger(FTPCommand.class);

	private final FTPClient client;

	protected AbstractFTPCommand(FTPClient client) {
		this.client = client;
	}
	
	@Override
	public PropertyList execute(Metadata metadata, PropertyList properties)
			throws FTPException {
		
		try {
			setCommandListener();
			return executeCallback(metadata, properties);
		} catch (IOException ex) {
			setException(ex);
			throw new FTPCommunicationException(
					"Error during communication with FTP-Server", ex);
		} finally {
			removeCommandListener();
		}
	}
	
	public abstract PropertyList executeCallback(Metadata metadata, PropertyList properties)
			throws FTPException, IOException;

	protected FTPClient getFTPClient() {
		return this.client;
	}

	protected boolean isConnected() {
		return this.client.isConnected();
	}

	@Override
	protected void info(String msg) {
		logger.info(msg);
	}

	@Override
	protected void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	protected void error(String msg) {
		logger.error(msg);
	}

	protected void setCommandListener() {
		this.client.addProtocolCommandListener(this);
	}
	
	protected void removeCommandListener() {
		this.client.removeProtocolCommandListener(this);
	}
	
	@Override
	public void protocolCommandSent(ProtocolCommandEvent event) {
		String rq = this.getRequest();
		
		if (rq == null) {
			rq = "";
		}
		this.setRequest(rq + event.getMessage());
	}

	@Override
	public void protocolReplyReceived(ProtocolCommandEvent event) {
		String rs = this.getResponse();
		
		if (rs == null) {
			rs = "";
		}
		this.setResponse(rs + event.getMessage());
	}

}
