/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.gui.Modal;
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
				m_retryModal.open();
			}
		}
		else{
			m_connectingText.setOrigin(m_connectingText.getWidth()/2.0f, m_connectingText.getHeight()/2.0f);
			m_connectingText.setPosition((float)Application.getWidth()/2.0f, (float)Application.getHeight()/2.0f);
			m_connectingText.render();
		}
		int result = m_retryModal.render();
		if(result == Modal.CANCEL) Application.close();
		else if(result == Modal.OK) m_startConnection = true;
	}

	private boolean m_startConnection = true;
	private Text m_connectingText = new Text("Connecting...");
	private Modal m_retryModal = new Modal("Failed to connect to server", "Do you want to connect again?");
}
