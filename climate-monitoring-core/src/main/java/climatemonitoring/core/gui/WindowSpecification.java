/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

/**
 * The class that holds the window settings to use during its creation
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see Window
 */
public class WindowSpecification {

	/**
	 * The desired window name.
	 * "App" by default
	 */
	public String title = "App";

	/**
	 * The desired window width in pixels.
	 * 1280 by default
	 */
	public int width = 1280;

	/**
	 * The desired window height in pixels.
	 * 720 by default
	 */
	public int height = 720;

	/**
	 * If the window should start as fullscreen.
	 * False by default
	 */
	public boolean isFullscreen = false;

	/**
	 * If the window should enable the vertical synchronization (The window's frame rate will be the monitor's frame rate).
	 * True by default to prevent the window from overloading the CPU
	 */
	public boolean vSync = true;
}
