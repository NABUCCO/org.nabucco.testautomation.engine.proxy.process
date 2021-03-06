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

import java.io.File;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;
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
class ListFilesCommand extends AbstractFTPCommand implements FTPCommand {

    /**
     * Creates a new {@link ListFilesCommand} instance.
     * 
     * @param client
     *            the FTP client
     */
    public ListFilesCommand(FTPClient client) {
        super(client);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws FTPException, IOException {

        if (isConnected()) {
            String pathname = getPathname(metadata, properties);
            FTPFile[] files = null;

            if (pathname == null) {
                pathname = getFTPClient().printWorkingDirectory();
                info("List files of current directory: " + pathname);
                start();
                files = getFTPClient().listFiles(pathname);
                stop();
            } else {
                info("List files of directory: " + pathname);
                start();
                files = getFTPClient().listFiles(pathname);
                stop();
            }

            PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
            addFileList(pathname, files, returnProperties);
            return returnProperties;
        }

        throw new FTPException("Cannot list files. Cause: Not connected");
    }

    private void addFileList(String pathname, FTPFile[] files, PropertyList properties) throws FTPException {

        if (files == null) {
            throw new FTPException("No fileList received");
        }

        PropertyList directory = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
        directory.setName(pathname);
        add(directory, properties);
        int i = 0;

        for (FTPFile file : files) {
            debug("File " + i + ": " + file.getName());
            TextProperty prop = (TextProperty) PropertyFactory.getInstance().produceProperty(PropertyType.TEXT);
            prop.setName("" + i++);
            prop.setValue(new File(file.getName()).getName());
            add(prop, directory);
        }
    }

    private String getPathname(Metadata metadata, PropertyList properties) throws FTPException {

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

        // PATHNAME is optional
        return null;
    }

}
