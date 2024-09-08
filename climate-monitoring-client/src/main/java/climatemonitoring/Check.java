/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import climatemonitoring.core.Category;
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
	 * @return An error message as string if the parameter is already taken, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String geonameID(String geoname_id) throws ConnectionLostException {

		String msg = null;

		if ((msg = isValidInteger(geoname_id)) != null)
			return msg;

		try {

			int geonameID = Integer.valueOf(geoname_id.trim());
			if (Handler.getProxyServer().getArea(geonameID) != null)
				msg = "There is already another area with the same geoname id";
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

		if (country_code.length() != 2)
			return "The value must be 2 characters long";

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
	public static String latitude(String l) {

		String msg = null;
		if ((msg = isValidDouble(l)) != null)
			return msg;

		double lat = Double.valueOf(l);
		if (lat > 90 || lat < -90)
			return "The value must be around -90 and 90";

		return null;
	}

	/**
	 * Checks if the longitude is between -180 and 180
	 * @param l The longitude
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String longitude(String l) {

		String msg = null;
		if ((msg = isValidDouble(l)) != null)
			return msg;

		double lon = Double.valueOf(l);
		if (lon > 180 || lon < -180)
			return "The value must be around -180 and 180";

		return null;
	}

	/**
	 * Checks if the center id is already taken or is empty or has contiguous dashes
	 * @param center_id The center id
	 * @return An error message as string if the parameter is already taken, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String creationCenterID(String center_id) throws ConnectionLostException {

		String msg = null;

		if ((msg = isEmpty(center_id)) != null)
			return msg;

		try {

			if (Handler.getProxyServer().getCenter(center_id) != null)
				msg = "There is already another center with the same name";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the center id is present or is empty or has contiguous dashes
	 * @param center_id The center id
	 * @return An error message as string if the parameter is not present, null if it exists
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String registrationCenterID(String center_id) throws ConnectionLostException {

		String msg = null;

		if (isEmpty(center_id) != null)
			return null;

		try {

			if (Handler.getProxyServer().getCenter(center_id) == null)
				msg = "This center does not exist";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the address is already taken or is empty or has contiguous dashes
	 * @param city The geoname id of the center's city
	 * @param street The address' street
	 * @param house_number The address' house number
	 * @return An array of strings that is the place-holder for the error messages. The array is guaranteed to be not null with a length of 4.
	 * At index 0 is saved the possible error message for city geoname ID.
	 * At index 1 is saved the possible error message for house number.
	 * At index 2 is saved the possible error message for the general address.
	 * At index 3 is saved the possible error message from the database.
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String[] address(String city, String street, String house_number) throws ConnectionLostException {

		String[] msgs = new String[4];

		try {

			String msgC = null;
			int valueC = 0;
			if ((msgC = isValidInteger(city)) != null)
				msgs[0] = msgC;
			else
				valueC = Integer.parseInt(city.trim());

			String msgH = null;
			int valueH = 0;
			if ((msgH = isValidInteger(house_number)) != null)
				msgs[1] = msgH;
			else
				valueH = Integer.parseInt(house_number.trim());

			if (msgC != null || msgH != null)
				return msgs;

			String msgA = null;
			if (Handler.getProxyServer().getCenterByAddress(valueC, street, valueH) != null)
				msgA = "There is already another center in the same place";

			msgs[2] = msgA;
		}

		catch (DatabaseRequestException e) {

			msgs[3] = e.getMessage();
		}

		return msgs;
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
	 * @return An error message as string if the parameter is already taken, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String userID(String user_id)  throws ConnectionLostException {

		String msg = null;

		if ((msg = isEmpty(user_id)) != null)
			return msg;

		try {

			if (Handler.getProxyServer().getOperator(user_id) != null)
				msg = "There is already another operator with the same user id";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the SSID is already present in the database or is empty or has contiguous dashes or does not have 16 characters
	 * @param s The SSID
	 * @return An error message as string if the parameter is already taken, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String ssid(String s) throws ConnectionLostException {

		String msg = null;

		if ((msg = isEmpty(s)) != null)
			return msg;

		if (s.length() != 16)
			return "The SSID must be 16 characters (" + s.length() + "/16)";

		try {

			if (Handler.getProxyServer().getOperatorBySSID(s) != null)
				msg = "There is already another operator with this SSID";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
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
	 * Checks if the email is already taken or is empty or has contiguous dashes or is not a valid email
	 * @param e The email
	 * @return An error message as string if the parameter is already taken, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String email(String e) throws ConnectionLostException {

		String msg = null;

		if ((msg = isEmpty(e)) != null)
			return msg;

		if (!Pattern.compile("^(.+)@(.+)$").matcher(e).matches())
			return "The value is not a valid e-mail";

		try {

			if (Handler.getProxyServer().getOperatorByEmail(e) != null)
				msg = "This email is already used by another operator";
		}

		catch (DatabaseRequestException ex) {

			msg = ex.getMessage();
		}

		return msg;
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

		if (p.split(" ").length > 2)
			return "The value must not have spaces";

		return null;
	}

	/**
	 * Checks if the operator's credentials
	 * @param user_id The user id
	 * @param password The user's password
	 * @return An error message as string if the parameters are not valid, null if they are correct
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String login(String user_id, String password) throws ConnectionLostException {

		String msg = null;

		if (password.split(" ").length > 2)
			return "The value must not have spaces";

		try {

			if (Handler.getProxyServer().validateCredentials(user_id, password) == null)
				msg = "User ID or password are incorrect";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the specified center monitors the specified area
	 * @param center_id The center id
	 * @param geoname_id The area's geoname id
	 * @return An error message as string if the center does not monitor the area, null if it does
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String monitors(String center_id, String geoname_id) throws ConnectionLostException {

		String msg = null;

		try {

			if ((msg = isValidInteger(geoname_id)) != null)
				return msg;

			int geonameID = Integer.parseInt(geoname_id.trim());

			if (!Handler.getProxyServer().monitors(center_id, geonameID))
				msg = "The center does not monitor the specified area";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the specified center employs the specified operator
	 * @param center_id The center id
	 * @param user_id The operator's user id
	 * @return An error message as string if the center does not employ the operator, null if does
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String employs(String center_id, String user_id) throws ConnectionLostException {

		String msg = null;

		try {

			if (!Handler.getProxyServer().employs(center_id, user_id))
				msg = "The center does not employ the specified operator";
		}

		catch (DatabaseRequestException e) {

			msg = e.getMessage();
		}

		return msg;
	}

	/**
	 * Checks if the category exists
	 * @param c The category to be checked
	 * @return An error message as string if the parameter is not found, null if it is unique
	 * @throws ConnectionLostException When the connection is lost
	 */
	public static String category(String c) throws ConnectionLostException {

		String msg = null;

		if ((msg = isEmpty(c)) != null)
			return msg;

			try {

				Category[] categories = Handler.getProxyServer().getCategories();
				for (Category category : categories) {

					if (category.getCategory().trim().toLowerCase().equals(c.trim().toLowerCase())) {

						return null;
					}
				}

				msg = "The value must be a valid category";
			}
	
			catch (DatabaseRequestException e) {
	
				msg = e.getMessage();
			}
	
			return msg;
	}

	/**
	 * Checks if the score is between 1 and 5
	 * @param s The score
	 * @return Null if the parameter is valid, an error message as string if not
	 */
	public static String score(String s) {

		String msg = null;
		if ((msg = isValidInteger(s)) != null)
			return msg;

		int sc = Integer.parseInt(s.trim());

		if (sc > 5 || sc < 1)
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

		if (n.length() > 256)
			return "The value must be less than 256 characters (" + n.length() + "/256)";

		return null;
	}

	/**
	 * To check whether a string is empty or not
	 * @param str The desired string
	 * @return Null if the parameter is valid, a error message as a string if the parameter is null/empty/blank
	 */
	public static String isEmpty(String str) {

		return str == null || str.isEmpty() || str.isBlank() ? "This box must not be left blank" : null;
	}

	/**
	 * Checks if the string is a valid integer
	 * @param str The string to check
	 * @return An error message as string if the parameter is not an integer, null if it is
	 */
	public static String isValidInteger(String str) {

		try {

			if (str.length() > 9)
				return "The value is too big";

			Integer.parseInt(str.trim());
		}
		
		catch (NumberFormatException e) {
			
			return "The value must be a number";
		}

		return null;
	}

	/**
	 * Checks if the string is a valid double
	 * @param str The string to check
	 * @return An error message as string if the parameter is not an double, null if it is
	 */
	public static String isValidDouble(String str) {

		try {

			if (str.length() > 9)
				return "The value is too big";

			Double.parseDouble(str.trim());
		}

		catch (NumberFormatException e) {

			return "The value must be a number";
		}

		return null;
	}

	/**
	 * To check whether a string has contiguous dashed or not
	 * @param str The desired string
	 * @return Null if the parameter is valid, a error message as a string if the parameter has "--"
	 */
	public static String noDashes(String str) {

		return str.contains("--") ? "Contiguous dashes are not allowed" : null;
	}
}
