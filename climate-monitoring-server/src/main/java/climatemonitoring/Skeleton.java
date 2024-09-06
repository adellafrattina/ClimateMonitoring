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
import climatemonitoring.core.headless.Console;

/**
 * A thread class that gets created every time a new client
 * connects to the server.
 * 
 * Handles the communication between client and server
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
class Skeleton extends Thread {

	public Skeleton(Socket socket, ServerDatabase database) {

		try {

			m_out = new ObjectOutputStream(socket.getOutputStream());
			m_in = new ObjectInputStream(socket.getInputStream());

			m_client = socket;
			m_serverDatabase = database;

			start();
		}

		catch (IOException e) {

			Console.error("Failed to initialize I/O channels");
		}
	}

	/**
	 * The main loop of this class that manages client-server I/O.
	 */
	public void run() {

		try {

			while (m_running) {

				RequestType request = (RequestType) m_in.readObject();
				Console.debug("Client [" + m_client.getInetAddress() + ":" + m_client.getPort() + "] issued " + request);

				switch (request) {

					case BEGIN:

						try {

							m_serverDatabase.begin();

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;

					case END:

						try {

							m_serverDatabase.end();

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;

					case SEARCH_AREAS_BY_NAME: {

						String str = (String) m_in.readObject();
	
						try {
	
							Area[] result = m_serverDatabase.searchAreasByName(str);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case SEARCH_AREAS_BY_COUNTRY: {

						String str = (String) m_in.readObject();
	
						try {
	
							Area[] result = m_serverDatabase.searchAreasByCountry(str);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case SEARCH_AREAS_BY_COORDS: {

						double latitude = (Double) m_in.readObject();
						double longitude = (Double) m_in.readObject();
	
						try {
	
							Area[] result = m_serverDatabase.searchAreasByCoords(latitude, longitude);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case SEARCH_CENTERS_BY_NAME: {

						String str = (String) m_in.readObject();
	
						try {
	
							Center[] result = m_serverDatabase.searchCentersByName(str);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_AREA: {

						int geonameID = (Integer) m_in.readObject();
	
						try {
	
							Area result = m_serverDatabase.getArea(geonameID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}


					case GET_MONITORED_AREAS: {

						String centerID = (String) m_in.readObject();

						try {

							Area[] result = m_serverDatabase.getMonitoredAreas(centerID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case GET_CENTER: {

						String centerID = (String) m_in.readObject();
	
						try {
	
							Center result = m_serverDatabase.getCenter(centerID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_CENTERS: {

						try {

							Center[] result = m_serverDatabase.getCenters();

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case GET_CENTER_BY_ADDRESS: {

						int city = (Integer) m_in.readObject();
						String street = (String) m_in.readObject();
						int houseNumber = (Integer) m_in.readObject();
	
						try {
	
							Center result = m_serverDatabase.getCenterByAddress(city, street, houseNumber);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_LATEST_CENTER: {

						int geonameID = (Integer) m_in.readObject();

						try {

							Center result = m_serverDatabase.getLatestCenter(geonameID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case GET_ASSOCIATED_CENTERS: {

						int geonameID = (Integer) m_in.readObject();

						try {

							Center[] result = m_serverDatabase.getAssociatedCenters(geonameID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case GET_OPERATOR: {

						String userID = (String) m_in.readObject();
	
						try {
	
							Operator result = m_serverDatabase.getOperator(userID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_OPERATOR_BY_SSID: {

						String SSID = (String) m_in.readObject();
	
						try {
	
							Operator result = m_serverDatabase.getOperatorBySSID(SSID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_OPERATOR_BY_EMAIL: {

						String email = (String) m_in.readObject();
	
						try {
	
							Operator result = m_serverDatabase.getOperatorByEmail(email);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}


					case GET_PARAMETERS: {

						int geonameID = (Integer) m_in.readObject();
						String centerID = (String) m_in.readObject();
						String category = (String) m_in.readObject();
	
						try {
	
							Parameter[] result = m_serverDatabase.getParameters(geonameID, centerID, category);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_PARAMETERS_AVERAGE: {

						int geonameID = (Integer) m_in.readObject();
						String centerID = (String) m_in.readObject();
						String category = (String) m_in.readObject();
	
						try {
	
							double result = m_serverDatabase.getParametersAverage(geonameID, centerID, category);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_CATEGORIES: {

						try {
	
							Category[] result = m_serverDatabase.getCategories();
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case GET_LATEST_CATEGORY: {

						int geonameID = (Integer) m_in.readObject();
						String centerID = (String) m_in.readObject();

						try {
	
							Category result = m_serverDatabase.getLatestCategory(geonameID, centerID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case ADD_AREA: {

						Area area = (Area) m_in.readObject();
	
						try {
	
							m_serverDatabase.addArea(area);
	
							m_out.writeObject(true);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case ADD_CENTER: {

						Center center = (Center) m_in.readObject();
	
						try {
	
							m_serverDatabase.addCenter(center);
	
							m_out.writeObject(true);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}


					case ADD_OPERATOR: {

						Operator operator = (Operator) m_in.readObject();
	
						try {
	
							m_serverDatabase.addOperator(operator);
	
							m_out.writeObject(true);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}


					case ADD_PARAMETER: {

						Parameter parameter = (Parameter) m_in.readObject();
	
						try {
	
							m_serverDatabase.addParameter(parameter);
	
							m_out.writeObject(true);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case EDIT_OPERATOR: {

						String userID = (String) m_in.readObject();
						Operator operator = (Operator) m_in.readObject();
	
						try {
	
							m_serverDatabase.editOperator(userID, operator);
	
							m_out.writeObject(true);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case INCLUDE_AREA_TO_CENTER: {

						int geonameID = (Integer) m_in.readObject();
						String centerID = (String) m_in.readObject();

						try {

							m_serverDatabase.includeAreaToCenter(geonameID, centerID);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}

						break;
					}

					case MONITORS: {

						String centerID = (String) m_in.readObject();
						int geonameID = (Integer) m_in.readObject();
	
						try {
	
							boolean result = m_serverDatabase.monitors(centerID, geonameID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case EMPLOYS: {

						String centerID = (String) m_in.readObject();
						String userID = (String) m_in.readObject();
	
						try {
	
							boolean result = m_serverDatabase.employs(centerID, userID);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case VALIDATE_CREDENTIALS: {

						String userID = (String) m_in.readObject();
						String password = (String) m_in.readObject();
	
						try {
	
							Operator result = m_serverDatabase.validateCredentials(userID, password);
	
							m_out.writeObject(true);
							m_out.writeObject(result);
						}
	
						catch (DatabaseRequestException e) {
	
							m_out.writeObject(false);
							m_out.writeObject(e);
							Console.error("Error on request " + request + ": " + e.getMessage());
						}
	
						break;
					}

					case PING: {

						m_out.writeObject(request);
	
						break;
					}

					case DISCONNECT: {

						m_running = false;
						m_out.writeObject(request);
	
						break;
					}

					default: {

						Console.error("Unhandled request type: " + request);
	
						break;
					}
				}
			}

			Console.info("Client [" + m_client.getInetAddress() + ":" + m_client.getPort() + "] has disconnected");
			close();
		}

		catch (IOException | ConnectionLostException e) {

			Console.warn("Client [" + m_client.getInetAddress() + ":" + m_client.getPort() + "] has disconnected unexpectedly");
		}

		catch (ClassNotFoundException ex) {

			Console.error("Class of a serialized object cannot be found");
		}
	}

	/**
	 * Closes I/O streams after exiting the execution loop
	 */
	private void close() {

		try {

			m_out.flush();
			m_out.close();
			m_in.close();
			m_client.close();
			m_serverDatabase.shutdown();
		}

		catch (IOException e) {

			Console.error("I/O stream closure failed");
		}
	}

	private ObjectOutputStream m_out;
	private ObjectInputStream m_in;

	private Socket m_client;

	private boolean m_running = true;
	private ServerDatabase m_serverDatabase;
}
