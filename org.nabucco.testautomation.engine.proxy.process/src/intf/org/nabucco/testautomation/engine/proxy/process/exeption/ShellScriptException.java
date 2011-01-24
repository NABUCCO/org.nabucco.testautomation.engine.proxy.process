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
package org.nabucco.testautomation.engine.proxy.process.exeption;

/**
 * ShellScriptException
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ShellScriptException extends ProcessEngineException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ShellScriptException() {
	}

	/**
	 * @param cause
	 */
	public ShellScriptException(Exception cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ShellScriptException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ShellScriptException(String message) {
		super(message);
	}

}
