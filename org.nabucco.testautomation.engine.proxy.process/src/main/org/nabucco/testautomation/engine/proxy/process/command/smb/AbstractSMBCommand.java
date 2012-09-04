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

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyCommand;
import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * AbstractSMBCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
abstract class AbstractSMBCommand extends AbstractProxyCommand {

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SMBCommand.class);

    private final NtlmPasswordAuthentication auth;

    /**
     * Creates a new {@link AbstractSMBCommand} instance.
     * 
     * @param client
     *            the FTP client
     */
    protected AbstractSMBCommand(NtlmPasswordAuthentication auth) {
        this.auth = auth;
    }

    @Override
    public PropertyList execute(Metadata metadata, PropertyList properties) throws SMBException {

        try {
            return this.executeCallback(metadata, properties);
        } catch (IOException ex) {
            setException(ex);
            throw new SMBException("Error during communication with SMB-FileServer", ex);
        }
    }

    protected abstract PropertyList executeCallback(Metadata metadata, PropertyList properties) throws SMBException,
            IOException;

    protected NtlmPasswordAuthentication getAuthentication() {
        return this.auth;
    }

    @Override
    protected void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    protected void info(String msg) {
        logger.info(msg);
    }

    @Override
    protected void warning(String msg) {
        logger.warning(msg);
    }

    @Override
    protected void error(String msg) {
        logger.error(msg);
    }

}
