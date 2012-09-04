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
 * RenameCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class RenameCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link RenameCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected RenameCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (super.isConnected()) {

            String oldPath = this.getOldFile(metadata, properties);
            String newPath = this.getNewFile(metadata, properties);

            super.info("Rename file: " + oldPath + " to: " + newPath);

            super.start();
            super.getSFTPClient().rename(oldPath, newPath);
            super.stop();

            return null;
        }

        throw new SFTPException("Renaming of file failed. Cause: Not connected");
    }

    /**
     * Resolve the filename from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the filename
     * 
     * @return the filename of the old file
     * 
     * @throws SFTPException
     *             when the filename cannot be resolved
     */
    private String getOldFile(Metadata metadata, PropertyList properties) throws SFTPException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "OLD_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "OLD_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SFTPException("OLD_FILE not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

    /**
     * Resolve the filename from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the filename
     * 
     * @return the filename of the new file
     * 
     * @throws SFTPException
     *             when the filename cannot be resolved
     */
    private String getNewFile(Metadata metadata, PropertyList properties) throws SFTPException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "NEW_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "NEW_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SFTPException("NEW_FILE not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }
}
