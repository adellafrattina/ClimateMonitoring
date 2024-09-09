/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package server;

import climatemonitoring.core.Application;
import climatemonitoring.core.ApplicationSpecification;
import climatemonitoring.core.gui.ApplicationGUI;
import climatemonitoring.core.headless.ApplicationHeadless;

/**
 * The application's entry point
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public class ServerApp {

	public static void main(String[] args) {

		Application serverApp = null;
		ApplicationSpecification serverSpec = new ApplicationSpecification();
		
		if (args == null || args.length == 0 || !args[0].equals("nogui"))
			serverApp = new ApplicationGUI(serverSpec);

		else
			serverApp = new ApplicationHeadless(serverSpec);

		serverApp.pushLayer(new ServerLayer());
		serverApp.run();

		serverApp.shutdown();
	}
}
