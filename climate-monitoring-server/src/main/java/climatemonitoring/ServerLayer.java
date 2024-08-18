/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import climatemonitoring.core.Layer;
import climatemonitoring.core.headless.Console;

/**
 * The actual server application
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
class ServerLayer extends Layer {

	/**
	 * Code to be executed as the layer gets created
	 */
	public void onAttach() {

		String url = Console.read();
		String username = Console.read();
		String password = Console.read();

		try {

			m_server = new ServerSocket(25565);
			m_serverDatabase = new ServerDatabaseImpl(url, username, password);
		}

		catch (IOException e) {

			Console.write("Server starting failed");
			e.printStackTrace();
		}
	}

	/**
	 * Code that runs on every iteration of the main loop
	 */
	public void onUpdate() {

		try {

			Socket client = m_server.accept();
			new Skeleton(client, m_serverDatabase);
		}

		catch (IOException ex) {

			Console.write("Client binding failed");
		}
	}

	/**
	 * Code that runs in headless configuration only
	 */
	public void onHeadlessRender() {


	}

	/**
	 * Code that runs in GUI configuration only
	 */
	public void onGUIRender() {


	}

	/**
	 * Code to be executed as the layer gets deleted
	 */
	public void onDetach() {

		m_serverDatabase.shutdown();
	}

	private ServerSocket m_server;
	private ServerDatabase m_serverDatabase;
}
