/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.RequestType;


/**
 * The proxy class is able to create the connection, closure and communication from the client to the server
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class ProxyImpl implements Proxy{

	/**
	 * Allows the connection to the desired address.
	 * Return true if the connection was succesfull, false if not
	 * @param address the address the user wants to connect to
	 * @param port the port the user wants to connect to
	 * @throws ConnectionLostException 
	 */
	@Override
	public boolean connect(String address, short port) throws ConnectionLostException {
		
		try {

			s = new Socket(address, port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());

			return true;
		} catch (IOException e) {
			throw new ConnectionLostException();
		}

	}
	
	/**
	 * Close the connection between the socket and the server
	 * @throws ConnectionLostException 
	 */
	@Override
	public void close() throws ConnectionLostException {

		if(s != null && !s.isClosed()){

			try {
				s.close();
				System.out.println("Connection closed");
			} catch (IOException e) {
				throw new ConnectionLostException();
			}
		}
	}

	/**
	 * To start a transaction
	 * 
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized void begin() throws ConnectionLostException, DatabaseRequestException {

		try {
			
			out.writeObject(RequestType.BEGIN);
			boolean success = (boolean) in.readObject();

			if(success == true){

				return;
			}else{

				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * To end a transaction
	 * 
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized void end() throws ConnectionLostException, DatabaseRequestException {

		try {
			
			out.writeObject(RequestType.END);
			boolean success = (boolean) in.readObject();

			if(success == true){

				return;
			}else{

				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	@Override
	public synchronized Area[] searchAreasByName(String str) throws ConnectionLostException, DatabaseRequestException {

		Area[] areafoundbyname = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_NAME);
			out.writeObject(str);
			
			boolean success = (boolean) in.readObject();

			if(success == true){
				areafoundbyname = (Area[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return areafoundbyname;
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

		Area[] areafoundbycountry = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_COUNTRY);
			out.writeObject(str);
	
			boolean success = (boolean) in.readObject();

			if(success == true){
				areafoundbycountry = (Area[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return areafoundbycountry;
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
	public synchronized Area[] searchAreasByCoords(double latitude, double longitude) throws ConnectionLostException, DatabaseRequestException {

		Area[] areafoundbycoords = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_COORDS);
			out.writeObject(latitude);
			out.writeObject(longitude);

			boolean success = (boolean) in.readObject();

			if(success == true){
				areafoundbycoords = (Area[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return areafoundbycoords;
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
	public synchronized Center[] searchCentersByName(String str) throws ConnectionLostException, DatabaseRequestException {

		Center[] searchCentersByName = null;

		try {

			out.writeObject(RequestType.SEARCH_CENTERS_BY_NAME);
			out.writeObject(str);

			boolean success = (boolean) in.readObject();

			if(success == true){
				searchCentersByName = (Center[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return searchCentersByName;
	}

	/**
	 * To get an area by its geoname id
	 * 
	 * @param geoname_id The geoname id of the area to be searched
	 * @return The area that corresponds to the given geoname id
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized Area getArea (int geoname_id) throws ConnectionLostException, DatabaseRequestException {

		Area getarea = null;

		try {

			out.writeObject(RequestType.GET_AREA);
			out.writeObject(geoname_id);

			boolean success = (boolean) in.readObject();

			if(success == true){
				getarea = (Area) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return getarea;
	}

	/**
	 * To get a center by its center id
	 * 
	 * @param center_id The center id of the center to be searched
	 * @return The center that corresponds to the given center id
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized Center getCenter (String center_id) throws ConnectionLostException, DatabaseRequestException {

		Center getcenter = null;

		try {

			out.writeObject(RequestType.GET_AREA);
			out.writeObject(center_id);

			boolean success = (boolean) in.readObject();

			if(success == true){
				getcenter = (Center) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return getcenter;
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
	public synchronized Center getCenterByAddress(int city, String street, int house_number) throws ConnectionLostException, DatabaseRequestException {

		Center center = null;
	
		try {

			out.writeObject(RequestType.GET_CENTER_BY_ADDRESS);
			out.writeObject(city);
			out.writeObject(street);
			out.writeObject(house_number);
	
			boolean success = (boolean) in.readObject();
	
			if (success == true) {

				center = (Center) in.readObject();
			} else {

				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {

			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	
		return center;
	}

	/**
	 * To get an operator based on their user id
	 * 
	 * @param user_id The operator's ID
	 * @return The operator whose ID corresponds with the given one
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized Operator getOperator(String user_id) throws ConnectionLostException, DatabaseRequestException {
		Operator operator = null;
	
		try {
			out.writeObject(RequestType.GET_OPERATOR);
			out.writeObject(user_id);
	
			boolean success = (boolean) in.readObject();
	
			if (success == true) {
				operator = (Operator) in.readObject();
			} else {
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return operator;
	}

	/**
	 * To get an operator based on their SSID
	 * 
	 * @param ssid The operator's SSID
	 * @return The operator whose SSID corresponds with the given one
	 * @throws ConnectionLostException If the client loses connection during the operation
	 * @throws DatabaseRequestException If the database fails to process the given request
	 */
	public synchronized Operator getOperatorBySSID(String ssid) throws ConnectionLostException, DatabaseRequestException {

		Operator operator = null;
	
		try {

			out.writeObject(RequestType.GET_OPERATOR_BY_SSID);
			out.writeObject(ssid);
	
			boolean success = (boolean) in.readObject();
	
			if (success == true) {

				operator = (Operator) in.readObject();
			} else {

				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {

			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	
		return operator;
	}

	
	public synchronized Operator getOperatorByEmail(String email) throws ConnectionLostException, DatabaseRequestException {

		Operator operator = null;
	
		try {

			out.writeObject(RequestType.GET_OPERATOR_BY_EMAIL);
			out.writeObject(email);
	
			boolean success = (boolean) in.readObject();
	
			if (success == true) {

				operator = (Operator) in.readObject();
			} else {
			
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
		} catch (IOException e) {

			throw new ConnectionLostException();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	
		return operator;
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
	public synchronized Parameter[] getParameters(int geoname_id, String center_id) throws ConnectionLostException, DatabaseRequestException {

		Parameter[] getparameters = null;

		try {

			out.writeObject(RequestType.GET_PARAMETERS_AREA_CENTER);
			out.writeObject(geoname_id);
			out.writeObject(center_id);

			boolean success = (boolean) in.readObject();

			if(success == true){
				getparameters = (Parameter[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return getparameters;
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

		Category[] getcategories = null;

		try {
			out.writeObject(RequestType.GET_CATEGORIES);
	
			boolean success = (boolean) in.readObject();

			if(success == true){
				getcategories = (Category[]) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		return getcategories;
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

		Boolean addedArea = null;

		try {
			
			out.writeObject(RequestType.ADD_AREA);
			out.writeObject(area);

			boolean success = (boolean) in.readObject();

			if(success == true){
				addedArea = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return addedArea;
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

		Boolean addedCenter = null;

		try {
			out.writeObject(RequestType.ADD_CENTER);
			out.writeObject(center);

			boolean success = (boolean) in.readObject();

			if(success == true){
				addedCenter = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return addedCenter;
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
		
		Boolean addedOperator = null;

		try {
			out.writeObject(RequestType.ADD_OPERATOR);
			out.writeObject(operator);
			
			boolean success = (boolean) in.readObject();
			
			if(success == true){
				addedOperator = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return addedOperator;
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

		Boolean addedParameter = null;

		try {
			out.writeObject(RequestType.ADD_PARAMETER);
			out.writeObject(parameter);
	
			boolean success = (boolean) in.readObject();
			
			if(success == true){
				addedParameter = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return addedParameter;
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
	public synchronized boolean editOperator(String user_id, Operator operator) throws ConnectionLostException, DatabaseRequestException {

		Boolean editedOperator = null;

		try {
			out.writeObject(RequestType.EDIT_OPERATOR);
			out.writeObject(user_id);
			out.writeObject(operator);
			boolean success = (boolean) in.readObject();
			
			if(success == true){
				editedOperator = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return editedOperator;
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
	@Override
	public boolean monitors(String center_id, int geoname_id) throws ConnectionLostException, DatabaseRequestException {
		
		Boolean monitor = null;

		try {
			
			out.writeObject(RequestType.MONITORS);
			out.writeObject(center_id);
			out.writeObject(geoname_id);
			boolean success = (boolean) in.readObject();

			if(success == true){
				monitor = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return monitor;
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
	@Override
	public boolean employs(String center_id, String user_id) throws ConnectionLostException, DatabaseRequestException {
		
		Boolean employ = null;

		try {
			
			out.writeObject(RequestType.EMPLOYS);
			out.writeObject(center_id);
			out.writeObject(user_id);
			boolean success = (boolean) in.readObject();

			if(success == true){
				employ = (Boolean) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}

		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return employ;
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
	public synchronized Operator validateCredentials(String user_id, String password) throws ConnectionLostException, DatabaseRequestException {
		
		Operator op = null;

		try {
			out.writeObject(RequestType.VALIDATE_CREDENTIALS);
			out.writeObject(user_id);
			out.writeObject(password);
			
			boolean success = (boolean) in.readObject();

			if(success == true){
				op = (Operator) in.readObject();
			}else{
				DatabaseRequestException e = (DatabaseRequestException) in.readObject();
				throw e;
			}
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return op;
	}

	/**
	 * Sends a ping request. 
	 * @return The time (in milliseconds) elapsed between sending the ping packet and receiving it from the server
	 */
	public long ping() throws ConnectionLostException {

		long servertime = -1;

		try {

			out.writeObject(RequestType.PING);
			long start_time = System.nanoTime();
			in.readObject();
			long end_time = System.nanoTime();

			long final_time = (end_time - start_time) * 1000000;

			return final_time;
			
		} catch (IOException e) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return servertime;
	}
	
	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;

}
