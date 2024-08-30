/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.StringTokenizer;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.headless.Console;

/**
 * To show all the parameters based on an area. This is not a typical view state, because it behaves differenty in GUI and Headless mode
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class ParameterInfo {

	public static void onHeadlessRender(String args) {

		String geonameID = null;

		try {

			// view parameter area center
			StringTokenizer st = new StringTokenizer(args);

			// Geoname ID
			if (!st.hasMoreTokens()) {

				Console.write("Incorrect command syntax. Expected a valid area's geoname ID");
				return;
			}

			geonameID = st.nextToken().trim();
			m_geonameID = Integer.parseInt(geonameID);

			if (Check.geonameID(m_geonameID) == null) {

				Console.write("The specified area's geoname ID does not exist");
				return;
			}

			// Center ID
			if (!st.hasMoreTokens())
				m_parameters = Handler.getProxyServer().getParameters(m_geonameID);

			else {

				m_centerID = st.nextToken();
				if (Check.registrationCenterID(m_centerID) != null) {

					Console.write("The specified center ID does not exist");
					return;
				}

				String errorMsg = null;
				if ((errorMsg = Check.monitors(m_centerID, m_geonameID)) != null) {

					Console.write(errorMsg);
					return;
				}

				m_parameters = Handler.getProxyServer().getParameters(m_geonameID, m_centerID);
			}

			if (m_parameters == null) {

				Console.write("No available parameters");
				return;
			}

			// Show parameters
			for (Parameter parameter : m_parameters)
				Console.write(parameter.getDate() + " " + parameter.getTime() + " [by " + parameter.getUserID() + "] --- " + parameter.getCategory() + " : " + parameter.getScore() + " (" + parameter.getNotes() + ")");
		}

		catch (NumberFormatException e) {

			Console.write("Incorrect command syntax --> '" + geonameID + "'. Geoname ID should be a number");
		}

		catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}
	}

	public static void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	private static Parameter[] m_parameters;
	private static int m_geonameID;
	private static String m_centerID;
}
