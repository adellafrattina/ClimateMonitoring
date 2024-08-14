/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The layer class is used to create the actual application.
 * A layer gets "inserted" into the core engine application which handles the events.
 * With this system, we can create test applications or even a debug menu on top of our main application layer
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 * @see Application
 */
public class Layer {

	/**
	 * Code to be executed as the layer gets created
	 */
	public void onAttach() {}

	/**
	 * Code that runs on every iteration of the main loop
	 */
	public void onUpdate() {}

	/**
	 * Code that runs in headless configuration only
	 */
	public void onHeadlessRender() {}

	/**
	 * Code that runs in GUI configuration only
	 */
	public void onGUIRender() {}

	/**
	 * Code to be executed as the layer gets deleted
	 */
	public void onDetach() {}
}
