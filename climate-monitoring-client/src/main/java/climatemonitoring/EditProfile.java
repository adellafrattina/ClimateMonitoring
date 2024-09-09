/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.InputText;
import climatemonitoring.core.gui.InputTextButton;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;

/**
 * To edit the operator's profile
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
class EditProfile extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		if (!Handler.isOperatorLoggedIn()) {

			Console.write("You must be a logged operator to edit your profile");
			return;
		}

		String password = Console.read("Password for " + Handler.getLoggedOperator().getUserID() + " > ");
		if (!password.equals(Handler.getLoggedOperator().getPassword())) {

			Console.write("Invalid password");
			return;
		}

		Console.deletePreviousLine();

		m_userID = Handler.getLoggedOperator().getUserID();
		m_SSID = new String(Handler.getLoggedOperator().getSSID());
		m_surname = Handler.getLoggedOperator().getSurname();
		m_name = Handler.getLoggedOperator().getName();
		m_email = Handler.getLoggedOperator().getEmail();
		m_password = Handler.getLoggedOperator().getPassword();
		m_centerID = Handler.getLoggedOperator().getCenterID();

		Console.write("\nUser ID > " + m_userID);
		Console.write("SSID > " + m_SSID);
		Console.write("Surname > " + m_surname);
		Console.write("Name > " + m_name);
		Console.write("Email > " + m_email);
		Console.write("Center ID > " + m_centerID);

		String errorMsg = null;
		do {

			String answer = Console.read("Do you want to edit your profile? [Y/n]").trim().toLowerCase();
			if (answer.equals("y"))
				errorMsg = null;
			else if (answer.equals("n"))
				return;
			else
				errorMsg = "Not a valid answer";

			if (errorMsg != null)
				Console.write(errorMsg);

		} while(errorMsg != null);

		try {

			do {

				String newSSID = Console.read("New SSID > ").toUpperCase().trim();

				if (Check.isEmpty(newSSID) != null || newSSID.equals(m_SSID))
					errorMsg = null;

				else
					if ((errorMsg = Check.ssid(m_SSID)) == null)
						m_SSID = newSSID;
					else
						Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newSurname = Console.read("New Surname > ").trim();

				if (Check.isEmpty(newSurname) != null || newSurname.equals(m_surname))
					errorMsg = null;

				else
					if ((errorMsg = Check.surname(m_surname)) == null)
						m_surname = newSurname;
					else
						Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newName = Console.read("New Name > ").trim();

				if (Check.isEmpty(newName) != null || newName.equals(m_name))
					errorMsg = null;

				else
					if ((errorMsg = Check.name(m_name)) == null)
						m_name = newName;
					else
						Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newEmail = Console.read("New Email > ").toLowerCase().trim();

				if (Check.isEmpty(newEmail) != null || newEmail.equals(m_email))
					errorMsg = null;

				else
					if ((errorMsg = Check.email(m_email)) == null)
						m_email = newEmail;
					else
						Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String oldPassword = Console.read("Old Password > ").trim();

				if (oldPassword.equals(m_password)) {

					Console.deletePreviousLine();
					String newPassword = Console.read("New Password > ");

					if ((errorMsg = Check.password(newPassword)) == null)
						m_password = newPassword;

					Console.deletePreviousLine();
				}

				else {

					Console.deletePreviousLine();
					errorMsg = "Incorrect password";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newCenterID = Console.read("New Center ID > ").trim();

				if (Check.isEmpty(newCenterID) != null || newCenterID.equals(m_centerID))
					errorMsg = null;

				else
					if ((errorMsg = Check.registrationCenterID(newCenterID)) == null)
						m_centerID = newCenterID;
					else
						Console.write(errorMsg);

			} while (errorMsg != null);

			Operator operator = new Operator(m_userID, m_SSID.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
			Handler.getProxyServer().editOperator(m_userID, operator);
			Handler.setLoggedOperator(operator);

			Console.write("Operator info edited succesfully");
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

			if (setUpInitialData) {

				Operator loggedOperator = Handler.getLoggedOperator();
				userIDInputText.setString(loggedOperator.getUserID());
				emailInputText.setString(loggedOperator.getEmail());
				passwordInputText.setString(loggedOperator.getPassword());
				ssidInputText.setString(new String(loggedOperator.getSSID()));
				surnameInputText.setString(loggedOperator.getSurname());
				nameInputText.setString(loggedOperator.getName());
				selectedCenterInputText.setString(loggedOperator.getCenterID());
				centerID = loggedOperator.getCenterID();

				m_userID = loggedOperator.getUserID();
				m_email = loggedOperator.getEmail();
				m_password = loggedOperator.getPassword();
				m_SSID = new String(loggedOperator.getSSID());
				m_surname = loggedOperator.getSurname();
				m_name = loggedOperator.getName();
				m_centerID = loggedOperator.getCenterID();

				setUpInitialData = false;
			}

			panel.setSize(Application.getWidth() / 2.0f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
			panel.setOriginX(panel.getWidth() / 2.0f);
			panel.setPositionX(Application.getWidth() / 2.0f);
			panel.begin("Edit profile");

			ImGui.newLine();

			// User ID
			userIDInputText.setWidth(panel.getWidth() / 2.0f);
			userIDInputText.setOriginX(userIDInputText.getWidth() / 2.0f);
			userIDInputText.setPositionX(panel.getWidth() / 2.0f);
			userIDInputText.setNoSpaces(true);
			userIDInputText.setReadOnly(true);
			userIDInputText.render();

			ImGui.newLine();

			// Email
			emailInputText.setWidth(panel.getWidth() / 2.0f);
			emailInputText.setOriginX(emailInputText.getWidth() / 2.0f);
			emailInputText.setPositionX(panel.getWidth() / 2.0f);
			emailInputText.setNoSpaces(true);
			emailInputText.render();

			ImGui.newLine();

			// Password
			passwordInputText.setWidth(panel.getWidth() / 2.0f);
			passwordInputText.setOriginX(passwordInputText.getWidth() / 2.0f);
			passwordInputText.setPositionX(panel.getWidth() / 2.0f);
			passwordInputText.setNoSpaces(true);
			passwordInputText.setPassword(hidePassword);
			passwordInputText.setEnterReturnsTrue(true);
			passwordInputText.render();
			if (passwordInputText.isButtonPressed())
				hidePassword = !hidePassword;

			ImGui.newLine();

			// SSID
			ssidInputText.setWidth(panel.getWidth() / 2.0f);
			ssidInputText.setOriginX(ssidInputText.getWidth() / 2.0f);
			ssidInputText.setPositionX(panel.getWidth() / 2.0f);
			ssidInputText.setAlwaysUpperCase(true);
			ssidInputText.setNoSpaces(true);
			ssidInputText.render();

			ImGui.newLine();

			// Surname
			surnameInputText.setWidth(panel.getWidth() / 2.0f);
			surnameInputText.setOriginX(surnameInputText.getWidth() / 2.0f);
			surnameInputText.setPositionX(panel.getWidth() / 2.0f);
			surnameInputText.render();

			ImGui.newLine();

			// Name
			nameInputText.setWidth(panel.getWidth() / 2.0f);
			nameInputText.setOriginX(nameInputText.getWidth() / 2.0f);
			nameInputText.setPositionX(panel.getWidth() / 2.0f);
			nameInputText.render();

			ImGui.newLine();

			// Center
			selectCenterText.setOriginX(selectCenterText.getWidth() / 2.0f);
			selectCenterText.setPositionX(panel.getWidth() / 2.0f);
			selectCenterText.render();

			if (showSearchCenter) {

				searchCenterPanel.setHeight(400.0f);
				searchCenterPanel.begin(null);

				searchCenter.onGUIRender();

				searchCenterPanel.end();

				if (searchCenter.isAnyCenterSelected()) {

					showSearchCenter = false;
					centerID = searchCenter.getSelectedCenter().getCenterID();
				}
			}

			else {

				if (centerID == null) {

					centerID = searchCenter.getSelectedCenter() != null ? searchCenter.getSelectedCenter().getCenterID() : null;
				}

				if (centerID != null) {

					selectedCenterInputText.setString(centerID);
					selectedCenterInputText.setWidth(ImGui.calcTextSizeX(selectedCenterInputText.getString()) + 8.0f);
					selectedCenterInputText.setOriginX(selectedCenterInputText.getWidth() / 2.0f);
					selectedCenterInputText.setPositionX(panel.getWidth() / 2.0f);
					selectedCenterInputText.setReadOnly(true);
					selectedCenterInputText.render();
				}

				searchCenterButton.setOriginX(searchCenterButton.getWidth() / 2.0f);
				searchCenterButton.setPositionX(panel.getWidth() / 2.0f);
				if (searchCenterButton.render())
					showSearchCenter = true;

				centerNotSelectedText.setOriginX(centerNotSelectedText.getWidth() / 2.0f);
				centerNotSelectedText.setPositionX(panel.getWidth() / 2.0f);
				if (!centerNotSelectedText.getString().isEmpty())
					centerNotSelectedText.render();
			}

			panel.end();

			cancel.setOriginX(0);
			cancel.setPositionX(panel.getPositionX() - panel.getWidth() / 2.0f);
			if (cancel.render()) {

				if (showSearchCenter) {

					showSearchCenter = false;
				}

				else {

					resetStateData(new EditProfile());
					returnToPreviousState();
				}
			}

			ImGui.sameLine();

			confirm.setOriginX(confirm.getWidth());
			confirm.setPositionX(panel.getPositionX() + panel.getWidth() / 2.0f);
			if (confirm.render()) {

				checkUserIDResult = CheckMT.userID(userIDInputText.getString());
				checkEmailResult = CheckMT.email(emailInputText.getString());
				checkSSIDResult = CheckMT.ssid(ssidInputText.getString());
				checkData = true;
			}

			if (checkData) {

				int status = checkData();

				if (status < 0)
					loadingText.render();

				// Success
				if (status == 0) {

					checkData = false;
					operator = new Operator(m_userID, m_SSID.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
					editOperatorResult = Handler.getProxyServerMT().editOperator(operator.getUserID(), operator);
				}

				// Failure
				else if (status > 0)
					checkData = false;
			}

			if (editOperatorResult != null && editOperatorResult.ready()) {

				editOperatorResult.get();
				Handler.setLoggedOperator(operator);
				resetStateData(new EditProfile());
				setCurrentState(ViewType.MASTER);
			}

			else if (editOperatorResult != null) {

				loadingText.setOriginX(loadingText.getWidth() / 2.0f);
				loadingText.setPositionX(panel.getPositionX());
				ImGui.sameLine();
				loadingText.setPositionY(cancel.getPositionY());
				loadingText.render();
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

		int failure = -1;

		if (checkUserIDResult != null && checkUserIDResult.ready() &&
			checkEmailResult != null && checkEmailResult.ready() &&
			checkSSIDResult != null && checkSSIDResult.ready())

		{

			failure = 0;

			String errorMsg = null;

			// User ID
			errorMsg = checkUserIDResult.get();
			if (errorMsg == null || userIDInputText.getString().equals(m_userID)) {

				m_userID = userIDInputText.getString();
				userIDInputText.showErrorMsg(false);
			}

			else {

				userIDInputText.setErrorMsg(errorMsg);
				userIDInputText.showErrorMsg(true);
				failure++;
			}

			// Email
			errorMsg = checkEmailResult.get();
			if (errorMsg == null || emailInputText.getString().equals(m_email)) {

				m_email = emailInputText.getString();
				emailInputText.showErrorMsg(false);
			}

			else {

				emailInputText.setErrorMsg(errorMsg);
				emailInputText.showErrorMsg(true);
				failure++;
			}

			// Password
			errorMsg = Check.password(passwordInputText.getString());
			if (errorMsg == null || passwordInputText.getString().equals(m_password)) {

				m_password = passwordInputText.getString();
				passwordInputText.showErrorMsg(false);
			}

			else {

				passwordInputText.setErrorMsg(errorMsg);
				passwordInputText.showErrorMsg(true);
				failure++;
			}

			// SSID
			errorMsg = checkSSIDResult.get();
			if (errorMsg == null || ssidInputText.getString().equals(m_SSID)) {

				m_SSID = ssidInputText.getString();
				ssidInputText.showErrorMsg(false);
			}

			else {

				ssidInputText.setErrorMsg(errorMsg);
				ssidInputText.showErrorMsg(true);
				failure++;
			}

			// Surname
			errorMsg = Check.surname(surnameInputText.getString());
			if (errorMsg == null || surnameInputText.getString().equals(m_surname)) {

				m_surname = surnameInputText.getString();
				surnameInputText.showErrorMsg(false);
			}

			else {

				surnameInputText.setErrorMsg(errorMsg);
				surnameInputText.showErrorMsg(true);
				failure++;
			}

			// Name
			errorMsg = Check.name(nameInputText.getString());
			if (errorMsg == null || nameInputText.getString().equals(m_name)) {

				m_name = nameInputText.getString();
				nameInputText.showErrorMsg(false);
			}

			else {

				nameInputText.setErrorMsg(errorMsg);
				nameInputText.showErrorMsg(true);
				failure++;
			}

			// Center
			if (centerID != null) {

				m_centerID = centerID;
				centerNotSelectedText.setString("");
			}

			else {

				centerNotSelectedText.setString("You must select a center");
				centerNotSelectedText.setColor(255, 0, 0, 255);
				failure++;
			}

			checkUserIDResult = null;
			checkEmailResult = null;
			checkSSIDResult = null;
		}

		else if (checkUserIDResult != null && checkEmailResult != null && checkSSIDResult != null) {

			loadingText.setOriginX(loadingText.getWidth() / 2.0f);
			loadingText.setPositionX(panel.getPositionX());
			ImGui.sameLine();
			loadingText.setPositionY(cancel.getPositionY());
		}

		return failure;
	}

	private String m_userID;
	private String m_SSID;
	private String m_surname;
	private String m_name;
	private String m_email;
	private String m_password;
	private String m_centerID;

	private boolean setUpInitialData = true;
	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Button confirm = new Button("Update account");
	private InputText userIDInputText = new InputText("User ID");
	private InputText emailInputText = new InputText("E-mail");
	private InputTextButton passwordInputText = new InputTextButton("Password", "", "", 50, "show");
	private boolean hidePassword = true;
	private InputText ssidInputText = new InputText("SSID", "", "", 16);
	private InputText surnameInputText = new InputText("Surname");
	private InputText nameInputText = new InputText("Name");
	private SearchCenter searchCenter = new SearchCenter();
	private Panel searchCenterPanel = new Panel();
	private boolean showSearchCenter = false;
	private Button searchCenterButton = new Button("Search center");
	private InputText selectedCenterInputText = new InputText(null);
	private String centerID = null;
	private Text centerNotSelectedText = new Text("");
	private Text selectCenterText = new Text("Center");
	private boolean checkData = false;
	private Result<String> checkUserIDResult;
	private Result<String> checkEmailResult;
	private Result<String> checkSSIDResult;
	private Text loadingText = new Text("Loading..");
	private Result<Boolean> editOperatorResult;
	private Operator operator;
}
