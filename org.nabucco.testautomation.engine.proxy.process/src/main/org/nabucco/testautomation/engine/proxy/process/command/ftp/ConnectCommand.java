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
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;
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
 * @author Steffen Schmidt, PRODYNA AG
 */
class ConnectCommand extends AbstractFTPCommand implements FTPCommand {

    /**
     * Creates a new {@link ConnectCommand} instance.
     * 
     * @param client
     *            the FTP client
     */
    public ConnectCommand(FTPClient client) {
        super(client);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws FTPException {

        try {
            int port = getPort(metadata.getPropertyList());
            String servername = getServername(metadata.getPropertyList());

            info("Connecting to FTP-Server: " + servername + ":" + port);
            start();
            getFTPClient().connect(servername, port);
            int reply = getFTPClient().getReplyCode();
            stop();

            if (!FTPReply.isPositiveCompletion(reply)) {
                getFTPClient().disconnect();
                throw new FTPException("FTP server refused connection");
            }
            return null;
        } catch (SocketException ex) {
            setException(ex);
            throw new FTPCommunicationException("Could not connect to FTP-Server", ex);
        } catch (IOException ex) {
            setException(ex);
            throw new FTPCommunicationException("Could not connect to FTP-Server", ex);
        }
    }

    private String getServername(PropertyList propertyList) throws FTPException {

        Property hostProperty = PropertyHelper.getFromList(propertyList, "SERVERNAME");

        if (hostProperty == null || hostProperty.getType() != PropertyType.TEXT) {
            throw new FTPException("No servername provided for FTP-Connection");
        }
        return ((TextProperty) hostProperty).getValue().getValue();
    }

    private int getPort(PropertyList propertyList) {

        Property portProperty = PropertyHelper.getFromList(propertyList, "PORT");

        if (portProperty == null) {
            return FTPClient.DEFAULT_PORT;
        }

        switch (portProperty.getType()) {
        case NUMERIC:
            return ((NumericProperty) portProperty).getValue().getValue().intValue();
        case TEXT:
            String value = ((TextProperty) portProperty).getValue().getValue();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return FTPClient.DEFAULT_PORT;
            }
        default:
            return FTPClient.DEFAULT_PORT;
        }
    }

}
