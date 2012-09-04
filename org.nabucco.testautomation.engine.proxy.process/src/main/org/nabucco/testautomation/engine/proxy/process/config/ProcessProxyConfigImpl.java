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
package org.nabucco.testautomation.engine.proxy.process.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.proxy.config.AbstractProxyEngineConfiguration;
import org.nabucco.testautomation.settings.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * ProcessProxyConfigImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ProcessProxyConfigImpl extends AbstractProxyEngineConfiguration implements ProcessProxyConfiguration {

    /** Logger */
    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            ProcessProxyConfigImpl.class);

    /**
     * Creates a new instance getting the configuration from the given Properties.
     * 
     * @param the
     *            classloader that loaded the proxy
     * @param properties
     *            the properties containing the configuration
     */
    public ProcessProxyConfigImpl(ClassLoader classloader, ProxyConfiguration configuration) {
        super(classloader, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getFtpServerHost() throws NBCTestConfigurationException {
        return extractInetAddress(getConfigurationValue(ProcessProxyConfigurationType.FTP_SERVER_HOST.getKey()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFtpServerPort() {
        try {
            return Integer.parseInt(getConfigurationValue(ProcessProxyConfigurationType.FTP_SERVER_PORT.getKey()));
        } catch (NumberFormatException e) {
            logger.warning(e, "Cannot parse Property name='"
                    + ProcessProxyConfigurationType.FTP_SERVER_PORT.getKey() + "'. Using default FTP port:  "
                    + DEFAULT_FTP_PORT);
            return DEFAULT_FTP_PORT;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFtpServerSchema() {
        return getConfigurationValue(ProcessProxyConfigurationType.FTP_SERVER_SCHEMA.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFtpUsername() {
        return getConfigurationValue(ProcessProxyConfigurationType.FTP_USERNAME.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFtpPassword() {
        return getConfigurationValue(ProcessProxyConfigurationType.FTP_PASSWORD.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFtpBasedir() {
        return getConfigurationValue(ProcessProxyConfigurationType.FTP_BASEDIR.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getBatchServerHost() throws NBCTestConfigurationException {
        return extractInetAddress(getConfigurationValue(ProcessProxyConfigurationType.BATCH_SERVER_HOST.getKey()));
    }

    private InetAddress extractInetAddress(String assertHost) throws NBCTestConfigurationException {
        if (assertHost == null || assertHost.equals("")) {
            logger.error("Configured Host is empty");
            throw new NBCTestConfigurationException("Configured Host is empty");
        }
        try {
            return InetAddress.getByName(assertHost);
        } catch (UnknownHostException e) {
            logger.error(e, "Configured Host is not valid: '" + assertHost + "'");
            throw new NBCTestConfigurationException("Configured Host is not valid: '" + assertHost + "'");
        }
    }

}
