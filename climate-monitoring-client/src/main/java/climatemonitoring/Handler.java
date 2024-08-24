/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import climatemonitoring.core.Result;
import climatemonitoring.core.View;
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
	 * 
	 * @return The view
	 */
	public synchronized static View getView() {

		return get().m_view;
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

		m_view.addState(new Master());
		m_view.addState(new Settings());
		m_view.addState(new Login());
		m_view.addState(new Registration());
		m_view.addState(new EditProfile());
		m_view.addState(new CenterCreation());
		m_view.addState(new AreaCreation());
		m_view.addState(new ParameterCreation());
		m_view.addState(new CenterInfo());
		m_view.addState(new AreaInfo());
		m_view.addState(new Connection());

		// adellafrattina: MASTER gets set before CONNECTION, so when we call "returnToPreviousState" inside the Connection view state,
		// it goes back to Master view state, instead of throwing NullPointerException
		m_view.setCurrentState(ViewType.MASTER);
		m_view.setCurrentState(ViewType.CONNECTION);
	}

	private static Handler s_instance;
	private Proxy m_proxy;
	private ProxyMT m_proxyMT;
	private Result<Boolean> m_connectionResult;
	private View m_view;
	private Operator m_loggedOperator;
}
