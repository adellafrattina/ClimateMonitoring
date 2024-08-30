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
 * To login into the application
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class Login extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Console.deletePreviousLine();

		String[] parts = args.split(" ");
		
		if(parts.length < 2) {

			Console.write("Enter username and password");
			return;
		}

		String userid = parts[0];
		String password = parts[1];

		try {

			String errorMsg = null;

			if ((errorMsg = Check.login(userid, password)) != null) {

				Console.write(errorMsg);
				return;
			}

			Operator op = Handler.getProxyServer().validateCredentials(userid, password);
			Console.write("Logged in as " + op.getUserID() + "!");
			
			Handler.setLoggedOperator(op);
		} catch (ConnectionLostException e) {

			Console.write("Connection lost");
			setCurrentState(ViewType.CONNECTION);
		} catch (DatabaseRequestException e) {

			Console.write("Error message from database: " + e.getMessage());
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
