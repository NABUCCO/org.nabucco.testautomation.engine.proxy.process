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

import org.nabucco.testautomation.engine.proxy.SubEngineOperationType;

/**
 * ProcessEngineOperationType
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public enum ProcessEngineOperationType implements SubEngineOperationType {

    /**
     * EngineOperationType for accessing a FTP-Server
     */
    FTP_SERVER,

    /**
     * EngineOperationType for accessing directories via FTP
     */
    FTP_DIRECTORY,

    /**
     * EngineOperationType for accessing files via FTP
     */
    FTP_FILE,

    /**
     * EngineOperationType for accessing a FTP-Server
     */
    SFTP_SERVER,

    /**
     * EngineOperationType for accessing directories via FTP
     */
    SFTP_DIRECTORY,

    /**
     * EngineOperationType for accessing files via FTP
     */
    SFTP_FILE,

    /**
     * EngineOperationType for shell script commands
     */
    SHELL_SCRIPT,

    /**
     * EngineOperationType for scheduled task commands.
     */
    SCHEDULEDTASK,
    
    /**
     * EngineOperationType for samba file server commands.
     */
    SMB_USER,
    
    /**
     * EngineOperationType for samba file commands.
     */
    SMB_FILE,
    
    /**
     * EngineOperationType for samba directory commands.
     */
    SMB_DIRECTORY;

}
