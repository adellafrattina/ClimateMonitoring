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

						double latitude = m_in.readDouble();
						double longitude = m_in.readDouble();

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

					case GET_PARAMETERS_AREA_CENTER:

						int geonameID = m_in.readInt();
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

						long userTime = m_in.readLong();

						Long result = System.currentTimeMillis() - userTime;
						m_out.writeObject(result);

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
