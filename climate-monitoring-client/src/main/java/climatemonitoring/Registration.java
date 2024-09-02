/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.Random;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Result;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Email;

/**
 * To register into the application
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
class Registration extends ViewState {

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

			Email email = new Email("climatemonitoringappservice@mail.com", "Climate Monitoring");
			email.setReceiverEmail(m_email);
			email.setReceiverName(m_name + " " + m_surname);
			email.setSubject("Climate Monitoring verification code");

			Random r = new Random();
			int codeGiven = r.nextInt(10000, 100000);
			email.setMessage("Your verification code is: " + codeGiven + "\nIt will expire in 2 minutes");

			Result<Boolean> result = email.send();
			result.join();

			long start = System.nanoTime();
			Console.write("An email with the verification code has been sent to " + m_email + "\nIt will expire in 2 minutes");
			int codeReceived = Integer.parseInt(Console.read("Your verification code > "));
			long end = System.nanoTime();

			if (end - start >= 120000000000L)
				throw new DatabaseRequestException("Time to enter verification code expired");

			if (codeReceived != codeGiven)
				throw new DatabaseRequestException("The verification code entered is incorrect");

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

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	private String m_userID;
	private String m_email;
	private String m_password;
	private String m_SSID;
	private String m_name;
	private String m_surname;
	private String m_centerID;
}

