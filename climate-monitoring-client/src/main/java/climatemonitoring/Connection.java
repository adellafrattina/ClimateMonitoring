/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import imgui.ImGui;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Button;
import climatemonitoring.core.gui.Panel;
import climatemonitoring.core.gui.Text;
import climatemonitoring.core.headless.Console;

/**
 * To show when the client is trying to connect to a server
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
class Connection extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		Console.write("Connecting...");
		Handler.connect();
		Handler.getConnectionResult().join();
		try{
			Handler.getConnectionResult().get();
			Console.write("Connection established");
			returnToPreviousState();
		}
		catch(ConnectionLostException e){
			Console.write("Failed to connect to server");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			onHeadlessRender(args);
		}
		catch(Exception e){
			Console.write("Unexpected error in Connection view (this should not have happened). Stack trace:");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void onGUIRender() {
		if(m_startConnection == true){
			Handler.connect();
			m_startConnection = false;
		}
		if(Handler.getConnectionResult().ready()==true){
			try{
				Handler.getConnectionResult().get();
				m_startConnection = true;
				returnToPreviousState();
			}catch(Exception e){
				m_failedToConnect = true;
			}
		}
		else{
			m_connectingText.setOrigin(m_connectingText.getWidth()/2.0f, m_connectingText.getHeight()/2.0f);
			m_connectingText.setPosition((float)Application.getWidth()/2.0f, (float)Application.getHeight()/2.0f);
			m_connectingText.render();
		}

		if (m_failedToConnect) {

			m_panel.setSize(Application.getWidth() / 4.0f, 75.0f);
			m_panel.setOrigin(m_panel.getWidth() / 2.0f, m_panel.getHeight() / 2.0f);
			m_panel.setPosition(Application.getWidth() / 2.0f, Application.getHeight() / 2.0f);
			m_panel.begin("Failed to connect to server");
			//ImGui.newLine();
			m_question.setOriginX(m_question.getWidth() / 2.0f);
			m_question.setPositionX(m_panel.getWidth() / 2.0f);
			m_question.render();
			ImGui.separator();
			m_no.setWidth(ImGui.getWindowWidth() / 2.0f - ImGui.getStyle().getWindowPaddingX() - 10.0f);
			m_no.setOriginX(m_no.getWidth() / 2.0f);
			m_no.setPositionX(m_panel.getWidth() / 4.0f);
			if (m_no.render()) {

				m_failedToConnect = false;
				Application.close();
			}
			ImGui.sameLine();
			m_yes.setWidth(ImGui.getWindowWidth() / 2.0f - ImGui.getStyle().getWindowPaddingX() - 10.0f);
			m_yes.setOriginX(m_yes.getWidth() / 2.0f);
			m_yes.setPositionX(m_panel.getWidth() * 3.0f / 4.0f);
			if (m_yes.render()) {

				m_startConnection = true;
				m_failedToConnect = false;
			}

			m_panel.end();
		}
	}

	private boolean m_startConnection = true;
	private Text m_connectingText = new Text("Connecting...");
	private Panel m_panel = new Panel();
	private boolean m_failedToConnect = false;
	private Text m_question = new Text("Select an option");
	private Button m_no = new Button("Quit");
	private Button m_yes = new Button("Retry");
}
