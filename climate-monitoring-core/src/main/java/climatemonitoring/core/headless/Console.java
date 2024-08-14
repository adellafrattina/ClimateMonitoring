/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.headless;

import java.util.Scanner;

/**
 * Console is used to log messages or to request input from the user through the terminal. It is mainly used for a headless application
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see ApplicationHeadless
 */
public class Console {

	/**
	 * Reads the user input
	 * @param str A message to communicate to the user when requesting input
	 * @return The user input
	 */
	public static String read(String str) {

		if (str != null && !str.isEmpty() && !str.isBlank())
			System.out.print(str);

		return s_instance.m_in.nextLine();
	}

	/**
	 * Reads the user input
	 * @return The user input
	 */
	public static String read() {

		return read(null);
	}

	/**
	 * Writes a string to the console and creates a new line
	 * @param str The desired string
	 */
	public static void write(String str) {

		System.out.println(str);
	}

	/**
	 * Constructs the input buffer
	 */
	private Console() {

		m_in = new Scanner(System.in);
	}

	private static Console s_instance = new Console();
	private Scanner m_in;
}
