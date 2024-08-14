/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.headless;

import climatemonitoring.core.Application;
import climatemonitoring.core.ApplicationSpecification;
import climatemonitoring.core.Layer;

/**
 * ApplicationHeadless is the application version that does not provide a Graphical User Interface (GUI).
 * It is limited to log messages on the terminal throught the use of the {@link Console} class.
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see Application
 * @see climatemonitoring.core.gui.ApplicationGUI
 */
public class ApplicationHeadless extends Application {

	/**
	 * This constructor needs to be called once only.
	 * @param spec The application specification
	 * @see ApplicationSpecification
	 */
	public ApplicationHeadless(ApplicationSpecification spec) {

		super(spec, Application.HEADLESS);
	}

	/**
	 * It runs the main loop where all the layers are updated and the graphics is rendered or the cmd inputs are executed
	 */
	@Override
	public void run() {

		while (isRunning()) {

			// Execute the onUpdate method
			for (Layer layer : m_layers)
				layer.onUpdate();

			// Execute the onHeadlessRender method
			for (Layer layer : m_layers)
				layer.onHeadlessRender();

			try {

				Thread.sleep(m_specification.sleepDuration);
			}

			catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * Shuts down the application when {@link #close()} method gets called
	 */
	@Override
	public void shutdown() {

		for (Layer layer : m_layers)
			layer.onDetach();
	}
}
