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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.proxy.process.exeption.ProcessSemaphoreExeption;


/**
 * ProcessExecutionSemaphoreMap
 * 
 * @deprecated Not longer used, functionallity moved to SubEngineInvoker.
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
final class ProcessExecutionSemaphoreMap {

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            ProcessExecutionSemaphoreMap.class);

    private static ProcessExecutionSemaphoreMap instance;

    private Map<ProcessExecutionType, Semaphore> semaphoreMap;

    private ProcessExecutionSemaphoreMap() {
        this.semaphoreMap = Collections
                .synchronizedMap(new HashMap<ProcessExecutionType, Semaphore>());

        ProcessExecutionType[] literals = ProcessExecutionType.values();
        for (int i = 0; i < literals.length; i++) {
            ProcessExecutionType type = literals[i];
            Semaphore sem = new Semaphore(/*type.getInstanceCount()*/ 1);
            this.semaphoreMap.put(type, sem);
        }
    }

    public final static synchronized ProcessExecutionSemaphoreMap getInstance() {
        if (instance == null) {
            instance = new ProcessExecutionSemaphoreMap();
        }
        return instance;
    }

    public final synchronized void accurireProcess(ProcessExecutionType type)
            throws ProcessSemaphoreExeption {
        try {
            Semaphore sem = this.semaphoreMap.get(type);
            logger.debug("Accuiring Semaphore for: ", type.getDescription(), " at ", ""
                    + System.currentTimeMillis());
            sem.acquire();
            logger.debug("Accuirred Semaphore for: ", type.getDescription(), " at ", ""
                    + System.currentTimeMillis());

        } catch (InterruptedException e) {
            logger.error(e, "Caught an InterruptedException while accuiring a Semaphore for ", type
                    .getDescription());
            throw new ProcessSemaphoreExeption(type.getDescription(), e);
        }

    }

    public synchronized void releaseProcess(ProcessExecutionType type) {
        Semaphore sem = this.semaphoreMap.get(type);
        sem.release();
    }
}
