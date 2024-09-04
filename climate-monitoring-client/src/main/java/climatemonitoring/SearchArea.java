/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;
import imgui.type.ImDouble;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.ResultBox;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.gui.Widget;
import climatemonitoring.core.headless.Console;

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

			selectSearchMethod.setWidth(100);
			selectSearchMethod.setOriginX(selectSearchMethod.getWidth() / 2.0f);
			selectSearchMethod.setPositionX(searchBar.getPositionX() + searchBar.getWidth() / 2.0f);

			int searchMethod = selectSearchMethod.render();

			if (currentSearchMethod != searchMethod) {

				searchBar.setString("");
				latitude = new ImDouble(0.0);
				longitude = new ImDouble(0.0);
				foundAreasResult = null;
				resultBox.setList(null);
				resultBox.setCurrentItem(-1);
				currentSearchMethod = searchMethod;
			}

			switch (selectSearchMethod.getList()[currentSearchMethod]) {

				case "name":

					searchBar.setEnterReturnsTrue(true);
					if (searchBar.render() || searchBar.isButtonPressed()) {
		
						foundAreasResult = Handler.getProxyServerMT().searchAreasByName(searchBar.getString());
						createResultBox = true;
					}

					break;
				case "country":

					searchBar.setEnterReturnsTrue(true);
					if (searchBar.render() || searchBar.isButtonPressed()) {
		
						foundAreasResult = Handler.getProxyServerMT().searchAreasByCountry(searchBar.getString());
						createResultBox = true;
						resultBox.setList(null);
					}

					break;
				case "coords":

					ImGui.newLine();

					final float labelY = ImGui.getCursorPosY();
					latitudeLabel.setOriginX(latitudeLabel.getWidth() / 2.0f);
					latitudeLabel.setPositionX(ImGui.getCursorPosX() + (searchBar.getWidth() / 4.0f) / 2.0f);
					latitudeLabel.render();
					ImGui.pushItemWidth(searchBar.getWidth() / 4.0f);
					ImGui.inputDouble("##latitude", latitude);
					ImGui.popItemWidth();

					searchCoords.setOriginX(searchCoords.getWidth() / 2.0f);
					searchCoords.setPosition(searchBar.getPositionX() - searchBar.getOriginX() + searchBar.getWidth() / 2.0f, Widget.SAME_LINE_Y);
					if (searchCoords.render()) {

						foundAreasResult = Handler.getProxyServerMT().searchAreasByCoords(latitude.doubleValue(), longitude.doubleValue());
						createResultBox = true;
						resultBox.setList(null);
					}

					longitudeLabel.setOriginX(longitudeLabel.getWidth() / 2.0f);
					longitudeLabel.setPosition(searchBar.getPositionX() + searchBar.getWidth() - searchBar.getWidth() / 4.0f + (searchBar.getWidth() / 4.0f) / 2.0f, labelY);
					longitudeLabel.render();
					ImGui.pushItemWidth(searchBar.getWidth() / 4.0f);
					ImGui.setCursorPosX(searchBar.getPositionX() + searchBar.getWidth() - searchBar.getWidth() / 4.0f);
					ImGui.inputDouble("##longitude", longitude);
					ImGui.popItemWidth();

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
					if (resultBox.getCurrentItem() != -1)
						m_selectedArea = m_foundAreas[resultBox.getCurrentItem()];
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

	public static synchronized Area[] getFoundAreas() {

		return m_foundAreas;
	}

	public static synchronized Area getSelectedArea() {

		return m_selectedArea;
	}

	private static int currentSearchMethod = 0;
	private static Dropdown selectSearchMethod = new Dropdown("Search area by ", new String[] { "name", "country", "coords" });
	private static InputTextButton searchBar = new InputTextButton(null, "", "No matching areas", 300, "Search");
	private static Text loadingText = new Text("Loading...");
	private static boolean createResultBox = false;
	private static ResultBox resultBox = new ResultBox("##");
	private static Result<Area[]> foundAreasResult;
	private static Text latitudeLabel = new Text("Latitude");
	private static Text longitudeLabel = new Text("Longitude");
	private static ImDouble latitude = new ImDouble();
	private static ImDouble longitude = new ImDouble();
	private static Button searchCoords = new Button("Search");
	private static Text errorText = new Text("No matching areas");

	private static Area[] m_foundAreas;
	private static Area m_selectedArea;
}
