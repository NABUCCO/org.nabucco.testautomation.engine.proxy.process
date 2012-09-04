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
package org.nabucco.testautomation.engine.proxy.process.command.smb;

import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.process.ProcessActionType;
import org.nabucco.testautomation.engine.proxy.process.exeption.SMBException;
import org.nabucco.testautomation.engine.proxy.process.smb.SMB;
import org.nabucco.testautomation.engine.proxy.process.smb.SMBCommand;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyFactory;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SMBImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class SMBImpl implements SMB {

    private static final long serialVersionUID = 1L;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SMBImpl.class);

    private NtlmPasswordAuthentication auth;
    
    @Override
    public ActionResponse execute(TestContext context, PropertyList propertyList, List<Metadata> metadataList,
            SubEngineActionType actionType) {

        ActionResponse result = TestResultHelper.createActionResponse();
        SMBCommand command = null;

        try {

            // check request-arguments
            if (propertyList == null) {
                propertyList = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
            }

            // get the metadata to be executed
            Metadata metadata = getLeaf(metadataList);

            switch ((ProcessActionType) actionType) {

            case LOGIN:
                command = new LoginCommand();
                command.execute(metadata, propertyList);
                this.auth = ((LoginCommand) command).getAuthentication();
                result.setActionStatus(ActionStatusType.EXECUTED);
                return result;
            case UPLOAD:
                command = new UploadCommand(this.auth);
                break;
            case DOWNLOAD:
                command = new DownloadCommand(this.auth);
                break;
            case LIST_FILES:
                command = new ListFilesCommand(this.auth);
                break;
            case MAKE_DIR:
                command = new MakeDirCommand(this.auth);
                break;
            case REMOVE_DIR:
                command = new RemoveDirCommand(this.auth);
                break;
            case RENAME_FILE:
                command = new RenameCommand(this.auth);
                break;
            default:
                result.setErrorMessage("Unsupported ProcessActionType for SMB: " + actionType);
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
            }

            // Execute SMBCommand
            PropertyList returnProperties = command.execute(metadata, propertyList);

            result.setMessage("Executed SMB-action='" + actionType + "'");
            result.setReturnProperties(returnProperties);
            result.setActionStatus(ActionStatusType.EXECUTED);
            return result;
        } catch (SMBException ex) {
            String errorMessage = "Could not execute SMB-command. Cause: " + ex.getMessage();
            logger.error(errorMessage);
            result.setErrorMessage(errorMessage);
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (Exception ex) {
            logger.fatal(ex);
            result.setErrorMessage("Could not execute SMB-command. Cause: " + ex.toString());
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } finally {

            if (context.isTracingEnabled() && command != null) {
                result.setActionTrace(command.getActionTrace());
            }
        }
    }

    /**
     * Returns the metadata for the SMB command
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
