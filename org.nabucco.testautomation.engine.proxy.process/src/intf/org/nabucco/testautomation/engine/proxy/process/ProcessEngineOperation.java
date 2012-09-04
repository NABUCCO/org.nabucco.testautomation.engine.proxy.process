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

import java.io.Serializable;
import java.util.List;

import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ProcessEngineOperation
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface ProcessEngineOperation extends Serializable {

    /**
     * Executes the given action on the ProcessEngineOperation.
     * 
     * @param context
     *            the context
     * @param propertyList
     *            the list properties
     * @param metadataList
     *            the list of metadata
     * @param actionType
     *            the action to be executed
     * 
     * @return the result object of the execution
     */
    public ActionResponse execute(TestContext context, PropertyList propertyList, List<Metadata> metadataList,
            SubEngineActionType actionType);

}
