/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.util.LinkedList;

/**
 * Application is the main engine responsible for the main loop and its termination.
 * An application can be either headless or GUI.
 * There can only be one instance of this class throughout the program
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public abstract class Application {

	/**
	 * Used to represent the configuration status
	 */
	public static final int HEADLESS = 0;

	/**
	 * Used to represent the configuration status
	 */
	public static final int GUI = 1;

	/**
	 * This constructor needs to be called once only. It is responsible of window creation (GUI mode only)
	 * @param spec The application specification
	 * @param configuration The application configuration
	 * @see ApplicationSpecification
	 * @see climatemonitoring.core.gui.Window
	 */
	public Application(ApplicationSpecification spec, int configuration) {

		if (s_instance != null)
			throw new RuntimeException("There can only be one Application instance throughout the program (s_instance was not null)");

		s_instance = this;

		m_specification = spec;
		m_configuration = configuration;
		m_running = true;
		m_layers = new LinkedList<Layer>();
	}

	/**
	 * 
	 * @return The application configuration
	 */
	public static int getConfiguration() {

		return s_instance.m_configuration;
	}

	/**
	 * To check whether the main loop is still running
	 * @return True if the application is still running, false if not
	 */
	public static boolean isRunning() {

		return s_instance.m_running;
	}

	/**
	 * To notify the intention of terminating the application
	 */
	public static void close() {

		s_instance.m_running = false;
	}

	/**
	 * 
	 * @return The application's window width (GUI mode only)
	 */
	public static int getWidth() {

		return s_instance.getWindowWidth();
	}

	/**
	 * 
	 * @return The application's window height (GUI mode only)
	 */
	public static int getHeight() {

		return s_instance.getWindowHeight();
	}

	/**
	 * Adds another layer to be updated in the main loop.
	 * It runs the {@link Layer#onAttach()} method
	 * @param layer The layer to be added to the application
	 * @see Layer
	 */
	public void pushLayer(Layer layer) {

		m_layers.add(layer);
		layer.onAttach();
	}

	/**
	 * To set a limit to the frame rate. By doing this, the application will not saturate the CPU and GPU
	 * by going as fast as it can. It is also recommended to use this method instead of relying on the {@link ApplicationSpecification#vSync} flag
	 * @param framerate The frame rate (it is recommended to set it above 60 fps)
	 */
	public static void setFramerateLimit(int framerate) {

		s_instance.setFramerateLimitGUI(framerate);
	}

	/**
	 * It runs the main loop where all the layers are updated and the graphics is rendered or the cmd inputs are executed
	 */
	public abstract void run();

	/**
	 * Shuts down the application when {@link #close()} method gets called
	 */
	public abstract void shutdown();

	/**
	 * To be implemented in the subclasses
	 * @return The application's window width (GUI mode only)
	 * @see climatemonitoring.core.gui.ApplicationGUI
	 */
	protected int getWindowWidth() {

		return 0;
	}

	/**
	 * To be implemented in the subclasses
	 * @return The application's window height (GUI mode only)
	 * @see climatemonitoring.core.gui.ApplicationGUI
	 */
	protected int getWindowHeight() {

		return 0;
	}

	/**
	 * To be implemented in the subclasses
	 * @param framerate The application's frame rate (GUI mode only)
	 */
	protected void setFramerateLimitGUI(int framerate) {

		
	}

	protected static Application s_instance;
	protected ApplicationSpecification m_specification;
	protected boolean m_running;
	protected LinkedList<Layer> m_layers;
	private int m_configuration;
}
