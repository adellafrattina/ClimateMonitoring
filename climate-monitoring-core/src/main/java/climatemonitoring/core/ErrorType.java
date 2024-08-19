/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The identifiers the database can use to assign an error message to the parameter that violated an integrity constraint or server check
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public enum ErrorType {

	GEONAME_ID,
	NAME,
	ASCII_NAME,
	COUNTRY_CODE,
	COUNTRY_NAME,
	LATITUDE,
	LONGITUDE,
	CENTER_ID,
	STREET,
	POSTAL_CODE,
	CITY,
	DISTRICT,
	USER_ID,
	SSID,
	SURNAME,
	EMAIL,
	PASSWORD,
	CATEGORY_ID,
	DATE,
	TIME,
	SCORE,
	NOTES,
	EXPLANATION
}
