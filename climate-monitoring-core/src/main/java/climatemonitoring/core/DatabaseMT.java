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
	 * To get a center by its center id
	 * 
	 * @param center_id The center id of the center to be searched
	 * @return The center that corresponds to the given center id
	 */
	public Result<Center> getCenter(String center_id);

	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 */
	public Result<Parameter[]> getParameters(int geoname_id, String center_id);

	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 */
	public Result<Category[]> getCategories();


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
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 */
	public Result<Operator> validateCredentials(String user_id, String password);
}
