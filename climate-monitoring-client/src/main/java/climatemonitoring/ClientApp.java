package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.ApplicationSpecification;
import climatemonitoring.core.gui.ApplicationGUI;
import climatemonitoring.core.headless.ApplicationHeadless;

class ClientApp {

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
