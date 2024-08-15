/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * Represents all the properties of a monitoring center
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Center {

	/**
	 * Initalizes center fields
	 * @param center_id
	 * @param street
	 * @param house_number
	 * @param postal_code
	 * @param city
	 * @param district
	 */
	public Center(String center_id, String street, int house_number, int postal_code, String city, String district) {

		m_centerID = center_id;
		m_street = street;
		m_houseNumber = house_number;
		m_postalCode = postal_code;
		m_city = city;
		m_district = district;
	}

	/**
	 * 
	 * @return The monitoring center ID
	 */
	public String getCenterID() {

		return m_centerID;
	}

	/**
	 * 
	 * @return The monitoring center's street
	 */
	public String getStreet() {

		return m_street;
	}

	/**
	 * 
	 * @return The monitoring center's house number
	 */
	public int getHouseNumber() {

		return m_houseNumber;
	}

	/**
	 * 
	 * @return The monitoring center's postal code
	 */
	public int getPostalCode() {

		return m_postalCode;
	}

	/**
	 * 
	 * @return The monitoring center's city
	 */
	public String getCity() {

		return m_city;
	}

	/**
	 * 
	 * @return The monitoring center's district
	 */
	public String getDistrict() {

		return m_district;
	}

	private String m_centerID;
	private String m_street;
	private int m_houseNumber;
	private int m_postalCode;
	private String m_city;
	private String m_district;
	
}
