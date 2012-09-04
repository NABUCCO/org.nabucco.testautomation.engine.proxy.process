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

import java.io.IOException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.FileProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * UploadCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class UploadCommand extends AbstractSMBCommand implements SMBCommand {

    /**
     * Creates a new {@link UploadCommand} instance.
     */
    public UploadCommand(NtlmPasswordAuthentication auth) {
        super(auth);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException, IOException {

        FileProperty fileProperty = getFile(metadata, properties);
        String content = fileProperty.getContent().getValue();
        String filename = getFilename(metadata, properties);
        
        if (filename == null) {
            filename = fileProperty.getFilename().getValue();
        }

        info("Upload of file: " + filename);
        start();
        SmbFileOutputStream out = new SmbFileOutputStream(new SmbFile(filename, getAuthentication()));
        out.write(content.getBytes());
        out.flush();
        out.close();
        stop();
        return null;
    }
    
    /**
     * @param metadata
     * @param properties
     * @return
     * @throws SMBException
     */
    private String getFilename(Metadata metadata, PropertyList properties) throws SMBException {

        Property filenameProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "FILENAME");

        if (filenameProperty != null && filenameProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) filenameProperty).getValue().getValue();
        }
        return null;
    }

    /**
     * @param metadata
     * @param properties
     * @return
     * @throws SMBException
     */
    private FileProperty getFile(Metadata metadata, PropertyList properties) throws SMBException {

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
        throw new SMBException("No FileProperty specified found for transfer");
    }

}
