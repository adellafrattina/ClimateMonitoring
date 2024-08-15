package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.ApplicationSpecification;
import climatemonitoring.core.gui.ApplicationGUI;

class ClientApp {

	public static void main(String[] args) {
		
		Application app = new ApplicationGUI(new ApplicationSpecification());
		app.pushLayer(new ClientLayer());
		app.run();
		app.shutdown();
	}
}
