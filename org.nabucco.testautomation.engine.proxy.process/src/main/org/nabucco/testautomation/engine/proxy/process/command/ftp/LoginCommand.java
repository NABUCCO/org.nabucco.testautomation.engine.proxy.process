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

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * LoginCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class LoginCommand extends AbstractFTPCommand implements FTPCommand {

    /**
     * Creates a new {@link LoginCommand} instance.
     * 
     * @param client
     *            the FTP client
     */
    public LoginCommand(FTPClient client) {
        super(client);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws FTPException, IOException {

        if (isConnected()) {
            String username = getUsername(metadata, properties);
            String password = getPassword(metadata, properties);
            info("Login to FTP-Server with username: " + username + ", pwd: " + password);
            start();
            boolean success = getFTPClient().login(username, password);
            stop();

            if (!success) {
                throw new FTPException("Login to FTP-Server failed. Cause: " + getResponse());
            }
            return null;
        }

        throw new FTPException("Cannot login to FTP-Server. Cause: Not connected");
    }

    private String getUsername(Metadata metadata, PropertyList propertyList) throws FTPException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(propertyList, "USERNAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "USERNAME");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }
        throw new FTPException("No username provided for Login");
    }

    private String getPassword(Metadata metadata, PropertyList propertyList) throws FTPException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(propertyList, "PASSWORD");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "PASSWORD");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }
        throw new FTPException("No password provided for Login");
    }

}
