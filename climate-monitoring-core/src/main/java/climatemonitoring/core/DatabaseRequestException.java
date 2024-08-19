/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.util.HashMap;

/**
 * Needs to be throw when a database query fails
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class DatabaseRequestException extends Exception {

	/**
	 * The only constructor to force the user to insert a message
	 * @param msg The desired message
	 */
	public DatabaseRequestException(HashMap<ErrorType, String> error_msgs) {

		m_errorMsgs = error_msgs;
	}

	/**
	 * To get a specific error message
	 * @param type The desired error message type
	 * @return The error message associated to the desired type
	 */
	public String getErrorMsg(ErrorType type) {

		return m_errorMsgs.get(type);
	}

	private HashMap<ErrorType, String> m_errorMsgs = new HashMap<ErrorType, String>();
}
