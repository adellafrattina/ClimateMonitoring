/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;
import java.util.HashMap;
import java.util.Stack;

/**
 * The View class manages the state of a view and handles rendering operations based on the configuration (headless or GUI)
 * 
 * @author francescolops
 * @version 1.0-SNAPSHOT
 */
public class View {

	/**
	 * To add a new state to the view
	 * @param state The desired view state
	 * @return True if the state was addes, false if not
	 */
	public synchronized boolean addState(ViewState state) {

		if (m_states.get(state.getClass().getSimpleName()) != null)
			return false;

		m_states.put(state.getClass().getSimpleName(), state);
		state.setView(this);

		return true;
	}

	/**
	 * 
	 * @return All the view's possible states
	 */
	public synchronized HashMap<String, ViewState> getStates() {

		return m_states;
	}

	/**
	 * Returns a specified state
	 * @param state_index The index state (usually the class's state name)
	 * @return The specified view state
	 */
	public synchronized ViewState getState(String state_index) {

		return m_states.get(state_index);
	}

	/**
	 * Sets the view state
	 */
	public synchronized void setCurrentState(String state_index){
		
		m_currentStateIndex = state_index;
		m_stateHistory.push(state_index);
	}

	/**
	 * 
	 * @return The current view state
	 */
	public synchronized ViewState getCurrentState() {

		return m_states.get(m_currentStateIndex);
	}

	/**
	 * 
	 * @return The previous view state
	 */
	public synchronized ViewState getPreviousState() {

		return m_states.get(getPreviousStateIndex());
	}

	/**
	 * 
	 * @return The current state's index as a String
	 */
	public synchronized String getCurrentStateIndex() {

		return m_currentStateIndex;
	}

	/**
	 * 
	 * @return The previous state's index as a String
	 */
	public synchronized String getPreviousStateIndex() {

		String index = null;
		if (m_stateHistory.size() > 1)
			index = m_stateHistory.get(m_stateHistory.size() - 2);
		else
			index = m_stateHistory.get(m_stateHistory.size() - 1);

		return index;
	}

	/**
	 * Resets the data of a specified view state.
	 * It can also be used to set variables for another view by
	 * getting the desired state with {@link View#getState(String)} method and casting to the correct view state implementation
	 * @param state The desired state
	 * @return True if the specified state exists in the view's state list, false if not
	 */
	public synchronized boolean resetStateData(ViewState state) {

		if (m_states.get(state.getClass().getSimpleName()) == null)
			return false;

		m_states.put(state.getClass().getSimpleName(), state);
		state.setView(this);

		return true;
	}

	/**
	 * To return to the previous view state
	 */
	public synchronized void returnToPreviousState() {

		m_stateHistory.pop();
		m_currentStateIndex = m_stateHistory.peek();
	}
	
	/**
	 * Code that runs in headless configuration only
	 */
	public void onHeadlessRender(String args){
		
		m_states.get(m_currentStateIndex).onHeadlessRender(args);
	}
	
	/**
	 * Code that runs in GUI configuration only
	 */
	public void onGUIRender(){
		
		m_states.get(m_currentStateIndex).onGUIRender();
	}

	private HashMap<String, ViewState> m_states = new HashMap<String, ViewState>();
	private String m_currentStateIndex;
	private Stack<String> m_stateHistory = new Stack<String>();
}
