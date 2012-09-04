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
package org.nabucco.testautomation.engine.proxy.process.command.sftp;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;

import com.jcraft.jsch.UserInfo;

/**
 * SFTPUserInfo
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SFTPUserInfo implements UserInfo {

    private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SFTPUserInfo.class);

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassword(String message) {
        logger.info("JSch: ", message);
        return true;
    }

    @Override
    public boolean promptPassphrase(String message) {
        logger.info("JSch: ", message);
        return true;
    }

    @Override
    public boolean promptYesNo(String message) {
        logger.info("JSch: ", message);
        return true;
    }

    @Override
    public void showMessage(String message) {
        logger.info("JSch: ", message);
    }

}
