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
package org.nabucco.testautomation.engine.proxy.process.command.script;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyCommand;
import org.nabucco.testautomation.engine.proxy.process.script.ShellScriptCommand;

/**
 * AbstractShellScriptCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public abstract class AbstractShellScriptCommand extends AbstractProxyCommand implements ShellScriptCommand {

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(ShellScriptCommand.class);

    /**
     * Creates a new {@link AbstractShellScriptCommand} instance.
     */
    protected AbstractShellScriptCommand() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void info(String msg) {
        logger.info(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void debug(String msg) {
        logger.debug(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void error(String msg) {
        logger.error(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void warning(String msg) {
        logger.warning(msg);
    }

}
