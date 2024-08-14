/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Abstract class that is used to represent all views of the application
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
public abstract class ViewState {

	/**
	 * Code that runs in headless configuration only
	 * @param args
	 */
	public abstract void onHeadlessRender(String args);


	/**
	 * Code that runs in GUI configuration only
	 */
	public abstract void onGUIRender();

	
}
