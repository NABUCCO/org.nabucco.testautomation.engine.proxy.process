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

import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTP;
import org.nabucco.testautomation.engine.proxy.process.scheduledtask.ScheduledTask;
import org.nabucco.testautomation.engine.proxy.process.script.ShellScript;
import org.nabucco.testautomation.engine.proxy.process.sftp.SFTP;
import org.nabucco.testautomation.engine.proxy.process.smb.SMB;

/**
 * ProcessEngine
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public interface ProcessEngine extends SubEngine {

    /**
     * Gets a SubEngine implementation for a {@link ShellScript}.
     * 
     * @return the Script implementation
     */
    public ShellScript getScript();

    /**
     * Gets a SubEngine implementation for a {@link FTP}.
     * 
     * @return the FTP implementation
     */
    public FTP getFTP();

    /**
     * Gets a SubEngine implementation for a {@link SFTP}.
     * 
     * @return the SFTP implementation
     */
    public SFTP getSFTP();

    /**
     * Gets a SubEngine implementation for a {@link SMB}.
     * 
     * @return the SMB implementation
     */
    public SMB getSMB();

    /**
     * Gets a SubEngine implementation for a {@link ScheduledTask}.
     * 
     * @return the ScheduledTask implementation
     */
    public ScheduledTask getScheduledTask();

}
