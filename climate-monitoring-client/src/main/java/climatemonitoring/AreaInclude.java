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
import climatemonitoring.core.headless.Console;

public class AreaInclude {

	public static void onHeadlessRender(String args) {

		if (!Handler.isOperatorLoggedIn()) {

			Console.write("You must be a logged operator to include an area into a center");
			return;
		}

		String geonameID = null;

		try {

			StringTokenizer st = new StringTokenizer(args);

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

			String errorMsg = null;
			if ((errorMsg = Check.monitors(Handler.getLoggedOperator().getCenterID(), m_geonameID)) != null) {

				Console.write(errorMsg);
				return;
			}

			Handler.getProxyServer().includeAreaToCenter(m_geonameID, Handler.getLoggedOperator().getCenterID());
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

	private static int m_geonameID;
}
