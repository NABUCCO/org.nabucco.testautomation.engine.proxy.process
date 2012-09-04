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
package org.nabucco.testautomation.engine.proxy.process.command.smb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.FileProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DownloadCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class DownloadCommand extends AbstractSMBCommand implements SMBCommand {

    /**
     * Creates a new {@link DownloadCommand} instance.
     */
    public DownloadCommand(NtlmPasswordAuthentication auth) {
        super(auth);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException, IOException {

        String filename = getFilename(metadata, properties);

        info("Download of file: " + filename);
        start();
        SmbFileInputStream in = new SmbFileInputStream(new SmbFile(filename, getAuthentication()));
        FileProperty fileProperty = receiveFile(metadata, filename, in);
        stop();
        PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
        add(fileProperty, returnProperties);
        return returnProperties;
    }

    /**
     * @param metadata
     * @param properties
     * @return
     * @throws FTPException
     */
    private String getFilename(Metadata metadata, PropertyList properties) throws SMBException {

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

        throw new SMBException("FILENAME not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

    private FileProperty receiveFile(Metadata metadata, String filename, SmbFileInputStream in) throws SMBException {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[8192];
            int n;
            while ((n = in.read(b)) > 0) {
                out.write(b, 0, n);
            }
            String content = new String(out.toByteArray());
            FileProperty file = (FileProperty) PropertyFactory.getInstance().produceProperty(PropertyType.FILE);
            file.setName(metadata.getName());
            file.setFilename(filename);
            file.setContent(content);
            return file;
        } catch (IOException ex) {
            throw new SMBException("Error while recieving file from SMB-Server", ex);
        }
    }

}
