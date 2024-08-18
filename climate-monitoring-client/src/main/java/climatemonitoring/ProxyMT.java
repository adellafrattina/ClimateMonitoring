/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.DatabaseMT;
import climatemonitoring.core.Result;

/**
 * The proxyMT interface is able to create the connection, closure and communication from the client to the server.
 * This interface contains functions that are the non-blocking version of the proxy methods.
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
interface ProxyMT extends DatabaseMT{
	
	/**
	 * Allows the connection to the desired address.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * Return true if the connection was succesfull, false if not
	 * @param address the address the user wants to connect to
	 * @param port the port the user wants to connect to
	 */
	public Result<Boolean> connect(String address, short port);

	/**
	 * Sends a ping request. 
	 * This method will be executed in another thread, so the method is non-blocking.
	 * @return The time (in milliseconds) elapsed between sending the ping packet and receiving it from the server
	 */
	public Result<Long> ping();
}
