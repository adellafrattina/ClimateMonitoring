package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

class Login extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		String[] parts = args.split(" ");
		
		if(parts.length < 2) {

			Console.write("Enter username and password");
			return;
		}

		String userid = parts[0];
		String password = parts[1];

		try {

			Operator op = Handler.getProxyServer().validateCredentials(userid, password);

			if(op == null)
				Console.write("Wrong username or password");
			
			Handler.setLoggedOperator(op);
		} catch (ConnectionLostException e) {
			
			getView().setCurrentState(ViewType.CONNECTION);
		} catch (DatabaseRequestException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
