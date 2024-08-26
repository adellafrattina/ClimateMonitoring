/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * This class contains enumeration constants that are used
 * to send a request from the proxy (client) to the skeleton
 * (server). Every enum corresponds to a Database class method
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public enum RequestType {

	/**
	 * To begin a transaction
	 */
	BEGIN,

	/**
	 * To end a transaction
	 */
	END,


	/**
	 * To search areas by name
	 */
	SEARCH_AREAS_BY_NAME,

	/**
	 * To search areas by country
	 */
	SEARCH_AREAS_BY_COUNTRY,

	/**
	 * To search areas by coordinates
	 */
	SEARCH_AREAS_BY_COORDS,

	/**
	 * To search centers by name
	 */
	SEARCH_CENTERS_BY_NAME,


	/**
	 * To get an area based on its geoname id
	 */
	GET_AREA,

	/**
	 * To get a center based on its center id
	 */
	GET_CENTER,

	/**
	 * To get a center based on its address
	 */
	GET_CENTER_BY_ADDRESS,

	/**
	 * To get an operator based on their user id
	 */
	GET_OPERATOR,

	/**
	 * To get an operator based on their ssid
	 */
	GET_OPERATOR_BY_SSID,

	/**
	 * To get an operator based on their email address
	 */
	GET_OPERATOR_BY_EMAIL,

	/**
	 * To get the parameters based on an area and a center
	 */
	GET_PARAMETERS_AREA_CENTER,

	/**
	 * To get all the categories that are present in the database
	 */
	GET_CATEGORIES,


	/**
	 * To add a new area
	 */
	ADD_AREA,

	/**
	 * To add a new center
	 */
	ADD_CENTER,

	/**
	 * To add a new operator
	 */
	ADD_OPERATOR,

	/**
	 * To add a new parameter
	 */
	ADD_PARAMETER,


	/**
	 * To edit an existing operator
	 */
	EDIT_OPERATOR,

	/**
	 * To check if a monitoring center is monitoring an area
	*/
	MONITORS,

	/**
	 * To check if a monitoring center is employing an operator
	 */
	EMPLOYS,

	/**
	 * To check for operator credentials during login
	 */
	VALIDATE_CREDENTIALS,


	/**
	 * To request the connection latency
	 */
	PING,

	/**
	 * To safely disconnect from the server
	 */
	DISCONNECT
}
