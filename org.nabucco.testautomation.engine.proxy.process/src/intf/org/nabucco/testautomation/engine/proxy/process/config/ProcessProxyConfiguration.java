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

import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;

/**
 * ProcessProxyConfiguration
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface ProcessProxyConfiguration extends ProxyEngineConfiguration {

    /** Default port for FTP */
    public static final int DEFAULT_FTP_PORT = 21;

    /** Default port for SFTP */
    public static final int DEFAULT_SFTP_PORT = 22;

    /**
     * Get FTP host.
     * 
     * @return FTP host
     */
    InetAddress getFtpServerHost() throws NBCTestConfigurationException;

    /**
     * Get FTP port.
     * 
     * @return port of FTP connection.
     */
    int getFtpServerPort();

    /**
     * Get the FTP schema. e.g. ftp or sftp
     */
    String getFtpServerSchema();

    /**
     * Get FTP user.
     * 
     * @return FTP username
     */
    String getFtpUsername();

    /**
     * Get FTP password.
     * 
     * @return FTP password
     */
    String getFtpPassword();

    /**
     * Get the base directory of the FTP server.
     */
    String getFtpBasedir();

    /**
     * Get the host of the batch server
     * 
     * @return the hostname of the batchserver
     */
    InetAddress getBatchServerHost() throws NBCTestConfigurationException;

}
