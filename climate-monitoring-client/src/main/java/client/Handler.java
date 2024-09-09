/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import climatemonitoring.core.Result;
import climatemonitoring.core.View;

import java.net.InetAddress;
import java.net.UnknownHostException;

import climatemonitoring.core.Application;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.Operator;

/**
 * The Handler class uses the singleton pattern and it is the center for all the common operations and variables. It will be shared across every class. Its methods must be synchronized
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Handler {

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
	 * To set the new server address and port. It is required to call the {@link #connect()} method
	 * @param address A valid server address
	 * @param port The server's port
	 */
	public synchronized static void setNewServerAddress(InetAddress address, short port) {

		try {

			if (!get().m_connectionResult.ready()) {

				Handler.getProxyServer().forceClose();
				get().m_connectionResult.interrupt();
				get().m_connectionResult = null;
			}
			else
				Handler.getProxyServer().close();
		}

		catch (ConnectionLostException e) {}

		get().m_address = address;
		get().m_port = port;
	}

	/**
	 * 
	 * @return The current server's address
	 */
	public synchronized static InetAddress getServerAddress() {

		return get().m_address;
	}

	/**
	 * 
	 * @return The current server's port
	 */
	public synchronized static short getServerPort() {

		return get().m_port;
	}

	/**
	 * Starts connecting to a server. The operation result can be retrieved with the {@link #getConnectionResult()} method
	 */
	public synchronized static void connect() {

		get().m_connectionResult = get().m_proxyMT.connect(get().m_address.getHostName(), get().m_port);
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

		try {

			m_address = InetAddress.getLocalHost();
			m_port = 25565;
		}

		catch (UnknownHostException e) {

			e.printStackTrace();
			Application.close();
			return;
		}

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
		m_view.addState(new Help());

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
	private InetAddress m_address;
	private short m_port;
}
