/*
 * Copyright 2012 PRODYNA AG
 *
 * Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/eclipse-1.0.php or
 * http://www.nabucco.org/License.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nabucco.testautomation.engine.proxy.process.command.sftp;

import java.io.IOException;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyCommand;
import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * AbstractSFTPCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
abstract class AbstractSFTPCommand extends AbstractProxyCommand {

    private SFTPClient sftpClient;

    private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(AbstractSFTPCommand.class);

    /**
     * Creates a new {@link SFTPImpl} instance.
     * 
     * @param sftpClient
     *            the sftp client
     */
    protected AbstractSFTPCommand(SFTPClient sftpClient) {
        this.sftpClient = sftpClient;
    }

    @Override
    public PropertyList execute(Metadata metadata, PropertyList properties) throws SFTPException {

        try {
            return this.executeCallback(metadata, properties);
        } catch (Exception ex) {
            setException(ex);
            throw new SFTPCommunicationException("Error during communication with FTP-Server", ex);
        }
    }

    /**
     * Callback for the concrete SFTP command execution.
     * 
     * @param metadata
     *            the metadata instance
     * @param properties
     *            the request properties
     * 
     * @return the resulting property list
     * 
     * @throws SFTPException
     *             when the command raises an exception
     * @throws IOException
     *             when the connection cannot be established
     */
    protected abstract PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException;

    /**
     * Getter for the sftpClient.
     * 
     * @return Returns the sftpClient.
     */
    public SFTPClient getSFTPClient() {
        return this.sftpClient;
    }

    @Override
    protected void error(String message) {
        logger.error(message);
    }

    @Override
    protected void warning(String message) {
        logger.warning(message);
    }

    @Override
    protected void info(String message) {
        logger.info(message);
    }

    @Override
    protected void debug(String message) {
        logger.debug(message);
    }

    /**
     * Checks whether the SFTP session is connected.
     * 
     * @return <b>true</b> if the SFTP session is connected, <b>false</b> if not
     */
    public boolean isConnected() {
        if (this.sftpClient != null) {
            return this.sftpClient.isConnected();
        }
        return false;
    }

}
