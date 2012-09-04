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
import org.nabucco.testautomation.property.facade.datatype.FileProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * GetFileCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class GetFileCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link GetFileCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected GetFileCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (this.isConnected()) {
            String filename = this.getFilename(metadata, properties);

            this.info("Download of file: " + filename);

            super.start();
            String content = super.getSFTPClient().get(filename);
            FileProperty fileProperty = this.receiveFile(metadata, filename, content);
            super.stop();

            PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
            this.add(fileProperty, returnProperties);
            return returnProperties;
        }

        throw new SFTPException("Cannot download file. Cause: Not connected");
    }

    /**
     * Creates a file property with the given file information.
     * 
     * @param metadata
     *            the command metadata
     * @param filename
     *            the file name
     * @param content
     *            the file content
     * 
     * @return the file property
     */
    private FileProperty receiveFile(Metadata metadata, String filename, String content) {
        FileProperty file = (FileProperty) PropertyFactory.getInstance().produceProperty(PropertyType.FILE);
        file.setName(metadata.getName());
        file.setFilename(filename);
        file.setContent(content);
        return file;
    }

    /**
     * Resolve the filename from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the filename
     * 
     * @return the filename of the file to download
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
