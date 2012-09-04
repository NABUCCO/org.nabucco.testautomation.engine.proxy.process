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

import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPException;
import org.nabucco.testautomation.engine.proxy.process.sftp.SFTPCommand;
import org.nabucco.testautomation.property.facade.datatype.NumericProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ConnectCommand
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class ConnectCommand extends AbstractSFTPCommand implements SFTPCommand {

    private static final String PROPERTY_USERNAME = "USERNAME";

    private static final String PROPERTY_PASSWORD = "PASSWORD";

    /**
     * Creates a new {@link ConnectCommand} instance.
     * 
     * @param sftpClient
     *            the SFTP client
     */
    protected ConnectCommand(SFTPClient sftpClient) {
        super(sftpClient);
    }

    @Override
    protected PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SFTPException {

        try {
            String username = this.getUsername(metadata, properties);
            String password = this.getPassword(metadata, properties);

            Integer port = this.getPort(metadata.getPropertyList());
            String host = this.getServername(metadata.getPropertyList());

            super.info("Connecting to SFTP-Server: " + username + "@" + host + ":" + port);

            super.start();
            super.getSFTPClient().connect(username, password, host, port);
            super.stop();

            return null;

        } catch (SFTPCommunicationException sce) {
            super.setException(sce);
            throw sce;
        }
    }

    /**
     * Retrieves the username from the property list.
     * 
     * @param metadata
     *            the metadata holding additional connection properties
     * @param propertyList
     *            the property list holding the connection properties
     * 
     * @return the SFTP username
     * 
     * @throws SFTPException
     *             when the properties do not hold a username
     */
    private String getUsername(Metadata metadata, PropertyList propertyList) throws SFTPException {

        // Check ActionProperties first
        Property username = PropertyHelper.getFromList(propertyList, PROPERTY_USERNAME);

        // Check MetadataProperties
        if (username == null) {
            username = PropertyHelper.getFromList(metadata.getPropertyList(), PROPERTY_USERNAME);
        }

        if (username == null || username.getType() != PropertyType.TEXT) {
            throw new SFTPException("No username provided for SFTP-Connection");
        }

        return ((TextProperty) username).getValue().getValue();
    }

    /**
     * Retrieves the password from the property list.
     * 
     * @param metadata
     *            the metadata holding additional connection properties
     * @param propertyList
     *            the property list
     * 
     * @return the SFTP user password
     * 
     * @throws SFTPException
     *             when the properties do not hold a password
     */
    private String getPassword(Metadata metadata, PropertyList propertyList) throws SFTPException {

        // Check ActionProperties first
        Property password = PropertyHelper.getFromList(propertyList, PROPERTY_PASSWORD);

        // Check MetadataProperties
        if (password == null) {
            password = PropertyHelper.getFromList(metadata.getPropertyList(), PROPERTY_PASSWORD);
        }

        if (password == null || password.getType() != PropertyType.TEXT) {
            throw new SFTPException("No password provided for SFTP-Connection");
        }

        return ((TextProperty) password).getValue().getValue();
    }

    /**
     * Retrieves the hostname from the property list.
     * 
     * @param propertyList
     *            the property list
     * 
     * @return the server host
     * 
     * @throws SFTPException
     *             when the properties do not hold a servername
     */
    private String getServername(PropertyList propertyList) throws SFTPException {

        Property hostProperty = PropertyHelper.getFromList(propertyList, "SERVERNAME");

        if (hostProperty == null || hostProperty.getType() != PropertyType.TEXT) {
            throw new SFTPException("No servername provided for SFTP-Connection");
        }

        return ((TextProperty) hostProperty).getValue().getValue();
    }

    /**
     * Retrieves the port from the property list.
     * 
     * @param propertyList
     *            the property list
     * 
     * @return the server port, or null if none is defined
     */
    private Integer getPort(PropertyList propertyList) {

        Property portProperty = PropertyHelper.getFromList(propertyList, "PORT");

        if (portProperty == null) {
            return null;
        }

        switch (portProperty.getType()) {
        case NUMERIC:
            return ((NumericProperty) portProperty).getValue().getValue().intValue();
        case TEXT:
            String value = ((TextProperty) portProperty).getValue().getValue();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return null;
            }
        default:
            return null;
        }
    }
}
