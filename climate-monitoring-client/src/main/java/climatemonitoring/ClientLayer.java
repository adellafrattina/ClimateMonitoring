/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Layer;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

class ClientLayer extends Layer {

	public void onAttach() {

		Handler.init();
		Handler.connect();

		if (Application.getConfiguration() == Application.HEADLESS)
			Handler.getView().onHeadlessRender("");
	}

	public void onHeadlessRender() {

		String line = Console.read(">").toLowerCase();
		Command c = new Command(line);
		boolean showView = true;

		switch (c.getCmd()) {
			case CommandType.SEARCH:
				Handler.getView().setCurrentState(ViewType.MASTER);
				break;
			case CommandType.VIEW:
				c = new Command(c.getArgs());
				switch (c.getCmd()) {
					case "area":
						Handler.getView().setCurrentState(ViewType.AREA_INFO);
						break;
					case "center":
						Handler.getView().setCurrentState(ViewType.CENTER_INFO);
						break;
					default:
						Console.write("Incorrect command syntax");
						showView = false;
						break;
				}
				break;
			case CommandType.LOGIN:
				Handler.getView().setCurrentState(ViewType.LOGIN);
				break;
			case CommandType.REGISTER:
				Handler.getView().setCurrentState(ViewType.REGISTRATION);
				break;
			case CommandType.ADD:
				c = new Command(c.getArgs());
				switch(c.getCmd()){
					case "area":
						Handler.getView().setCurrentState(ViewType.AREA_CREATION);
						break;
					case "center":
						Handler.getView().setCurrentState(ViewType.CENTER_CREATION);
						break;
					case "parameter":
						Handler.getView().setCurrentState(ViewType.PARAMETER_CREATION);
						break;
					default:
						Console.write("Incorrect command syntax");
						showView = false;
						break;
				}
				break;
			case CommandType.EDIT:
				c = new Command(c.getArgs());
				switch (c.getCmd()) {
					case "profile":
						Handler.getView().setCurrentState(ViewType.EDIT_PROFILE);
						break;
					default:
						Console.write("Incorrect command syntax");
						showView = false;
						break;
				}
				break;
			case CommandType.SETTINGS:
				Handler.getView().setCurrentState(ViewType.SETTINGS);
				break;
			case CommandType.PING:
				try {
					long ping = Handler.getProxyServer().ping();
					Console.write(ping + "ms");
				}

				catch (ConnectionLostException e) {
					Handler.getView().setCurrentState(ViewType.CONNECTION);
				}
				showView = false;
				break;
			case CommandType.HELP:
				Handler.getView().setCurrentState(ViewType.HELP);
				break;
			case CommandType.EXIT:
				Application.close();
				showView = false;
				break;
			default:
				Console.write("Unknown command");
				showView = false;
				break;
		}

		if(showView)
			Handler.onHeadlessRender(c.getArgs());
	}

	public void onGUIRender() {

		int flags = 0;
		flags |= ImGuiWindowFlags.NoDecoration;
		flags |= ImGuiWindowFlags.NoBackground;
		flags |= ImGuiWindowFlags.NoResize;
		flags |= ImGuiWindowFlags.NoMove;
		ImGui.setNextWindowSize(Application.getWidth(), Application.getHeight());
		ImGui.setNextWindowPos(0.0f, 0.0f);

		ImGui.begin("ClimateMonitoring", flags);
		Handler.onGUIRender();
		ImGui.end();
	}

	public void onDetach() {

		try {
			Handler.getProxyServer().close();
		} catch (ConnectionLostException e) {
			Console.write("Connection lost");
		}
	}
}
