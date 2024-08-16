/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

/**
 * All the possible view states that the view can be
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
enum ViewType {

	/**
	 * Main view. Consists of search box and login button
	 */
	MASTER,

	/**
	 * Settings view. Contains theme change option and cancel button
	 */
	SETTINGS,

	/**
	 * Login view. Contains user_id and password input text, create new account button, cancel button and confirm button
	 */
	LOGIN,

	/**
	 * Registration view. Contains user_id, surname, name, ssid, email and password input text, option to create or select center, cancel button and confirm button
	 */
	REGISTRATION,

	/**
	 * Edit profile view. Contains user_id, surname, name, ssid, email and password input text, option to create or select center, cancel button and confirm button.
	 * The operator's info are already filled out
	 */
	EDIT_PROFILE,

	/**
	 * Center creation view. Contains center_id, street, house number, postal code, city and district input text, cancel button and confirm button.
	 * If the center is being created after registration, there is no cancel button
	 */
	CENTER_CREATION,

	/**
	 * Area creation view. Contains geoname_id, name, ascii name, country code, country name, latitude and longitude input text, cancel button and confirmation button
	 */
	AREA_CREATION,

	/**
	 * Parameter creation view. Contains geoname_id, center_id, user_id, category, date, time, score and notes input text, cancel button and confirm button
	 */
	PARAMETER_CREATION,

	/**
	 * Center info view. Contains Contains center_id, street, house number, postal code, city and district input text and cancel button.
	 * The center's info are already filled out
	 */
	CENTER_INFO,

	/**
	 * Area info view. Contains the area description, a drop-down menu to change center, a button to view the center that monitors the area,
	 * a drop-down menu to select the category, the category description, the parameter's table, a cancel button and a button to add a parameter
	 */
	AREA_INFO,

	/**
	 * Verification view. Contains a string that tells that a user should check his inbox and insert the code in the input text down below
	 */
	VERIFICATION,

	/**
	 * Connection view. While the application tires to connect to a server, this view is shown. If the Connection fails, this view tells the user if he wants to retry the connection
	 */
	CONNECTION
}
