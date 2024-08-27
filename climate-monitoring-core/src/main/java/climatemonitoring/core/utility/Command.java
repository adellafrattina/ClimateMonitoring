/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.utility;

import java.util.StringTokenizer;

/**
 * The Command class processes a command line string to extract and store a command and its arguments, provides methods to retrieve these values
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
public class Command {

	/**
	 * Parses a command line string to extract a command and its associated arguments, storing them in the command and arguments fields
	 * @param line The line inputted by a user
	 */
	public Command(String line) {
		
		StringTokenizer st = new StringTokenizer(line.toLowerCase().trim());

		if(st.hasMoreTokens())
			m_cmd = st.nextToken().trim();

		if(st.hasMoreTokens())
			m_args = st.nextToken("").trim();
	}

	/**
	 * Returns the command (if the line is a command statement)
	 * @return The command that was entered by the user
	 */
	public String getCmd() {

		return m_cmd;
	}

	/**
	 * Returns the command arguments (if the line is a command statement)
	 * @return The command arguments that was entered by the user
	 */
	public String getArgs() {

		return m_args;
	}

	private String m_cmd = "";
	private String m_args = "";
}
