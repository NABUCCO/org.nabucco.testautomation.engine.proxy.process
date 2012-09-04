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
 * ChangeDirCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class ChangeDirCommand extends AbstractSFTPCommand implements SFTPCommand {

    /**
     * Creates a new {@link ChangeDirCommand} instance.
     * 
     * @param sftpClient
     */
    protected ChangeDirCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        if (super.isConnected()) {
            String pathname = this.getPathname(metadata, properties);

            super.info("Change directory to: " + pathname);

            super.start();
            super.getSFTPClient().cd(pathname);
            super.stop();

            String pwd = super.getSFTPClient().pwd();

            super.info("Directory is now: " + pwd);

            return null;
        }

        throw new SFTPException("Could not change directory. Cause: Not connected");
    }

    /**
     * Resolve the pathname from the given properties.
     * 
     * @param metadata
     *            the metadata holding properties
     * @param properties
     *            additional properties may hold the path name
     * 
     * @return the path to change the directory to
     * 
     * @throws SFTPException
     *             when the path cannot be resolved
     */
    private String getPathname(Metadata metadata, PropertyList properties) throws SFTPException {

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

        throw new SFTPException("PATHNAME not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

}
