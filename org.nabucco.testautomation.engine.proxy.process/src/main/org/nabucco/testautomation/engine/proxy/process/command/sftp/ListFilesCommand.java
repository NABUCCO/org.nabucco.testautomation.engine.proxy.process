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

import java.io.File;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPException;
import org.nabucco.testautomation.engine.proxy.process.sftp.SFTPCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * ListFilesCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class ListFilesCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link ListFilesCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected ListFilesCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (super.isConnected()) {

            String pathname = this.getPathname(metadata, properties);

            if (pathname == null) {
                pathname = super.getSFTPClient().pwd();
                super.info("List files of current directory: " + pathname);
            } else {
                super.info("List files of directory: " + pathname);
            }

            super.start();
            List<LsEntry> files = super.getSFTPClient().ls(pathname);
            super.stop();

            PropertyList returnProperties = PropertyHelper.createPropertyList(RETURN_PROPERTIES);
            this.addFileList(pathname, files, returnProperties);
            return returnProperties;
        }

        throw new SFTPException("Cannot list files. Cause: Not connected");
    }

    /**
     * Add the file list to the properties.
     * 
     * @param pathname
     *            the pathname of the files
     * @param files
     *            the files to add
     * @param properties
     *            the properties to add the files to
     */
    private void addFileList(String pathname, List<LsEntry> files, PropertyList properties) {

        PropertyList directory = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
        directory.setName(pathname);
        super.add(directory, properties);

        int i = 0;

        for (LsEntry file : files) {
            super.debug("File " + i + ": " + file.getFilename());
            TextProperty prop = (TextProperty) PropertyFactory.getInstance().produceProperty(PropertyType.TEXT);
            prop.setName("" + i++);
            prop.setValue(new File(file.getFilename()).getName());
            super.add(prop, directory);
        }
    }

    /**
     * Resolve the pathname from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the path name
     * 
     * @return the path to list the files for
     * 
     * @throws SFTPException
     *             when the path cannot be resolved
     */
    private String getPathname(Metadata metadata, PropertyList properties) {

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
