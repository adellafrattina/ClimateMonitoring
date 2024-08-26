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
			
			if (splitargs.length < 2) {
				throw new IllegalArgumentException("non ci sono abbastanza argomenti");
			}
			
			String by = splitargs[0];
			
			if ("id".equals(by)) {

				String id = splitargs[1];
				Center center = Handler.getProxyServer().getCenter(id);
				
				if (center != null) {

					Console.write(center.toString());
				} else {

					Console.write("centro non trovato");
				}
			} else if ("index".equals(by)) {

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
		} catch (NumberFormatException e) {

			Console.write("Error: Invalid number format.");
		} catch (ArrayIndexOutOfBoundsException e) {
			
			Console.write("Error: Index out of bounds.");
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}
}
