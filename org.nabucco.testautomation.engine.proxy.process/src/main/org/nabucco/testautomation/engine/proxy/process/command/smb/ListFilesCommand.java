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

import java.io.File;
import java.io.IOException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ListFilesCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class ListFilesCommand extends AbstractSMBCommand implements SMBCommand {

    /**
     * Creates a new {@link ListFilesCommand} instance.
     */
    public ListFilesCommand(NtlmPasswordAuthentication auth) {
        super(auth);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException, IOException {

        String pathname = getPathname(metadata, properties);

        info("List files of current directory: " + pathname);
        start();
        SmbFile[] fileList = new SmbFile(pathname, getAuthentication()).listFiles();
        stop();

        PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
        addFileList(pathname, fileList, returnProperties);
        return returnProperties;
    }

    private void addFileList(String pathname, SmbFile[] files, PropertyList properties) throws SMBException {

        if (files == null) {
            throw new SMBException("No fileList received");
        }

        PropertyList directory = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
        directory.setName(pathname);
        add(directory, properties);
        int i = 0;

        for (SmbFile file : files) {
            debug("File " + i + ": " + file.getName());
            TextProperty prop = (TextProperty) PropertyFactory.getInstance().produceProperty(PropertyType.TEXT);
            prop.setName("" + i++);
            prop.setValue(new File(file.getName()).getName());
            add(prop, directory);
        }
    }

    private String getPathname(Metadata metadata, PropertyList properties) throws SMBException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "PATHNAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "PATHNAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SMBException("No pathname defined");
    }

}
