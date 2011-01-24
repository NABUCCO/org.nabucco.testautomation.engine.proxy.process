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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * ProcessOutputStream
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public class ProcessExecutionOutputHandler extends Thread {

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            ProcessExecutionOutputHandler.class);

    private InputStream in;

    private ProcessExecutionOutput out;
    
    public ProcessExecutionOutputHandler(InputStream in, ProcessExecutionOutput out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = br.readLine()) != null) {
                out.println(line);
                logger.trace(line);
            }
        } catch (Exception e) {
           logger.error(e, "Caught Exception while reading from InputStream");
        }
    }

}
