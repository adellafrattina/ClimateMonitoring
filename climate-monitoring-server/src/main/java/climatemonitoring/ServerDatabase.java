/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.sql.ResultSet;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;

/**
 * The ServerDatabase hides the communication details
 * and the SQL queries between the server and the database.
 * 
 * Its methods must be synchronized and throw ConnectionLostException
 * and DatabaseRequestException
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
interface ServerDatabase {

	/**
	 * Closes the connection between the application and the database
	 * 
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public void shutdown() throws ConnectionLostException, DatabaseRequestException;

	/**
	 * Executes an SQL statement and returns a result set
	 * 
	 * @param statement The statement that will get executed
	 * @return The ResultSet given after the execution of the statement
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public ResultSet execute(String statement) throws ConnectionLostException, DatabaseRequestException;
}
