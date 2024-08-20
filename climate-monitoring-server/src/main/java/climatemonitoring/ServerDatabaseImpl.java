/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.headless.Console;

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

		try {

			m_connection = DriverManager.getConnection(url, username, password);
			if (m_connection != null) Console.write("Connected");
		}

		catch (SQLException e) {

			Console.write("Invalid credentials: " + e.getMessage());
		}
	}

	/**
	 * Closes the connection between the application and the database
	 */
	public synchronized void shutdown() {

		if (m_connection != null)
			try {

				m_connection.close();
			}

			catch (SQLException e) {

				Console.write("Failed to close connection: " + e.getMessage());
			}
	}

	/**
	 * Executes an SQL statement and
	 * @throws SQLException If the query fails to execute
	 * @return
	 */
	public synchronized ResultSet execute(String statement) throws SQLException {

		PreparedStatement pst = m_connection.prepareStatement(statement);
		boolean isQuery = pst.execute();

		if (isQuery)
			return pst.getResultSet();

		else
			return null;
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
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByName(String str) throws ConnectionLostException, DatabaseRequestException {

		try {
			
			ResultSet query = execute("SELECT * FROM area WHERE LOWER(area_name) LIKE '%" + str + "%' ORDER BY CASE WHEN LOWER(area_name) LIKE '" + str + "%' THEN 0 ELSE 1 END, POSITION('" + str + "' IN LOWER(area_name)), area_name;");
			Area[] result = new Area[query.getFetchSize()]; 

			int i = 0;
			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[i++] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByCountry(String str) throws ConnectionLostException, DatabaseRequestException {

		
		try {
			
			ResultSet query = execute("SELECT * FROM area WHERE LOWER(country_name) LIKE '%" + str + "%' ORDER BY CASE WHEN LOWER(country_name) LIKE '" + str + "%' THEN 0 ELSE 1 END, POSITION('" + str + "' IN LOWER(country_name)), country_name;");
			Area[] result = new Area[query.getFetchSize()];

			int i = 0;
			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[i++] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Returns in alphabetical order an array of areas which can be
	 * found around the specified coordinates. The around is about 0.5
	 * degrees for both latitude and longitude
	 * 
	 * @param latitude Value between -90 and 90
	 * @param longitude Value between -180 and 180
	 * @return The result of the search as an array of areas
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByCoords(double latitude, double longitude)
			throws ConnectionLostException, DatabaseRequestException {

		try {

			ResultSet query = execute("SELECT * FROM area WHERE latitude BETWEEN " + (latitude - 0.5) + " AND " + (latitude + 0.5) + " AND longitude BETWEEN " + (longitude - 0.5) + " AND " + (longitude + 0.5) + " ORDER BY area_name;");
			Area[] result = new Area[query.getFetchSize()];

			int i = 0;
			while (query.next()) {
		
				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[i++] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Parameter[] getParameters(int geoname_id, String center_id)
			throws ConnectionLostException, DatabaseRequestException {

		try {

			ResultSet query = execute("SELECT * FROM parameter WHERE geoname_id = " + geoname_id + " AND center_id = '" + center_id + "';");
			Parameter[] result = new Parameter[query.getFetchSize()];

			int i = 0;
			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String centerID = query.getString("center_id");
				String timestamp = query.getString("rec_timestamp");
				String categoryID = query.getString("category_id");
				String userID = query.getString("user_id");
				int score = query.getInt("score");
				String notes = query.getString("notes");

				String[] parts = timestamp.split(" ");
				String date = parts[0];
				String time = parts[1];

				result[i++] = new Parameter(geonameID, centerID, userID, categoryID, date, time, score, notes);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Category[] getCategories() throws ConnectionLostException, DatabaseRequestException {

		try {

			ResultSet query = execute("SELECT * FROM categories;");
			Category[] result = new Category[query.getFetchSize()];
		
			int i = 0;
			while (query.next()) {

				String categoryID = query.getString("category_id");
				String explanation = query.getString("explanation");

				result[i++] = new Category(categoryID, explanation);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Adds an area to the database
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addArea(Area area) throws ConnectionLostException, DatabaseRequestException {

		try {

			int geonameID = area.getGeonameID();
			String areaName = area.getName();
			String areaAsciiName = area.getAsciiName();
			String countryCode = area.getCountryCode();
			String countryName = area.getCountryName();
			double latitude = area.getLatitude();
			double longitude = area.getLongitude();

			execute("INSERT INTO area (geoname_id, area_name, area_ascii_name, country_code, country_name, latitude, longitude) VALUES (" + geonameID + ", " + areaName + ", " + areaAsciiName + ", " + countryCode + ", " + countryName + ", " + latitude + ", " + longitude + ");");
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Adds a center to the database
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addCenter(Center center) throws ConnectionLostException, DatabaseRequestException {

		try {

			String centerID = center.getCenterID();
			String city = center.getCity();
			String street = center.getStreet();
			int houseNumber = center.getHouseNumber();
			int postalCode = center.getPostalCode();
			String district = center.getDistrict();

			execute("INSERT INTO center (center_id, city, street, house_number, postal_code, district) VALUES (" + centerID + ", " + city + ", " + street + ", " + houseNumber + ", " + postalCode + ", " + district + ");");
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Adds an operator to the database
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addOperator(Operator operator) throws ConnectionLostException, DatabaseRequestException {

		try {

			String userID = operator.getUserID();
			String SSID = operator.getSSID().toString();
			String operatorSurname = operator.getSurname();
			String operatorName = operator.getName();
			String email = operator.getEmail();
			String password = operator.getPassword();
			String centerID = operator.getCenterID();

			execute("INSERT INTO operator (user_id, ssid, operator_surname, operator_name, email, password, center_id) VALUES (" + userID + ", " + SSID + ", " + operatorSurname + ", " + operatorName + ", " + email + ", " + password + ", " + centerID + ");");
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Adds a parameter to the database
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean addParameter(Parameter parameter) throws ConnectionLostException, DatabaseRequestException {

		try {

			int geonameID = parameter.getGeonameID();
			String centerID = parameter.getCenterID();
			String time = parameter.getTime();
			String date = parameter.getDate();
			String timestamp = time + " " + date;
			String categoryID = parameter.getCategory();
			String userID = parameter.getUserID();
			int score = parameter.getScore();
			String notes = parameter.getNotes();

			execute("INSERT INTO parameter (geoname_id, center_id, rec_timestamp, category_id, user_id, score, notes) VALUES (" + geonameID + ", " + centerID + ", " + timestamp + ", " + categoryID + ", " + userID + ", " + score + ", " + notes + ");");
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Edits an existing operator
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized boolean editOperator(String user_id, Operator operator)
			throws ConnectionLostException, DatabaseRequestException {

		try {

			String SSID = operator.getSSID().toString();
			String operatorSurname = operator.getSurname();
			String operatorName = operator.getName();
			String email = operator.getEmail();
			String password = operator.getPassword();
			String centerID = operator.getCenterID();

			execute("IF EXISTS (SELECT user_id FROM operator WHERE user_id = " + user_id + ") THEN UPDATE OPERATOR SET ssid = '" + SSID + "', operator_surname = '" + operatorSurname + "', operator_name = '" + operatorName + "', email = '" + email + "', password = '" + password + "', center_id = '" + centerID + "' END IF;");
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 * @throws ConnectionLostException If the server loses connection to the database during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Operator validateCredentials(String user_id, String password)
			throws ConnectionLostException, DatabaseRequestException {

		try {

			ResultSet query = execute("SELECT * FROM operator WHERE user_id = " + user_id + " AND password = '" + password + "'");
			Operator result = null;

			if (query.next()) {

				String userID = query.getString("user_id");
				char[] SSID = query.getString("ssid").toCharArray();
				String operatorSurname = query.getString("operator_surname");
				String operatorName = query.getString("operator_name");
				String email = query.getString("email");
				String pwd = query.getString("password");
				String centerID = query.getString("center_id");

				result = new Operator(userID, SSID, operatorSurname, operatorName, email, pwd, centerID);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * The connection estabilished with the database through the JDBC DriverManager
	 */
	private Connection m_connection;
}
