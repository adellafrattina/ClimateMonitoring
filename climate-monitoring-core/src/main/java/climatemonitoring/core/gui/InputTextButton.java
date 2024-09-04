/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * Same as {@link InputText}, but with a button on the right (useful to implement a show/hide password)
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class InputTextButton extends InputText {

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 * @param max_chars The maximum number of characters that can be typed in the input text box
	 * @param button_label The button label
	 */
	public InputTextButton(String label, String str, String error_msg, int max_chars, String button_label) {

		super(label, str, error_msg, max_chars);
		m_button = new Button(button_label);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 * @param max_chars The maximum number of characters that can be typed in the input text box
	 */
	public InputTextButton(String label, String str, String error_msg, int max_chars) {

		super(label, str, error_msg, max_chars);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 */
	public InputTextButton(String label, String str, String error_msg) {

		super(label, str, error_msg);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 */
	public InputTextButton(String label, String str) {

		super(label, str);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 */
	public InputTextButton(String label) {

		super(label);
	}

	/**
	 * Renders to screen the input text box
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses enter while the input text has focus
	 */
	public boolean render() {

		begin();
		float width = m_width == DEFAULT_HEIGHT ? Math.abs(getWidth() - m_button.getWidth()) : getWidth();
		ImGui.pushItemWidth(width);
		float y = getPositionY() - getOriginY();
		ImGui.setCursorPos(getPositionX() - getOriginX(), y);
		boolean value = ImGui.inputText("##" + validateLabel(m_label.getString()), m_string, m_flags);
		ImGui.popItemWidth();
		value = end(value);
		m_button.setHeight(ImGui.getFrameHeight());
		m_button.setPosition(getPositionX() - getOriginX() + width + 2.0f, y);
		isButtonPressed = m_button.render();

		return value;
	}

	/**
	 * 
	 * @return True if the button is pressed, false if not
	 */
	public boolean isButtonPressed() {

		return isButtonPressed;
	}

	/**
	 * To set the button's label
	 * @param label The button's label
	 */
	public void setButtonLabel(String label) {

		m_button = new Button(label);
	}

	/**
	 * To set the button's texture
	 * @param texture The button's texture
	 */
	public void setButtonTexture(int texture) {

		m_button.setTexture(texture);
	}

	private Button m_button = new Button("##");
	private boolean isButtonPressed = false;
}
