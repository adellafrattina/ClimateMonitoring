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

			Console.write("Failed to initialize I/O channels");
		}
	}

	/**
	 * The main loop of this class that manages client-server I/O.
	 */
	public void run() {

		while (m_running) {

			try {

				RequestType request = (RequestType) m_in.readObject();

				switch (request) {

					case BEGIN:

						try {

							m_serverDatabase.begin();

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
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
						}

						break;

					case SEARCH_AREAS_BY_NAME:

						String areaName = (String) m_in.readObject();

						try {

							Area[] result = m_serverDatabase.searchAreasByName(areaName);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case SEARCH_AREAS_BY_COUNTRY:

						String countryName = (String) m_in.readObject();

						try {

							Area[] result = m_serverDatabase.searchAreasByCountry(countryName);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case SEARCH_AREAS_BY_COORDS:

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
						}

						break;

					case SEARCH_CENTERS_BY_NAME:

						String centerName = (String) m_in.readObject();

						try {

							Center[] result = m_serverDatabase.searchCentersByName(centerName);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_AREA:

						int areaGeonameID = (Integer) m_in.readObject();

						try {

							Area result = m_serverDatabase.getArea(areaGeonameID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_CENTER:

						String monitoringCenterID = (String) m_in.readObject();

						try {

							Center result = m_serverDatabase.getCenter(monitoringCenterID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_CENTER_BY_ADDRESS:

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
						}

						break;

					case GET_OPERATOR:

						String operatorUserID = (String) m_in.readObject();

						try {

							Operator result = m_serverDatabase.getOperator(operatorUserID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_OPERATOR_BY_SSID:

						String operatorSSID = (String) m_in.readObject();

						try {

							Operator result = m_serverDatabase.getOperatorBySSID(operatorSSID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_OPERATOR_BY_EMAIL:

						String operatorEmail = (String) m_in.readObject();

						try {

							Operator result = m_serverDatabase.getOperatorByEmail(operatorEmail);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_PARAMETERS_AREA_CENTER:

						int geonameID = (Integer) m_in.readObject();
						String centerID = (String) m_in.readObject();

						try {

							Parameter[] result = m_serverDatabase.getParameters(geonameID, centerID);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case GET_CATEGORIES:

						try {

							Category[] result = m_serverDatabase.getCategories();

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case ADD_AREA:

						Area area = (Area) m_in.readObject();

						try {

							m_serverDatabase.addArea(area);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case ADD_CENTER:

						Center center = (Center) m_in.readObject();

						try {

							m_serverDatabase.addCenter(center);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case ADD_OPERATOR:

						Operator operator = (Operator) m_in.readObject();

						try {

							m_serverDatabase.addOperator(operator);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case ADD_PARAMETER:

						Parameter parameter = (Parameter) m_in.readObject();

						try {

							m_serverDatabase.addParameter(parameter);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case EDIT_OPERATOR:

						String userID = (String) m_in.readObject();
						Operator newOperator = (Operator) m_in.readObject();

						try {

							m_serverDatabase.editOperator(userID, newOperator);

							m_out.writeObject(true);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case MONITORS:

						String mCenterID = (String) m_in.readObject();
						int aGeonameID = (Integer) m_in.readObject();

						try {

							boolean result = m_serverDatabase.monitors(mCenterID, aGeonameID);

							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case EMPLOYS:

						String monitCenterID = (String) m_in.readObject();
						String opUserID = (String) m_in.readObject();

						try {

							boolean result = m_serverDatabase.employs(monitCenterID, opUserID);

							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case VALIDATE_CREDENTIALS:

						String loginID = (String) m_in.readObject();
						String loginPassword = (String) m_in.readObject();

						try {

							Operator result = m_serverDatabase.validateCredentials(loginID, loginPassword);

							m_out.writeObject(true);
							m_out.writeObject(result);
						}

						catch (DatabaseRequestException e) {

							m_out.writeObject(false);
							m_out.writeObject(e);
						}

						break;

					case PING:

						m_out.writeObject(request);

						break;

					case DISCONNECT:

						m_running = false;
						m_out.writeObject(request);

						break;

					default:

						Console.write("Unhandled request type: " + request);

						break;
				}
			}

			catch (IOException | ConnectionLostException e) {

				Console.write("Client [" + m_client.getInetAddress() + ":" + m_client.getPort() + "] has disconnected unexpectedly");
				m_running = false;
			}

			catch (ClassNotFoundException ex) {

				Console.write("Class of a serialized object cannot be found");
				m_running = false;
			}
		}

		close();
		Console.write("Client [" + m_client.getInetAddress() + ":" + m_client.getPort() + "] has disconnected");
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

			Console.write("I/O stream closure failed");
		}
	}

	private ObjectOutputStream m_out;
	private ObjectInputStream m_in;

	private Socket m_client;

	private boolean m_running = true;
	private ServerDatabase m_serverDatabase;
}
