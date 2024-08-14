/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The View class manages the state of a view and handles rendering operations based on the configuration (headless or GUI)
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
public class View{

	/**
	 * Sets the view state
	 */
	public void setState(ViewState state){
		
		m_state = state;
	}
	
	/**
	 * Code that runs in headless configuration only
	 */
	public void onHeadlessRender(String args){
		
		m_state.onHeadlessRender(args);
	}
	
	/**
	 * Code that runs in GUI configuration only
	 */
	public void onGUIRender(){
		
		m_state.onGUIRender();
	}

	private ViewState m_state; 
}
