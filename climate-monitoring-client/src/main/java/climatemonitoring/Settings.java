/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import climatemonitoring.core.Application;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

/**
 * To change the application settings
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class Settings extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Command c = new Command(args);
		switch (c.getCmd()) {

			case "address":
				try {

					setNewServerAddress(c.getArgs().trim());
				}

				catch (Exception e) {

					Console.write(e.getMessage());
				}
				break;
			default:
				Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [address]");
					return;
		}
	}

	@Override
	public void onGUIRender() {

		panel.setSize(Application.getWidth() / 3.0f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
		panel.setOriginX(panel.getWidth() / 2.0f);
		panel.setPositionX(Application.getWidth() / 2.0f);
		panel.begin("Settings");

		ImGui.newLine();
		basic.setOriginX(basic.getWidth() / 2.0f);
		basic.setPositionX(panel.getWidth() / 2.0f);
		basic.render();
		ImGui.separator();
		ImGui.newLine();
		selectTheme.setOriginX(selectTheme.getWidth());
		selectTheme.setPositionX(panel.getPositionX());
		int currentItem = selectTheme.render();

		switch (selectTheme.getList()[currentItem]) {

			case "Classic":
				ImGui.styleColorsDark();
				break;
			case "Dark":
				ImGui.styleColorsClassic();
				break;
			case "Light":
				ImGui.styleColorsLight();
				break;
		}

		ImGui.newLine();
		ImGui.newLine();
		advanced.setOriginX(advanced.getWidth() / 2.0f);
		advanced.setPositionX(panel.getWidth() / 2.0f);
		advanced.render();
		ImGui.separator();
		ImGui.newLine();

		inputAddress.setOriginX(inputAddress.getWidth() / 2.0f);
		inputAddress.setPositionX(panel.getWidth() / 2.0f);
		inputAddress.setNoSpaces(true);
		inputAddress.render();
		if (inputAddress.isButtonPressed()) {

			inputAddress.showErrorMsg(false);

			try {

				setNewServerAddress(inputAddress.getString());
			}

			catch (Exception e) {

				inputAddress.setErrorMsg(e.getMessage());
				inputAddress.showErrorMsg(true);
			}
		}

		panel.end();

		cancel.setOriginX(cancel.getWidth() / 2.0f);
		cancel.setPositionX(panel.getPositionX());
		if (cancel.render())
			returnToPreviousState();
	}

	private void setNewServerAddress(String address) throws UnknownHostException, NumberFormatException, IOException {

		String[] ip_port = address.split(":");

		if (ip_port.length < 2)
			throw new IOException("Wrong format");

		try {

			InetAddress ip = InetAddress.getByName(ip_port[0]);
			int port = 0;
			if (ip_port[1].trim().length() < 6)
				port = Integer.valueOf(ip_port[1]);
	
			Handler.setNewServerAddress(ip, (short)port);
			resetStateData(new Connection());
			setCurrentState(ViewType.MASTER);
			setCurrentState(ViewType.CONNECTION);
		}

		catch (UnknownHostException e) {

			throw new UnknownHostException("Unknown host");
		}

		catch (NumberFormatException e) {

			throw new NumberFormatException("Port must be a number");
		}
	}

	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Dropdown selectTheme = new Dropdown("Select theme: ", new String[] { "Classic", "Dark", "Light" });
	private Text basic = new Text("Basic");
	private Text advanced = new Text("Advanced");
	private InputTextButton inputAddress = new InputTextButton("Change server ip (format: [address:port])");
}
