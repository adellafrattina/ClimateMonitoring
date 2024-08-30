/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.ViewState;
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

		Handler.connect();
		Console.write("Connecting...");
		Handler.getConnectionResult().join();
		try{
			Handler.getConnectionResult().get();
			Console.write("Connection established");
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

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
