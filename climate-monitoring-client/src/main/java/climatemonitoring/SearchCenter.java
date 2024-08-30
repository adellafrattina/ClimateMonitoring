/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.headless.Console;

/**
 * To search a center
 * 
 * @author adellafrattina
 * @verison 1.0-SNAPSHOT
 */
class SearchCenter {

	public synchronized static void onHeadlessRender(final String by, final String args) throws ConnectionLostException, DatabaseRequestException {

		m_foundCenters = null;

		// <by>
		switch (by) {

			case "name":
				m_foundCenters = Handler.getProxyServer().searchCentersByName(args);
				break;
			default:
				Console.write("Incorrect command syntax -->'" + by + "', expected [name]");
				return;
		}

		if (m_foundCenters != null && m_foundCenters.length != 0) {

			int i = 0;
			for (Center center : m_foundCenters) {

				Area area = Handler.getProxyServer().getArea(center.getCity());
				Console.write(i++ + ". " + center.getCenterID() + " - (" + area.getGeonameID() + ") " + area.getAsciiName() + " " + center.getStreet() + ", " + center.getHouseNumber());
			}
		}

		else
			Console.write("No matching centers");
	}

	public synchronized static void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	public static synchronized Center[] getFoundCenters() {

		return m_foundCenters;
	}

	public static synchronized Center getSelectedCenter() {

		return m_selectedCenter;
	}

	private static Center[] m_foundCenters;
	private static Center m_selectedCenter;
}
