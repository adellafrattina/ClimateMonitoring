/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Encapsulates all the properties of a parameter
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes parameter fields
	 * @param geoname_id The area where the parameter was recorded
	 * @param center_id The center that registered the parameter
	 * @param user_id The operator's userid that registered the parameter
	 * @param category The parameter's category
	 * @param timestamp The recording date and time of the parameter
	 * @param score The score given to the parameter
	 * @param notes The operator's notes about the parameter
	 */
	public Parameter(int geoname_id, String center_id, String user_id, String category, LocalDateTime timestamp, int score, String notes) {

		m_geonameID = geoname_id;
		m_centerID = center_id;
		m_userID = user_id;
		m_category = category;
		m_timestamp = timestamp;
		m_score = score;
		m_notes = notes;
	}

	/**
	 * 
	 * @return The area where the parameter was recorded
	 */
	public int getGeonameID() {

		return m_geonameID;
	}

	/**
	 * 
	 * @return The center that registered the parameter
	 */
	public String getCenterID() {
	
		return m_centerID;
	}

	/**
	 * 
	 * @return The operator's userid that registered the parameter
	 */
	public String getUserID() {
	
		return m_userID;
	}

	/**
	 * 
	 * @return The parameter's category
	 */
	public String getCategory() {
	
		return m_category;
	}

	/**
	 * 
	 * @return The recording date and time of the parameter
	 */
	public LocalDateTime getTimestamp() {
	
		return m_timestamp;
	}

	/**
	 * 
	 * @return The score given to the parameter
	 */
	public int getScore() {
	
		return m_score;
	}

	/**
	 * 
	 * @return The operator's notes about the parameter
	 */
	public String getNotes() {
	
		return m_notes;
	}

	private int m_geonameID;
	private String m_centerID;
	private String m_userID;
	private String m_category;
	private LocalDateTime m_timestamp;
	private int m_score;
	private String m_notes;
}
