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
	 * @throws ConnectionLostException If the client loses connection during the operation
	 */
	public boolean connect(String address, short port) throws ConnectionLostException;

	/**
	 * Sends a ping request. 
	 * @return The time (in milliseconds) elapsed between sending the ping packet and receiving it from the server
	 * @throws ConnectionLostException If the client loses connection during the operation
	 */
	public long ping() throws ConnectionLostException;

	/**
	 * Closes the connection between the socket and the server
	 */
	public void close() throws ConnectionLostException;

	/**
	 * Forces the disconnection between the socket and the server
	 * @throws ConnectionLostException If the client loses connection during the operation
	 */
	public void forceClose() throws ConnectionLostException;
}
