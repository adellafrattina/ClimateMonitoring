/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

/**
 * To add a new parameter
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class ParameterCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		if (!Handler.isOperatorLoggedIn()) {

			Console.write("You must be a logged operator to add a new parameter");
			return;
		}

		try {

			Area[] monitoredAreas = Handler.getProxyServer().getMonitoredAreas(Handler.getLoggedOperator().getCenterID());

			Console.write("\n--- Available areas ---");
			for (Area area : monitoredAreas)
				Console.write(area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());

			Console.write("\n");

			String errorMsg = null;

			// Geoname ID
			do {

				errorMsg = null;

				try {

					m_geonameID = Integer.parseInt(Console.read("Area's geoname ID > ").trim());
					errorMsg = Check.monitors(Handler.getLoggedOperator().getCenterID(), m_geonameID);
				}

				catch (NumberFormatException e) {

					errorMsg ="The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Print all categories
			Category[] categories = null;

			categories = Handler.getProxyServer().getCategories();
			Console.write("\n--- Available categories ---");
			for (Category category : categories)
				Console.write(category.getCategory() + " - " + category.getExplanation());

			Console.write("\n");

			// Category
			do {

				errorMsg = null;

				m_category = Console.read("Category > ").trim().toLowerCase();
				boolean found = false;
				for (Category category : categories) {

					if (category.getCategory().trim().toLowerCase().equals(m_category)) {

						m_category = category.getCategory();
						found = true;
						break;
					}
				}

				if (!found)
					errorMsg = "The value must be a valid category";

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Score
			do {

				errorMsg = null;

				try {

					m_score = Integer.parseInt(Console.read("Score (between 1 and 5) > ").trim());
					errorMsg = Check.score(m_score);
				}

				catch (NumberFormatException e) {

					errorMsg ="The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Notes
			do {

				errorMsg = null;

				m_notes = Console.read("Notes (max 256 characters) > ").trim();
				errorMsg = Check.notes(m_notes);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			Parameter parameter = new Parameter(
				m_geonameID,
				Handler.getLoggedOperator().getCenterID(),
				Handler.getLoggedOperator().getUserID(),
				m_category,
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				new SimpleDateFormat("hh:mm:ss").format(new Date()),
				m_score,
				m_notes);

			Handler.getProxyServer().addParameter(parameter);

			Console.write("The parameter was registered successfully");
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
	private String m_category;
	private int m_score;
	private String m_notes;
}
