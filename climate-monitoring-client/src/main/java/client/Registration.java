/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import imgui.ImGui;

import climatemonitoring.core.Application;
import climatemonitoring.core.Center;
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
 * To register into the application
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public class Registration extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		String errorMsg = null;

		try {

			do {

				m_userID = Console.read("User ID > ");
				errorMsg = Check.userID(m_userID);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_email = Console.read("Email > ");
				errorMsg = Check.email(m_email);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_password = Console.read("Password > ");
				Console.deletePreviousLine();
				errorMsg = Check.password(m_password);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_SSID = Console.read("SSID > ");
				errorMsg = Check.ssid(m_SSID);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_surname = Console.read("Surname > ");
				errorMsg = Check.surname(m_surname);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_name = Console.read("Name > ");
				errorMsg = Check.name(m_name);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_centerID = Console.read("Center ID > ").trim();
				errorMsg = Check.registrationCenterID(m_centerID);

				if (errorMsg != null) {

					Console.write(errorMsg);
					m_centerID = null;
				}

			} while (errorMsg != null);


			CenterCreation cc = null;

			if (m_centerID == null || Check.isEmpty(m_centerID) != null) {

				Console.write("Center ID is missing, redirecting to Center Creation");

				cc = (CenterCreation) Handler.getView().getState(ViewType.CENTER_CREATION);
				setCurrentState(ViewType.CENTER_CREATION);
				cc.onHeadlessRender("");

				m_centerID = cc.newCenter.getCenterID();
			}

			Operator operator = new Operator(m_userID, m_SSID.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);

			verification.onHeadlessRender(operator);
			errorMsg = null;
			if ((errorMsg = verification.getErrorMsg()) != null) {

				Console.write(errorMsg);
				return;
			}

			if (Check.creationCenterID(m_centerID) == null)
				Handler.getProxyServer().addCenter(cc.newCenter);

			Handler.getProxyServer().addOperator(operator);
			Console.write("Registration successful!");
		}

		catch (DatabaseRequestException e) {

			Console.write(e.getMessage());
		}

		catch (ConnectionLostException e) {

			Console.write("Connection lost");
			setCurrentState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		try {

			if (showVerification) {

				verification.onGUIRender(operator);

				if (verification.clicked()) {

					getCenterResult = Handler.getProxyServerMT().getCenter(m_centerID);
					showVerification = false;
					createOperator = true;
				}

				return;
			}

			if (createOperator) {

				if (getCenterResult != null && getCenterResult.ready()) {

					if (getCenterResult.get() == null) {

						CenterCreation cc = (CenterCreation) getView().getState(ViewType.CENTER_CREATION);
						addCenterResult = Handler.getProxyServerMT().addCenter(cc.newCenter);
					}

					else {

						addOperatorResult = Handler.getProxyServerMT().addOperator(operator);
						addCenterResult = null;
					}

					getCenterResult = null;
				}

				else if (getCenterResult == null) {

					if (addCenterResult != null && addCenterResult.ready()) {

						addCenterResult.get();
						addOperatorResult = Handler.getProxyServerMT().addOperator(operator);
						addCenterResult = null;
					}

					else if (addCenterResult == null) {

						if (addOperatorResult != null && addOperatorResult.ready()) {

							addOperatorResult.get();
							addOperatorResult = null;
							Handler.setLoggedOperator(operator);
							setCurrentState(ViewType.MASTER);
							createOperator = false;
						}
					}
				}

				else if (addCenterResult != null || addOperatorResult != null || getCenterResult != null) {

					loadingText.setOrigin(loadingText.getWidth() / 2.0f, loadingText.getHeight() / 2.0f);
					loadingText.setPosition(ImGui.getWindowWidth() / 2.0f, ImGui.getWindowHeight() / 2.0f);
					loadingText.render();
				}

				return;
			}

			panel.setSize(Application.getWidth() / 2.0f, cancel.getPositionY() - ImGui.getCursorPosY() - 50);
			panel.setOriginX(panel.getWidth() / 2.0f);
			panel.setPositionX(Application.getWidth() / 2.0f);
			panel.begin("Create new account");

			ImGui.newLine();

			// User ID
			userIDInputText.setWidth(panel.getWidth() / 2.0f);
			userIDInputText.setOriginX(userIDInputText.getWidth() / 2.0f);
			userIDInputText.setPositionX(panel.getWidth() / 2.0f);
			userIDInputText.setNoSpaces(true);
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
			passwordInputText.setPassword(!showPassword);
			passwordInputText.render();
			if (passwordInputText.isButtonPressed())
				showPassword = !showPassword;

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

					CenterCreation cc = (CenterCreation) getView().getState(ViewType.CENTER_CREATION);
					if (cc.newCenter != null)
						centerID = cc.newCenter.getCenterID();
					else
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

				or.setOriginX(or.getWidth() / 2.0f);
				or.setPositionX(panel.getWidth() / 2.0f);
				or.render();

				createCenterButton.setOriginX(createCenterButton.getWidth() / 2.0f);
				createCenterButton.setPositionX(panel.getWidth() / 2.0f);
				if (createCenterButton.render()) {

					centerID = null;
					resetStateData(new CenterCreation());
					setCurrentState(ViewType.CENTER_CREATION);
				}

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

					resetStateData(new Registration());
					resetStateData(new CenterCreation());
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

				// Success
				if (status == 0) {

					checkData = false;
					showVerification = true;
					operator = new Operator(m_userID, m_SSID.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
				}

				// Failure
				else if (status > 0)
					checkData = false;
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
			if (errorMsg == null) {

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
			if (errorMsg == null) {

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
			if (errorMsg == null) {

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
			if (errorMsg == null) {

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
			if (errorMsg == null) {

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
			if (errorMsg == null) {

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
			loadingText.setPositionY(cancel.getPositionY());
		}

		return failure;
	}

	private String m_userID;
	private String m_email;
	private String m_password;
	private String m_SSID;
	private String m_name;
	private String m_surname;
	private String m_centerID;

	private Panel panel = new Panel();
	private Button cancel = new Button("Cancel");
	private Button confirm = new Button("Create new account");
	private InputText userIDInputText = new InputText("User ID (cannot be changed in the future)");
	private InputText emailInputText = new InputText("E-mail");
	private InputTextButton passwordInputText = new InputTextButton("Password", "", "", 64, "show");
	private boolean showPassword = false;
	private InputText ssidInputText = new InputText("SSID", "", "", 16);
	private InputText surnameInputText = new InputText("Surname");
	private InputText nameInputText = new InputText("Name");
	private SearchCenter searchCenter = new SearchCenter();
	private Panel searchCenterPanel = new Panel();
	private boolean showSearchCenter = false;
	private Button searchCenterButton = new Button("Search center");
	private Text or = new Text("or");
	private Button createCenterButton = new Button("Create a new center");
	private InputText selectedCenterInputText = new InputText(null);
	private String centerID = null;
	private Text centerNotSelectedText = new Text("");
	private Text selectCenterText = new Text("Center");
	private boolean checkData = false;
	private Result<String> checkUserIDResult;
	private Result<String> checkEmailResult;
	private Result<String> checkSSIDResult;
	private Text loadingText = new Text("Loading..");
	private boolean showVerification = false;
	private Verification verification = new Verification();
	private Operator operator;
	private Result<Boolean> addCenterResult;
	private Result<Boolean> addOperatorResult;
	private Result<Center> getCenterResult;
	private boolean createOperator = false;
}
