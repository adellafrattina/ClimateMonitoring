/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.headless;

import java.io.IOException;
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
	public static final String RED = isWindows() ? "\033[31m" : "\u001B[31m";

	/**
	 * To set the green color
	 */
	public static final String GREEN = isWindows() ? "\033[32m" : "\u001B[32m";

	/**
	 * To set the yellow color
	 */
	public static final String YELLOW = isWindows() ? "\033[33m" : "\u001B[33m";

	/**
	 * To set the blue color
	 */
	public static final String BLUE = isWindows() ? "\033[34m" : "\u001B[34m";

	/**
	 * To set the purple color
	 */
	public static final String PURPLE = isWindows() ? "\033[35m" : "\u001B[35m";

	/**
	 * To set the cyan color
	 */
	public static final String CYAN = isWindows() ? "\033[36m" : "\u001B[36m";

	/**
	 * To set the white color
	 */
	public static final String WHITE = isWindows() ? "\033[97m" : "\u001B[37m";

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

		System.out.println(color + str + RESET);
	}

	/**
	 * Writes a string to the console and creates a new line
	 * @param str The desired string
	 */
	public static void write(String str) {

		write(str, WHITE);
	}

	/**
	 * To delete the previous line (used to hide info, such as a password)
	 */
	public static void deletePreviousLine() {

		System.out.print("\033[1A");
		System.out.print("\033[2K");
	}

	/**
	 * To print out debug messages
	 * @param msg The desired message
	 */
	public static void debug(String msg) {

		if (s_instance.m_logLevel > DEBUG)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][" + CYAN + "debug" + RESET + "] " + msg);
	}

	/**
	 * To print out info messages
	 * @param msg The desired message
	 */
	public static void info(String msg) {

		if (s_instance.m_logLevel > INFO)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][" + GREEN + "info" + RESET + "] " + msg);
	}

	/**
	 * To print out warning messages
	 * @param msg The desired message
	 */
	public static void warn(String msg) {

		if (s_instance.m_logLevel > WARN)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][" + YELLOW + "warn" + RESET + "] " + msg);
	}

	/**
	 * To print out error messages
	 * @param msg The desired message
	 */
	public static void error(String msg) {

		if (s_instance.m_logLevel > ERROR)
			return;

		write("[" + new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date()) + "][" + RED + "error" + RESET + "] " + msg);
	}

	/**
	 * Constructs the input buffer
	 */
	private Console() {

		m_in = new Scanner(System.in);
		activateANSICmd();
	}

	private static boolean isWindows() {

		return System.getProperty("os.name").toLowerCase().contains("windows");
	}

	private static void activateANSICmd() {

		if (!isWindows())
			return;

		try {

			Process proc = Runtime.getRuntime().exec("REG ADD HKCU\\Console /v VirtualTerminalLevel /d 0x00000001 /t REG_DWORD/f");
			proc.waitFor();
			if (proc.exitValue() == 0)
				System.err.println("Failed to set command prompt as ANSI. Headless mode could be corrupted");
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	/**
	 * To revert to deafult color
	 */
	private static final String RESET = isWindows() ? "\033[0m" : "\u001B[0m";

	private static Console s_instance = new Console();

	private int m_logLevel = 1;
	private Scanner m_in;
}
