/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Encapsulates all the properties of an operator
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Operator {

	/**
	 * Initializes operator properties
	 * @param user_id
	 * @param ssid
	 * @param surname
	 * @param name
	 * @param email
	 * @param password
	 * @param center_id
	 */
	public Operator(String user_id, char[]ssid, String surname, String name, String email, String password, String center_id) {
		m_userID = user_id;
		m_ssid = ssid;
		m_surname = surname;
		m_name = name;
		m_email = email;
		m_password = password;
		m_centerID = center_id;
	}

	/**
	 * 
	 * @return operator user id
	 */
	public String getUserID() {

		return m_userID;
	}

	/**
	 * 
	 * @return operator ssid
	 */
	public char[] getSSID() {

		return m_ssid;
	}

	/**
	 * 
	 * @return operator surname
	 */
	public String getSurname() {

		return m_surname;
	}

	/**
	 * 
	 * @return operator name
	 */
	public String getName() {

		return m_name;
	}

	/**
	 * 
	 * @return operator email
	 */
	public String getEmail() {

		return m_email;
	}

	/**
	 * 
	 * @return operator password
	 */
	public String getPassword() {

		return m_password;
	}

	/**
	 * 
	 * @return operator center ID
	 */
	public String getCenterID() {

		return m_centerID;
	}

	private String m_userID;
	private char[] m_ssid;
	private String m_surname;
	private String m_name;
	private String m_email;
	private String m_password;
	private String m_centerID;
	
}
