/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Result;

/**
 * The CheckMT class is used to check whether a resource's parameter is compliant to the application or not.
 * If not, then it will be returned an error message string. It is the same as the Check class, but multi-threaded
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see Check
 */
class CheckMT {

	/**
	 * Checks if the geoname id is already taken
	 * @param geoname_id The geoname id
	 * @return A result that contains an error message as string if the parameter is already taken, null if it is unique
	 */
	public static Result<String> geonameID(int geoname_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.geonameID(geoname_id);
			}
		};
	}

	/**
	 * Checks if the center id is already taken or is empty or has contiguous dashes
	 * @param center_id The center id
	 * @return A result that contains an error message as string if the parameter is already taken, null if it is unique
	 */
	public static Result<String> creationCenterID(String center_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.creationCenterID(center_id);
			}
		};
	}

	/**
	 * Checks if the specified center is present or is empty or has contiguous dashes
	 * @param center_id The center id
	 * @return A result that contains an error message as string if the parameter is not present, null if it exists
	 */
	public static Result<String> registrationCenterID(String center_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.registrationCenterID(center_id);
			}
		};
	}

	/**
	 * Checks if the address is already taken or is empty or has contiguous dashes
	 * @param city The geoname id of the center's city
	 * @param street The address' street
	 * @param house_number The address' house number
	 * @return A result that contains an error message as string if the parameter is already taken, null if it is unique
	 */
	public static Result<String> address(int city, String street, int house_number) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.address(city, street, house_number);
			}
		};
	}

	/**
	 * Checks if the user id is already taken or is empty or has contiguous dashes
	 * @param user_id The user id
	 * @return A result that contains an error message as string if the parameter is already taken, null if it is unique
	 */
	public static Result<String> userID(String user_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.userID(user_id);
			}
		};
	}

	/**
	 * Checks if the SSID is already present in the database or is empty or has contiguous dashes or does not have 16 characters
	 * @param s The SSID
	 * @return A result that contains an error message as string if the parameter is already taken, null if it is unique
	 */
	public static Result<String> ssid(String s) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.ssid(s);
			}
		};
	}

	/**
	 * Checks if the email is already taken or is empty or has contiguous dashes or is not a valid email
	 * @param e The email
	 * @return A result that contains an error message as string if the parameter is already taken / is not valid, null if it is unique
	 */
	public static Result<String> email(String e) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.email(e);
			}
		};
	}

	/**
	 * Checks if the operator's credentials are valid
	 * @param user_id The user id
	 * @param password The user's password
	 * @return A result that contains an error message as string if the parameters are not valid, null if they are corrent
	 */
	public static Result<String> login(String user_id, String password) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.login(user_id, password);
			}
		};
	}

	/**
	 * Checks if the specified center monitors the specified area
	 * @param center_id The center id
	 * @param geoname_id The area's geoname id
	 * @return A result that contains an error message as string if the center does not monitor the area, null if it does
	 */
	public static Result<String> monitors(String center_id, int geoname_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.monitors(center_id, geoname_id);
			}
		};
	}

	/**
	 * Checks if the specified center employs the specified operator
	 * @param center_id The center id
	 * @param user_id The operator's user id
	 * @return A result that contains an error message as string if the center does not employ the operator, null if does
	 */
	public static Result<String> employs(String center_id, String user_id) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.employs(center_id, user_id);
			}
		};
	}

	/**
	 * Checks if the category exists
	 * @param c The category to be checked
	 * @return A result that contains an error message as string if the parameter is not found, null if it is unique
	 */
	public static Result<String> category(String c) {

		return new Result<String>() {

			@Override
			public String exec() throws ConnectionLostException {

				return Check.category(c);
			}
		};
	}
}
