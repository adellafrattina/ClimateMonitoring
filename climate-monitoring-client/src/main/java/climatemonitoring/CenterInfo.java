package climatemonitoring;

import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

class CenterInfo extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		try {

			String[] splitargs = args.split(" ");
			
			String by = splitargs[0];
			
			if (by.equals("id")) {

				String id = splitargs[1];
				Center center = Handler.getProxyServer().getCenter(id);
				
				if (center != null) {

					Console.write(center.toString());
				} else {

					Console.write("centro non trovato");
				}
			} else if (by.equals("index")) {

				int index = Integer.parseInt(splitargs[1]);
				
				Master m = (Master) Handler.getView().getState(ViewType.MASTER); 
				Center[] centers = m.foundCenters; 
				Center center = centers[index];
				
				if (center != null) {

					Console.write(center.toString());  
				} else {
					
					Console.write("centro non trovato");
				}
			} 
		} catch (DatabaseRequestException e) {

			Console.write(e.getMessage());
		} catch (ConnectionLostException e) {

			getView().setCurrentState("ViewType.CONNECTION");
		} catch (ArrayIndexOutOfBoundsException e) {
			
			Console.write("errore indexoutofbounds");
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
