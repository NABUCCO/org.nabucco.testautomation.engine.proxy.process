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

import org.nabucco.testautomation.engine.proxy.SubEngineActionType;

/**
 * ProcessActionType
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public enum ProcessActionType implements SubEngineActionType {

	/**
     * ActionType to connect to a server.
     */
	CONNECT("Disconnecting from a server"),
	
	/**
     * ActionType to disconnect from a server.
     */
	DISCONNECT("Connecting to a server"),
	
    /**
     * ActionType for an upload.
     */
    UPLOAD("Uploading file to Ftp"),

    /**
     * ActionType for a download.
     */
    DOWNLOAD("Downloading file from Ftp"),

    /**
     * ActionType for listing files of a directory.
     */
    LIST_FILES("List files in directory on ftp"),
    
    /**
     * ActionType for changing a directory.
     */
    CHANGE_DIR("Changes the directory"),

    /**
     * ActionType to move up to the parent directory.
     */
    MOVE_UP(""),

    /**
     * ActionType to delete a file.
     */
    DELETE_FILE(""),
    
    /**
     * ActionType to rename a file.
     */
    RENAME_FILE(""),
    
    /**
     * ActionType to login.
     */
    LOGIN(""),
    
    /**
     * ActionType logout.
     */
    LOGOUT(""),
    
    /**
     * ActionType to creae a new directory.
     */
    MAKE_DIR(""),
    
    /**
     * ActionType to remove a directory.
     */
    REMOVE_DIR(""),
    
    /**
     * ActionType for starting a service.
     */
    START("Starting service"),

    /**
     * ActionType for stopping a service.
     */
    STOP("Stopping service"),

    /**
     * ActionType for getting the state of a service.
     */
    QUERY("Quering the state of the service"),

    /**
     * ActionType for restarting a service.
     */
    RESTART("Restarting service"),

    /**
     * ActionType for running a scheduled task.
     */
    RUN("Run Scheduled Task"),
    
    /**
     * ActionType for executing a shell-script.
     */
    EXECUTE("Execute a Shell-Script");

    private String description;

    private ProcessActionType(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
