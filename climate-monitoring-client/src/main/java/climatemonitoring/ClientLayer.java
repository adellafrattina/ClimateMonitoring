package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Layer;
import climatemonitoring.core.headless.Console;

class ClientLayer extends Layer {

	public void onAttach() {

		Handler.init();
		Handler.connect();
	}

	public void onHeadlessRender() {

		String line = Console.read(">").toLowerCase();
		Command c = new Command(line);
		boolean showView = true; 

		switch (c.getCmd()) {
			case Command.SEARCH:
				Handler.setViewState(ViewType.MASTER);
				break;
			case Command.VIEW:
				c = new Command(c.getArgs());
				switch (c.getCmd()) {
					case "area":
						Handler.setViewState(ViewType.AREA_INFO);
						break;
					case "center":
						Handler.setViewState(ViewType.CENTER_INFO);
						break;
					default:
						Console.write("Incorrent command syntax");
						showView = false;
						break;
				}
			break;
			case Command.LOGIN:
				Handler.setViewState(ViewType.LOGIN);
				break;
			case Command.REGISTER:
				Handler.setViewState(ViewType.REGISTRATION);
				break;
			case Command.ADD:
				c = new Command(c.getArgs());
				switch(c.getCmd()){
					case "area":
						Handler.setViewState(ViewType.AREA_CREATION);
						break;
					case "center":
						Handler.setViewState(ViewType.CENTER_CREATION);
						break;
					case "parameter":
						Handler.setViewState(ViewType.PARAMETER_CREATION);
						break;
					default:
						Console.write("Incorrent command syntax");
						showView = false;
						break;
				}
			break;
			case Command.EDIT:
				c = new Command(c.getArgs());
				switch (c.getCmd()) {
					case "profile":
						Handler.setViewState(ViewType.EDIT_PROFILE);
						break;
					default:
						Console.write("Incorrent command syntax");
						showView = false;
						break;
				}
			break;
			case Command.SETTINGS:
				Handler.setViewState(ViewType.SETTINGS);
				break;
			case Command.PING:
				try {
					long ping = Handler.getProxyServer().ping();
					Console.write(ping + "ms");
				} catch (ConnectionLostException e) {
					e.printStackTrace();
				}
				break;
			case Command.HELP:
				//TODO: list of commands
				break;
			case Command.EXIT:
				Application.close();
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

		Handler.onGUIRender();
	}

	public void onDetach() {

		try {
			Handler.getProxyServer().close();
		} catch (ConnectionLostException e) {
			e.printStackTrace();
		}
	}
}
