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
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

class Master extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Command c = new Command(args);

		try {

			// <what>
			switch (c.getCmd()) {

				case "area":
					c = new Command(c.getArgs());
					searchArea(c);
					break;
				case "center":
					c = new Command(c.getArgs());
					searchCenter(c);
					break;
				default:
					Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [area, center]");
					break;
			}
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

	private void searchArea(Command c) throws ConnectionLostException, DatabaseRequestException {

		try {

			foundAreas = null;

			// <by>
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
	
			int i = 0;
			if (foundAreas != null)
				for (Area area : foundAreas)
					Console.write(i++ + ". " + area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());
			else
				Console.write("No matching areas");
		}

		catch (NumberFormatException e) {

			Console.write("The <latitude, longitude> field must be a couple of floating point numbers");
		}
	}

	private void searchCenter(Command c) throws ConnectionLostException, DatabaseRequestException {

		foundCenters = null;

		// <by>
		switch (c.getCmd()) {

			case "name":
				foundCenters = Handler.getProxyServer().searchCentersByName(c.getArgs());
				break;
			default:
				Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [name]");
				return;
		}

		if (foundCenters != null) {

			int i = 0;
			for (Center center : foundCenters) {

				Area area = Handler.getProxyServer().getArea(center.getCity());
				Console.write(i++ + ". " + center.getCenterID() + " - (" + area.getGeonameID() + ") " + area.getAsciiName() + " " + center.getStreet() + ", " + center.getHouseNumber());
			}
		}
		else
			Console.write("No matching centers");
	}

	Area[] foundAreas;
	Area[] selectedArea;
	Center[] foundCenters;
	Center[] selectedCenter;
}
