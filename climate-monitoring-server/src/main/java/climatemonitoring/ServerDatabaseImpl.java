/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.sql.Connection;
import java.sql.ResultSet;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;

/**
 * The actual ServerDatabase implementation
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 * @see ServerDatabase
 */
class ServerDatabaseImpl implements ServerDatabase {

	/**
	 * The constructor is responsible for the database connection
	 * 
	 * @param url Database URL address
	 * @param username Username to log into the database
	 * @param password Password to log into the database
	 */
	public ServerDatabaseImpl(String url, String username, String password) {


	}

	/**
	 * Closes the connection between the application and the database
	 */
	public synchronized void shutdown() {


	}

	/**
	 * Executes an SQL statement and
	 * @return
	 */
	public synchronized ResultSet execute(String statement) {

		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	/**
	 * Returns in alphabetical order an array of areas which have a name that
	 * contains the input string.
	 * 
	 * For example, if the given string is 'var' then
	 * the output would be an array like this:
	 * 
	 * {
	 * 	"Varese",
	 * 	"Novarese",
	 * 	"Isola Dovarese",
	 * 	...
	 * }
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByName(String str) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByName'");
	}

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByCountry(String str) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCountry'");
	}

	/**
	 * Returns in alphabetical order an array of areas which can be
	 * found around the specified coordinates. The around is about 0.5
	 * degrees for both latitude and longitude
	 * 
	 * @param latitude Value between -90 and 90
	 * @param longitude Value between -180 and 180
	 * @return The result of the search as an array of areas
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByCoords(double latitude, double longitude)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCoords'");
	}

	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Parameter[] getParameters(int geoname_id, String center_id)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'getParameters'");
	}

	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Category[] getCategories() throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'getCategories'");
	}

	/**
	 * Adds an area to the database
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addArea(Area area) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addArea'");
	}

	/**
	 * Adds a center to the database
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addCenter(Center center) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addCenter'");
	}

	/**
	 * Adds an operator to the database
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addOperator(Operator operator) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addOperator'");
	}

	/**
	 * Adds a parameter to the database
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addParameter(Parameter parameter) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addParameter'");
	}

	/**
	 * Edits an existing operator
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean editOperator(String user_id, Operator operator)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'editOperator'");
	}

	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Operator validateCredentials(String user_id, String password)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'validateCredentials'");
	}

	/**
	 * The connection estabilished with the database through the JDBC DriverManager
	 */
	private Connection m_connection;
}
