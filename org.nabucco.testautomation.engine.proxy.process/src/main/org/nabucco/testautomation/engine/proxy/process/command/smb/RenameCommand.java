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

import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * RenameCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class RenameCommand extends AbstractSMBCommand implements SMBCommand {

    /**
     * Creates a new {@link RenameCommand} instance.
     */
    public RenameCommand(NtlmPasswordAuthentication auth) {
        super(auth);
    }

    @Override
    public PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException, IOException {

        String old_file = getOldFile(metadata, properties);
        String new_file = getNewFile(metadata, properties);

        info("Rename file: " + old_file + " to: " + new_file);
        start();
        new SmbFile(old_file, getAuthentication()).renameTo(new SmbFile(new_file, getAuthentication()));
        stop();
        return null;
    }

    /**
     * @param metadata
     * @param properties
     * @return
     * @throws SMBException
     */
    private String getOldFile(Metadata metadata, PropertyList properties) throws SMBException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "OLD_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "OLD_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SMBException("OLD_FILE not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

    /**
     * @param metadata
     * @param properties
     * @return
     * @throws SMBException
     */
    private String getNewFile(Metadata metadata, PropertyList properties) throws SMBException {

        // First, check PropertyList from Action
        Property userProperty = PropertyHelper.getFromList(properties, "NEW_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        // Second, check PropertyList from Metadata
        userProperty = PropertyHelper.getFromList(metadata.getPropertyList(), "NEW_FILE");

        if (userProperty != null && userProperty.getType() == PropertyType.TEXT) {
            return ((TextProperty) userProperty).getValue().getValue();
        }

        throw new SMBException("NEW_FILE not found in "
                + properties.getName().getValue() + " and " + metadata.getPropertyList().getName().getValue());
    }

}
