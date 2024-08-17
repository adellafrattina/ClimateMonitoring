/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.util.HashMap;
import java.util.Stack;

import climatemonitoring.core.Result;
import climatemonitoring.core.View;
import climatemonitoring.core.ViewState;
import climatemonitoring.core.Operator;

/**
 * The Handler class uses the singleton pattern and it is the center for all the common operations and variables. It will be shared across every class. Its methods must be synchronized
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
class Handler {

	/**
	 * 
	 * @return The static singleton instance
	 */
	public static Handler get() {

		return s_instance;
	}

	/**
	 * Initializes all the handler members required by the application by creating  singleton static instance
	 */
	public static void init() {

		s_instance = new Handler();
	}

	/**
	 * 
	 * @return The single-threaded proxy server
	 */
	public synchronized static Proxy getProxyServer() {

		return get().m_proxy;
	}

	/**
	 * 
	 * @return The multithreaded version of the proxy server
	 */
	public synchronized static ProxyMT getProxyServerMT() {

		return get().m_proxyMT;
	}

	/**
	 * Starts connecting to a server. The operation result can be retrieved with the {@link #getConnectionResult()} method
	 */
	public synchronized static void connect() {

		get().m_connectionResult = get().m_proxyMT.connect("localhost", (short)25565);
	}

	/**
	 * 
	 * @return The connection result
	 */
	public synchronized static Result<Boolean> getConnectionResult() {

		return get().m_connectionResult;
	}

	/**
	 * Checks if an operator is logged in or not
	 * @return True if an operator is logged in, false if not
	 */
	public synchronized static boolean isOperatorLoggedIn() {

		return get().m_loggedOperator != null;
	}

	/**
	 * Sets the operator that has logged in the application
	 * @param operator The operator that logged in
	 */
	public synchronized static void setLoggedOperator(Operator operator) {

		get().m_loggedOperator = operator;
	}

	/**
	 * 
	 * @return The operator that has previously logged in the application
	 */
	public synchronized static Operator getLoggedOperator() {

		return get().m_loggedOperator;
	}

	/**
	 * Sets the new view state to be rendered
	 * @param state The new state
	 */
	public synchronized static void setViewState(ViewType type) {

		get().m_view.setState(getViewState(type));
		get().m_viewTypeHistory.add(type);
	}

	/**
	 * Returns back to the previous view state
	 */
	public synchronized static void returnToPreviousViewState() {

		get().m_viewTypeHistory.pop();
		get().m_view.setState(getViewState(get().m_viewTypeHistory.peek()));
	}

	/**
	 * Deletes all the data about the specified view state
	 * @param type The desired view state'stype
	 */
	public synchronized static void resetViewStateData(ViewType type) {

		get().m_viewStates.put(type, convertViewFromStateToType(type));
	}

	/**
	 * Code that runs in Headless configuration only
	 * @param args The command arguments
	 */
	public synchronized static void onHeadlessRender(String args) {

		get().m_view.onHeadlessRender(args);
	}

	/**
	 * Code that runs in GUI configuration only
	 */
	public synchronized static void onGUIRender() {

		get().m_view.onGUIRender();
	}

	/**
	 * Contains the code to initialize all the handler's members 
	 */
	private Handler() {

		m_proxy = new ProxyImpl();
		m_proxyMT = new ProxyMTImpl(m_proxy);
		m_view = new View();
		m_viewStates = new HashMap<ViewType, ViewState>();
		m_viewTypeHistory = new Stack<ViewType>();

		for (ViewType type : ViewType.values())
			m_viewStates.put(type, convertViewFromStateToType(type));

		m_view.setState(m_viewStates.get(ViewType.CONNECTION));
		m_viewTypeHistory.push(ViewType.CONNECTION);
	}

	private static ViewState getViewState(ViewType type) {

		return get().m_viewStates.get(type);
	}

	private static ViewState convertViewFromStateToType(ViewType type) {

		switch (type) {

			case MASTER: return new Master();
			case SETTINGS: return new Settings();
			case LOGIN: return new Login();
			case REGISTRATION: return new Registration();
			case EDIT_PROFILE: return new EditProfile();
			case CENTER_CREATION: return new CenterCreation();
			case AREA_CREATION: return new AreaCreation();
			case PARAMETER_CREATION: return new ParameterCreation();
			case CENTER_INFO: return new CenterInfo();
			case AREA_INFO: return new AreaInfo();
			case VERIFICATION: return new Verification();
			case CONNECTION: return new Connection();
			default:
				throw new RuntimeException("'" + type.toString() + "'' is not handled");
		}
	}

	private static Handler s_instance;
	private Proxy m_proxy;
	private ProxyMT m_proxyMT;
	private Result<Boolean> m_connectionResult;
	private View m_view;
	private HashMap<ViewType, ViewState> m_viewStates;
	private Stack<ViewType> m_viewTypeHistory;
	private Operator m_loggedOperator;
}
