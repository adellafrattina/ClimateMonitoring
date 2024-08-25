package climatemonitoring;

import java.util.function.Consumer;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Email;

import java.util.Random;

class Registration extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		m_userID = input("User ID > ", (n) -> { Check.userID(result); });
		m_email = input("Email > ", "EMAIL");
		m_password = input("Password > ", "PASSWORD");
		m_SSID = input("SSID > ", "SSID").toCharArray();
		m_name = input("Name > ", "NAME");
		m_surname = input("Surname > ", "SURNAME");
		m_centerID = input("Center ID > ", "CENTER_ID");

		try {

			Handler.getProxyServer().begin();

			if (m_centerID == null) {

				CenterCreation cc = (CenterCreation) Handler.getView().getState(ViewType.CENTER_CREATION);
				cc.onHeadlessRender("");
				m_centerID = cc.centerID;
			}
	
			Operator operator = new Operator(m_userID, m_SSID, m_surname, m_name, m_email, m_password, m_centerID);

			Handler.getProxyServer().addOperator(operator);

			// Verification
			Email email = new Email("climatemonitoringappservice@mail.com", "Climate Monitoring");
			email.setReceiverEmail(m_email);
			email.setReceiverName(m_name + " " + m_surname);
			email.setSubject("Climate Monitoring verification code");

			Random r = new Random();
			int codeGiven = r.nextInt();
			email.setMessage("Your verification code is: " + codeGiven + "\nIt will expire in 2 minutes");

			long start = System.nanoTime();
			int codeReceived = Integer.parseInt(Console.read("Your verification code > "));
			long end = System.nanoTime();

			if (end - start >= 120000000000L)
				throw new DatabaseRequestException("Time to enter verification code expired");

			if (codeReceived != codeGiven)
				throw new DatabaseRequestException("The verification code entered is incorrect");

			Handler.getProxyServer().end();
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

	private String input(String msg, Function<String, String> method) {

		String result = null;
		String errorMsg = null;

		do {

			result = Console.read(msg);
			errorMsg = method.exec(result);

			if (errorMsg != null)
				Console.write(errorMsg);

		} while (errorMsg != null);

		return result;
	}

	private String validate(String result, String type) {

		switch (type) {

			case "USER_ID": return Check.userID(result);
			case "EMAIL": return Check.email(result);
			case "PASSWORD": return Check.password(result);
			case "SSID": return Check.ssid(result);
			case "NAME": return Check.name(result);
			case "SURNAME": return Check.surname(result);
			case "CENTER_ID": return Check.centerID(result);

			default: return null;
		}
	}

	private String m_userID;
	private String m_email;
	private String m_password;
	private char[] m_SSID;
	private String m_name;
	private String m_surname;
	private String m_centerID;
}

