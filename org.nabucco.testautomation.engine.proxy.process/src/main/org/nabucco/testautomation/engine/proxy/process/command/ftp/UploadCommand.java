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
package org.nabucco.testautomation.engine.proxy.process.command.ftp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;
import org.nabucco.testautomation.property.facade.datatype.FileProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * UploadCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class UploadCommand extends AbstractFTPCommand implements FTPCommand {

    /**
     * Creates a new {@link UploadCommand} instance.
     * 
     * @param client
     *            the FTP client
     */
    public UploadCommand(FTPClient client) {
        super(client);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws FTPException, IOException {

        FileProperty fileProperty = getFile(metadata, properties);

        String filename = fileProperty.getFilename().getValue();
        String content = fileProperty.getContent().getValue();

        if (isConnected()) {
            info("Upload of file: " + filename);
            start();
            BufferedOutputStream out = new BufferedOutputStream(getFTPClient().storeFileStream(filename));
            sendFile(out, content);
            getFTPClient().completePendingCommand();
            stop();
            return null;
        }

        throw new FTPException("Cannot upload file. Cause: Not connected");
    }

    /**
     * @param metadata
     * @param properties
     * @return
     * @throws FTPException
     */
    private FileProperty getFile(Metadata metadata, PropertyList properties) throws FTPException {

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
        throw new FTPException("No FileProperty specified found for transfer");
    }

    /**
     * @param content
     * @throws FTPException
     */
    private void sendFile(OutputStream out, String content) throws IOException {

        // TODO Check and handle Encoding
        out.write(content.getBytes());
        out.close();
    }

}
