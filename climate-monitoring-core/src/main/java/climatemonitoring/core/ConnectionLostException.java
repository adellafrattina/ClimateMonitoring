/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Needs to be thrown when an IOException gets thrown by a networking function
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class ConnectionLostException extends Exception {

	/**
	 * To create a connection lost exception with a message
	 * @param msg The desired message
	 */
	public ConnectionLostException(String msg) {

		super(msg);
	}

	/**
	 * To create a connection lost exception without a message
	 */
	public ConnectionLostException() {


	}
}
