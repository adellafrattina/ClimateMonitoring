/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
public class ServerDatabaseImpl implements ServerDatabase {

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
			if (m_connection != null) Console.info("Connected");
		}

		catch (SQLException e) {

			Console.error("Invalid credentials: " + e.getMessage());
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

				Console.error("Failed to close connection: " + e.getMessage());
			}
	}

	/**
	 * Executes an SQL statement and
	 * @throws SQLException If the query fails to execute
	 * @return The query's rows as a ResultSet
	 */
	public synchronized ResultSet execute(String statement) throws SQLException {

		PreparedStatement pst = m_connection.prepareStatement(statement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		Console.debug("Issued query: " + statement);
		boolean isQuery = pst.execute();

		if (isQuery)
			return pst.getResultSet();

		else
			return null;
	}

	/**
	 * To start a transaction
	 * 
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized void begin() throws ConnectionLostException, DatabaseRequestException {

		try {

			m_connection.setAutoCommit(false);
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To end a transaction
	 * 
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized void end() throws ConnectionLostException, DatabaseRequestException {

		try {

			m_connection.commit();
			m_connection.setAutoCommit(true);
		}

		catch (SQLException e) {

			try {

				m_connection.rollback();
				m_connection.setAutoCommit(true);
			}

			catch (SQLException ex) {

				try {

					m_connection.setAutoCommit(true);
				}

				catch (SQLException exc) {

					throw new DatabaseRequestException(exc.getMessage());
				}

				throw new DatabaseRequestException(e.getMessage());
			}

			throw new DatabaseRequestException(e.getMessage());
		}
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

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM area
				WHERE LOWER(area_name)
				LIKE ?
				ORDER BY CASE
				WHEN LOWER(area_name) LIKE ? THEN 0
				ELSE 1
				END,
				POSITION(? IN LOWER(area_name)), area_name;
			""");

			pst.setString(1, "%" + str + "%");
			pst.setString(2, str + "%");
			pst.setString(3, str);
			
			ResultSet query = pst.executeQuery();
			Area[] result = new Area[getRowCount(query)];

			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[query.getRow() - 1] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
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
			
			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM area
				WHERE LOWER(country_name)
				LIKE ?
				ORDER BY CASE
				WHEN LOWER(country_name) LIKE ? THEN 0
				ELSE 1
				END,
				POSITION(? IN LOWER(country_name)), country_name;
			""");

			pst.setString(1, "%" + str + "%");
			pst.setString(2, str + "%");
			pst.setString(3, str);
			
			ResultSet query = pst.executeQuery();
			Area[] result = new Area[getRowCount(query)];

			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[query.getRow() - 1] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
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

			PreparedStatement pst = prepareStatement("""
					SELECT *
					FROM area
					WHERE latitude
					BETWEEN ?
					AND ?
					AND longitude
					BETWEEN ?
					AND ?
					ORDER BY area_name;
			""");

			pst.setDouble(1, latitude - 0.5);
			pst.setDouble(2, latitude + 0.5);
			pst.setDouble(3, longitude - 0.5);
			pst.setDouble(4, longitude + 0.5);

			ResultSet query = pst.executeQuery();
			Area[] result = new Area[getRowCount(query)];

			while (query.next()) {
		
				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[query.getRow() - 1] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
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
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Center[] searchCentersByName(String str) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM center
				WHERE LOWER(center_id)
				LIKE ?
				ORDER BY CASE
				WHEN LOWER(center_id) LIKE ? THEN 0
				ELSE 1
				END,
				POSITION(? IN LOWER(center_id)), center_id;
			""");

			pst.setString(1, "%" + str + "%");
			pst.setString(2, str + "%");
			pst.setString(3, str);

			ResultSet query = pst.executeQuery();
			Center[] result = new Center[getRowCount(query)];

			while (query.next()) {

				String centerID = query.getString("center_id");
				int city = query.getInt("city");
				String street = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result[query.getRow() - 1] = new Center(centerID, street, houseNumber, postalCode, city, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get an area by its geoname id
	 * 
	 * @param geoname_id The geoname id of the area to be searched
	 * @return The area that corresponds to the given geoname id
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area getArea(int geoname_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM area
				WHERE geoname_id = ?;
			""");

			pst.setInt(1, geoname_id);

			ResultSet query = pst.executeQuery();
			Area result = null;

			if (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get all the areas monitored by a specified center
	 * 
	 * @param center_id The id of the center the search is based on
	 * @return The areas monitored as an array of areas
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] getMonitoredAreas(String center_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT A.*
				FROM area A
				JOIN monitors M
				ON A.geoname_id = M.geoname_id
				WHERE LOWER(center_id) = LOWER(?);
			""");

			pst.setString(1, center_id);

			ResultSet query = pst.executeQuery();
			Area[] result = new Area[getRowCount(query)];

			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String areaName = query.getString("area_name");
				String areaAsciiName = query.getString("area_ascii_name");
				String countryCode = query.getString("country_code");
				String countryName = query.getString("country_name");
				double coordsLatitude = query.getDouble("latitude");
				double coordsLongitude = query.getDouble("longitude");

				result[query.getRow() - 1] = new Area(geonameID, areaName, areaAsciiName, countryCode, countryName, coordsLatitude, coordsLongitude);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get a center by its center id
	 * 
	 * @param center_id The center id of the center to be searched
	 * @return The center that corresponds to the given center id
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Center getCenter(String center_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM center
				WHERE LOWER(center_id) = LOWER(?);
			""");

			pst.setString(1, center_id);

			ResultSet query = pst.executeQuery();
			Center result = null;

			if (query.next()) {

				String centerID = query.getString("center_id");
				int city = query.getInt("city");
				String street = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result = new Center(centerID, street, houseNumber, postalCode, city, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get all the available centers
	 * 
	 * @return All the centers as an array of centers
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public Center[] getCenters() throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM center;
			""");

			ResultSet query = pst.executeQuery();
			Center[] result = new Center[getRowCount(query)];

			while (query.next()) {

				String centerID = query.getString("center_id");
				int city = query.getInt("city");
				String street = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result[query.getRow() - 1] = new Center(centerID, street, houseNumber, postalCode, city, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get a center based on its address
	 * 
	 * @param city The city the center is located in
	 * @param street The street the center is located in
	 * @param house_number The house number the center is located in
	 * @return The center that corresponds to the given address
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public Center getCenterByAddress(int city, String street, int house_number) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM center
				WHERE city = ?
				AND LOWER(street) = LOWER(?)
				AND house_number = ?;
			""");

			pst.setInt(1, city);
			pst.setString(2, street);
			pst.setInt(3, house_number);

			ResultSet query = pst.executeQuery();
			Center result = null;

			if (query.next()) {

				String centerID = query.getString("center_id");
				int centerCity = query.getInt("city");
				String centerStreet = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result = new Center(centerID, centerStreet, houseNumber, postalCode, centerCity, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get the center with the most recent recording about the specified area
	 * 
	 * @param geoname_id The area's id
	 * @return The latest center that submitted a recording for the given area
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public Center getLatestCenter(int geoname_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT C.*
				FROM center C
				JOIN parameter P
				ON C.center_id = P.center_id
				WHERE P.geoname_id = ?
				ORDER BY P.rec_timestamp DESC
				LIMIT 1;
			""");

			pst.setInt(1, geoname_id);
			
			ResultSet query = pst.executeQuery();
			Center result = null;

			if (query.next()) {

				String centerID = query.getString("center_id");
				int centerCity = query.getInt("city");
				String centerStreet = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result = new Center(centerID, centerStreet, houseNumber, postalCode, centerCity, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		} 
	}

	/**
	 * To get all the centers that monitor a specified area
	 * 
	 * @param geoname_id The id of the area the search is based on
	 * @return The centers associated as an array of centers
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public Center[] getAssociatedCenters(int geoname_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT C.*
				FROM monitors M
				JOIN center C
				ON M.center_id = C.center_id
				WHERE geoname_id = ?;
			""");

			pst.setInt(1, geoname_id);
			
			ResultSet query = pst.executeQuery();
			Center[] result = new Center[getRowCount(query)];

			while (query.next()) {

				String centerID = query.getString("center_id");
				int centerCity = query.getInt("city");
				String centerStreet = query.getString("street");
				int houseNumber = query.getInt("house_number");
				int postalCode = query.getInt("postal_code");
				String district = query.getString("district");

				result[query.getRow() - 1] = new Center(centerID, centerStreet, houseNumber, postalCode, centerCity, district);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get an operator based on their user id
	 * 
	 * @param user_id The operator's ID
	 * @return The operator whose ID corresponds with the given one
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public Operator getOperator(String user_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM operator
				WHERE user_id = ?;
			""");

			pst.setString(1, user_id);

			ResultSet query = pst.executeQuery();
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
	 * To get an operator based on their SSID
	 * 
	 * @param ssid The operator's SSID
	 * @return The operator whose SSID corresponds with the given one
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public Operator getOperatorBySSID(String ssid) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM operator
				WHERE ssid = ?;
			""");

			pst.setString(1, ssid);

			ResultSet query = pst.executeQuery();
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
	 * To get an operator based on their email address
	 * 
	 * @param email The operator's email address
	 * @return The operator whose email address corresponds with the given one
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public Operator getOperatorByEmail(String email) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM operator
				WHERE email = ?;
			""");

			pst.setString(1, email);

			ResultSet query = pst.executeQuery();
			Operator result = null;

			if (query.next()) {

				String userID = query.getString("user_id");
				char[] SSID = query.getString("ssid").toCharArray();
				String operatorSurname = query.getString("operator_surname");
				String operatorName = query.getString("operator_name");
				String operatorEmail = query.getString("email");
				String pwd = query.getString("password");
				String centerID = query.getString("center_id");

				result = new Operator(userID, SSID, operatorSurname, operatorName, operatorEmail, pwd, centerID);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * Returns an array containing parameters about a specified area that were recorded
	 * by the desired center about a specific category
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @param category The parameter's category
	 * @return The result of the search as an array of parameters
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Parameter[] getParameters(int geoname_id, String center_id, String category) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM parameter
				WHERE geoname_id = ?
				AND LOWER(center_id) = LOWER(?)
				AND LOWER(category_id) = LOWER(?);
			""");

			pst.setInt(1, geoname_id);
			pst.setString(2, center_id);
			pst.setString(3, category);
			
			ResultSet query = pst.executeQuery();
			Parameter[] result = new Parameter[getRowCount(query)];

			while (query.next()) {

				int geonameID = query.getInt("geoname_id");
				String centerID = query.getString("center_id");
				Timestamp timestamp = query.getTimestamp("rec_timestamp");
				String categoryID = query.getString("category_id");
				String userID = query.getString("user_id");
				int score = query.getInt("score");
				String notes = query.getString("notes");

				result[query.getRow() - 1] = new Parameter(geonameID, centerID, userID, categoryID, timestamp.toLocalDateTime(), score, notes);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get the average about the score of a specific area,
	 * of a specific center about a specific category
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @param category The parameter's category
	 * @return The average of the scores as a double
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized double getParametersAverage(int geoname_id, String center_id, String category) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT AVG(score)
				FROM parameter
				WHERE geoname_id = ?
				AND LOWER(center_id) = LOWER(?)
				AND LOWER(category_id) = LOWER(?);
			""");

			pst.setInt(1, geoname_id);
			pst.setString(2, center_id);
			pst.setString(3, category);
			
			ResultSet query = pst.executeQuery();
			double result = 0.0;

			if (query.next())
				result = query.getDouble("avg");

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

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM category;
			""");
			
			ResultSet query = pst.executeQuery();
			Category[] result = new Category[getRowCount(query)];
		
			while (query.next()) {

				String categoryID = query.getString("category_id");
				String explanation = query.getString("explanation");

				result[query.getRow() - 1] = new Category(categoryID, explanation);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To get the category with the most recent recording about the specified area in the specified center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The latest category of the given center for the given area
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public Category getLatestCategory(int geoname_id, String center_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM parameter P
				JOIN category C
				ON C.category_id = P.category_id
				WHERE P.geoname_id = ?
				AND LOWER(P.center_id) = LOWER(?)
				ORDER BY P.rec_timestamp DESC
				LIMIT 1;
			""");

			pst.setInt(1, geoname_id);
			pst.setString(2, center_id);
			
			ResultSet query = pst.executeQuery();
			Category result = null;
		
			if (query.next()) {

				String categoryID = query.getString("category_id");
				String explanation = query.getString("explanation");

				result = new Category(categoryID, explanation);
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

			PreparedStatement pst = prepareStatement("""
				INSERT INTO area (geoname_id, area_name, area_ascii_name, country_code, country_name, latitude, longitude)
				VALUES (?, ?, ?, ?, ?, ?, ?);
			""");

			pst.setInt(1, geonameID);
			pst.setString(2, areaName);
			pst.setString(3, areaAsciiName);
			pst.setString(4, countryCode);
			pst.setString(5, countryName);
			pst.setDouble(6, latitude);
			pst.setDouble(7, longitude);

			pst.executeUpdate();
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
			int city = center.getCity();
			String street = center.getStreet();
			int houseNumber = center.getHouseNumber();
			int postalCode = center.getPostalCode();
			String district = center.getDistrict();

			PreparedStatement pst = null;

			pst = prepareStatement("""
				INSERT INTO center (center_id, city, street, house_number, postal_code, district)
				VALUES (LOWER(?), ?, LOWER(?), ?, ?, ?);
			""");

			pst.setString(1, centerID);
			pst.setInt(2, city);
			pst.setString(3, street);
			pst.setInt(4, houseNumber);
			pst.setInt(5, postalCode);
			pst.setString(6, district == null ? "null" : district);

			pst.executeUpdate();

			pst = prepareStatement("""
				INSERT INTO monitors (center_id, geoname_id)
				VALUES (LOWER(?), ?);
			""");

			pst.setString(1, centerID);
			pst.setInt(2, city);

			pst.executeUpdate();
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
			String SSID = new String(operator.getSSID());
			String operatorSurname = operator.getSurname();
			String operatorName = operator.getName();
			String email = operator.getEmail();
			String password = hashPassword(operator.getPassword());
			String centerID = operator.getCenterID();

			PreparedStatement pst = prepareStatement("""
				INSERT INTO operator (user_id, ssid, operator_surname, operator_name, email, password, center_id)
				VALUES (?, ?, ?, ?, ?, ?, LOWER(?));
			""");

			pst.setString(1, userID);
			pst.setString(2, SSID);
			pst.setString(3, operatorSurname);
			pst.setString(4, operatorName);
			pst.setString(5, email);
			pst.setString(6, password);
			pst.setString(7, centerID);

			pst.executeUpdate();
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
			Timestamp timestamp = Timestamp.valueOf(parameter.getTimestamp());
			String categoryID = parameter.getCategory();
			String userID = parameter.getUserID();
			int score = parameter.getScore();
			String notes = parameter.getNotes();

			PreparedStatement pst = prepareStatement("""
				INSERT INTO parameter (geoname_id, center_id, rec_timestamp, category_id, user_id, score, notes)
				VALUES (?, LOWER(?), ?, ?, ?, ?, ?);
			""");

			pst.setInt(1, geonameID);
			pst.setString(2, centerID);
			pst.setTimestamp(3, timestamp);
			pst.setString(4, categoryID);
			pst.setString(5, userID);
			pst.setInt(6, score);
			pst.setString(7, notes);

			pst.executeUpdate();
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

			String SSID = new String(operator.getSSID());
			String operatorSurname = operator.getSurname();
			String operatorName = operator.getName();
			String email = operator.getEmail();
			String password = hashPassword(operator.getPassword());
			String centerID = operator.getCenterID();

			PreparedStatement pst = prepareStatement("""
				UPDATE operator
				SET ssid = ?,
				operator_surname = ?,
				operator_name = ?,
				email = ?,
				password = ?,
				center_id = LOWER(?)
				WHERE user_id = ?;
			""");

			pst.setString(1, SSID);
			pst.setString(2, operatorSurname);
			pst.setString(3, operatorName);
			pst.setString(4, email);
			pst.setString(5, password);
			pst.setString(6, centerID);
			pst.setString(7, user_id);

			pst.executeUpdate();
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To add an existing area to a specified center
	 * 
	 * @param geoname_id The area to be added in the center
	 * @param center_id The center the area needs to be added in
	 * @return Success or failure of the operation
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public boolean includeAreaToCenter(int geoname_id, String center_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				INSERT INTO monitors (center_id, geoname_id)
				VALUES (LOWER(?), ?);
			""");

			pst.setString(1, center_id);
			pst.setInt(2, geoname_id);

			pst.executeUpdate();
			return true;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To check if a monitoring center is monitoring an area
	 * 
	 * @param center_id The ID of the center
	 * @param geoname_id The ID of the area
	 * @return True if the area is being monitored by a center, false otherwise
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public boolean monitors(String center_id, int geoname_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM monitors
				WHERE LOWER(center_id) = LOWER(?)
				AND geoname_id = ?;
			""");

			pst.setString(1, center_id);
			pst.setInt(2, geoname_id);

			ResultSet query = pst.executeQuery();

			if (getRowCount(query) == 1) return true;
			else return false;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	/**
	 * To check if a monitoring center is employing an operator
	 * 
	 * @param center_id The ID of the center
	 * @param user_id The ID of the operator
	 * @return True if an operator is being employed by a center, false otherwise
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public boolean employs(String center_id, String user_id) throws ConnectionLostException, DatabaseRequestException {

		try {

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM center C
				JOIN operator O
				ON C.center_id = O.center_id
				WHERE LOWER(C.center_id) = LOWER(?)
				AND O.user_id = ?;
			""");

			pst.setString(1, center_id);
			pst.setString(2, user_id);

			ResultSet query = pst.executeQuery();

			if (getRowCount(query) == 1) return true;
			else return false;
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

			PreparedStatement pst = prepareStatement("""
				SELECT *
				FROM operator
				WHERE user_id = ?
				AND password = ?;
			""");

			pst.setString(1, user_id);
			pst.setString(2, hashPassword(password));
			
			ResultSet query = pst.executeQuery();
			Operator result = null;

			if (query.next()) {

				String userID = query.getString("user_id");
				char[] SSID = query.getString("ssid").toCharArray();
				String operatorSurname = query.getString("operator_surname");
				String operatorName = query.getString("operator_name");
				String email = query.getString("email");
				String centerID = query.getString("center_id");

				result = new Operator(userID, SSID, operatorSurname, operatorName, email, password, centerID);
			}

			return result;
		}

		catch (SQLException e) {

			throw new DatabaseRequestException(e.getMessage());
		}
	}

	private int getRowCount(ResultSet query) throws SQLException {

		int rows = 0;

		if (query.last()) {

			rows = query.getRow();
			query.beforeFirst();
		}

		return rows;
	}

	private synchronized PreparedStatement prepareStatement(String statement) throws SQLException {

		return m_connection.prepareStatement(statement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}

	private static String hashPassword(String password) {

		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = digest.digest(password.getBytes());

			StringBuilder hexString = new StringBuilder();
			for (byte b : hashedBytes) {

				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');

				hexString.append(hex);
			}

			return hexString.toString();
		}

		catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Cannot find hashing algorithm", e);
		}
	}

	/**
	 * The connection estabilished with the database through the JDBC DriverManager
	 */
	private Connection m_connection;
}
