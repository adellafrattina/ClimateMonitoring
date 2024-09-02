/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.StringTokenizer;

import climatemonitoring.core.Category;
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

			// view parameter area
			StringTokenizer st = new StringTokenizer(args);

			// Geoname ID
			if (!st.hasMoreTokens()) {

				Console.write("Incorrect command syntax. Expected a valid area's geoname ID");
				return;
			}

			geonameID = st.nextToken().trim();
			m_geonameID = Integer.parseInt(geonameID);

			if (Handler.getProxyServer().getLatestCenter(m_geonameID) == null) {

				Console.write("This area does not have any parameters / is not monitored by any center");
				return;
			}

			Console.write("(You can leave blank to get the latest parameters)");

			String errorMsg = null;

			// Center ID
			do {

				errorMsg = null;
				String centerID = Console.read("Center ID > ").trim().toLowerCase();

				if (Check.isEmpty(centerID) != null) {

					m_centerID = Handler.getProxyServer().getLatestCenter(m_geonameID).getCenterID();
					break;
				}

				if ((errorMsg = Check.registrationCenterID(centerID)) != null) {

					Console.write(errorMsg);
					continue;
				}

				if ((errorMsg = Check.monitors(centerID, m_geonameID)) != null) {

					Console.write(errorMsg);
					continue;
				}

				if (errorMsg == null)
					m_centerID = Handler.getProxyServer().getCenter(centerID).getCenterID();

			} while (errorMsg != null);

			// Category
			do {

				errorMsg = null;
				String category = Console.read("Category > ").trim().toLowerCase();

				if (Check.isEmpty(category) != null) {

					m_category = Handler.getProxyServer().getLatestCategory(m_geonameID, m_centerID).getCategory();
					break;
				}

				if ((errorMsg = Check.category(category)) == null) {

					Category[] categories = Handler.getProxyServer().getCategories();
					for (Category c : categories) {

						if (c.getCategory().trim().toLowerCase().equals(category)) {

							m_category = c.getCategory();
							break;
						}
					}

					break;
				}

				else
					Console.write(errorMsg);

			} while (errorMsg != null);

			m_parameters = Handler.getProxyServer().getParameters(m_geonameID, m_centerID, m_category);

			if (m_parameters == null || m_parameters.length == 0) {

				Console.write("No available parameters");
				return;
			}

			Console.write("--- " + Handler.getProxyServer().getArea(m_geonameID).getAsciiName() + " parameters ---");
			Console.write("From " + m_centerID);
			Console.write("Grouped by " + m_category);
			Console.write("Count: " + m_parameters.length);
			Console.write("Average: " + Handler.getProxyServer().getParametersAverage(m_geonameID, m_centerID, m_category));

			// Show parameters
			for (Parameter parameter : m_parameters)
				Console.write("[" + parameter.getDate() + " " + parameter.getTime() + "] [by " + parameter.getUserID() + "] --- " + parameter.getScore() + " (" + parameter.getNotes() + ")");
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
	private static String m_category;
}
