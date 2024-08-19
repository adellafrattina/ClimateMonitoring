package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.ErrorType;
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
					double latitude = Double.parseDouble(coords[0]);
					double longitude = Double.parseDouble(coords[1]);
					foundAreas = Handler.getProxyServer().searchAreasByCoords(latitude, longitude);
					break;
				default:
					Console.write("Incorrent command syntax -->'" + c.getCmd() + "', expected [name, country, coords]");
					return;
			}

			for (Area area : foundAreas)
				Console.write(area.getGeonameID() + " - " + area.getName() + "(" + area.getAsciiName() + "), " + area.getCountryCode());
		}

		catch (NumberFormatException e) {

			Console.write("The <latitude, longitude> field must be a couple of floating point numbers");
		}

		catch (DatabaseRequestException e) {

			Console.write(e.getErrorMsg(ErrorType.GEONAME_ID));
		}

		catch (ConnectionLostException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
