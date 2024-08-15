/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

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
	 * Return true if the connection was succesfull, false if not
	 * @param address the address the user wants to connect to
	 * @param port the port the user wants to connect to
	 */
	public boolean connect(String address, short port);

	/**
	 * Close the connection between the socket and the server
	 */
	public void close();
	
}
