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
	 */
	@Override
	public boolean connect(String address, short port) {
		
		try {

			s = new Socket(address, port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());

			return true;
		} catch (IOException e) {

			System.err.println("Error during the connection");
		}

		return false;
	}
	
	/**
	 * Close the connection between the socket and the server
	 */
	@Override
	public void close() {

		if(s != null && !s.isClosed()){

			try {

				s.close();
				System.out.println("Connection closed");
			} catch (IOException e) {

				System.out.println("Error during the closure of the connection");
			}
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
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Area[] searchAreasByName(String str) throws Exception {

		Area[] areafoundbyname = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_NAME);
			out.writeObject(str);
	
			areafoundbyname = (Area[]) in.readObject();

			RequestType type;

			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.SEARCH_AREAS_BY_NAME));

			if(areafoundbyname == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}

		} catch (IOException e) {
			throw new IOException("Connection error");
		}

		return areafoundbyname;
	}

	/**
	 * Returns in alphabetical order an array of areas which belong
	 * to the country whose name contains the input string
	 * 
	 * @param str The input string the search is based on
	 * @return The result of the search as an array of areas
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Area[] searchAreasByCountry(String str) throws Exception {

		Area[] areafoundbycountry = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_COUNTRY);
			out.writeObject(str);
	
			RequestType type;

			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.SEARCH_AREAS_BY_COUNTRY));

			areafoundbycountry = (Area[]) in.readObject();

			if(areafoundbycountry == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
		
		} catch (IOException e) {
			throw new IOException("Connection error");
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
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Area[] searchAreasByCoords(double latitude, double longitude) throws Exception {

		Area[] areafoundbycoords = null;

		try {
			
			out.writeObject(RequestType.SEARCH_AREAS_BY_COORDS);
			out.writeObject(latitude);
			out.writeObject(longitude);

			RequestType type;

			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.SEARCH_AREAS_BY_COORDS));

			areafoundbycoords= (Area[]) in.readObject();

			if(areafoundbycoords == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}

		} catch (IOException e) {
			throw new IOException("Connection error");
		}

		return areafoundbycoords;
	}
	
	/**
	 * Returns an array containing parameters about a specified area that
	 * were recorded by the desired center
	 * 
	 * @param geoname_id The area's ID
	 * @param center_id The center's ID
	 * @return The result of the search as an array of parameters
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Parameter[] getParameters(int geoname_id, String center_id) throws Exception {

		Parameter[] getparameters = null;

		try {

			out.writeObject(RequestType.GET_PARAMETERS_AREA_CENTER);
			out.writeObject(geoname_id);
			out.writeObject(center_id);

			RequestType type;
		
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.GET_PARAMETERS_AREA_CENTER));

			getparameters = (Parameter[]) in.readObject();

			if(getparameters == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		
		return getparameters;
	}
	
	/**
	 * Get all the categories and their explanation
	 * 
	 * @return An array of all categories with relative descriptions
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Category[] getCategories() throws Exception {

		Category[] getcategories = null;

		try {
			out.writeObject(RequestType.GET_CATEGORIES);
	
			RequestType type;
		
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.GET_CATEGORIES));
	
			getcategories = (Category[]) in.readObject();

			if(getcategories == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		return getcategories;
	}
	
	/**
	 * Adds an area to the database
	 * 
	 * @param area The area that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public boolean addArea(Area area) throws Exception {

		Boolean addedArea = null;

		try {
			
			out.writeObject(RequestType.ADD_AREA);
			out.writeObject(area);

			RequestType type;
	
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.ADD_AREA));


			addedArea = (Boolean) in.readObject();

			if(addedArea == false){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		
		return addedArea;
	}
	
	/**
	 * Adds a center to the database
	 * 
	 * @param center The center that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public boolean addCenter(Center center) throws Exception {

		Boolean addedCenter = null;

		try {
			out.writeObject(RequestType.ADD_CENTER);
			out.writeObject(center);

			RequestType type;
	
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.ADD_CENTER));

			addedCenter = (Boolean) in.readObject();

			if(addedCenter == false){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		
		return addedCenter;
	}
	
	/**
	 * Adds an operator to the database
	 * 
	 * @param operator The operator that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public boolean addOperator(Operator operator) throws Exception {
		
		Boolean addedOperator = null;

		try {
			out.writeObject(RequestType.ADD_OPERATOR);
			out.writeObject(operator);
			
			RequestType type;
			
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.ADD_OPERATOR));
			
			addedOperator = (Boolean) in.readObject();

			if(addedOperator == false){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}

		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		return addedOperator;
	}
	
	/**
	 * Adds a parameter to the database
	 * 
	 * @param parameter The parameter that needs to be added to the database
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public boolean addParameter(Parameter parameter) throws Exception {

		Boolean addedParameter = null;

		try {
			out.writeObject(RequestType.ADD_PARAMETER);
			out.writeObject(parameter);
	
			RequestType type;
	
			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.ADD_PARAMETER));
	
			addedParameter = (Boolean) in.readObject();

			if(addedParameter == false){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		return addedParameter;
	}
	
	/**
	 * Edits an existing operator
	 * 
	 * @param user_id The ID of the operator that will get edited
	 * @param operator The new operator that will overwrite the previous one
	 * @return Success or failure of the operation
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public boolean editOperator(String user_id, Operator operator) throws Exception {

		Boolean addedOperator = null;

		try {
			out.writeObject(RequestType.EDIT_OPERATOR);
			out.writeObject(user_id);
			out.writeObject(operator);
			RequestType type;

			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.EDIT_OPERATOR));

			addedOperator = (Boolean) in.readObject();

			if(addedOperator == false){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			}
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
		return addedOperator;
	}
	
	/**
	 * Checks whether the userid and password are valid. If so, the corresponding
	 * operator will be returned, null otherwise
	 * 
	 * @param user_id Operator's user id
	 * @param password Operator's password
	 * @return The operator whose credentials correspond to the ones in input
	 * @throws Exception If anything went wrong while executing the operation
	 */
	@Override
	public Operator validateCredentials(String user_id, String password) throws Exception {
		
		Operator op = null;

		try {
			out.writeObject(RequestType.VALIDATE_CREDENTIALS);
			out.writeObject(user_id);
			out.writeObject(password);
			RequestType type;

			do{
				type = (RequestType) in.readObject();
			}while((type != RequestType.VALIDATE_CREDENTIALS));

			op = (Operator) in.readObject();

			if(op == null){
				String msg = (String) in.readObject();
				throw new Exception(msg);
			} 
			
		} catch (IOException e) {
			throw new IOException("Connection error");
		}
			
		return op;
	}
	
	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;
}
