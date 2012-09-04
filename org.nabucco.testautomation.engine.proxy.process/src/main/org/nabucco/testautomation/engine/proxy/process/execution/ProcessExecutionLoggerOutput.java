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
package org.nabucco.testautomation.engine.proxy.process.execution;

import org.apache.log4j.Level;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;

/**
 * ProcessExecutionLoggerOutput
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public final class ProcessExecutionLoggerOutput implements ProcessExecutionOutput {

    private NabuccoLogger logger;

    private final Level level;

    /**
     * Creates a new {@link ProcessExecutionLoggerOutput} instance.
     * 
     * @param type
     *            the execution type
     * @param level
     *            the log level
     */
    public ProcessExecutionLoggerOutput(ProcessExecutionType type, Level level) {
        this.level = level;
        this.logger = NabuccoLoggingFactory.getInstance().getLogger(ProcessExecutionLoggerOutput.class);
    }

    @Override
    public final void println(String line) {
        if (this.level == Level.DEBUG) {
            this.logger.debug(line);
        } else if (this.level == Level.WARN) {
            this.logger.warning(line);
        } else if (this.level == Level.INFO) {
            this.logger.info(line);
        } else if (this.level == Level.ERROR) {
            this.logger.error(line);
        } else if (this.level == Level.FATAL) {
            this.logger.fatal(line);
        }
    }

}
