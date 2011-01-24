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
package org.nabucco.testautomation.engine.proxy.process.command.ftp;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.process.ProcessActionType;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.FTPException;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTP;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTPCommand;

import org.nabucco.testautomation.facade.datatype.PropertyFactory;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * FTPImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class FTPImpl implements FTP {

	private static final long serialVersionUID = 1L;

	private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
			 FTPImpl.class);
	
	private final FTPClient client;
	
	public FTPImpl(FTPClient ftpClient) {
		this.client = ftpClient;
	}
	
	@Override
	public ActionResponse execute(TestContext context,
			PropertyList propertyList, List<Metadata> metadataList,
			SubEngineActionType actionType) {
		
		ActionResponse result = TestResultHelper.createActionResponse();
		FTPCommand command = null;

		try {

			// check request-arguments
			if (propertyList == null) {
				propertyList = (PropertyList) PropertyFactory.getInstance()
						.produceProperty(PropertyType.LIST);
			}

            // get the metadata to be executed
            Metadata metadata = getLeaf(metadataList);

            switch ((ProcessActionType) actionType) {
            
            case CONNECT:
            	command = new ConnectCommand(this.client);
            	break;
            case DISCONNECT:
            	command = new DisconnectCommand(this.client);
            	break;
            case LOGIN:
            	command = new LoginCommand(this.client);
            	break;
            case LOGOUT:
            	command = new LogoutCommand(this.client);
            	break;
            case UPLOAD:
            	command = new UploadCommand(this.client);
            	break;
            case DOWNLOAD:
            	command = new DownloadCommand(this.client);
            	break;
            case MOVE_UP:
            	command = new MoveUpCommand(this.client);
            	break;
            case CHANGE_DIR:
            	command = new ChangeDirCommand(this.client);
            	break;
            case DELETE_FILE:
            	command = new DeleteFileCommand(this.client);
            	break;
            case LIST_FILES:
            	command = new ListFilesCommand(this.client);
            	break;
            case MAKE_DIR:
            	command = new MakeDirCommand(this.client);
            	break;
            case REMOVE_DIR:
            	command = new RemoveDirCommand(this.client);
            	break;
            case RENAME_FILE:
            	command = new RenameCommand(this.client);
            	break;
            default:
                result.setErrorMessage("Unsupported ProcessActionType for FTP: " + actionType);
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
            }

            // Execute FTPCommand
        	PropertyList returnProperties = command.execute(metadata, propertyList);
        	
        	result.setMessage("Executed FTP-action='" + actionType + "'");
            result.setReturnProperties(returnProperties);
            result.setActionStatus(ActionStatusType.EXECUTED);
            return result;
        } catch (FTPCommunicationException ex) {
            logger.error(ex);
            result.setErrorMessage("Could not execute FTP-command. Cause: " + ex.getMessage());
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (FTPException ex) {
        	String errorMessage = "Could not execute FTP-command. Cause: " + ex.getMessage();
            logger.error(errorMessage);
			result.setErrorMessage(errorMessage);
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (Exception ex) {
            logger.fatal(ex);
            result.setErrorMessage("Could not execute FTP-command. Cause: " + ex.toString());
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } finally {
        	
        	if (context.isTracingEnabled() && command != null) {
        		result.setActionTrace(command.getActionTrace());
        	}
        }
	}
	
	/**
     * Returns the metadata for the FTP command
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
