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
package org.nabucco.testautomation.engine.proxy.process.execution;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * ProcessExecutionLoggerOutput
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public final class ProcessExecutionLoggerOutput implements ProcessExecutionOutput {

    private NBCTestLogger logger;

    private final Level level;

    public ProcessExecutionLoggerOutput(ProcessExecutionType type, Level level) {
        this.level = level;

        this.logger = NBCTestLoggingFactory.getInstance().getLogger(ProcessExecutionLoggerOutput.class, type.getDescription());
    }

    @Override
    public final void println(String line) {
        if (level == Level.DEBUG) {
            logger.debug(line);
        } else if (level == Level.WARN) {
            logger.warning(line);
        } else if (level == Level.INFO) {
            logger.info(line);
        } else if (level == Level.ERROR) {
            logger.error(line);
        } else if (level == Level.FATAL) {
            logger.fatal(line);
        }
    }

}
