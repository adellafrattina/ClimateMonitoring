/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import climatemonitoring.core.Application;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.InputText;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;
import imgui.ImGui;

/**
 * To add a new center
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public class CenterCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		String errorMsg = null;

		try {

			do {

				m_centerID = Console.read("Center ID > ");
				errorMsg = Check.creationCenterID(m_centerID);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String city = Console.read("City's geoname ID > ");
				String houseNumber = Console.read("House number > ");
				m_street = Console.read("Street > ");

				String[] errorMsgs = Check.address(city, m_street, houseNumber);

				if ((errorMsg = errorMsgs[0]) == null)
					m_city = Integer.parseInt(city);

				if ((errorMsg = errorMsgs[1]) == null)
					m_houseNumber = Integer.parseInt(houseNumber);

				if (errorMsgs[2] == null)
					errorMsg = errorMsgs[2];

				if (errorMsgs[3] == null)
					errorMsg = errorMsgs[3];

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				errorMsg = null;

				try {
					m_postalCode = Integer.parseInt(Console.read("Postal code > "));
				} catch (NumberFormatException e) {
					errorMsg = "Postal code must be a number";
					Console.write(errorMsg);
				}

			} while (errorMsg != null);

			do {

				m_district = Console.read("District > ");
				errorMsg = Check.district(m_district);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			newCenter = new Center(m_centerID, m_street, m_houseNumber, m_postalCode, m_city, m_district);

			if (getView().getPreviousStateIndex() == ViewType.REGISTRATION) return;

			Handler.getProxyServer().addCenter(newCenter);
			Console.write("Monitoring center created successfully!");
		}

		catch (DatabaseRequestException e) {

			Console.write(e.getMessage());
			onHeadlessRender("");
		}

		catch (ConnectionLostException e) {

			setCurrentState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		try {

			m_panel.setSize(Application.getWidth() / 2.0f, m_cancelButton.getPositionY() - ImGui.getCursorPosY() - 50);
			m_panel.setOriginX(m_panel.getWidth() / 2.0f);
			m_panel.setPositionX(Application.getWidth() / 2.0f);
	
			m_panel.begin("Center Creation");
	
			ImGui.newLine();
	
			// Center ID
			m_centerIDInputText.setWidth(m_panel.getWidth() / 2.0f);
			m_centerIDInputText.setOriginX(m_centerIDInputText.getWidth() / 2.0f);
			m_centerIDInputText.setPositionX(m_panel.getWidth() / 2.0f);
			m_centerIDInputText.render();
	
			ImGui.newLine();
	
			// Street
			m_streetInputText.setWidth(m_panel.getWidth() / 2.0f);
			m_streetInputText.setOriginX(m_streetInputText.getWidth() / 2.0f);
			m_streetInputText.setPositionX(m_panel.getWidth() / 2.0f);
			m_streetInputText.render();
	
			ImGui.newLine();
	
			// House number
			m_houseNumberInputText.setWidth(m_panel.getWidth() / 2.0f);
			m_houseNumberInputText.setOriginX(m_houseNumberInputText.getWidth() / 2.0f);
			m_houseNumberInputText.setPositionX(m_panel.getWidth() / 2.0f);
			m_houseNumberInputText.setNumbersOnly(true);
			m_houseNumberInputText.setNoSpaces(true);
			m_houseNumberInputText.render();
	
			ImGui.newLine();
	
			// Postal code
			m_postalCodeInputText.setWidth(m_panel.getWidth() / 2.0f);
			m_postalCodeInputText.setOriginX(m_postalCodeInputText.getWidth() / 2.0f);
			m_postalCodeInputText.setPositionX(m_panel.getWidth() / 2.0f);
			m_postalCodeInputText.setNumbersOnly(true);
			m_postalCodeInputText.setNoSpaces(true);
			m_postalCodeInputText.render();
	
			ImGui.newLine();
	
			// City
			m_cityText.setWidth(m_panel.getWidth() / 2.0f);
			m_cityText.setOriginX(m_cityText.getWidth() / 2.0f);
			m_cityText.setPositionX(m_panel.getWidth() / 2.0f);
			m_cityText.render();
	
			if (!m_showSearchBox && !m_citySelection.getString().isEmpty()) {
	
				m_citySelection.setWidth(ImGui.calcTextSizeX(m_citySelection.getString()) + 8.0f);
				m_citySelection.setOriginX(m_citySelection.getWidth() / 2.0f);
				m_citySelection.setPositionX(m_panel.getWidth() / 2.0f);
				m_citySelection.setReadOnly(true);
				m_citySelection.render();
			}
	
			m_showCityButton.setOriginX(m_showCityButton.getWidth() / 2.0f);
			m_showCityButton.setPositionX(m_panel.getWidth() / 2.0f);
			if (!m_showSearchBox && m_showCityButton.render())
				m_showSearchBox = true;
	
			else if (m_showSearchBox) {
	
				m_searchArea.onGUIRender();
	
				if (m_searchArea.isAnyAreaSelected()) {

					m_showErrorMessage = false;
					m_showSearchBox = false;
					m_citySelection.setString(m_searchArea.getSelectedArea().getAsciiName() + ", " + 
												m_searchArea.getSelectedArea().getCountryCode());
				}
			}

			if (m_showErrorMessage) {

				m_noSelectedAreas.setOriginX(m_noSelectedAreas.getWidth() / 2.0f);
				m_noSelectedAreas.setPositionX(m_panel.getWidth() / 2.0f);
				m_noSelectedAreas.setColor(255, 0, 0, 255);
				m_noSelectedAreas.render();
			}
	
			ImGui.newLine();
	
			// District
			m_districtInputText.setWidth(m_panel.getWidth() / 2.0f);
			m_districtInputText.setOriginX(m_districtInputText.getWidth() / 2.0f);
			m_districtInputText.setPositionX(m_panel.getWidth() / 2.0f);
			m_districtInputText.render();
	
			m_panel.end();
	
			// Cancel
			m_cancelButton.setOriginX(0);
			m_cancelButton.setPositionX(m_panel.getPositionX() - m_panel.getWidth() / 2.0f);
			if (m_cancelButton.render()) {

				if (m_showSearchBox) {

					m_showSearchBox = false;
				}

				else {

					resetStateData(new CenterCreation());
					returnToPreviousState();
				}
			}

			ImGui.sameLine();
	
			// Create center
			m_createCenterButton.setOriginX(m_createCenterButton.getWidth());
			m_createCenterButton.setPositionX(m_panel.getPositionX() + m_panel.getWidth() / 2.0f);
			if (m_createCenterButton.render()) {
	
				String area = "";
				if (m_searchArea.getSelectedArea() != null) {

					area = m_searchArea.getSelectedArea().getGeonameID() + "";
				}

				else
					m_showErrorMessage = true;

				m_centerIDResult = CheckMT.creationCenterID(m_centerIDInputText.getString());
				m_addressResult = CheckMT.address(area.trim(), m_streetInputText.getString().trim(), m_houseNumberInputText.getString().trim());

				m_checkData = true;
			}
	
			if (m_checkData) {
	
				int failures = checkData();
	
				if (failures == 0) {
	
					m_checkData = false;
	
					newCenter = new Center(m_centerID, m_street, m_houseNumber, m_postalCode, m_city, m_district);
					if (getPreviousStateIndex() != ViewType.REGISTRATION) {

						m_addCenterResult = Handler.getProxyServerMT().addCenter(newCenter);
						Operator operator = Handler.getLoggedOperator();
						Operator newOperator = new Operator(operator.getUserID(), operator.getSSID(), operator.getSurname(), operator.getName(), operator.getEmail(), operator.getPassword(), newCenter.getCenterID());
						m_editOperatorResult = Handler.getProxyServerMT().editOperator(newOperator.getUserID(), newOperator);
					}

					else {

						returnToPreviousState();
					}
				}
	
				else if (failures > 0)
					m_checkData = false;
			}

			if (m_addCenterResult != null && m_addCenterResult.ready() &&
				m_editOperatorResult != null && m_editOperatorResult.ready()) {

				m_addCenterResult.get();
				m_editOperatorResult.get();

				resetStateData(new CenterCreation());
				returnToPreviousState();

				m_addCenterResult = null;
				m_editOperatorResult = null;
			}

			else if (m_addCenterResult != null && m_editOperatorResult != null) {

				m_loadingText.setOriginX(m_loadingText.getWidth() / 2.0f);
				m_loadingText.setPositionX(m_panel.getPositionX());
				m_loadingText.setPositionY(m_cancelButton.getPositionY());
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

		int failures = -1;

		if (m_centerIDResult != null && m_centerIDResult.ready() &&
			m_addressResult != null  && m_addressResult.ready()) {

			failures = 0;
			m_checkData = false;

			String errorMsg = null;
			String[] addressErrorMsg = null;

			// Center ID
			errorMsg = m_centerIDResult.get();
			m_centerIDResult = null;
			if (errorMsg == null) {

				m_centerID = m_centerIDInputText.getString();
				m_centerIDInputText.showErrorMsg(false);
			}

			else {

				m_centerIDInputText.setErrorMsg(errorMsg);
				m_centerIDInputText.showErrorMsg(true);

				failures++;
			}

			// Address
			addressErrorMsg = m_addressResult.get();
			m_addressResult = null;
			if (addressErrorMsg[0] == null && addressErrorMsg[1] == null &&
				addressErrorMsg[2] == null && addressErrorMsg[3] == null &&
				addressErrorMsg[4] == null) {

				m_city = m_searchArea.getSelectedArea().getGeonameID();
				m_street = m_streetInputText.getString();
				m_houseNumber = Integer.parseInt(m_houseNumberInputText.getString());
			}

			if (addressErrorMsg[0] != null) {

				failures++;
			}

			else {

				m_showErrorMessage = false;
			}

			if (addressErrorMsg[1] != null) {

				m_houseNumberInputText.setErrorMsg(addressErrorMsg[1]);
				m_houseNumberInputText.showErrorMsg(true);

				failures++;
			}

			else {

				m_houseNumberInputText.showErrorMsg(false);
			}

			if (addressErrorMsg[2] != null) {

				m_streetInputText.setErrorMsg(addressErrorMsg[2]);
				m_streetInputText.showErrorMsg(true);

				failures++;
			}

			else {

				m_streetInputText.showErrorMsg(false);
			}

			if (addressErrorMsg[3] != null) {

				m_houseNumberInputText.setErrorMsg(addressErrorMsg[3]);
				m_houseNumberInputText.showErrorMsg(true);
				m_citySelection.setErrorMsg(addressErrorMsg[3]);
				m_citySelection.showErrorMsg(true);
				m_streetInputText.setErrorMsg(addressErrorMsg[3]);
				m_streetInputText.showErrorMsg(true);

				failures++;
			}

			else if (addressErrorMsg[0] == null && addressErrorMsg[1] == null && addressErrorMsg[2] == null) {

				m_houseNumberInputText.showErrorMsg(false);
				m_citySelection.showErrorMsg(false);
				m_streetInputText.showErrorMsg(false);
			}

			if (addressErrorMsg[4] != null) {

				failures++;

				throw new DatabaseRequestException(addressErrorMsg[4]);
			}

			errorMsg = null;
			if ((errorMsg = Check.isValidInteger(m_postalCodeInputText.getString())) != null) {

				m_postalCodeInputText.setErrorMsg(errorMsg);
				m_postalCodeInputText.showErrorMsg(true);
				failures++;
			}

			else {

				m_postalCodeInputText.showErrorMsg(false);
				m_postalCode = Integer.parseInt(m_postalCodeInputText.getString());
			}

			m_district = m_districtInputText.getString();
		}

		return failures;
	}

	Center newCenter;

	private String m_centerID;
	private String m_street;
	private int m_houseNumber;
	private int m_postalCode;
	private int m_city;
	private String m_district;

	private Panel m_panel = new Panel();
	private InputText m_centerIDInputText = new InputText("Center ID");
	private InputText m_streetInputText = new InputText("Street");
	private InputText m_houseNumberInputText = new InputText("House number");
	private InputText m_postalCodeInputText = new InputText("Postal code");
	private InputText m_districtInputText = new InputText("District");

	private boolean m_showSearchBox = false;
	private Text m_cityText = new Text("City");
	private Button m_showCityButton = new Button(" Select ");
	private InputText m_citySelection = new InputText("");

	private Button m_cancelButton = new Button(" Cancel ");
	private Button m_createCenterButton = new Button(" Create center ");

	private SearchArea m_searchArea = new SearchArea();

	private boolean m_checkData = false;
	private Result<String> m_centerIDResult;
	private Result<String[]> m_addressResult;
	private Result<Boolean> m_addCenterResult;
	private Result<Boolean> m_editOperatorResult;

	private Text m_loadingText = new Text("Loading...");
	private Text m_noSelectedAreas = new Text("You must select an area");
	private boolean m_showErrorMessage = false;
}
