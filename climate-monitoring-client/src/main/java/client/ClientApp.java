/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import climatemonitoring.core.Application;
import climatemonitoring.core.ApplicationSpecification;
import climatemonitoring.core.gui.ApplicationGUI;
import climatemonitoring.core.headless.ApplicationHeadless;

public class ClientApp {

	public static void main(String[] args) {

		Application app = null;
		ApplicationSpecification spec = new ApplicationSpecification();
		if (args == null || args.length == 0 || !args[0].equals("nogui"))
			app = new ApplicationGUI(spec);

		else
			app = new ApplicationHeadless(spec);

		app.pushLayer(new ClientLayer());

		app.run();
		app.shutdown();
	}
}
