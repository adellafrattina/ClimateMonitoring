/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The database interface is responsible for communication between the application
 * and the database. It allows to perform specific operations, but not to execute
 * new SQL queries. Every operation should throw a controlled exception when an
 * error occurs
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public interface Database {

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
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Area[] searchAreasByName(String str) throws Exception;

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Area[] searchAreasByCountry(String str) throws Exception;

	/**
	 * Returns in alphabetical order an array of areas which can be
	 * found around the specified coordinates. The around is about 0.5
	 * degrees for both latitude and longitude
	 * 
	 * @param latitude Value between -90 and 90
	 * @param longitude Value between -180 and 180
	 * @return The result of the search as an array of areas
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Area[] searchAreasByCoords(double latitude, double longitude) throws Exception;


	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Parameter[] getParameters(int geoname_id, String center_id) throws Exception;

	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Category[] getCategories() throws Exception;


	/**
	 * Adds an area to the database
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public boolean addArea(Area area) throws Exception;

	/**
	 * Adds a center to the database
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public boolean addCenter(Center center) throws Exception;

	/**
	 * Adds an operator to the database
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public boolean addOperator(Operator operator) throws Exception;

	/**
	 * Adds a parameter to the database
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public boolean addParameter(Parameter parameter) throws Exception;


	/**
	 * Edits an existing operator
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public boolean editOperator(String user_id, Operator operator) throws Exception;

	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 * @throws Exception If anything went wrong while executing the operation
	 */
	public Operator validateCredentials(String user_id, String password) throws Exception;
}
