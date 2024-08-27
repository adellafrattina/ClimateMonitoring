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
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

class AreaCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		try {

			String errorMsg = null;

			// Geoname ID
			do {

				try {

					m_geonameID = Integer.parseInt(Console.read("Geoname ID >").trim());
					errorMsg = Check.geonameID(m_geonameID);
				}

				catch (NumberFormatException e) {

					errorMsg = "The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Name
			do {

				m_name = Console.read("Name >").trim();
				errorMsg = Check.name(m_name);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// ASCII Name
			do {

				m_asciiName = Console.read("ASCII Name >").trim();
				errorMsg = Check.asciiName(m_asciiName);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Country Name
			do {

				m_countryName = Console.read("Country name >").trim();
				errorMsg = Check.countryName(m_countryName);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Country code
			do {

				m_countryCode = Console.read("Country code >").trim();
				errorMsg = Check.countryCode(m_countryCode);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Latitude
			do {

				try {

					m_latitude = Double.parseDouble(Console.read("Latitude >").trim());
					errorMsg = Check.latitude(m_latitude);
				}

				catch (NumberFormatException e) {

					errorMsg = "The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Latitude
			do {

				try {

					m_longitude = Double.parseDouble(Console.read("Longitude >").trim());
					errorMsg = Check.latitude(m_longitude);
				}

				catch (NumberFormatException e) {

					errorMsg = "The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			Area area = new Area(m_geonameID, m_name, m_asciiName, m_countryCode, m_countryName, m_latitude, m_longitude);

			Handler.getProxyServer().addArea(area);

			Console.write("The area was registered successfully");
		}

		catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			setCurrentState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	private int m_geonameID;
	private String m_name;
	private String m_asciiName;
	private String m_countryName;
	private String m_countryCode;
	private double m_latitude;
	private double m_longitude;
}
