/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;

/**
 * The Check class is used to check whether a resource's parameter is compliant to the application or not.
 * If not, then it will be returned an error message string
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class Check {

	/**
	 * Checks if the geoname id is already taken
	 * @param geoname_id The geoname id
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String geonameID(int geoname_id) {

		String msg = null;

		try {

			if (Handler.getProxyServer().getArea(geoname_id) != null)
				msg = "There is already another area with the same geoname id";
		}

		catch (ConnectionLostException e) {

			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the name is empty or has contiguous dashes
	 * @param n The name
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String name(String n) {

		String msg = null;

		if ((msg = isEmpty(n)) != null)
			return msg;

		if ((msg = noDashes(n)) != null)
			return msg;

		return msg;
	}

	/**
	 * Checks if the ascii name is empty or has contiguous dashes or is not ASCII
	 * @param ascii_name The ascii name
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String asciiName(String ascii_name) {

		String msg = null;

		if ((msg = isEmpty(ascii_name)) != null)
			return msg;

		if ((msg = noDashes(ascii_name)) != null)
			return msg;

		if (!Charset.forName("US-ASCII").newEncoder().canEncode(ascii_name))
			return "The value must be in ASCII format";

		return msg;
	}

	/**
	 * Checks if the country code is empty or has contiguous dashes or does not have only 2 letters or has a character that is not a letter
	 * @param country_code The country code
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String countryCode(String country_code) {

		String msg = null;

		if ((msg = isEmpty(country_code)) != null)
			return msg;

		if ((msg = noDashes(country_code)) != null)
			return msg;

		if (country_code.length() != 2)
			return "The value must be only 2 characters";

		for (int i = 0; i < country_code.length(); i++)
			if (!Character.isAlphabetic(country_code.charAt(i)))
				return "Only letters allowed";

		return msg;
	}

	/**
	 * Checks if the country name is empty or has contiguous dashes or is not ASCII
	 * @param country_name The ascii name
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String countryName(String country_name) {

		return asciiName(country_name);
	}

	/**
	 * Checks if the latitude is between -90 and 90
	 * @param l The latitude
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String latitude(double l) {

		if (l > 90 || l < -90)
			return "The value must be around -90 and 90";

		return null;
	}

	/**
	 * Checks if the longitude is between -180 and 180
	 * @param l The longitude
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String longitude(double l) {

		if (l > 180 || l < -180)
			return "The value must be around -180 and 180";

		return null;
	}

	/**
	 * Checks if the center id is already taken or is empty or has contiguous dashes
	 * @param center_id The center id
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String centerID(String center_id) {

		String msg = null;

		if ((msg = isEmpty(center_id)) != null)
			return msg;

		if ((msg = noDashes(center_id)) != null)
			return msg;

		try {

			if (Handler.getProxyServer().getCenter(center_id) != null)
				msg = "There is already another center with the same name";
		}

		catch (ConnectionLostException e) {

			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the address is already taken or is empty or has contiguous dashes
	 * @param street The address' street
	 * @param house_number The address' house number
	 * @param postal_code The address' postal code
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String address(String street, int house_number, int postal_code) {

		String msg = null;

		if ((msg = isEmpty(street)) != null)
			return msg;

		if ((msg = noDashes(street)) != null)
			return msg;

		//TODO: Create a method in Database interface to return a center based on the address

		return msg;
	}

	/**
	 * Checks if the district has contiguous dashes
	 * @param d The district
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String district(String d) {

		return noDashes(d);
	}

	/**
	 * Checks if the user id is already taken or is empty or has contiguous dashes
	 * @param user_id The user id
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String userID(String user_id) {

		String msg = null;

		if ((msg = isEmpty(user_id)) != null)
			return msg;

		if ((msg = noDashes(user_id)) != null)
			return msg;

		/*TODO:

		String msg = null;

		try {

			if (Handler.getProxyServer().getOperator(user_id) != null)
				msg = "There is already another operator with the same user id";
		}

		catch (ConnectionLostException e) {

			Handler.getView().setCurrentState(ViewType.CONNECTION);
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;

		 */

		return msg;
	}

	/**
	 * Checks if the name is empty or has contiguous dashes or does not have 16 characters
	 * @param s The SSID
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String ssid(String s) {

		String msg = null;

		if ((msg = isEmpty(s)) != null)
			return msg;

		if ((msg = noDashes(s)) != null)
			return msg;

		if (s.length() != 16)
			return "The SSID must be 16 characters (" + s.length() + "/16)";

		return null;
	}

	/**
	 * Checks if the surname is empty or has contiguous dashes
	 * @param sname The surname
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String surname(String sname) {

		return name(sname);
	}

	/**
	 * Checks if the email is empty or has contiguous dashes or is not a valid email
	 * @param e The email
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String email(String e) {

		String msg = null;

		if ((msg = isEmpty(e)) != null)
			return msg;

		if ((msg = noDashes(e)) != null)
			return msg;

		if (!Pattern.compile("^(.+)@(.+)$").matcher(e).matches())
			return "The value is not a valid e-mail";

		return null;
	}

	/**
	 * Checks if the password is empty or has contiguous dashes or has spaces
	 * @param p The password
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String password(String p) {

		String msg = null;

		if ((msg = isEmpty(p)) != null)
			return msg;

		if ((msg = noDashes(p)) != null)
			return msg;

		if (p.split(" ").length > 2)
			return "The value must not have spaces";

		return null;
	}

	/**
	 * Checks if the score is between 1 and 5
	 * @param s The score
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String score(int s) {

		if (s > 5 || s < 1)
			return "The value must be between 1 and 5";

		return null;
	}

	/**
	 * Checks if the notes are empty or have contiguous dashes or have spaces or have more than 256 characters
	 * @param n The notes
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String notes(String n) {

		String msg = null;

		if ((msg = isEmpty(n)) != null)
			return msg;

		if ((msg = noDashes(n)) != null)
			return msg;

		if (n.length() > 256)
			return "The value must be less than 256 characters (" + n.length() + "/256)";

		return null;
	}

	/**
	 * To check whether a string is empty or not
	 * @param str The desired string
	 * @return Null if the parameter is valid, a error message as a string if the parameter is null/empty/blank
	 */
	private static String isEmpty(String str) {

		return str == null || str.isEmpty() || str.isBlank() ? "This box must not be left blank" : null;
	}

	/**
	 * To check whether a string has contiguous dashed or not
	 * @param str The desired string
	 * @return Null if the parameter is valid, a error message as a string if the parameter has "--"
	 */
	private static String noDashes(String str) {

		return str.contains("--") ? "Contiguous dashes are not allowed" : null;
	}
}
