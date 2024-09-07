/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Layer;
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Tooltip;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

class ClientLayer extends Layer {

	public void onAttach() {

		Handler.init();

		if (Application.getConfiguration() == Application.GUI)
			Resources.init();
	}

	public void onHeadlessRender() {

		// If the view type is connection, execute the view without waiting for commands
		if (Handler.getView().getCurrentStateIndex() == ViewType.CONNECTION)
			Handler.getView().onHeadlessRender("");

		String line = Console.read("> ").toLowerCase();
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
					case "parameter":
						ParameterInfo.onHeadlessRender(c.getArgs());
						showView = false;
						break;
					default:
						Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [area, center, parameter]");
						showView = false;
						break;
				}
				break;
			case CommandType.LOGIN:
				Handler.getView().setCurrentState(ViewType.LOGIN);
				break;
			case CommandType.LOGOUT:
				Handler.setLoggedOperator(null);
				showView = false;
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
						Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [area, center, parameter]");
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
						Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [profile]");
						showView = false;
						break;
				}
				break;
			case CommandType.INCLUDE:
				c = new Command(c.getArgs());
				switch (c.getCmd()) {
					case "area":
						AreaInclude.onHeadlessRender(c.getArgs());
						showView = false;
						break;
					default:
						Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [area]");
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
				Console.write("Unknown command. Type 'help' to get a command list");
				showView = false;
				break;
		}

		if(showView)
			Handler.onHeadlessRender(c.getArgs());
	}

	public void onGUIRender() {

		int flags = 0;
		flags |= ImGuiWindowFlags.NoDecoration;
		//flags |= ImGuiWindowFlags.NoBackground;
		flags |= ImGuiWindowFlags.NoResize;
		flags |= ImGuiWindowFlags.NoMove;
		flags |= ImGuiWindowFlags.NoNav;
		flags |= ImGuiWindowFlags.NoNavFocus;
		flags |= ImGuiWindowFlags.NoNavInputs;
		ImGui.setNextWindowSize(Application.getWidth(), Application.getHeight());
		ImGui.setNextWindowPos(0.0f, 0.0f);

		ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ChildBorderSize, 0.0f);
		ImGui.begin("ClimateMonitoring", flags);
		ImGui.setCursorPos(0.0f, 0.0f);
		ImGui.image(Resources.getTexture(Resources.MAP).getID(), Application.getWidth(), Application.getHeight(), 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, 0.5f, 0.5f, 0.4f);
		ImGui.end();
		ImGui.popStyleVar(3);

		ImGui.setNextWindowSize(Application.getWidth(), Application.getHeight());
		ImGui.setNextWindowPos(0.0f, 0.0f);

		ImGui.begin("ClimateMonitoring", flags);
		ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 5.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 5.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 5.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 5.0f);
		ImGui.setCursorPos(ImGui.getStyle().getWindowPaddingX(), ImGui.getStyle().getWindowPaddingY());
		renderSettingsButton();
		Handler.onGUIRender();
		ImGui.popStyleVar(4);
		ImGui.end();
	}

	public void onDetach() {

		try {
			Handler.getProxyServer().close();
		} catch (ConnectionLostException e) {
			Console.write("Connection lost");
		}
	}

	private void renderSettingsButton() {

		settingsButton.setTexture(Resources.getTexture(Resources.GEAR).getID());
		ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 0.0f, 0.0f, 0.0f);
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.2f, 0.5f);
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.2f, 0.2f, 0.8f);
		if (settingsButton.render()) {

			Handler.getView().setCurrentState(ViewType.SETTINGS);
		}
		ImGui.popStyleColor(3);

		if (Handler.getView().getCurrentStateIndex() == ViewType.CONNECTION ||
			(Handler.getView().getPreviousStateIndex() == ViewType.CONNECTION &&
			Handler.getView().getCurrentStateIndex() == ViewType.SETTINGS))

		{

			ping = Long.MAX_VALUE;
			resetPing = true;
		}

		else if (resetPing) {

			ping = 0;
			resetPing = false;
		}

		ImGui.sameLine();
		pingHint.render();

		if (ping != Long.MAX_VALUE) {

			try {

				if (System.currentTimeMillis() - time > 1000) {
	
					pingResult = Handler.getProxyServerMT().ping();
					time = System.currentTimeMillis();
				}

				if (pingResult != null && pingResult.ready())
						ping = pingResult.get();
			}
	
			catch (ConnectionLostException e) {
	
				Handler.getView().setCurrentState(ViewType.CONNECTION);
			}
	
			catch (Exception e) {
	
				e.printStackTrace();
				Application.close();
			}

			ImGui.sameLine();
			if (ping == 0)
				ImGui.textColored(53, 119, 206, 255, "ping: " + ping + "ms");
			else if (ping <= 50)
				ImGui.textColored(35, 165, 89, 255, "ping: " + ping + "ms");
			else if (ping <= 100)
				ImGui.textColored(191, 134, 28, 255, "ping: " + ping + "ms");
			else
				ImGui.textColored(242, 63, 66, 255, "ping: " + ping + "ms");
		}

		else {

			ImGui.sameLine();
			ImGui.text("ping: " + Float.NaN);
		}
	}

	private Button settingsButton = new Button("##");
	private Result<Long> pingResult;
	long time = 0;
	long ping = Long.MAX_VALUE;
	boolean resetPing = true;
	private Tooltip pingHint = new Tooltip("(?)",
	"""
		Ping (latency is the technically more correct term)
		refers the time it takes for a small data set to be transmitted
		from your device to a server on the Internet (or locally) and back to your device again.
		Ping is measured in milliseconds (ms)
	""");
}
