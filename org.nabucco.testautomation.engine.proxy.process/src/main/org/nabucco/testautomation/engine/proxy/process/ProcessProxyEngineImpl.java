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
package org.nabucco.testautomation.engine.proxy.process;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyEngine;
import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;
import org.nabucco.testautomation.engine.proxy.process.command.sftp.SFTPClient;
import org.nabucco.testautomation.engine.proxy.process.config.ProcessProxyConfigImpl;
import org.nabucco.testautomation.engine.proxy.process.config.ProcessProxyConfiguration;
import org.nabucco.testautomation.settings.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.settings.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * ProcessProxyEngineImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * @author Nicolas Moser, PRODYNA AG
 */
public class ProcessProxyEngineImpl extends AbstractProxyEngine {

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            ProcessProxyEngineImpl.class);

    private FTPClient ftpClient;

    private SFTPClient sftpClient;

    /**
     * Constructs a new ProxyEngine with {@link SubEngineType.PROCESS}.
     */
    protected ProcessProxyEngineImpl() {
        super(SubEngineType.PROCESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() throws ProxyConfigurationException {
        // nothing todo so far
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(ProxyEngineConfiguration config) throws ProxyConfigurationException {
        this.ftpClient = new FTPClient();
        this.ftpClient.setListHiddenFiles(true);

        this.sftpClient = new SFTPClient();

        logger.info("ProcessProxyEngine configured");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubEngine start() throws ProxyConfigurationException {

        try {
            logger.info("Starting ProcessProxyEngine ...");

            SubEngine subEngine = null;
            subEngine = new ProcessSubEngineImpl(this.ftpClient, this.sftpClient);

            logger.info("ProcessSubEngine created");
            return subEngine;

        } catch (NBCTestConfigurationException ex) {
            throw new ProxyConfigurationException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws ProxyConfigurationException {

        try {
            if (this.ftpClient != null) {
                if (this.ftpClient.isConnected()) {
                    this.ftpClient.disconnect();
                }
            }
            if (this.sftpClient != null) {
                if (this.sftpClient.isConnected()) {
                    this.sftpClient.disconnect();
                }
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unconfigure() throws ProxyConfigurationException {
        this.ftpClient = null;
        this.sftpClient = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ProxyEngineConfiguration getProxyConfiguration(ProxyConfiguration configuration) {

        ProcessProxyConfiguration config = new ProcessProxyConfigImpl(getProxySupport().getProxyClassloader(),
                configuration);
        return config;
    }

}
