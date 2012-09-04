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
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * PutFileCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class PutFileCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link PutFileCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected PutFileCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {
        FileProperty fileProperty = this.getFile(metadata, properties);

        String filename = fileProperty.getFilename().getValue();
        String content = fileProperty.getContent().getValue();

        if (super.isConnected()) {

            super.info("Upload of file: " + filename);

            super.start();
            super.getSFTPClient().get(filename, content);
            super.stop();
            return null;
        }

        throw new SFTPException("Cannot upload file. Cause: Not connected");
    }

    /**
     * Resolve the file from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the path name
     * 
     * @return the file to put on the server
     * 
     * @throws SFTPException
     *             when the path cannot be resolved
     */
    private FileProperty getFile(Metadata metadata, PropertyList properties) throws SFTPException {

        // first, check PropertyList from Action
        if (properties != null) {
            for (PropertyContainer container : properties.getPropertyList()) {
                Property prop = container.getProperty();

                if (prop.getType() == PropertyType.FILE) {
                    return (FileProperty) prop;
                }
            }
        }

        // Second, check PropertyList from Metadata
        if (metadata.getPropertyList() != null) {
            for (PropertyContainer container : metadata.getPropertyList().getPropertyList()) {
                Property prop = container.getProperty();

                if (prop.getType() == PropertyType.FILE) {
                    return (FileProperty) prop;
                }
            }
        }

        throw new SFTPException("No FileProperty specified found for transfer.");
    }

}
