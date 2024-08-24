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
class ViewType {

	/**
	 * Main view. Consists of search box and login button
	 */
	public static final String MASTER = Master.class.getSimpleName();

	/**
	 * Settings view. Contains theme change option and cancel button
	 */
	public static final String SETTINGS = Settings.class.getSimpleName();

	/**
	 * Login view. Contains user_id and password input text, create new account button, cancel button and confirm button
	 */
	public static final String LOGIN = Login.class.getSimpleName();

	/**
	 * Registration view. Contains user_id, surname, name, ssid, email and password input text, option to create or select center, cancel button and confirm button
	 */
	public static final String REGISTRATION = Registration.class.getSimpleName();

	/**
	 * Edit profile view. Contains user_id, surname, name, ssid, email and password input text, option to create or select center, cancel button and confirm button.
	 * The operator's info are already filled out
	 */
	public static final String EDIT_PROFILE = EditProfile.class.getSimpleName();

	/**
	 * Center creation view. Contains center_id, street, house number, postal code, city and district input text, cancel button and confirm button.
	 * If the center is being created after registration, there is no cancel button
	 */
	public static final String CENTER_CREATION = CenterCreation.class.getSimpleName();

	/**
	 * Area creation view. Contains geoname_id, name, ascii name, country code, country name, latitude and longitude input text, cancel button and confirmation button
	 */
	public static final String AREA_CREATION = AreaCreation.class.getSimpleName();

	/**
	 * Parameter creation view. Contains geoname_id, center_id, user_id, category, date, time, score and notes input text, cancel button and confirm button
	 */
	public static final String PARAMETER_CREATION = ParameterCreation.class.getSimpleName();

	/**
	 * Center info view. Contains Contains center_id, street, house number, postal code, city and district input text and cancel button.
	 * The center's info are already filled out
	 */
	public static final String CENTER_INFO = CenterInfo.class.getSimpleName();

	/**
	 * Area info view. Contains the area description, a drop-down menu to change center, a button to view the center that monitors the area,
	 * a drop-down menu to select the category, the category description, the parameter's table, a cancel button and a button to add a parameter
	 */
	public static final String AREA_INFO = AreaInfo.class.getSimpleName();

	/**
	 * Connection view. While the application tires to connect to a server, this view is shown. If the Connection fails, this view tells the user if he wants to retry the connection
	 */
	public static final String CONNECTION = Connection.class.getSimpleName();

	/**
	 * Connection view. Prints out all the available commands in headless configuration and shows the user manual in GUI configuration
	 */
	public static final String HELP = Help.class.getSimpleName();
}
