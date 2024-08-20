/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

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
	public DatabaseRequestException(String msg) {

		super(msg);
	}
}
