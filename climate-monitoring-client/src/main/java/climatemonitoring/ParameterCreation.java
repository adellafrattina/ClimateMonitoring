/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import climatemonitoring.core.Application;
import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.InputTextMultiline;
import climatemonitoring.core.gui.Modal;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.gui.Widget;
import climatemonitoring.core.headless.Console;
import imgui.ImGui;

/**
 * To add a new parameter
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class ParameterCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		if (!Handler.isOperatorLoggedIn()) {

			Console.write("You must be a logged operator to add a new parameter");
			return;
		}

		try {

			Area[] monitoredAreas = Handler.getProxyServer().getMonitoredAreas(Handler.getLoggedOperator().getCenterID());

			Console.write("\n--- Available areas ---");
			for (Area area : monitoredAreas)
				Console.write(area.getGeonameID() + " - " + area.getAsciiName() + ", " + area.getCountryCode());

			Console.write("\n");

			String errorMsg = null;

			// Geoname ID
			do {

				errorMsg = null;

				try {

					m_geonameID = Integer.parseInt(Console.read("Area's geoname ID > ").trim());
					errorMsg = Check.monitors(Handler.getLoggedOperator().getCenterID(), m_geonameID);
				}

				catch (NumberFormatException e) {

					errorMsg ="The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Print all categories
			Category[] categories = null;

			categories = Handler.getProxyServer().getCategories();
			Console.write("\n--- Available categories ---");
			for (Category category : categories)
				Console.write(category.getCategory() + " - " + category.getExplanation());

			Console.write("\n");

			// Category
			do {

				errorMsg = null;

				m_category = Console.read("Category > ").trim().toLowerCase();
				boolean found = false;
				for (Category category : categories) {

					if (category.getCategory().trim().toLowerCase().equals(m_category)) {

						m_category = category.getCategory();
						found = true;
						break;
					}
				}

				if (!found)
					errorMsg = "The value must be a valid category";

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Score
			do {

				errorMsg = null;

				try {

					m_score = Integer.parseInt(Console.read("Score (between 1 and 5) > ").trim());
					errorMsg = Check.score(m_score);
				}

				catch (NumberFormatException e) {

					errorMsg ="The value must be a number";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			// Notes
			do {

				errorMsg = null;

				m_notes = Console.read("Notes (max 256 characters) > ").trim();
				errorMsg = Check.notes(m_notes);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			Parameter parameter = new Parameter(
				m_geonameID,
				Handler.getLoggedOperator().getCenterID(),
				Handler.getLoggedOperator().getUserID(),
				m_category,
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				new SimpleDateFormat("hh:mm:ss").format(new Date()),
				m_score,
				m_notes);

			Handler.getProxyServer().addParameter(parameter);

			Console.write("The parameter was registered successfully");
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

		if (addResult != null) {

			if (addResult.ready()) {

				try {

					addResult.get();
				}

				catch (ConnectionLostException e) {

					setCurrentState(ViewType.CONNECTION);
					addResult = null;
					return;
				}

				catch (Exception e) {

					e.printStackTrace();
					Application.close();
				}

				addResult = null;
				ParameterInfo.resetData();
				resetStateData(new ParameterCreation());
				returnToPreviousState();
			}

			else {

				loadingText.setOrigin(loadingText.getWidth() / 2.0f, loadingText.getHeight() / 2.0f);
				loadingText.setPosition(ImGui.getWindowWidth() / 2.0f, ImGui.getWindowHeight() / 2.0f);
				loadingText.render();
			}

			return;
		}

		panel.setSize(Application.getWidth() / 1.2f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
		panel.setOriginX(panel.getWidth() / 2.0f);
		panel.setPositionX(Application.getWidth() / 2.0f);
		panel.begin(ParameterInfo.getSelectedArea().getName() + ", " + ParameterInfo.getSelectedArea().getCountryCode());

		ImGui.text("Center: " + ParameterInfo.getSelectedCenter().getCenterID());
		ImGui.text("Category: " + ParameterInfo.getSelectedCategory().getCategory() + " (" + ParameterInfo.getSelectedCategory().getExplanation() + ")");
		ImGui.newLine();
		ImGui.separator();
		ImGui.newLine();
		ImGui.text("Slide to set the score: ");
		ImGui.sameLine(m_geonameID);
		ImGui.pushItemWidth(200);
		ImGui.sliderInt("##slider", score, 0, 5);
		ImGui.popItemWidth();
		ImGui.newLine();
		textLabel.setOriginX(textLabel.getWidth() / 2.0f);
		textLabel.setPositionX(panel.getWidth() / 2.0f);
		textLabel.render();
		if (notes.render()) {

			if (notes.getString().length() > 0) {

				textLabel.setString("(" + notes.getString().length() + "/" + notes.getMaxCharacters() + ")");
			}

			else {

				textLabel.setString("Notes (max 256 characters)");
			}
		}

		panel.end();

		cancel.setOriginX(0);
		cancel.setPositionX(panel.getPositionX() - panel.getWidth() / 2.0f);
		if (cancel.render())
			discard.open();

		int discardResult = discard.render();
		if (discardResult == Modal.CANCEL)
			discardResult = 0;
		else if (discardResult == Modal.OK) {

			resetStateData(new ParameterCreation());
			returnToPreviousState();
		}

		record.setOriginX(record.getWidth());
		record.setPositionX(panel.getPositionX() + panel.getWidth() / 2.0f);
		record.setPositionY(Widget.SAME_LINE_Y);
		if (record.render())
			confirm.open();

		int confirmResult = confirm.render();
		if (confirmResult == Modal.CANCEL)
			confirmResult = 0;
		else if (confirmResult == Modal.OK) {

			Parameter parameter = new Parameter(
				ParameterInfo.getSelectedArea().getGeonameID(),
				ParameterInfo.getSelectedCenter().getCenterID(),
				Handler.getLoggedOperator().getUserID(),
				ParameterInfo.getSelectedCategory().getCategory(),
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				new SimpleDateFormat("hh:mm:ss").format(new Date()),
				score[0],
				notes.getString());

			addResult = Handler.getProxyServerMT().addParameter(parameter);
		}
	}

	private int m_geonameID;
	private String m_category;
	private int m_score;
	private String m_notes;

	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Button record = new Button("Record");
	private Modal confirm = new Modal("Parameter recording", "Do you want to record this new parameter?");
	private Modal discard = new Modal("WARNING", "Are you shure you want to discard your work?");
	private int[] score = new int[1];
	private Text textLabel = new Text("Notes (max 256 characters)");
	private InputTextMultiline notes = new InputTextMultiline(null, "", "", 256);
	private Result<Boolean> addResult;
	private Text loadingText = new Text("Loading...");
}
