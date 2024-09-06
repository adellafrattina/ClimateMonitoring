/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

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
import imgui.ImGui;

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

		panel.setSize(Application.getWidth() / 2.0f, Application.getHeight() / 2.0f);
		panel.setOrigin(panel.getWidth() / 2.0f, panel.getHeight() / 2.0f);
		panel.setPosition(Application.getWidth() / 2.0f, Application.getHeight() / 2.0f);
		panel.begin("Login");
		
		ImGui.newLine();
		ImGui.newLine();

		userid.setWidth(panel.getWidth() / 2.0f);
		userid.setOriginX(userid.getWidth() / 2.0f);
		userid.setPositionX(panel.getWidth() / 2.0f);
		ImGui.newLine();
		userid.render();

		password.setWidth(panel.getWidth() / 2.0f);
		password.setOriginX(password.getWidth() / 2.0f);
		password.setPositionX(panel.getWidth() / 2.0f);
		ImGui.newLine();
		password.render();

		password.setPassword(showpassword);
		if(password.isButtonPressed()){

			showpassword = !showpassword;
		}

		panel.end();

		error.setOriginX(error.getWidth() / 2.0f);
		error.setPositionX(panel.getPositionX());
		error.setColor(255, 0, 0, 255);
		error.render();
		ImGui.newLine();
		registration.setOriginX(0);
		registration.setPositionX(panel.getPositionX() - panel.getWidth() / 2.0f);

		if(registration.render()){

			resetStateData(new Login());
			setCurrentState(ViewType.REGISTRATION);
		}

		ImGui.sameLine();
		cancel.setOriginX(cancel.getWidth() / 2.0f);
		cancel.setPositionX(panel.getPositionX());

		if(cancel.render()){

			resetStateData(new Login());
			returnToPreviousState();
		}
		
		ImGui.sameLine();
		confirm.setOriginX(confirm.getWidth());
		confirm.setPositionX(panel.getPositionX() + panel.getWidth() / 2.0f);
		if(confirm.render()){

			try {
				String msgerror = null;
				if((msgerror = Check.login(userid.getString(), password.getString())) == null){

					operatorresult =  Handler.getProxyServerMT().validateCredentials(userid.getString(), password.getString());
					error.setString("");
				}else{

					error.setString(msgerror);
				}
			} catch (ConnectionLostException e) {

				setCurrentState(ViewType.CONNECTION);
			}
		}
		
		if(operatorresult == null){
			
			return;
		}

		if(operatorresult.ready()){

			try {

				Operator op = operatorresult.get();
				Handler.setLoggedOperator(op);
				operatorresult = null;
				resetStateData(new Login());
				returnToPreviousState();
			} catch (ConnectionLostException e) {

				setCurrentState(ViewType.CONNECTION);
			} catch (Exception e) {

				e.printStackTrace();
				Application.close();
			}
		}
	}

	private Panel panel = new Panel();
	private InputText userid = new InputText("User ID");
	private InputTextButton password = new InputTextButton("Password");
	private boolean showpassword = true;
	private Button confirm = new Button("    Confirm Login    ");
	private Button cancel = new Button(" Cancel ");
	private Button registration = new Button(" Create new account ");
	private Result <Operator> operatorresult;
	private Text error = new Text("");
}
