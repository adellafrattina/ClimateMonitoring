/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The class that holds the application settings to use during its creation.
 * It holds the settings for both Headless and GUI configuration
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see Application
 */
public class ApplicationSpecification {

	/**
	 * The desired window name.
	 * "App" by default
	 */
	public String title = "App";

	/**
	 * The desired window width in pixels. Meaningful only in GUI configuration.
	 * 1280 by default
	 */
	public int width = 1280;

	/**
	 * The desired window height in pixels. Meaningful only in GUI configuration.
	 * 720 by default
	 */
	public int height = 720;

	/**
	 * If the application should start as fullscreen
	 * False by default
	 */
	public boolean isFullscreen = false;

	/**
	 * If the application should enable the vertical synchronization (The application's frame rate will be the monitor's frame rate).
	 * False by default because it is preferable to use the {@link Application#setFramerateLimit(int)} method to reduce the CPU and GPU utilization
	 */
	public boolean vSync = false;

	/**
	 * Time (in milliseconds) that elapses between two iterations of the main loop. Meaningful only in headless configuration.
	 * 10 by default
	 */
	public long sleepDuration = 10;
}
