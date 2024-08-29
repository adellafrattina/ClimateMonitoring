/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

/**
 * The CommandType class gives a list of the application's commands that can be used by a user
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class CommandType {
	
	/**
	 * The command used to search areas by name, country and coords
	 */
	public static final String SEARCH = "search";
	
	/**
	 * The command used to visualize the info about an area or a center
	 */
	public static final String VIEW = "view";
	
	/**
	 * The command used to log into the application with user_id and password
	 */
	public static final String LOGIN = "login";

	/**
	 * The command used to log out
	 */
	public static final String LOGOUT = "logout";
	
	/**
	 * The command used to create a new account with user_id, surname, name, ssid, email and password
	 */
	public static final String REGISTER = "register";
	
	/**
	 * The command to add an area, center or parameter
	 */
	public static final String ADD = "add";
	
	/**
	 * The command to edit the operator's account
	 */
	public static final String EDIT = "edit";

	/**
	 * The command used to include an existing area to the logged operator's center
	 */
	public static final String INCLUDE = "include";
	
	/**
	 * The command to change a bunch of settings such as the application's theme to classic, light and dark
	 */
	public static final String SETTINGS = "settings";

	/**
	 * The command to show in milliseconds how long does it take for a packet sent from the client to arrive to the server
	 */
	public static final String PING = "ping";

	/**
	 * The command that shows all the commands
	 */
	public static final String HELP = "help";

	/**
	 * The command that closes the application
	 */
	public static final String EXIT = "exit";
}
