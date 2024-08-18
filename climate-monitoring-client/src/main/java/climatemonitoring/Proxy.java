/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Database;

/**
 * The proxy interface is able to create the connection, closure and communication from the client to the server
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
interface Proxy extends Database{

	/**
	 * Allows the connection to the desired address.
	 * @return True if the connection was succesfull, false if not
	 * @param address the address the user wants to connect to
	 * @param port the port the user wants to connect to
	 */
	public boolean connect(String address, short port) throws ConnectionLostException;

	/**
	 * Sends a ping request. 
	 * @return The time (in milliseconds) elapsed between sending the ping packet and receiving it from the server
	 */
	public long ping() throws ConnectionLostException;

	/**
	 * Close the connection between the socket and the server
	 */
	public void close() throws ConnectionLostException;
	
}
