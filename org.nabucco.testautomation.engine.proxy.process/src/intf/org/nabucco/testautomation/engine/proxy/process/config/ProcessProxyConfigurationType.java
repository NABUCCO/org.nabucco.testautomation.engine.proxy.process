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

/**
 * ProcessProxyConfigurationType
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public enum ProcessProxyConfigurationType {

    FTP_BASEDIR("ftpBasedir"),

    BATCH_SERVER_HOST("BatchServerHost"),

    FTP_SERVER_HOST("ftpServerHost"),

    FTP_SERVER_PORT("ftpServerPort"),

    FTP_SERVER_SCHEMA("ftpServerSchema"),

    FTP_USERNAME("ftpUsername"),

    FTP_PASSWORD("ftpPassword");

    private String key;

    /**
     * Creates a new {@link ProcessProxyConfigurationType} instance.
     * 
     * @param key
     *            the key
     */
    private ProcessProxyConfigurationType(String key) {
        this.key = key;
    }

    /**
     * Getter for the process proxy configuration type key.
     * 
     * @return the key as string
     */
    public String getKey() {
        return this.key;
    }

}
