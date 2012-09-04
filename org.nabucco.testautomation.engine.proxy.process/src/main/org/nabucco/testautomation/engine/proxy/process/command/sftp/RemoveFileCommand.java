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
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DeleteFileCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class RemoveFileCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link RemoveFileCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected RemoveFileCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (super.isConnected()) {

            String filename = this.getFilename(metadata, properties);

            super.info("Delete remote file: " + filename);

            super.start();
            super.getSFTPClient().rm(filename);
            super.stop();

            return null;
        }

        throw new SFTPException("Deletion of file failed. Cause: Not connected");
    }

    /**
     * Resolve the filename from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the filename
     * 
     * @return the filename of the file to remove
     * 
     * @throws SFTPException
     *             when the filename cannot be resolved
     */
    private String getFilename(Metadata metadata, PropertyList properties) throws SFTPException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "FILENAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "FILENAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SFTPException("FILENAME not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

}
