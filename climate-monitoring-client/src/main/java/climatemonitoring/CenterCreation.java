/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
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
class CenterCreation extends ViewState {

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

				try {

					m_city = Integer.parseInt(Console.read("City's geoname ID > "));
					m_houseNumber = Integer.parseInt(Console.read("House number > "));
					m_street = Console.read("Street > ");

					errorMsg = Check.address(m_city, m_street, m_houseNumber);
				}

				catch (NumberFormatException e) {

					errorMsg = "The value must be a number";
				}

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

		m_panel.setSize(Application.getWidth() / 2.0f, m_cancelButton.getPositionY() - ImGui.getCursorPosY() - 50);
		m_panel.setOriginX(m_panel.getWidth() / 2.0f);
		m_panel.setPositionX(Application.getWidth() / 2.0f);

		m_panel.begin("Center Creation");

		ImGui.newLine();

		m_centerIDInputText.setWidth(m_panel.getWidth() / 2.0f);
		m_centerIDInputText.setOriginX(m_centerIDInputText.getWidth() / 2.0f);
		m_centerIDInputText.setPositionX(m_panel.getWidth() / 2.0f);
		m_centerIDInputText.render();

		ImGui.newLine();

		m_streetInputText.setWidth(m_panel.getWidth() / 2.0f);
		m_streetInputText.setOriginX(m_streetInputText.getWidth() / 2.0f);
		m_streetInputText.setPositionX(m_panel.getWidth() / 2.0f);
		m_streetInputText.render();

		ImGui.newLine();

		m_houseNumberInputText.setWidth(m_panel.getWidth() / 2.0f);
		m_houseNumberInputText.setOriginX(m_houseNumberInputText.getWidth() / 2.0f);
		m_houseNumberInputText.setPositionX(m_panel.getWidth() / 2.0f);
		m_houseNumberInputText.render();

		ImGui.newLine();

		m_postalCodeInputText.setWidth(m_panel.getWidth() / 2.0f);
		m_postalCodeInputText.setOriginX(m_postalCodeInputText.getWidth() / 2.0f);
		m_postalCodeInputText.setPositionX(m_panel.getWidth() / 2.0f);
		m_postalCodeInputText.render();

		ImGui.newLine();

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

				m_showSearchBox = false;
				m_citySelection.setString(m_searchArea.getSelectedArea().getAsciiName() + ", " + 
											m_searchArea.getSelectedArea().getCountryCode());
			}
		}


		ImGui.newLine();

		m_districtInputText.setWidth(m_panel.getWidth() / 2.0f);
		m_districtInputText.setOriginX(m_districtInputText.getWidth() / 2.0f);
		m_districtInputText.setPositionX(m_panel.getWidth() / 2.0f);
		m_districtInputText.render();

		m_panel.end();

		m_cancelButton.setOriginX(m_cancelButton.getWidth() / 2.0f);
		m_cancelButton.setPositionX(m_panel.getPositionX());
		if (m_cancelButton.render())
			returnToPreviousState();
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
}
