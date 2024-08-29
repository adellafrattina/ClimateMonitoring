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
	 * @return The query's rows as a ResultSet
	 */
	public synchronized ResultSet execute(String statement) throws SQLException {

		PreparedStatement pst = m_connection.prepareStatement(statement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
			
			ResultSet query = execute("SELECT * FROM area WHERE LOWER(area_name) LIKE '%" + str + "%' ORDER BY CASE WHEN LOWER(area_name) LIKE '" + str + "%' THEN 0 ELSE 1 END, POSITION('" + str + "' IN LOWER(area_name)), area_name;");
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
			
			ResultSet query = execute("SELECT * FROM area WHERE LOWER(country_name) LIKE '%" + str + "%' ORDER BY CASE WHEN LOWER(country_name) LIKE '" + str + "%' THEN 0 ELSE 1 END, POSITION('" + str + "' IN LOWER(country_name)), country_name;");
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

			ResultSet query = execute("SELECT * FROM area WHERE latitude BETWEEN " + (latitude - 0.5) + " AND " + (latitude + 0.5) + " AND longitude BETWEEN " + (longitude - 0.5) + " AND " + (longitude + 0.5) + " ORDER BY area_name;");
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

			ResultSet query = execute("SELECT * FROM center WHERE LOWER(center_id) LIKE '%" + str + "%' ORDER BY CASE WHEN LOWER(center_id) LIKE '" + str + "%' THEN 0 ELSE 1 END, POSITION('" + str + "' IN LOWER(center_id)), center_id;");
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

			ResultSet query = execute("SELECT * FROM area WHERE geoname_id = " + geoname_id + ";");
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

			ResultSet query = execute("SELECT A.* FROM area A JOIN monitors M on A.geoname_id = M.geoname_id WHERE center_id = '" + center_id + "';");
			Area[] result = new Area[getRowCount(query)];

			if (query.next()) {

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

			ResultSet query = execute("SELECT * FROM center WHERE center_id = '" + center_id + "';");
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

			ResultSet query = execute("SELECT * FROM center WHERE city = " + city + " AND street = '" + street + "' AND house_number = " + house_number + ";");
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

			ResultSet query = execute("SELECT C.* FROM monitors M JOIN center C on M.center_id = C.center_id WHERE geoname_id = "+ geoname_id + ";");
			Center[] result = new Center[getRowCount(query)];

			if (query.next()) {

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

			ResultSet query = execute("SELECT * FROM operator WHERE user_id = '" + user_id + "';");
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

			ResultSet query = execute("SELECT * FROM operator WHERE ssid = '" + ssid + "';");
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

			ResultSet query = execute("SELECT * FROM operator WHERE email = '" + email + "';");
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
			Parameter[] result = new Parameter[getRowCount(query)];

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

				result[query.getRow() - 1] = new Parameter(geonameID, centerID, userID, categoryID, date, time, score, notes);
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

			ResultSet query = execute("SELECT * FROM category;");
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

			execute("INSERT INTO area (geoname_id, area_name, area_ascii_name, country_code, country_name, latitude, longitude) VALUES (" + geonameID + ", '" + areaName + "', '" + areaAsciiName + "', '" + countryCode + "', '" + countryName + "', " + latitude + ", " + longitude + ");");
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

			execute("INSERT INTO center (center_id, city, street, house_number, postal_code, district) VALUES ('" + centerID + "', " + city + ", '" + street + "', " + houseNumber + ", " + postalCode + ", " + (district == null ? "null" : "'" + district + "'") + ");");
			execute("INSERT INTO monitors (center_id, geoname_id) VALUES ('" + centerID + "', " + city + ");");
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
			String password = operator.getPassword();
			String centerID = operator.getCenterID();

			execute("INSERT INTO operator (user_id, ssid, operator_surname, operator_name, email, password, center_id) VALUES ('"+ userID + "', '" + SSID + "', '" + operatorSurname + "', '" + operatorName + "', '" + email + "', '" + password + "', '" + centerID + "');");
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
			String timestamp = date + " " + time;
			String categoryID = parameter.getCategory();
			String userID = parameter.getUserID();
			int score = parameter.getScore();
			String notes = parameter.getNotes();

			execute("INSERT INTO parameter (geoname_id, center_id, rec_timestamp, category_id, user_id, score, notes) VALUES (" + geonameID + ", '" + centerID + "', '" + timestamp + "', '" + categoryID + "', '" + userID + "', " + score + ", '" + notes + "');");
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

			execute("IF EXISTS (SELECT user_id FROM operator WHERE user_id = '" + user_id + "') THEN UPDATE OPERATOR SET ssid = '" + SSID + "', operator_surname = '" + operatorSurname + "', operator_name = '" + operatorName + "', email = '" + email + "', password = '" + password + "', center_id = '" + centerID + "' END IF;");
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

			ResultSet query = execute("SELECT * FROM monitors WHERE center_id = '" + center_id + "' AND geoname_id = " + geoname_id + ";");

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

			ResultSet query = execute("SELECT * FROM center C JOIN operator O ON C.center_id = O.center_id WHERE C.center_id = '" + center_id + " AND O.user_id = " + user_id + ";");

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

			ResultSet query = execute("SELECT * FROM operator WHERE user_id = '" + user_id + "' AND password = '" + password + "';");
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

	private int getRowCount(ResultSet query) throws SQLException {

		int rows = 0;

		if (query.last()) {

			rows = query.getRow();
			query.beforeFirst();
		}

		return rows;
	}

	/**
	 * The connection estabilished with the database through the JDBC DriverManager
	 */
	private Connection m_connection;
}
