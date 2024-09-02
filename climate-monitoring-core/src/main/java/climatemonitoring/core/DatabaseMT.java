/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The database multithreaded interface has the same responsibilities
 * as the non-threaded interface (Database). Every operation returns
 * a Result<T> type
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 * @see Result
 */
public interface DatabaseMT {

	/**
	 * To start a transaction
	 * 
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> begin();

	/**
	 * To end a transaction
	 * 
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> end();

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
	 */
	public Result<Area[]> searchAreasByName(String str);

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 */
	public Result<Area[]> searchAreasByCountry(String str);

	/**
	 * Returns in alphabetical order an array of areas which can be
	 * found around the specified coordinates. The around is about 0.5
	 * degrees for both latitude and longitude
	 * 
	 * @param latitude Value between -90 and 90
	 * @param longitude Value between -180 and 180
	 * @return The result of the search as an array of areas
	 */
	public Result<Area[]> searchAreasByCoords(double latitude, double longitude);

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
	public Result<Center[]> searchCentersByName(String str);


	/**
	 * To get an area by its geoname id
	 * 
	 * @param geoname_id The geoname id of the area to be searched
	 * @return The area that corresponds to the given geoname id
	 */
	public Result<Area> getArea(int geoname_id);

	/**
	 * To get all the areas monitored by a specified center
	 * 
	 * @param center_id The id of the center the search is based on
	 * @return The areas monitored as an array of areas
	 */
	public Result<Area[]> getMonitoredAreas(String center_id);

	/**
	 * To get a center by its center id
	 * 
	 * @param center_id The center id of the center to be searched
	 * @return The center that corresponds to the given center id
	 */
	public Result<Center> getCenter(String center_id);

	/**
	 * To get all the available centers
	 * 
	 * @return All the centers as an array of centers
	 */
	public Result<Center[]> getCenters();

	/**
	 * To get a center based on its address
	 * 
	 * @param city The city the center is located in
	 * @param street The street the center is located in
	 * @param house_number The house number the center is located in
	 * @return The center that corresponds to the given address
	 */
	public Result<Center> getCenterByAddress(int city, String street, int house_number);

	/**
	 * To get the center with the most recent recording about the specified area
	 * 
	 * @param geoname_id The area's id
	 * @return The latest center that submitted a recording for the given area
	 */
	public Result<Center> getLatestCenter(int geoname_id);

	/**
	 * To get all the centers that monitor a specified area
	 * 
	 * @param geoname_id The id of the area the search is based on
	 * @return The centers associated as an array of centers
	 */
	public Result<Center[]> getAssociatedCenters(int geoname_id);

	/**
	 * To get an operator based on their user id
	 * 
	 * @param user_id The operator's ID
	 * @return The operator whose ID corresponds with the given one
	 */
	public Result<Operator> getOperator(String user_id);

	/**
	 * To get an operator based on their SSID
	 * 
	 * @param ssid The operator's SSID
	 * @return The operator whose SSID corresponds with the given one
	 */
	public Result<Operator> getOperatorBySSID(String ssid);

	/**
	 * To get an operator based on their email address
	 * 
	 * @param email The operator's email address
	 * @return The operator whose email address corresponds with the given one
	 */
	public Result<Operator> getOperatorByEmail(String email);

	/**
	 * Returns an array containing parameters about a specified area that were recorded
	 * by the desired center about a specific category
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @param category The parameter's category
	 * @return The result of the search as an array of parameters
	 */
	public Result<Parameter[]> getParameters(int geoname_id, String center_id, String category);

	/**
	 * To get the average about the score of a specific area,
	 * of a specific center about a specific category
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @param category The parameter's category
	 * @return The average of the scores as a double
	 */
	public Result<Double> getParametersAverage(int geoname_id, String center_id, String category);

	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 */
	public Result<Category[]> getCategories();

	/**
	 * To get the category with the most recent recording about the specified area in the specified center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The latest category of the given center for the given area
	 */
	public Result<Category> getLatestCategory(int geoname_id, String center_id);


	/**
	 * Adds an area to the database
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> addArea(Area area);

	/**
	 * Adds a center to the database
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> addCenter(Center center);

	/**
	 * Adds an operator to the database
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> addOperator(Operator operator);

	/**
	 * Adds a parameter to the database
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> addParameter(Parameter parameter);


	/**
	 * Edits an existing operator
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> editOperator(String user_id, Operator operator);

	/**
	 * To add an existing area to a specified center
	 * 
	 * @param geoname_id The area to be added in the center
	 * @param center_id The center the area needs to be added in
	 * @return Success or failure of the operation
	 */
	public Result<Boolean> includeAreaToCenter(int geoname_id, String center_id) throws ConnectionLostException, DatabaseRequestException;

	/**
	 * To check if a monitoring center is monitoring an area
	 * 
	 * @param center_id The ID of the center
	 * @param geoname_id The ID of the area
	 * @return True if the area is being monitored by a center, false otherwise
	 */
	public Result<Boolean> monitors(String center_id, int geoname_id);

	/**
	 * To check if a monitoring center is employing an operator
	 * 
	 * @param center_id The ID of the center
	 * @param user_id The ID of the operator
	 * @return True if an operator is being employed by a center, false otherwise
	 */
	public Result<Boolean> employs(String center_id, String user_id);

	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 */
	public Result<Operator> validateCredentials(String user_id, String password);
}
