/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Abstract class that is used to represent all the view state of an application
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
public abstract class ViewState {

	/**
	 * To set the view inside the view state (it can only be done from the core package)
	 * @param view The desired view
	 */
	void setView(View view) {

		m_view = view;
	}

	/**
	 * 
	 * @return The specified view
	 */
	protected synchronized View getView() {

		return m_view;
	}

	/**
	 * Sets the view state
	 */
	protected synchronized void setCurrentState(String state_index){

		m_view.setCurrentState(state_index);
	}

	/**
	 * 
	 * @return The current view state
	 */
	protected synchronized ViewState getCurrentState() {

		return m_view.getCurrentState();
	}

	/**
	 * 
	 * @return The previous view state
	 */
	public synchronized ViewState getPreviousState() {

		return m_view.getPreviousState();
	}

	/**
	 * 
	 * @return The current state's index as a String
	 */
	public synchronized String getCurrentStateIndex() {

		return m_view.getCurrentStateIndex();
	}

	/**
	 * 
	 * @return The previous state's index as a String
	 */
	protected synchronized String getPreviousStateIndex() {

		return m_view.getPreviousStateIndex();
	}

	/**
	 * Resets the data of a specified view state.
	 * It can also be used to set variables for another view by
	 * getting the desired state with {@link View#getState(String)} method and casting to the correct view state implementation
	 * @param state The desired state
	 * @return True if the specified state exists in the view's state list, false if not
	 */
	protected synchronized boolean resetStateData(ViewState state) {

		return m_view.resetStateData(state);
	}

	/**
	 * To return to the previous view state
	 */
	protected synchronized void returnToPreviousState() {

		m_view.returnToPreviousState();
	}

	/**
	 * Code that runs in headless configuration only
	 * @param args
	 */
	public abstract void onHeadlessRender(String args);

	/**
	 * Code that runs in GUI configuration only
	 */
	public abstract void onGUIRender();

	private View m_view;
}
