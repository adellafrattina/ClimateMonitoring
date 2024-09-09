/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

/**
 * To show a list of commands or a simplified manual
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public class Help extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Console.write("\n--- Available Commands ---\n");

		Console.write("SEARCH > To retrieve a list of areas or centers with user-defined filters");
		Console.write("search <what> <by> <args>");
		Console.write("<what>: [area, center]");
		Console.write("<by>: <[name, country, coords], [name]>");
		Console.write("<args>: <str, str, (latitude, longitude)>\n");

		Console.write("VIEW > To show detailed infos about areas and centers");
		Console.write("view <what> <by> <args>");
		Console.write("<what>: [area, center]");
		Console.write("<by>: [id, index]");
		Console.write("<args>: <geoname_id, center_id>\n");

		Console.write("LOGIN > To access user-restricted functions, registration needed");
		Console.write("login <user_id> <password>\n");

		Console.write("LOGOUT > To go back to a default user");
		Console.write("logout\n");

		Console.write("REGISTER > To register as an operator");
		Console.write("register\n");

		Console.write("ADD > To add a new area, center or parameter, registration needed");
		Console.write("add <what>");
		Console.write("<what>: [area, center, parameter]\n");

		Console.write("EDIT > To edit profile information, registration needed");
		Console.write("edit <profile>\n");

		Console.write("INCLUDE > To set an area to be monitored by the user's center, registration needed");
		Console.write("include <what> <args>");
		Console.write("<what>: [area]");
		Console.write("<args>: <geoname_id>\n");

		Console.write("SETTINGS > To change the visual style of the application");
		Console.write("settings <setting> <args>");
		Console.write("<setting>: [theme]");
		Console.write("<args>: <classic, light, dark>\n");

		Console.write("PING > To check the latency between the client and the server");
		Console.write("ping\n");

		Console.write("HELP > To show a list of available commands");
		Console.write("help\n");

		Console.write("EXIT > To exit the application");
		Console.write("exit\n");
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
