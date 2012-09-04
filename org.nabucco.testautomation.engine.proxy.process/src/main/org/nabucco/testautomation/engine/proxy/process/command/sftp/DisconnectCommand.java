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

import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPException;
import org.nabucco.testautomation.engine.proxy.process.sftp.SFTPCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DisconnectCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class DisconnectCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link DisconnectCommand} instance.
     * 
     * @param sftpClient
     *            the sftp client
     */
    protected DisconnectCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (super.isConnected()) {

            super.info("Disconnecting from SFTP-Server");

            super.start();
            super.getSFTPClient().disconnect();
            super.stop();

        } else {
            super.info("Already disconnected");
        }

        return null;
    }

}
