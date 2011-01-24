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
package org.nabucco.testautomation.engine.proxy.process.command.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.process.exeption.ShellScriptException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ExecuteShellScriptCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ExecuteShellScriptCommand extends AbstractShellScriptCommand {

	/**
	 * 
	 */
	public ExecuteShellScriptCommand() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertyList execute(Metadata metadata, PropertyList properties)
			throws ShellScriptException {
		
		String command = getCommand(metadata);

		try {
			this.setRequest(command);
			this.start();
			Process process = Runtime.getRuntime().exec(command);
			this.stop();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuffer rs = new StringBuffer();
			String line;
			
			while ((line = reader.readLine()) != null) {
				rs.append(line);
				rs.append("\r\n");
			}
			this.setResponse(rs.toString());
		} catch (IOException ex) {
			this.stop();
			this.setException(ex);
			throw new ShellScriptException(ex.getMessage());
		}
		
		return null;
	}
	
	private String getCommand(Metadata metadata) throws ShellScriptException {
		
		StringProperty prop = (StringProperty) PropertyHelper.getFromList(metadata.getPropertyList(), PropertyType.STRING, COMMAND);
		
		if (prop == null) {
			throw new ShellScriptException("No command defined in metadata '" + metadata.getName() + "'");
		}
		return prop.getValue().getValue();
	}

}
