/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;
import imgui.ImGui;

/**
 * To view area info (also parameters info in GUI mode)
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class AreaInfo extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Command c = new Command(args);

		try {

			switch (c.getCmd()) {

				case "id":
					viewByID(c);
					break;
				case "index":
					viewByIndex(c);
					break;
				default:
					Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [id, index]");
					return;
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

		if (Master.getSearchArea().getSelectedArea() == null) {

			Console.error("The selected area should not be null");
			returnToPreviousState();
			return;
		}

		else if (selectedArea != Master.getSearchArea().getSelectedArea()) {

			selectedArea = Master.getSearchArea().getSelectedArea();
			ParameterInfo.resetData();
		}

		panel.setSize(Application.getWidth() / 1.2f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
		panel.setOriginX(panel.getWidth() / 2.0f);
		panel.setPositionX(Application.getWidth() / 2.0f);
		panel.begin(selectedArea.getName() + ", " + selectedArea.getCountryCode());
		ImGui.text("Geoname ID: " + selectedArea.getGeonameID());
		ImGui.text("Country: " + selectedArea.getCountryName() + " (" + selectedArea.getCountryCode() + ")");
		ImGui.text("Coordinates: (" + selectedArea.getLatitude() + ", " + selectedArea.getLongitude() + ")");
		ImGui.newLine();
		ImGui.separator();
		ImGui.newLine();
		ParameterInfo.onGUIRender();
		panel.end();

		cancel.setOriginX(cancel.getWidth() / 2.0f);
		cancel.setPositionX(panel.getPositionX());
		if (cancel.render())
			returnToPreviousState();
	}

	private void viewByID(Command c) throws ConnectionLostException, DatabaseRequestException {

		try {

			int id = Integer.parseInt(c.getArgs().trim());
			Area area = Handler.getProxyServer().getArea(id);

			if (area == null) {

				Console.write("The area with the specified geoname id does not exist");
				return;
			}

			printAreaInfo(area);
		}

		catch (NumberFormatException e) {

			Console.write("The id value must be a valid number");
		}
	}

	private void viewByIndex(Command c) throws ConnectionLostException, DatabaseRequestException {

		try {

			int index = Integer.parseInt(c.getArgs().trim());

			if (Master.getSearchArea().getFoundAreas() == null) {

				Console.write("Before calling this command, you should call 'search area [name, country, coords]' to get a list of selectable areas");
				return;
			}

			Area area = Master.getSearchArea().getFoundAreas()[index];

			printAreaInfo(area);
		}

		catch (NumberFormatException e) {

			Console.write("The id value must be a valid number");
		}

		catch (ArrayIndexOutOfBoundsException e) {

			Console.write("The specified index is invalid");
		}
	}

	private void printAreaInfo(Area area) {

		Console.write("Geoname ID: " + area.getGeonameID());
		Console.write("Name: " + area.getAsciiName());
		Console.write("Country: " + area.getCountryName() + " (" + area.getCountryCode() + ")");
		Console.write("Coordinates: (" + area.getLatitude() + ", " + area.getLongitude() + ")");
	}

	private Area selectedArea = null;
	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
}
