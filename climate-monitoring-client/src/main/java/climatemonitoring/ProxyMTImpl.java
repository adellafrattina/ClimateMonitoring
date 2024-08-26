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
	 * To start a transaction
	 */
	@Override
	public Result<Boolean> begin() {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				m_proxy.begin();
				return null;
			}
		};
	}

	/**
	 * To end a transaction
	 */
	@Override
	public Result<Boolean> end() {

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				m_proxy.end();
				return null;
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
	 * Returns in alphabetical order an array of centers which have
	 * a name that contains the input string
	 * 
	 * For example, if the given string is "var" then the output
	 * would be an array like this:
	 * 
	 * {
	 * 	"Centro di Varese",
	 * 	"Centro di Novarese",
	 * 	"Centro di Isola Dovarese",
	 * 	...
	 * }
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of centers
	 */
	public Result<Center[]> searchCentersByName(String str) {

		return new Result<Center[]>() {
			public Center[] exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.searchCentersByName(str);
			}
		};
	}

	/**
	 * To get an area by its geoname id
	 * 
	 * @param geoname_id The geoname id of the area to be searched
	 * @return The area that corresponds to the given geoname id
	 */
	public Result<Area> getArea(int geoname_id) {

		return new Result<Area>() {
			public Area exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.getArea(geoname_id);
			}
		};
	}

	/**
	 * To get a center by its center id
	 * 
	 * @param center_id The center id of the center to be searched
	 * @return The center that corresponds to the given center id
	 */
	public Result<Center> getCenter(String center_id) {

		return new Result<Center>() {
			public Center exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.getCenter(center_id);
			}
		};
	}

	/**
	 * To get a center based on its address
	 * 
	 * @param city The city the center is located in
	 * @param street The street the center is located in
	 * @param house_number The house number the center is located in
	 * @return The center that corresponds to the given address
	 */
	@Override
	public Result<Center> getCenterByAddress(int city, String street, int house_number) {

		return new Result<Center>() {
			public Center exec() throws ConnectionLostException, DatabaseRequestException {
				return m_proxy.getCenterByAddress(city, street, house_number);
			}
		};
	}

	/**
	 * To get an operator based on their user id
	 * 
	 * @param user_id The operator's ID
	 * @return The operator whose ID corresponds with the given one
	 */
	@Override
	public Result<Operator> getOperator(String user_id) {

		return new Result<Operator>() {
			public Operator exec() throws ConnectionLostException, DatabaseRequestException {
				return m_proxy.getOperator(user_id);
			}
		};
	}

	/**
	 * To get an operator based on their SSID
	 * 
	 * @param ssid The operator's SSID
	 * @return The operator whose SSID corresponds with the given one
	 */
	@Override
	public Result<Operator> getOperatorBySSID(String ssid) {

		return new Result<Operator>() {
			public Operator exec() throws ConnectionLostException, DatabaseRequestException {
				return m_proxy.getOperatorBySSID(ssid);
			}
		};
	}

	/**
	 * To get an operator based on their email address
	 * 
	 * @param email The operator's email address
	 * @return The operator whose email address corresponds with the given one
	 */
	@Override
	public Result<Operator> getOperatorByEmail(String email) {

		return new Result<Operator>() {
			public Operator exec() throws ConnectionLostException, DatabaseRequestException {
				return m_proxy.getOperatorByEmail(email);
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
	 * To check if a monitoring center is monitoring an area
	 * 
	 * @param center_id The ID of the center
	 * @param geoname_id The ID of the area
	 * @return True if the area is being monitored by a center, false otherwise
	 */
	public Result<Boolean> monitors (String center_id, int geoname_id){

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.monitors(center_id, geoname_id);
			}
		};
	}

	/**
	 * To check if a monitoring center is employing an operator
	 * 
	 * @param center_id The ID of the center
	 * @param user_id The ID of the operator
	 * @return True if an operator is being employed by a center, false otherwise
	 */
	public Result<Boolean> employs (String center_id, String user_id){

		return new Result<Boolean>() {
			public Boolean exec() throws ConnectionLostException, DatabaseRequestException{
				return m_proxy.employs(center_id, user_id);
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
