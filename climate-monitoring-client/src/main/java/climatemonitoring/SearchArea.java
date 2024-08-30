/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.headless.Console;

/**
 * To search an area
 * 
 * @author adellafrattina
 * @verison 1.0-SNAPSHOT
 */
class SearchArea {

	public synchronized static void onHeadlessRender(final String by, final String args) throws ConnectionLostException, DatabaseRequestException {

		try {

			m_foundAreas = null;

			// <by>
			switch (by) {
	
				case "name":
					m_foundAreas = Handler.getProxyServer().searchAreasByName(args);
					break;
				case "country":
					m_foundAreas = Handler.getProxyServer().searchAreasByCountry(args);
					break;
				case "coords":
					String[] coords = args.split(" ");
					if (coords.length < 2)
						throw new NumberFormatException();
					double latitude = Double.parseDouble(coords[0]);
					double longitude = Double.parseDouble(coords[1]);
					m_foundAreas = Handler.getProxyServer().searchAreasByCoords(latitude, longitude);
					break;
				default:
					Console.write("Incorrect command syntax -->'" + by + "', expected [name, country, coords]");
					return;
			}

			int i = 0;
			if (m_foundAreas != null)
				for (Area area : m_foundAreas)
					Console.write(i++ + ". " + area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());
			else
				Console.write("No matching areas");
		}

		catch (NumberFormatException e) {

			Console.write("The <latitude, longitude> field must be a couple of floating point numbers");
		}
	}

	public synchronized static void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	public static synchronized Area[] getFoundAreas() {

		return m_foundAreas;
	}

	public static synchronized Area getSelectedArea() {

		return m_selectedArea;
	}

	private static Area[] m_foundAreas;
	private static Area m_selectedArea;
}
