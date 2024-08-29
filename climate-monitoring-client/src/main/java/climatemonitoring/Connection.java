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
			e.printStackTrace();
		}
		returnToPreviousState();
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
