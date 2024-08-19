/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.Result;

/**
 * The proxyMT class is able to create the connection, closure and communication from the client to the server.
 * This class contains functions that are the non-blocking version of the proxy methods.
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class ProxyMTImpl implements ProxyMT {

	/**
	 * Accepts as input the original non-threaded proxy
	 * @param proxy
	 */
	public ProxyMTImpl(Proxy proxy) {
		
		m_proxy = proxy;
	}
	
	/**
	 * Allows the connection to the desired address.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * Return true if the connection was succesfull, false if not
	 * @param address the address the user wants to connect to
	 * @param port the port the user wants to connect to
	 */
	@Override
	public Result<Boolean> connect(String address, short port) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException{
				return m_proxy.connect(address, port);
			}
		};
	}

	/**
	 * Returns in alphabetical order an array of areas which have a name that
	 * contains the input string.
	 * 
	 * This method will be executed in another thread, so the method is non-blocking.
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
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 */
	@Override
	public Result<Area[]> searchAreasByName(String str) {

		return new Result<Area[]>() {
			public Area[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.searchAreasByName(str);
			}
		};
	}

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 */
	@Override
	public Result<Area[]> searchAreasByCountry(String str) {

		return new Result<Area[]>() {
			public Area[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.searchAreasByCountry(str);
			}
		};
	}

	/**
	 * Returns in alphabetical order an array of areas which can be
	 * found around the specified coordinates. The around is about 0.5
	 * degrees for both latitude and longitude.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param latitude Value between -90 and 90
	 * @param longitude Value between -180 and 180
	 * @return The result of the search as an array of areas
	 */
	@Override
	public Result<Area[]> searchAreasByCoords(double latitude, double longitude) {

		return new Result<Area[]>() {
			public Area[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.searchAreasByCoords(latitude, longitude);
			}
		};
	}

	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 */
	@Override
	public Result<Parameter[]> getParameters(int geoname_id, String center_id) {

		return new Result<Parameter[]>() {
			public Parameter[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.getParameters(geoname_id, center_id);
			}
		};
	}

	/**
	 * Get all the categories and their explanation.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @return An array of all categories with relative descriptions
	 */
	@Override
	public Result<Category[]> getCategories() {

		return new Result<Category[]>() {
			public Category[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.getCategories();
			}
		};
	}

	/**
	 * Adds an area to the database.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	@Override
	public Result<Boolean> addArea(Area area) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.addArea(area);
			}
		};
	}

	/**
	 * Adds a center to the database.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	@Override
	public Result<Boolean> addCenter(Center center) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.addCenter(center);
			}
		};
	}

	/**
	 * Adds an operator to the database.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	@Override
	public Result<Boolean> addOperator(Operator operator) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.addOperator(operator);
			}
		};
	}

	/**
	 * Adds a parameter to the database.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	@Override
	public Result<Boolean> addParameter(Parameter parameter) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.addParameter(parameter);
			}
		};
	}

	/**
	 * Edits an existing operator.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 */
	@Override
	public Result<Boolean> editOperator(String user_id, Operator operator) {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.editOperator(user_id, operator);
			}
		};
	}

	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise.
	 * This method will be executed in another thread, so the method is non-blocking.
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 */
	@Override
	public Result<Operator> validateCredentials(String user_id, String password) {

		return new Result<Operator>() {
			public Operator exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.validateCredentials(user_id, password);
			}
		};
	}

	/**
	 * Sends a ping request. 
	 * This method will be executed in another thread, so the method is non-blocking.
	 * @return The time (in milliseconds) elapsed between sending the ping packet and receiving it from the server
	 */
	@Override
	public Result<Long> ping() {

		return new Result<Long>() {
			public Long exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.ping();
			}
		};
	}

	private Proxy m_proxy;

}
