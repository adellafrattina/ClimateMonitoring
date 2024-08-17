/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.StringTokenizer;

/**
 * The Command class processes a command line string to extract and store a command and its arguments, provides methods to retrieve these values, and includes constants for various predefined commands
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class Command {
	
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



	/**
	 * Parses a command line string to extract a command and its associated arguments, storing them in the command and arguments fields
	 * @param line
	 */
	public Command(String line){
		
		StringTokenizer st = new StringTokenizer(line);

		if(st.hasMoreTokens())
			m_cmd = st.nextToken().trim();

		if(st.hasMoreTokens())
			m_args = st.nextToken("").trim();
		
	}

	/**
	 * Returns the command (if the line is a command statement)
	 * @return the command that was entered by the user
	 */
	public String getCmd(){

		return m_cmd;
	}

	/**
	 * Returns the command arguments (if the line is a command statement)
	 * @return the command arguments that was entered by the user
	 */
	public String getArgs(){

		return m_args;
	}

	private String m_cmd = "";
	private String m_args = "";
}
