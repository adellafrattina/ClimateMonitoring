/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.sql.ResultSet;

import climatemonitoring.core.Database;

/**
 * The ServerDatabase hides the communication details
 * and the SQL queries between the server and the database.
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
interface ServerDatabase extends Database {

	/**
	 * Closes the connection between the application and the database
	 */
	public void shutdown();

	/**
	 * Executes an SQL statement and returns a result set
	 * 
	 * @param statement The statement that will get executed
	 * @return The ResultSet given after the execution of the statement
	 */
	public ResultSet execute(String statement);
}
