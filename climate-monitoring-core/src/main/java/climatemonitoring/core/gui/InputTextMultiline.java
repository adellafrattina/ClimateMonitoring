/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * Same as {@link InputText}, but on multiple lines
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class InputTextMultiline extends InputText {

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 * @param max_chars The maximum number of characters that can be typed in the input text box
	 */
	public InputTextMultiline(String label, String str, String error_msg, int max_chars) {

		super(label, str, error_msg, max_chars);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 */
	public InputTextMultiline(String label, String str, String error_msg) {

		super(label, str, error_msg);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 */
	public InputTextMultiline(String label, String str) {

		super(label, str);
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 */
	public InputTextMultiline(String label) {

		super(label);
	}

	/**
	 * Renders to screen the input text box
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses ctrl + enter while the input text has focus
	 */
	public boolean render() {

		begin();
		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		final boolean value = ImGui.inputTextMultiline("##" + validateLabel(m_label.getString()), m_string, getWidth(), getHeight(), m_flags);

		return end(value);
	}
}
