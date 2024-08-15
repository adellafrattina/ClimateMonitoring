/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.io.Serializable;

/**
 * Encapsulates all the properties of an area
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes area fields
	 * @param geoname_id
	 * @param name
	 * @param ascii_name
	 * @param country_code
	 * @param country_name
	 * @param latitude
	 * @param longitude
	 */
	public Area(int geoname_id, String name, String ascii_name, String country_code, String country_name, double latitude, double longitude) {

		m_geonameID = geoname_id;
		m_name = name;
		m_asciiName = ascii_name;
		m_countryCode = country_code;
		m_countryName = country_name;
		m_latitude = latitude;
		m_longitude = longitude;
	}

	/**
	 * 
	 * @return The area geoname ID
	 */
	public int getGeonameID() {

		return m_geonameID;
	}

	/**
	 * 
	 * @return The area name
	 */
	public String getName() {

		return m_name;
	}

	/**
	 * 
	 * @return The area ascii name
	 */
	public String getAsciiName() {

		return m_asciiName;
	}

	/**
	 * 
	 * @return The area country code
	 */
	public String getCountryCode() {

		return m_countryCode;
	}

	/**
	 * 
	 * @return The area country name
	 */
	public String getCountryName() {

		return m_countryName;
	}

	/**
	 * 
	 * @return The area latitude
	 */
	public double getLatitude() {

		return m_latitude;
	}

	/**
	 * 
	 * @return The area longitude
	 */
	public double getLongitude() {

		return m_longitude;
	}

	private int m_geonameID;
	private String m_name;
	private String m_asciiName;
	private String m_countryCode;
	private String m_countryName;
	private double m_latitude;
	private double m_longitude;
}
