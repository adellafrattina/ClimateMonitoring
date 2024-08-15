/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Encapsulates all the properties of a parameter
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Parameter {

	/**
	 * Initializes parameter fields
	 * @param geoname_id The area where the parameter was recorded
	 * @param center_id The center that registered the parameter
	 * @param user_id The operator's userid that registered the parameter
	 * @param category The parameter's category
	 * @param date The recording date of the parameter
	 * @param time The recording time of the parameter
	 * @param score The score given to the parameter
	 * @param notes The operator's notes about the parameter
	 */
	public Parameter(int geoname_id, String center_id, String user_id, String category, String date, String time, int score, String notes) {

		m_geonameID = geoname_id;
		m_centerID = center_id;
		m_userID = user_id;
		m_category = category;
		m_date = date;
		m_time = time;
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
	 * @return The recording date of the parameter
	 */
	public String getDate() {
	
		return m_date;
	}

	/**
	 * 
	 * @return The recording time of the parameter
	 */
	public String getTime() {
	
		return m_time;
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
	private String m_date;
	private String m_time;
	private int m_score;
	private String m_notes;
}
