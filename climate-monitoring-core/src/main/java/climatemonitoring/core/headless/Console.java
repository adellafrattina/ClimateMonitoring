/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.headless;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	 * To set the log level to debug
	 */
	public static final int DEBUG = 1;

	/**
	 * To set the log level to info
	 */
	public static final int INFO = 2;

	/**
	 * To set the log level to warning
	 */
	public static final int WARN = 3;

	/**
	 * To set the log level to error
	 */
	public static final int ERROR = 4;

	/**
	 * To disable the console
	 */
	public static final int DISABLE = 5;

	/**
	 * To set the red color
	 */
	public static final String RED = "\u001B[31m";

	/**
	 * To set the green color
	 */
	public static final String GREEN = "\u001B[32m";

	/**
	 * To set the yellow color
	 */
	public static final String YELLOW = "\u001B[33m";

	/**
	 * To set the blue color
	 */
	public static final String BLUE = "\u001B[34m";

	/**
	 * To set the purple color
	 */
	public static final String PURPLE = "\u001B[35m";

	/**
	 * To set the cyan color
	 */
	public static final String CYAN = "\u001B[36m";

	/**
	 * To set the white color
	 */
	public static final String WHITE = "\u001B[37m";

	/**
	 * <br> To set the log level: </br>
	 * <p> {@link Console#DEBUG} = all messages </p>
	 * <p> {@link Console#INFO} = all messages, except DEBUG </p>
	 * <p> {@link Console#WARN} = all messages, except DEBUG and INFO </p>
	 * <p> {@link Console#ERROR} = all messages, except DEBUG, INFO and WARN </p>
	 * <p> {@link Console#DISABLE} = to disable all messages </p>
	 * @param level The log level
	 */
	public void setLogLevel(int level) {

		m_logLevel = level;
	}

	/**
	 * Reads the user input
	 * @param str A message to communicate to the user when requesting input
	 * @param color The message color ({@link Console#RED}, {@link Console#GREEN}, {@link Console#YELLOW}, {@link Console#BLUE}, {@link Console#PURPLE}, {@link Console#CYAN}, {@link Console#WHITE})
	 * @return The user input
	 */
	public static String read(String str, String color) {

		if (str != null && !str.isEmpty() && !str.isBlank())
			System.out.print(color + str + RESET);

		return s_instance.m_in.nextLine();
	}

	/**
	 * Reads the user input
	 * @param str A message to communicate to the user when requesting input
	 * @return The user input
	 */
	public static String read(String str) {

		return read(str, WHITE);
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
	 * @param color The string color ({@link Console#RED}, {@link Console#GREEN}, {@link Console#YELLOW}, {@link Console#BLUE}, {@link Console#PURPLE}, {@link Console#CYAN}, {@link Console#WHITE})
	 */
	public static void write(String str, String color) {

		System.out.println(str);
	}

	/**
	 * Writes a string to the console and creates a new line
	 * @param str The desired string
	 */
	public static void write(String str) {

		System.out.println(str);
	}

	/**
	 * To print out debug messages
	 * @param msg The desired message
	 */
	public static void debug(String msg) {

		if (s_instance.m_logLevel > DEBUG)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][DEBUG] " + msg, BLUE);
	}

	/**
	 * To print out info messages
	 * @param msg The desired message
	 */
	public static void info(String msg) {

		if (s_instance.m_logLevel > INFO)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][INFO] " + msg, GREEN);
	}

	/**
	 * To print out warning messages
	 * @param msg The desired message
	 */
	public static void warn(String msg) {

		if (s_instance.m_logLevel > WARN)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][WARN] " + msg, YELLOW);
	}

	/**
	 * To print out error messages
	 * @param msg The desired message
	 */
	public static void error(String msg) {

		if (s_instance.m_logLevel > ERROR)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][ERROR] " + msg, RED);
	}

	/**
	 * Constructs the input buffer
	 */
	private Console() {

		m_in = new Scanner(System.in);
	}

	/**
	 * To revert to deafult color
	 */
	private static final String RESET = "\u001B[0m";

	private static Console s_instance = new Console();

	private int m_logLevel = 1;
	private Scanner m_in;
}
