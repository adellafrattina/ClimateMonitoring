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

class Master extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Command c = new Command(args);

		try {

			Area[] foundAreas = null;
			switch (c.getCmd()) {

				case "name":
					foundAreas = Handler.getProxyServer().searchAreasByName(c.getArgs());
					break;
				case "country":
					foundAreas = Handler.getProxyServer().searchAreasByCountry(c.getArgs());
					break;
				case "coords":
					String[] coords = c.getArgs().split(" ");
					if (coords.length < 2)
						throw new NumberFormatException();
					double latitude = Double.parseDouble(coords[0]);
					double longitude = Double.parseDouble(coords[1]);
					foundAreas = Handler.getProxyServer().searchAreasByCoords(latitude, longitude);
					break;
				default:
					Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [name, country, coords]");
					return;
			}

			Handler.setFoundAreas(foundAreas);

			if (foundAreas != null)
				for (Area area : foundAreas)
					Console.write(area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());
			else
				Console.write("No matching areas");
		}

		catch (NumberFormatException e) {

			Console.write("The <latitude, longitude> field must be a couple of floating point numbers");
		}

		catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			Handler.setViewState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
