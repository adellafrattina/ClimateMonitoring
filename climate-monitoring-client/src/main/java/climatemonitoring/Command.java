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
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
class Command {

	/**
	 * Parses a command line string to extract a command and its associated arguments, storing them in m_cmd and m_args
	 * @param line
	 */
	public void Command(String line){
		
		StringTokenizer st = new StringTokenizer(line);
		if(st.hasMoreTokens()){

			m_cmd = st.nextToken().substring(1);
			m_args = st.nextToken("");
		}
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
	
	public static final String SEARCH = "search";
	public static final String VIEW = "view";
	public static final String LOGIN = "login";
	public static final String REGISTER = "register";
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String SETTINGS = "settings";
	private String m_cmd;
	private String m_args;
}
