/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.InputText;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;
import imgui.ImGui;

/**
 * To add a new area
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class AreaCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		try {

			String errorMsg = null;

			// Geoname ID
			do {

				String geonameID = Console.read("Geoname ID >").trim();
				errorMsg = Check.geonameID(geonameID);

				if (errorMsg != null)
					Console.write(errorMsg);
				else
					m_geonameID = Integer.parseInt(geonameID);

			} while (errorMsg != null);

			// Name
			do {

				m_name = Console.read("Name >").trim();
				errorMsg = Check.name(m_name);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// ASCII Name
			do {

				m_asciiName = Console.read("ASCII Name >").trim();
				errorMsg = Check.asciiName(m_asciiName);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Country Name
			do {

				m_countryName = Console.read("Country name >").trim();
				errorMsg = Check.countryName(m_countryName);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Country code
			do {

				m_countryCode = Console.read("Country code >").trim();
				errorMsg = Check.countryCode(m_countryCode);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Latitude
			do {

				String latitude = Console.read("Latitude >").trim();
				errorMsg = Check.latitude(latitude);

				if (errorMsg != null)
					Console.write(errorMsg);
				else
					m_latitude = Double.parseDouble(latitude.trim());

			} while (errorMsg != null);

			// Longitude
			do {

				String longitude = Console.read("Longitude >").trim();
				errorMsg = Check.longitude(longitude);

				if (errorMsg != null)
					Console.write(errorMsg);
				else
					m_longitude = Double.parseDouble(longitude.trim());

			} while (errorMsg != null);

			Area area = new Area(m_geonameID, m_name, m_asciiName, m_countryCode, m_countryName, m_latitude, m_longitude);

			Handler.getProxyServer().addArea(area);

			Console.write("The area was registered successfully");
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

		try {

			panel.setSize(Application.getWidth() / 2.0f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
			panel.setOriginX(panel.getWidth() / 2.0f);
			panel.setPositionX(Application.getWidth() / 2.0f);
			panel.begin("Create new area");

			ImGui.newLine();

			// Geoname ID
			geonameIDInputText.setWidth(panel.getWidth() / 2.0f);
			geonameIDInputText.setOriginX(geonameIDInputText.getWidth() / 2.0f);
			geonameIDInputText.setPositionX(panel.getWidth() / 2.0f);
			geonameIDInputText.setNumbersOnly(true);
			geonameIDInputText.setNoSpaces(true);
			geonameIDInputText.render();

			ImGui.newLine();

			// Name
			nameInputText.setWidth(panel.getWidth() / 2.0f);
			nameInputText.setOriginX(nameInputText.getWidth() / 2.0f);
			nameInputText.setPositionX(panel.getWidth() / 2.0f);
			nameInputText.render();

			ImGui.newLine();

			// ASCII name
			asciiNameInputText.setWidth(panel.getWidth() / 2.0f);
			asciiNameInputText.setOriginX(asciiNameInputText.getWidth() / 2.0f);
			asciiNameInputText.setPositionX(panel.getWidth() / 2.0f);
			asciiNameInputText.render();

			ImGui.newLine();

			// Country name
			countryNameInputText.setWidth(panel.getWidth() / 2.0f);
			countryNameInputText.setOriginX(countryNameInputText.getWidth() / 2.0f);
			countryNameInputText.setPositionX(panel.getWidth() / 2.0f);
			countryNameInputText.render();

			ImGui.newLine();

			// Country code
			countryCodeInputText.setWidth(panel.getWidth() / 2.0f);
			countryCodeInputText.setOriginX(countryCodeInputText.getWidth() / 2.0f);
			countryCodeInputText.setPositionX(panel.getWidth() / 2.0f);
			countryCodeInputText.setAlwaysUpperCase(true);
			countryCodeInputText.render();

			ImGui.newLine();

			// Latitude
			latitudeInputText.setWidth(panel.getWidth() / 2.0f);
			latitudeInputText.setOriginX(latitudeInputText.getWidth() / 2.0f);
			latitudeInputText.setPositionX(panel.getWidth() / 2.0f);
			latitudeInputText.setNumbersOnly(true);
			latitudeInputText.setNoSpaces(true);
			latitudeInputText.render();
			
			ImGui.newLine();

			// Longitude
			longitudeInputText.setWidth(panel.getWidth() / 2.0f);
			longitudeInputText.setOriginX(longitudeInputText.getWidth() / 2.0f);
			longitudeInputText.setPositionX(panel.getWidth() / 2.0f);
			longitudeInputText.setNumbersOnly(true);
			longitudeInputText.setNoSpaces(true);
			longitudeInputText.render();

			panel.end();

			cancel.setOriginX(0);
			cancel.setPositionX(panel.getPositionX() - panel.getWidth() / 2.0f);
			if (cancel.render()) {

				resetStateData(new AreaCreation());
				returnToPreviousState();
			}

			ImGui.sameLine();

			confirm.setOriginX(confirm.getWidth());
			confirm.setPositionX(panel.getPositionX() + panel.getWidth() / 2.0f);
			if (confirm.render()) {

				geonameIDResult = CheckMT.geonameID(geonameIDInputText.getString());
				checkData = true;
			}

			if (checkData) {

				int status = checkData();

				// Success
				if (status == 0) {

					checkData = false;

					Area area = new Area(m_geonameID, m_name, m_asciiName, m_countryCode, m_countryName, m_latitude, m_longitude);
					addAreaResult = Handler.getProxyServerMT().addArea(area);
				}

				// Failure
				else if (status > 0)
					checkData = false;
			}

			if (addAreaResult != null && addAreaResult.ready()) {

				addAreaResult.get();
				resetStateData(new AreaCreation());
				returnToPreviousState();
			}

			else if (addAreaResult != null) {

				loadingText.setOriginX(loadingText.getWidth() / 2.0f);
				loadingText.setPositionX(panel.getPositionX());
				loadingText.setPositionY(cancel.getPositionY());
			}
		}

		catch (ConnectionLostException e) {

			setCurrentState(ViewType.CONNECTION);
		}

		catch (Exception e) {

			e.printStackTrace();
			Application.close();
		}
	}

	private int checkData() throws ConnectionLostException, Exception {

		int success = -1;

		if (geonameIDResult != null && geonameIDResult.ready()) {

			success = 0;

			String errorMsg = null;

			// Geoname ID
			errorMsg = geonameIDResult.get();
			if (errorMsg == null) {

				m_geonameID = Integer.valueOf(geonameIDInputText.getString());
				geonameIDInputText.showErrorMsg(false);
			}

			else {

				geonameIDInputText.setErrorMsg(errorMsg);
				geonameIDInputText.showErrorMsg(true);
				success++;
			}

			// Name
			errorMsg = Check.name(nameInputText.getString());
			if (errorMsg == null) {

				m_name = nameInputText.getString();
				nameInputText.showErrorMsg(false);
			}

			else {

				nameInputText.setErrorMsg(errorMsg);
				nameInputText.showErrorMsg(true);
				success++;
			}

			// ASCII name
			errorMsg = Check.asciiName(asciiNameInputText.getString());
			if (errorMsg == null) {

				m_asciiName = asciiNameInputText.getString();
				asciiNameInputText.showErrorMsg(false);
			}

			else {

				asciiNameInputText.setErrorMsg(errorMsg);
				asciiNameInputText.showErrorMsg(true);
				success++;
			}

			// Country name
			errorMsg = Check.countryName(countryNameInputText.getString());
			if (errorMsg == null) {

				m_countryName = countryNameInputText.getString();
				countryNameInputText.showErrorMsg(false);
			}

			else {

				countryNameInputText.setErrorMsg(errorMsg);
				countryNameInputText.showErrorMsg(true);
				success++;
			}

			// Country code
			errorMsg = Check.countryCode(countryCodeInputText.getString());
			if (errorMsg == null) {

				m_countryCode = countryCodeInputText.getString();
				countryCodeInputText.showErrorMsg(false);
			}

			else {

				countryCodeInputText.setErrorMsg(errorMsg);
				countryCodeInputText.showErrorMsg(true);
				success++;
			}

			// Latitude
			errorMsg = Check.latitude(latitudeInputText.getString());
			if (errorMsg == null) {

				double latitude = Double.valueOf(latitudeInputText.getString());
				m_latitude = latitude;
				latitudeInputText.showErrorMsg(false);
			}

			else {

				latitudeInputText.setErrorMsg(errorMsg);
				latitudeInputText.showErrorMsg(true);
				success++;
			}

			// Longitude
			errorMsg = Check.latitude(longitudeInputText.getString());
			if (errorMsg == null) {

				double longitude = Double.valueOf(longitudeInputText.getString());
				m_longitude = longitude;
				longitudeInputText.showErrorMsg(false);
			}

			else {

				longitudeInputText.setErrorMsg(errorMsg);
				longitudeInputText.showErrorMsg(true);
				success++;
			}

			geonameIDResult = null;
		}

		else if (geonameIDInputText != null) {

			loadingText.setOriginX(loadingText.getWidth() / 2.0f);
			loadingText.setPositionX(panel.getPositionX());
			loadingText.setPositionY(cancel.getPositionY());
		}

		return success;
	}

	private int m_geonameID;
	private String m_name;
	private String m_asciiName;
	private String m_countryName;
	private String m_countryCode;
	private double m_latitude;
	private double m_longitude;

	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Button confirm = new Button("Create new area");
	private InputText geonameIDInputText = new InputText("Geoname ID");
	private InputText nameInputText = new InputText("Name");
	private InputText asciiNameInputText = new InputText("ASCII name");
	private InputText countryNameInputText = new InputText("Country name");
	private InputText countryCodeInputText = new InputText("Country code", "", "", 2);
	private InputText latitudeInputText = new InputText("Latitude");
	private InputText longitudeInputText = new InputText("Longitude");
	private Result<String> geonameIDResult;
	private boolean checkData = false;
	private Text loadingText = new Text("Loading...");
	private Result<Boolean> addAreaResult;
}
