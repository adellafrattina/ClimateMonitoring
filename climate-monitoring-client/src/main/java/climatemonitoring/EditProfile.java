/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

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

		String password = Console.read("Password for " + Handler.getLoggedOperator().getUserID() + " > ");
		if (!password.equals(Handler.getLoggedOperator().getPassword())) {

			Console.write("Invalid password");
			return;
		}

		Console.deletePreviousLine();

		m_userID = Handler.getLoggedOperator().getUserID();
		m_ssid = new String(Handler.getLoggedOperator().getSSID());
		m_surname = Handler.getLoggedOperator().getSurname();
		m_name = Handler.getLoggedOperator().getName();
		m_email = Handler.getLoggedOperator().getEmail();
		m_password = Handler.getLoggedOperator().getPassword();
		m_centerID = Handler.getLoggedOperator().getCenterID();

		Console.write("\nUser ID > " + m_userID);
		Console.write("SSID > " + m_ssid);
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

				if (Check.isEmpty(newSSID) != null || newSSID.equals(m_ssid))
					errorMsg = null;

				else
					if ((errorMsg = Check.ssid(m_ssid)) == null)
						m_ssid = newSSID;
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

			Operator operator = new Operator(m_userID, m_ssid.toCharArray(), m_surname, m_name, m_email, m_password, m_centerID);
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
