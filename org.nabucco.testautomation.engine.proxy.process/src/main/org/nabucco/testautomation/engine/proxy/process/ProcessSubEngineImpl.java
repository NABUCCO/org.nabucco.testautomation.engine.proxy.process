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
package org.nabucco.testautomation.engine.proxy.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.SubEngineOperationType;
import org.nabucco.testautomation.engine.proxy.base.AbstractSubEngine;
import org.nabucco.testautomation.engine.proxy.exception.SubEngineException;
import org.nabucco.testautomation.engine.proxy.process.ProcessActionType;
import org.nabucco.testautomation.engine.proxy.process.ProcessEngine;
import org.nabucco.testautomation.engine.proxy.process.ProcessEngineOperationType;
import org.nabucco.testautomation.engine.proxy.process.command.ftp.FTPImpl;
import org.nabucco.testautomation.engine.proxy.process.command.script.ShellScriptImpl;
import org.nabucco.testautomation.engine.proxy.process.ftp.FTP;
import org.nabucco.testautomation.engine.proxy.process.scheduledtask.ScheduledTask;
import org.nabucco.testautomation.engine.proxy.process.script.ShellScript;

import org.nabucco.testautomation.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ProcessSubEngineImpl
 *
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ProcessSubEngineImpl extends AbstractSubEngine implements
		ProcessEngine {

	private static final long serialVersionUID = 1L;

	private static final NBCTestLogger logger = NBCTestLoggingFactory
			.getInstance().getLogger(ProcessSubEngineImpl.class);

	private ShellScript script;
	
	private FTP ftp;
	
	private ScheduledTask scheduledTask;

	public ProcessSubEngineImpl(FTPClient ftpClient)
			throws NBCTestConfigurationException {
		this.ftp = new FTPImpl(ftpClient);
		this.script = new ShellScriptImpl();
	}

	@Override
	public ActionResponse executeSubEngineOperation(
			SubEngineOperationType operationType, SubEngineActionType actionType,
			List<Metadata> metadataList, PropertyList propertyList,
			TestContext context) throws SubEngineException {

		ProcessEngineOperationType processEngineOperationType = (ProcessEngineOperationType) operationType;

		// execute operation
		switch (processEngineOperationType) {

		case SHELL_SCRIPT: {
			return this.getScript().execute(context, propertyList, metadataList,
					actionType);
		}
		case FTP_SERVER:
		case FTP_DIRECTORY:
		case FTP_FILE: 
		    return this.getFTP().execute(context, propertyList, metadataList,
                    actionType);
		
		case SCHEDULEDTASK:
			// TODO new implementation or refactor existing one
		    return getScheduledTask().execute(context, propertyList, metadataList,
                    actionType);
		
		default:
			String error = "Unsupported ProcessEngineOperationType = '"
					+ operationType + "'";
			logger.error(error);
			throw new UnsupportedOperationException(error);
		
		}
	}

	@Override
	public ShellScript getScript() {
		return this.script;
	}

	@Override
	public FTP getFTP() {
	    return this.ftp;
	}
	
	@Override
	public ScheduledTask getScheduledTask() {
	    return scheduledTask;
	}

	@Override
	public Map<String, SubEngineActionType> getActions() {
		Map<String, SubEngineActionType> actions = new HashMap<String, SubEngineActionType>();

		for (ProcessActionType action : ProcessActionType.values()) {
			actions.put(action.toString(), action);
		}
		return actions;
	}

	@Override
	public Map<String, SubEngineOperationType> getOperations() {
		Map<String, SubEngineOperationType> operations = new HashMap<String, SubEngineOperationType>();

		for (ProcessEngineOperationType operation : ProcessEngineOperationType
				.values()) {
			operations.put(operation.toString(), operation);
		}
		return operations;
	}

	@Override
	public SubEngineType getType() {
		return SubEngineType.PROCESS;
	}

}
