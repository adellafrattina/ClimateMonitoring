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

	SEARCH_AREAS_BY_NAME,
	SEARCH_AREAS_BY_COUNTRY,
	SEARCH_AREAS_BY_COORDS,

	GET_PARAMETERS_AREA_CENTER,
	GET_CATEGORIES,

	ADD_AREA,
	ADD_CENTER,
	ADD_OPERATOR,
	ADD_PARAMETER,

	EDIT_OPERATOR,
	VALIDATE_CREDENTIALS
}
