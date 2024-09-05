/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;

//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.UnknownHostException;

import climatemonitoring.core.Application;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;

/**
 * To change the application settings
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
class Settings extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		
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
			String[] address = inputAddress.getString().split(":");
			if (address.length >= 2) {

				/*try {

					InetAddress ip = InetAddress.getByName(address[0]);
					long portL = Long.valueOf(address[1]);
				}

				catch (UnknownHostException e) {

					inputAddress.setErrorMsg("Unknown host");
					inputAddress.showErrorMsg(true);
				}

				catch (NumberFormatException e) {

					inputAddress.setErrorMsg("Port must be a number");
					inputAddress.showErrorMsg(true);
				}*/
			}

			else {

				inputAddress.setErrorMsg("Wrong format");
				inputAddress.showErrorMsg(true);
			}
		}

		panel.end();

		cancel.setOriginX(cancel.getWidth() / 2.0f);
		cancel.setPositionX(panel.getPositionX());
		if (cancel.render())
			returnToPreviousState();
	}

	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Dropdown selectTheme = new Dropdown("Select theme: ", new String[] { "Classic", "Dark", "Light" });
	private Text basic = new Text("Basic");
	private Text advanced = new Text("Advanced");
	private InputTextButton inputAddress = new InputTextButton("Change server ip (format: [address:port])");
}
