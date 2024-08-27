package climatemonitoring;

import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.headless.Console;

class CenterCreation extends ViewState {

	@Override
	public void onHeadlessRender(String args) {

		String errorMsg = null;

		try {

			do {

				m_centerID = Console.read("Center ID > ");
				errorMsg = Check.creationCenterID(m_centerID);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				try {

					m_city = Integer.parseInt(Console.read("City > "));
					m_street = Console.read("Street > ");
					m_houseNumber = Integer.parseInt(Console.read("House number > "));

					errorMsg = Check.address(m_city, m_street, m_houseNumber);
				}

				catch (NumberFormatException e) {

					errorMsg = "City and house number must be numbers";
				}

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			do {

				errorMsg = null;

				try {
					m_postalCode = Integer.parseInt(Console.read("Postal code > "));
				} catch (NumberFormatException e) {
					errorMsg = "Postal code must be a number";
					Console.write(errorMsg);
				}

			} while (errorMsg != null);

			do {

				m_district = Console.read("District > ");
				errorMsg = Check.district(m_district);

				if (errorMsg != null)
					Console.write(errorMsg);

			} while (errorMsg != null);

			newCenter = new Center(m_centerID, m_street, m_houseNumber, m_postalCode, m_city, m_district);

			if (getView().getPreviousStateIndex() == ViewType.REGISTRATION) return;

			Handler.getProxyServer().addCenter(newCenter);
			Console.write("Monitoring center created succesfully!");
		}

		catch (DatabaseRequestException e) {

			Console.write(e.getMessage());
			onHeadlessRender("");
		}

		catch (ConnectionLostException e) {

			setCurrentState(ViewType.CONNECTION);
		}
	}

	@Override
	public void onGUIRender() {

		throw new UnsupportedOperationException("Unimplemented method 'onGUIRender'");
	}

	Center newCenter;

	private String m_centerID;
	private String m_street;
	private int m_houseNumber;
	private int m_postalCode;
	private int m_city;
	private String m_district;
}
