/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.io.Serializable;

/**
 * Encapsulates all the properties of an operator
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Operator implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes operator properties
	 * @param user_id The operator's user id
	 * @param ssid The operator's social security identification
	 * @param surname The operator's surname
	 * @param name The operator's name
	 * @param email The operator's email
	 * @param password The operator's password
	 * @param center_id The operator's center id
	 */
	public Operator(String user_id, char[] ssid, String surname, String name, String email, String password, String center_id) {

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
	 * @return The operator's user id
	 */
	public String getUserID() {

		return m_userID;
	}

	/**
	 * 
	 * @return The operator's ssid
	 */
	public char[] getSSID() {

		return m_ssid;
	}

	/**
	 * 
	 * @return The operator's surname
	 */
	public String getSurname() {

		return m_surname;
	}

	/**
	 * 
	 * @return The operator's name
	 */
	public String getName() {

		return m_name;
	}

	/**
	 * 
	 * @return The operator's email
	 */
	public String getEmail() {

		return m_email;
	}

	/**
	 * 
	 * @return The operator's password
	 */
	public String getPassword() {

		return m_password;
	}

	/**
	 * 
	 * @return The operator's center ID
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
