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
		if(parts[0] == null || parts[1] == null)
			Console.write("inserire username o password");

		String userid = parts[0];
		String password = parts[1];

		try {

			Operator op = Handler.getProxyServer().validateCredentials(userid, password);

			if(op == null)
				Console.write("credenziali errate");
			
			Handler.setLoggedOperator(op);
		} catch (ConnectionLostException e) {
			
			getView().setCurrentState("ViewType.CONNECTION");
		} catch (DatabaseRequestException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
