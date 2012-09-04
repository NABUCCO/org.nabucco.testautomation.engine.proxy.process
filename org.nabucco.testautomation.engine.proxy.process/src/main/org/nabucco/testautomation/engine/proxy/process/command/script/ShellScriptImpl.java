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

import java.util.List;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.process.ProcessActionType;
import org.nabucco.testautomation.engine.proxy.process.exeption.ShellScriptException;
import org.nabucco.testautomation.engine.proxy.process.script.ShellScript;
import org.nabucco.testautomation.engine.proxy.process.script.ShellScriptCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ShellScriptImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ShellScriptImpl implements ShellScript {

    private static final long serialVersionUID = 1L;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(ShellScriptImpl.class);

    /**
     * Creates a new {@link ShellScriptImpl} instance.
     */
    public ShellScriptImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse execute(TestContext context, PropertyList propertyList, List<Metadata> metadataList,
            SubEngineActionType actionType) {

        ActionResponse result = TestResultHelper.createActionResponse();
        ShellScriptCommand command = null;

        try {

            // check request-arguments
            if (propertyList == null) {
                propertyList = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
            }

            // get the metadata to be executed
            Metadata metadata = getLeaf(metadataList);

            switch ((ProcessActionType) actionType) {

            case EXECUTE:
                command = new ExecuteShellScriptCommand();
                break;
            default:
                result.setErrorMessage("Unsupported ProcessActionType for ShellScript: " + actionType);
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
            }

            // Execute ShellScriptCommand
            PropertyList returnProperties = command.execute(metadata, propertyList);

            result.setMessage("Executed ShellScript-action='" + actionType + "'");
            result.setReturnProperties(returnProperties);
            result.setActionStatus(ActionStatusType.EXECUTED);
            return result;
        } catch (ShellScriptException ex) {
            String errorMessage = "Could not execute ShellScript-command. Cause: " + ex.getMessage();
            logger.error(errorMessage);
            result.setErrorMessage(errorMessage);
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (Exception ex) {
            logger.fatal(ex);
            result.setErrorMessage("Could not execute ShellScript-command. Cause: " + ex.toString());
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } finally {

            if (context.isTracingEnabled() && command != null) {
                result.setActionTrace(command.getActionTrace());
            }
        }
    }

    /**
     * Returns the metadata for the ShellScript command
     * 
     * @param metadataList
     *            the list of {@link Metadata}
     * 
     * @return the leaf element to work with
     */
    private Metadata getLeaf(List<Metadata> metadataList) {
        Metadata metadata = metadataList.get(metadataList.size() - 1);
        return metadata;
    }

}
