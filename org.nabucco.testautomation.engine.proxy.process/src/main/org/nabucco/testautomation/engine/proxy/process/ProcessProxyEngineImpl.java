/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
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
import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyEngine;
import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;
import org.nabucco.testautomation.engine.proxy.process.config.ProcessProxyConfigImpl;
import org.nabucco.testautomation.engine.proxy.process.config.ProcessProxyConfiguration;

import org.nabucco.testautomation.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * ProcessProxyEngineImpl
 *
 * @author Steffen Schmidt, PRODYNA AG
 *
 */
public class ProcessProxyEngineImpl extends AbstractProxyEngine {

	private FTPClient ftpClient;
	
    /**
     * Constructs a new ProxyEngine with {@link SubEngineType.PROCESS}.
     */
	protected ProcessProxyEngineImpl() {
		super(SubEngineType.PROCESS);
	}

	@Override
    public void initialize() throws ProxyConfigurationException {
		// nothing todo so far
    }

    @Override
    public void configure(ProxyEngineConfiguration config) throws ProxyConfigurationException {
    	this.ftpClient = new FTPClient();
		this.ftpClient.setListHiddenFiles(true);
    }

    @Override
    public SubEngine start() throws ProxyConfigurationException {
		try {
			return new ProcessSubEngineImpl(this.ftpClient);
		} catch (NBCTestConfigurationException ex) {
			throw new ProxyConfigurationException(ex);
		}
    }

    @Override
    public void stop() throws ProxyConfigurationException {
        
    	if (this.ftpClient != null) {
    		try {
				if (this.ftpClient.isConnected()) {
					this.ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    @Override
    public void unconfigure() throws ProxyConfigurationException {
    	this.ftpClient = null;
    }

    protected ProxyEngineConfiguration getProxyConfiguration(
			ProxyConfiguration configuration) {
    	
    	ProcessProxyConfiguration config = new ProcessProxyConfigImpl(
				getProxySupport().getProxyClassloader(), configuration);
		return config;
	}

}