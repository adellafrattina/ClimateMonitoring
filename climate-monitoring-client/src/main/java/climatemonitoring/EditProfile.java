package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

class EditProfile extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Console.write("User ID > " + m_userID);
		Console.write("SSID > " + m_ssid);
		Console.write("Surname > " + m_surname);
		Console.write("Name > " + m_name);
		Console.write("Email > " + m_email);
		Console.write("Password > " + m_password);
		Console.write("Center ID > " + m_centerID);

		String errorMsg = null;

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

			m_centerID = Console.read("New Center ID > ");
			errorMsg = Check.centerID(m_centerID);

			if (errorMsg != null)
				Console.write(errorMsg);

		} while (errorMsg != null);

		try {

			Operator operator = new Operator(m_userID, m_ssid.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
			Handler.getProxyServer().editOperator(m_userID, operator);

			Console.write("Operator info edited succesfully");
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
