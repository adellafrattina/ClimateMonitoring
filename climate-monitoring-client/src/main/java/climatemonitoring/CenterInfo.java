package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;
import climatemonitoring.core.utility.Command;

class CenterInfo extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		try {

			Command c = new Command(args);

			String by = c.getCmd();

			if (by.equals("id")) {

				String id = c.getArgs();
				Center center = Handler.getProxyServer().getCenter(id.trim());
				
				if (center != null) {

					printCenterInfo(center);
				} else {

					Console.write("Center not found");
				}
			} else if (by.equals("index")) {

				int index = Integer.parseInt(c.getArgs());
				
				Master m = (Master) Handler.getView().getState(ViewType.MASTER);
				Center[] centers = m.foundCenters;
				Center center = centers[index];
				
				if (center != null) {

					printCenterInfo(center);
				} else {
					
					Console.write("Center not found");
				}
			}else{

				Console.write("Incorrect command syntax -->'" + c.getCmd() + "', expected [id, index]");
			}
		} catch (DatabaseRequestException e) {

			Console.write(e.getMessage());
		} catch (ConnectionLostException e) {

			getView().setCurrentState(ViewType.CONNECTION);
		} catch (ArrayIndexOutOfBoundsException e) {
			
			Console.write("Invalid index provided. Please ensure the index is within the valid range");
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	private void printCenterInfo(Center center) {

		Console.write("Name > " + center.getCenterID());
		Console.write("Street > " + center.getStreet() + ", "+ center.getHouseNumber());
		Console.write("Postal code > " + center.getPostalCode());
		Area area = Handler.getProxyServer().getArea(center.getCity());
		Console.write("City > " + area.getAsciiName() + "(" + area.getGeonameID() + ")");
		Console.write("District > " + center.getDistrict());
	}
}
