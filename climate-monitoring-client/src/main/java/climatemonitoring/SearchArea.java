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
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.ResultBox;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;
import imgui.ImGui;

/**
 * To search an area
 * 
 * @author adellafrattina
 * @verison 1.0-SNAPSHOT
 */
class SearchArea {

	public synchronized static void onHeadlessRender(final String by, final String args) throws ConnectionLostException, DatabaseRequestException {

		try {

			m_foundAreas = null;

			// <by>
			switch (by) {
	
				case "name":
					m_foundAreas = Handler.getProxyServer().searchAreasByName(args);
					break;
				case "country":
					m_foundAreas = Handler.getProxyServer().searchAreasByCountry(args);
					break;
				case "coords":
					String[] coords = args.split(" ");
					if (coords.length < 2)
						throw new NumberFormatException();
					double latitude = Double.parseDouble(coords[0]);
					double longitude = Double.parseDouble(coords[1]);
					m_foundAreas = Handler.getProxyServer().searchAreasByCoords(latitude, longitude);
					break;
				default:
					Console.write("Incorrect command syntax -->'" + by + "', expected [name, country, coords]");
					return;
			}

			int i = 0;
			if (m_foundAreas != null && m_foundAreas.length != 0)
				for (Area area : m_foundAreas)
					Console.write(i++ + ". " + area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());
			else
				Console.write("No matching areas");
		}

		catch (NumberFormatException e) {

			Console.write("The <latitude, longitude> field must be a couple of floating point numbers");
		}
	}

	public synchronized static void onGUIRender() {

		try {

			//searchBar.setWidth((float)Application.getWidth() / 2.0f);
			//searchBar.setOriginX(searchBar.getWidth() / 2.0f);
			//searchBar.setPositionX((float)Application.getWidth() / 2.0f);

			selectSearchMethod.setOriginX(selectSearchMethod.getWidth() / 2.0f);
			selectSearchMethod.setWidth(100);
			selectSearchMethod.setPositionX(searchBar.getPositionX() + searchBar.getWidth() / 2.0f);

			int searchMethod = selectSearchMethod.render();

			switch (selectSearchMethod.getList()[searchMethod]) {

				case "name":

					searchBar.setEnterReturnsTrue(true);
					if (searchBar.render() || searchBar.isButtonPressed()) {
		
						foundAreasResult = Handler.getProxyServerMT().searchAreasByName(searchBar.getString());
						createResultBox = true;
						searchBar.showErrorMsg(false);
					}

					break;
				case "country":

					searchBar.setEnterReturnsTrue(true);
					if (searchBar.render() || searchBar.isButtonPressed()) {
		
						foundAreasResult = Handler.getProxyServerMT().searchAreasByCountry(searchBar.getString());
						createResultBox = true;
						searchBar.showErrorMsg(false);
					}

					break;
				case "coords":
					ImGui.text("Not implemented yet");
					createResultBox = true;
					searchBar.showErrorMsg(false);
					break;
			}

			if (foundAreasResult == null)
				return;

			if (foundAreasResult.ready()) {

				if (createResultBox) {

					m_foundAreas = foundAreasResult.get();
					String[] tmp = new String[m_foundAreas.length];
	
					for (int i = 0; i < tmp.length; i++)
						tmp[i] = m_foundAreas[i].getName() + ", " + m_foundAreas[i].getCountryCode();
	
					resultBox.setList(tmp);
					createResultBox = false;
				}

				if (m_foundAreas != null && m_foundAreas.length > 0) {

					resultBox.setWidth(searchBar.getWidth());
					resultBox.setPositionX(searchBar.getPositionX());
					resultBox.render();
				}

				else {

					searchBar.showErrorMsg(true);
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

	public static synchronized Area[] getFoundAreas() {

		return m_foundAreas;
	}

	public static synchronized Area getSelectedArea() {

		return m_selectedArea;
	}

	private static Dropdown selectSearchMethod = new Dropdown("Search area by ", new String[] { "name", "country", "coords" });
	private static InputTextButton searchBar = new InputTextButton(null, "", "No matching areas", 300, "Search");
	private static Text loadingText = new Text("Loading...");
	private static boolean createResultBox = false;
	private static ResultBox resultBox = new ResultBox("##");
	private static Result<Area[]> foundAreasResult;

	private static Area[] m_foundAreas;
	private static Area m_selectedArea;
}
