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

import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
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
class LoginCommand extends AbstractSMBCommand implements SMBCommand {

    private NtlmPasswordAuthentication auth;

    /**
     * Creates a new {@link LoginCommand} instance.
     */
    public LoginCommand() {
        super(null);
    }

    public NtlmPasswordAuthentication getAuthentication() {
        return this.auth;
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException, IOException {

        String username = getUsername(metadata, properties);
        String password = getPassword(metadata, properties);
        String domain = getDomain(metadata, properties);
        info("Creating authentication with domain: " + domain + ", username: " + username + ", pwd: " + password);
        start();
        this.auth = new NtlmPasswordAuthentication(domain, username, password);
        stop();
        return null;
    }

    private String getUsername(Metadata metadata, PropertyList propertyList) throws SMBException {

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
        throw new SMBException("No username provided for Login");
    }

    private String getPassword(Metadata metadata, PropertyList propertyList) throws SMBException {

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
        throw new SMBException("No password provided for Login");
    }

    private String getDomain(Metadata metadata, PropertyList propertyList) throws SMBException {

        // First, check PropertyList from Action
        Property domainProperty = PropertyHelper.getFromList(propertyList, "DOMAIN");

        if (domainProperty != null && domainProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) domainProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        domainProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "DOMAIN");

        if (domainProperty != null && domainProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) domainProperty).getValue().getValue();
        }
        return ""; // return Default-Domain
    }

}
