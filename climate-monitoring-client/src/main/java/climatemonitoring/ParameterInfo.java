/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.StringTokenizer;

import imgui.ImGui;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.Result;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Dropdown;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Table;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.gui.Widget;
import climatemonitoring.core.headless.Console;

/**
 * To show all the parameters based on an area. This is not a typical view state, because it behaves differenty in GUI and Headless mode
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class ParameterInfo {

	public static void onHeadlessRender(String args) {

		String geonameID = null;

		try {

			// view parameter area
			StringTokenizer st = new StringTokenizer(args);

			// Geoname ID
			if (!st.hasMoreTokens()) {

				Console.write("Incorrect command syntax. Expected a valid area's geoname ID");
				return;
			}

			geonameID = st.nextToken().trim();
			m_geonameID = Integer.parseInt(geonameID);

			if (Handler.getProxyServer().getLatestCenter(m_geonameID) == null) {

				Console.write("This area does not have any parameters / is not monitored by any center");
				return;
			}

			Console.write("(You can leave blank to get the latest parameters)");

			String errorMsg = null;

			// Center ID
			do {

				errorMsg = null;
				String centerID = Console.read("Center ID > ").trim().toLowerCase();

				if (Check.isEmpty(centerID) != null) {

					m_centerID = Handler.getProxyServer().getLatestCenter(m_geonameID).getCenterID();
					break;
				}

				if ((errorMsg = Check.registrationCenterID(centerID)) != null) {

					Console.write(errorMsg);
					continue;
				}

				if ((errorMsg = Check.monitors(centerID, m_geonameID)) != null) {

					Console.write(errorMsg);
					continue;
				}

				if (errorMsg == null)
					m_centerID = Handler.getProxyServer().getCenter(centerID).getCenterID();

			} while (errorMsg != null);

			// Category
			do {

				errorMsg = null;
				String category = Console.read("Category > ").trim().toLowerCase();

				if (Check.isEmpty(category) != null) {

					m_category = Handler.getProxyServer().getLatestCategory(m_geonameID, m_centerID).getCategory();
					break;
				}

				if ((errorMsg = Check.category(category)) == null) {

					Category[] categories = Handler.getProxyServer().getCategories();
					for (Category c : categories) {

						if (c.getCategory().trim().toLowerCase().equals(category)) {

							m_category = c.getCategory();
							break;
						}
					}

					break;
				}

				else
					Console.write(errorMsg);

			} while (errorMsg != null);

			get().parameters = Handler.getProxyServer().getParameters(m_geonameID, m_centerID, m_category);

			if (get().parameters == null || get().parameters.length == 0) {

				Console.write("No available parameters");
				return;
			}

			Console.write("--- " + Handler.getProxyServer().getArea(m_geonameID).getAsciiName() + " parameters ---");
			Console.write("From " + m_centerID);
			Console.write("Grouped by " + m_category);
			Console.write("Count: " + get().parameters.length);
			Console.write("Average: " + Handler.getProxyServer().getParametersAverage(m_geonameID, m_centerID, m_category));

			// Show parameters
			for (Parameter parameter : get().parameters)
				Console.write("[" + parameter.getDate() + " " + parameter.getTime() + "] [by " + parameter.getUserID() + "] --- " + parameter.getScore() + " (" + parameter.getNotes() + ")");
		}

		catch (NumberFormatException e) {

			Console.write("Incorrect command syntax --> '" + geonameID + "'. Geoname ID should be a number");
		}

		catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}
	}

	public static void onGUIRender() {

		if (Master.getSearchArea().getSelectedArea() == null) {

			Console.error("The selected area should not be null");
			Handler.getView().returnToPreviousState();
			return;
		}

		get().selectedArea = Master.getSearchArea().getSelectedArea();

		try {

			get().render();
		}

		catch (ConnectionLostException e) {

			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}

		catch (Exception e) {

			e.printStackTrace();
			Application.close();
		}
	}

	private void render() throws ConnectionLostException, Exception {

		if (get().loadInitialData) {

			get().loadInitialData();
			return;
		}

		else if (get().notMonitored) {

			ImGui.text("NOTE: This area does not have any parameters / is not monitored by any center");
			return;
		}

		int currentCenterIndex = get().selectCenter.render();
		ImGui.sameLine();
		if (ImGui.button("view")) {

			CenterInfo ci = (CenterInfo) Handler.getView().getState(ViewType.CENTER_INFO);
			ci.center = selectedCenter;
			Handler.getView().setCurrentState(ViewType.CENTER_INFO);
		}

		int currentCategoryIndex = get().selectCategory.render();
		ImGui.sameLine();
		ImGui.text("(" + get().selectedCategory.getExplanation() + ")");

		if (get().selectCenter.isAnyItemSelected() || get().selectCategory.isAnyItemSelected())
			get().loadNewData = true;

		if (get().loadNewData) {

			get().loadNewData(currentCenterIndex, currentCategoryIndex);
			get().loadingText.setOrigin(get().loadingText.getWidth() / 2.0f, get().loadingText.getHeight() / 2.0f);
			get().loadingText.setPosition(ImGui.getWindowWidth() / 2.0f, ImGui.getWindowHeight() / 2.0f);
			get().loadingText.render();
			return;
		}

		if (dataNotFound) {

			ImGui.textColored(255, 0.0f, 0.0f, 255, "No available data");
			if (Handler.isOperatorLoggedIn()) {

				if (Handler.getLoggedOperator().getCenterID().equals(selectedCenter.getCenterID())) {

					ImGui.text("Record a new parameter");
					add.setOriginX(0);
					add.setPositionX(Widget.DEFAULT_X);
					ImGui.sameLine();
					if (add.render())
						Handler.getView().setCurrentState(ViewType.PARAMETER_CREATION);
				}
			}

			return;
		}

		if (parameters != null) {

			// Show parameters
			ImGui.text("Recordings count: " + parameters.length);
			ImGui.text("Score average: " + average);
			
			tablePanelLabel.setOriginX(tablePanelLabel.getWidth() / 2.0f);
			tablePanelLabel.setPositionX(tablePanel.getPositionX() + tablePanel.getWidth() / 2.0f);
			tablePanelLabel.render();

			if (Handler.isOperatorLoggedIn()) {

				if (Handler.getLoggedOperator().getCenterID().equals(selectedCenter.getCenterID())) {

					ImGui.sameLine();
					add.setOriginX(add.getWidth());
					add.setPositionX(tablePanel.getPositionX() + tablePanel.getWidth());
					if (add.render()) {

						Handler.getView().setCurrentState(ViewType.PARAMETER_CREATION);
						return;
					}
				}
			}

			tablePanel.begin(null);
			table.render();
			tablePanel.end();
		}
	}

	/**
	 * 
	 * @return The parameter's singleton instance
	 */
	public static ParameterInfo get() {

		return s_instance;
	}

	/**
	 * 
	 * @return The selected area
	 */
	public static Area getSelectedArea() {

		return get().selectedArea;
	}

	/**
	 * 
	 * @return The selected center
	 */
	public static Center getSelectedCenter() {

		return get().selectedCenter;
	}

	/**
	 * 
	 * @return The selected category
	 */
	public static Category getSelectedCategory() {

		return get().selectedCategory;
	}

	/**
	 * Reset the ParameterInfo view data
	 */
	public static void resetData() {

		s_instance = new ParameterInfo();
	}

	private void loadInitialData() throws ConnectionLostException, Exception {

		if (requestInitialData) {

			selectedArea = Master.getSearchArea().getSelectedArea();
			associatedCentersResult = Handler.getProxyServerMT().getAssociatedCenters(selectedArea.getGeonameID());
			latestCenterResult = Handler.getProxyServerMT().getLatestCenter(selectedArea.getGeonameID());
			categoriesResult = Handler.getProxyServerMT().getCategories();

			requestInitialData = false;
		}

		if (associatedCentersResult != null && associatedCentersResult.ready()) {

			if (associatedCentersResult.get() == null || associatedCentersResult.get().length == 0) {

				notMonitored = true;
				loadInitialData = false;
			}

			else
				centers = associatedCentersResult.get();

			associatedCentersResult = null;
		}

		else if (associatedCentersResult == null) {

			// Last center
			if (latestCenterResult != null && latestCenterResult.ready()) {

				String[] centerIDs = new String[centers.length];
				int index = 0;

				if (latestCenterResult.get() == null) {

					index = 0;
					for (int i = 0; i < centerIDs.length; i++)
						centerIDs[i] = centers[i].getCenterID();
				}

				else {

					for (int i = 0; i < centerIDs.length; i++) {

						centerIDs[i] = centers[i].getCenterID();
						if (centerIDs[i].equals(latestCenterResult.get().getCenterID()))
							index = i;
					}
				}

				selectCenter = new Dropdown("Select center:	  ", centerIDs);
				selectCenter.setCurrentItem(index);
				selectedCenter = centers[index];

				latestCategoryResult = Handler.getProxyServerMT().getLatestCategory(selectedArea.getGeonameID(), selectedCenter.getCenterID());
				latestCenterResult = null;
			}

			else if (latestCenterResult == null) {

				// Last category
				if (categoriesResult != null && categoriesResult.ready() && latestCategoryResult != null && latestCategoryResult.ready()) {

					Category[] categories = categoriesResult.get();
					String[] categoriesName = new String[categories.length];
					int index = 0;

					if (latestCategoryResult.get() == null) {

						index = 0;
						for (int i = 0; i < categoriesName.length; i++)
							categoriesName[i] = categories[i].getCategory();
					}

					else {

						for (int i = 0; i < categoriesName.length; i++) {

							categoriesName[i] = categories[i].getCategory();
							if (categoriesName[i].equals(latestCategoryResult.get().getCategory()))
								index = i;
						}
					}

					selectCategory = new Dropdown("Select category: ", categoriesName);
					selectCategory.setCurrentItem(index);
					selectedCategory = categories[index];

					parametersResult = Handler.getProxyServerMT().getParameters(selectedArea.getGeonameID(), selectedCenter.getCenterID(), selectedCategory.getCategory());
					latestCategoryResult = null;
					categoriesResult = null;
				}

				else if (categoriesResult == null && latestCategoryResult == null) {

					// Parameters
					if (parametersResult != null && parametersResult.ready()) {

						parameters = parametersResult.get();
						if (parameters != null && parameters.length != 0)
							averageResult = Handler.getProxyServerMT().getParametersAverage(selectedArea.getGeonameID(), selectedCenter.getCenterID(), selectedCategory.getCategory());

						else {

							dataNotFound = true;
							loadInitialData = false;
							requestInitialData = true;
						}

						parametersResult = null;
					}

					else if (parametersResult == null) {

						// Parameters average
						if (averageResult != null && averageResult.ready()) {

							average = averageResult.get();
							setUpParametersData();
							averageResult = null;
						}

						else if (averageResult == null) {

							loadInitialData = false;
							requestInitialData = true;
						}
					}
				}
			}
		}

		loadingText.setOrigin(loadingText.getWidth() / 2.0f, loadingText.getHeight() / 2.0f);
		loadingText.setPosition(ImGui.getWindowWidth() / 2.0f, ImGui.getWindowHeight() / 2.0f);
		loadingText.render();
	}

	private void loadNewData(int current_center_index, int current_category_index) throws ConnectionLostException, Exception {

		if (requestNewData) {

			currentCenterResult = Handler.getProxyServerMT().getCenter(selectCenter.getList()[current_center_index]);
			categoriesResult = Handler.getProxyServerMT().getCategories();
			parametersResult = Handler.getProxyServerMT().getParameters(selectedArea.getGeonameID(), selectCenter.getList()[current_center_index], selectCategory.getList()[current_category_index]);
			requestNewData = false;
		}

		if (currentCenterResult != null && currentCenterResult.ready() && categoriesResult != null && categoriesResult.ready() && parametersResult != null && parametersResult.ready()) {

			// Center
			selectedCenter = currentCenterResult.get();

			// Category
			Category[] categories = categoriesResult.get();
			selectedCategory = categories[current_category_index];

			// Parameters
			parameters = parametersResult.get();
			if (parameters != null && parameters.length != 0) {

				dataNotFound = false;
				averageResult = Handler.getProxyServerMT().getParametersAverage(selectedArea.getGeonameID(), selectedCenter.getCenterID(), selectedCategory.getCategory());
			}

			else
				dataNotFound = true;

			currentCenterResult = null;
			categoriesResult = null;
			parametersResult = null;
		}

		else if (currentCenterResult == null && categoriesResult == null && parametersResult == null) {

			if (dataNotFound) {

				loadNewData = false;
				requestNewData = true;
				averageResult = null;
				return;
			}

			if (averageResult != null && averageResult.ready()) {

				// Parameters average
				average = averageResult.get();
				setUpParametersData();
				loadNewData = false;
				requestNewData = true;
				averageResult = null;
			}
		}
	}

	private void setUpParametersData() {

		table = new Table("Parameters", new String[] { "Date", "Time", "Operator", "Score", "Notes" });

		for (Parameter parameter : parameters)
			table.addRow(new String[] { parameter.getDate(), parameter.getTime(), parameter.getUserID(), "" + parameter.getScore(), parameter.getNotes() });
	}

	private static ParameterInfo s_instance = new ParameterInfo();

	private Parameter[] parameters;

	// Headless
	private static int m_geonameID;
	private static String m_centerID;
	private static String m_category;

	// GUI
	private boolean notMonitored = false;
	private boolean dataNotFound = false;
	private boolean requestInitialData = true;
	private boolean loadInitialData = true;
	private boolean requestNewData = true;
	private boolean loadNewData = false;
	private Text loadingText = new Text("Loading...");
	private Area selectedArea;
	private Dropdown selectCenter;
	private Dropdown selectCategory;
	private Result<Center[]> associatedCentersResult;
	private Result<Center> latestCenterResult;
	private Result<Center> currentCenterResult;
	private Center[] centers;
	private Center selectedCenter;
	private Result<Category[]> categoriesResult;
	private Result<Category> latestCategoryResult;
	private Category selectedCategory;
	private Result<Double> averageResult;
	private double average;
	private Result<Parameter[]> parametersResult;
	private Table table;
	private Panel tablePanel = new Panel();
	private Text tablePanelLabel = new Text("Recordings");
	private Button add = new Button(" + ");
}
