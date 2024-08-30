package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.ViewState;
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

		m_userID = Handler.getLoggedOperator().getUserID();
		m_ssid = new String(Handler.getLoggedOperator().getSSID());
		m_surname = Handler.getLoggedOperator().getSurname();
		m_name = Handler.getLoggedOperator().getName();
		m_email = Handler.getLoggedOperator().getEmail();
		m_password = Handler.getLoggedOperator().getPassword();
		m_centerID = Handler.getLoggedOperator().getCenterID();

		Console.write("User ID > " + m_userID);
		Console.write("SSID > " + m_ssid);
		Console.write("Surname > " + m_surname);
		Console.write("Name > " + m_name);
		Console.write("Email > " + m_email);
		Console.write("Password > " + m_password);
		Console.write("Center ID > " + m_centerID);

		String errorMsg = null;
		do {

			String answear = Console.read("Do you want to edit your profile? [Y/n]").trim().toLowerCase();
			if (answear.equals("y"))
				errorMsg = null;
			else if (answear.equals("n"))
				return;
			else
				errorMsg = "Not a valid answear";

			if (errorMsg != null)
				Console.write(errorMsg);

		} while(errorMsg != null);

		try {

			do {

				String newSSID = Console.read("New SSID > ");

				if (newSSID != m_ssid) {

					errorMsg = Check.ssid(m_ssid);
					m_ssid = newSSID;
		
					if (errorMsg != null)
						Console.write(errorMsg);
				}

			} while (errorMsg != null);

			do {

				m_surname = Console.read("New Surname > ");
				errorMsg = Check.surname(m_surname);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				m_name = Console.read("New Name > ");
				errorMsg = Check.name(m_name);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newEmail = Console.read("New Email > ");

				if (newEmail != m_email) {

					errorMsg = Check.email(m_email);
					m_email = newEmail;
		
					if (errorMsg != null)
						Console.write(errorMsg);
				}

			} while (errorMsg != null);

			do {

				m_password = Console.read("New Password > ");
				errorMsg = Check.password(m_password);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				String newCenterID = Console.read("New Center ID > ");

				if (newCenterID != m_centerID) {

					errorMsg = Check.registrationCenterID(m_centerID);
					m_centerID = newCenterID;

					if (errorMsg != null)
						Console.write(errorMsg);
				}

			} while (errorMsg != null);

			Operator operator = new Operator(m_userID, m_ssid.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
			Handler.getProxyServer().editOperator(m_userID, operator);

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

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	private String m_userID;
	private String m_ssid;
	private String m_surname;
	private String m_name;
	private String m_email;
	private String m_password;
	private String m_centerID;
}
