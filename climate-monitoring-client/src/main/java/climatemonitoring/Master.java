/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;
import climatemonitoring.core.Application;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Popup;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

/**
 * The main view. Mainly to search areas (and centers in Headless mode)
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class Master extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Command c = new Command(args);

		try {

			// <what>
			switch (c.getCmd()) {

				case "area":
					c = new Command(c.getArgs());
					getSearchArea().onHeadlessRender(c.getCmd(), c.getArgs());
					break;
				case "center":
					c = new Command(c.getArgs());
					SearchCenter.onHeadlessRender(c.getCmd(), c.getArgs());
					break;
				default:
					Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [area, center]");
					break;
			}
		}

		catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			setCurrentState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		ImGui.sameLine();

		if (Handler.isOperatorLoggedIn()) {

			if (optionsButton == null)
				optionsButton = new Button("(" + Handler.getLoggedOperator().getUserID() + ")");

			optionsButton.setOriginX(optionsButton.getWidth());
			optionsButton.setPositionX(Application.getWidth() - 10.0f);

			if (optionsButton.render())
				optionsPopup.open();

			int currentItem = optionsPopup.render();
			if (currentItem != -1) {

				switch (options[currentItem]) {

					case "Edit profile":
						setCurrentState(ViewType.EDIT_PROFILE);
						break;
					case "Center info":
						getCenterResult = Handler.getProxyServerMT().getCenter(Handler.getLoggedOperator().getCenterID());
						break;
					case "Logout":
						optionsButton = null;
						Handler.setLoggedOperator(null);
						break;
				}
			}
		}

		else {

			loginButton.setOriginX(loginButton.getWidth());
			loginButton.setPositionX(Application.getWidth() - 10.0f);
			if (loginButton.render())
				setCurrentState(ViewType.LOGIN);
		}

		ImGui.separator();
		ImGui.newLine();
		panel.setSize(Application.getWidth() / 2.0f, Application.getHeight() / 1.2f);
		panel.setOriginX(panel.getWidth() / 2.0f);
		panel.setPositionX(Application.getWidth() / 2.0f);
		panel.begin(null);
		getSearchArea().onGUIRender();
		if (getSearchArea().isAnyAreaSelected())
			setCurrentState(ViewType.AREA_INFO);
		panel.end();

		try {

			if (getCenterResult != null && getCenterResult.ready()) {

				CenterInfo ci = (CenterInfo) getView().getState(ViewType.CENTER_INFO);
				ci.center = getCenterResult.get();
				setCurrentState(ViewType.CENTER_INFO);
				getCenterResult = null;
			}
		}

		catch (ConnectionLostException e) {

			resetStateData(new Master());
			setCurrentState(ViewType.CONNECTION);
		}

		catch (Exception e) {

			e.printStackTrace();
			Application.close();
		}
	}

	public static SearchArea getSearchArea() {

		return searchArea;
	}

	private static SearchArea searchArea = new SearchArea();

	private Panel panel = new Panel();
	private Button loginButton = new Button(" Login ");
	private Button optionsButton = null;
	private String[] options = new String[] { "Edit profile", "Center info", "Logout" };
	private Popup optionsPopup = new Popup("Options", options);
	private Result<Center> getCenterResult;
}
