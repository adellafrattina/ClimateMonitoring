/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.ResultBox;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;

/**
 * To search a center
 * 
 * @author adellafrattina
 * @verison 1.0-SNAPSHOT
 */
class SearchCenter {

	public synchronized void onHeadlessRender(final String by, final String args) throws ConnectionLostException, DatabaseRequestException {

		m_foundCenters = null;

		// <by>
		switch (by) {

			case "name":
				m_foundCenters = Handler.getProxyServer().searchCentersByName(args);
				break;
			default:
				Console.write("Incorrect command syntax -->'" + by + "', expected [name]");
				return;
		}

		if (m_foundCenters != null && m_foundCenters.length != 0) {

			int i = 0;
			for (Center center : m_foundCenters) {

				Area area = Handler.getProxyServer().getArea(center.getCity());
				Console.write(i++ + ". " + center.getCenterID() + " - (" + area.getGeonameID() + ") " + area.getAsciiName() + " " + center.getStreet() + ", " + center.getHouseNumber());
			}
		}

		else
			Console.write("No matching centers");
	}

	public synchronized void onGUIRender() {

		try {

			selectSearchMethod.setWidth(100);
			selectSearchMethod.setOriginX(selectSearchMethod.getWidth() / 2.0f);
			selectSearchMethod.setPositionX(searchBar.getPositionX() + searchBar.getWidth() / 2.0f);

			int searchMethod = selectSearchMethod.render();

			if (currentSearchMethod != searchMethod) {

				searchBar.setString("");
				foundCentersResult = null;
				resultBox.setList(null);
				resultBox.setCurrentItem(-1);
				currentSearchMethod = searchMethod;
			}

			switch (selectSearchMethod.getList()[currentSearchMethod]) {

				case "name":

					searchBar.setEnterReturnsTrue(true);
					if (searchBar.render() || searchBar.isButtonPressed()) {
		
						foundCentersResult = Handler.getProxyServerMT().searchCentersByName(searchBar.getString());
						createResultBox = true;
						resultBox.setList(null);
						resultBox.setCurrentItem(-1);
					}

					break;
			}

			if (foundCentersResult == null)
				return;

			if (foundCentersResult.ready()) {

				if (createResultBox) {

					m_foundCenters = foundCentersResult.get();
					String[] tmp = new String[m_foundCenters.length];

					for (int i = 0; i < tmp.length; i++)
						tmp[i] = m_foundCenters[i].getCenterID() + " (" + m_foundCenters[i].getStreet() + ", " + m_foundCenters[i].getHouseNumber() + ")";

					resultBox.setList(tmp);
					createResultBox = false;
				}

				if (m_foundCenters != null && m_foundCenters.length > 0) {

					resultBox.setWidth(searchBar.getWidth());
					resultBox.setPositionX(searchBar.getPositionX());
					resultBox.render();
					if (resultBox.getCurrentItem() != -1)
						m_selectedCenter = m_foundCenters[resultBox.getCurrentItem()];
				}

				else {

					errorText.setColor(255, 0, 0, 255);
					errorText.setOriginX(errorText.getWidth() / 2.0f);
					errorText.setPositionX(searchBar.getPositionX() + searchBar.getWidth() / 2.0f);
					errorText.render();
				}
			}

			else {

				loadingText.setOriginX(loadingText.getWidth() / 2.0f);
				loadingText.setPositionX(searchBar.getPositionX() + searchBar.getWidth() / 2.0f);
				loadingText.render();
			}
		}

		catch (ConnectionLostException e) {

			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}

		catch (Exception e) {

			e.printStackTrace();
			Application.close();
		}
	}

	public synchronized Center[] getFoundCenters() {

		return m_foundCenters;
	}

	public synchronized Center getSelectedCenter() {

		return m_selectedCenter;
	}

	private int currentSearchMethod = 0;
	private Dropdown selectSearchMethod = new Dropdown("Search center by ", new String[] { "name" });
	private InputTextButton searchBar = new InputTextButton(null, "", "No matching centers", 300, "Search");
	private Text loadingText = new Text("Loading...");
	private boolean createResultBox = false;
	private ResultBox resultBox = new ResultBox("##");
	private Result<Center[]> foundCentersResult;
	private Text errorText = new Text("No matching centers");

	private Center[] m_foundCenters;
	private Center m_selectedCenter;
}
