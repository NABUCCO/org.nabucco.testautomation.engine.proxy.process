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

import java.io.IOException;
import java.io.InputStream;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * ProcessExecutorImpl
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
final class ProcessExecutorImpl implements ProcessExecutor {

    private static final int DEFAULT_RETURN_VALUE = 0;

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            ProcessExecutorImpl.class);

  
    ProcessExecutorImpl() {
       
    }

    @Override
    public final ProcessExecutionResult executeExternalProcess(final ProcessExecutionType type,
            final String command) {

        logger.info("Trying to execute external command='", command, "'");
        Process process;
        int returnValue = DEFAULT_RETURN_VALUE;
        String errMessage = "";
        String outMessage = "";

        try {

            process = Runtime.getRuntime().exec(command);

            InputStream is = process.getInputStream();

            // Nothing to send to, ignoring the stream
            // OutputStream out = process.getOutputStream();
            
            InputStream err = process.getErrorStream();

            ProcessExecutionStringOutput outLogger = new ProcessExecutionStringOutput();
            ProcessExecutionStringOutput errLogger = new ProcessExecutionStringOutput();

            ProcessExecutionOutputHandler outHandler = new ProcessExecutionOutputHandler(is,
                    outLogger);
            ProcessExecutionOutputHandler errHandler = new ProcessExecutionOutputHandler(err,
                    errLogger);

            logger.debug("Starting Stream Handler Threads");
            outHandler.start();
            errHandler.start();

            logger.debug("Waiting for Execution");
            returnValue = process.waitFor();
            logger.debug("Excution finished");

            logger.debug("Joining Stream Handler Threads");
            outHandler.join();
            errHandler.join();

            outMessage = outLogger.getString();
            errMessage = errLogger.getString();

        } catch (IOException e) {
            logger.error(e, "Caught an IOExceptiom while executing external process");
        } catch (InterruptedException e) {
            logger.error(e, "Caught an InterruptedException while executing external process");
        } catch (Exception e) {
            logger.error(e, "Caught an Unkown Exception while executing external process");
        } 

        ProcessExecutionResult result = new ProcessExecutionResult(returnValue, errMessage, outMessage);
        return result;
    }

}
