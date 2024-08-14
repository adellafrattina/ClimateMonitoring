/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.type.ImString;
import imgui.flag.ImGuiInputTextFlags;

/**
 * The InputText class renders to screen a box to input text and holds the written string.
 * It is also possible to set an error message that will appear under the input box.
 * The error message can be turned on or off however the user likes
 * It can also enforce some conditions, such as disabling spaces or making the input box read only
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class InputText {

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 */
	public InputText(String label, String str, String error_msg) {

		m_label = label;
		m_string = new ImString(str, ImString.DEFAULT_LENGTH);
		m_errorMsg = error_msg;
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 */
	public InputText(String label, String str) {

		m_label = label;
		m_string = new ImString(str, ImString.DEFAULT_LENGTH);
		m_errorMsg = null;
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 */
	public InputText(String label) {

		m_label = label;
		m_string = new ImString(ImString.DEFAULT_LENGTH);
		m_errorMsg = null;
	}

	/**
	 * Renders to screen the input text box
	 * @param width The box width
	 * @param x The box x position
	 * @param y The box y position
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses enter while the input text has focus
	 */
	public boolean render(float width, float x, float y) {

		ImGui.beginDisabled(((flags & ImGuiInputTextFlags.ReadOnly) == ImGuiInputTextFlags.ReadOnly));

		ImGui.pushItemWidth(width);
		ImGui.setCursorPos(x, y);
		ImGui.text(m_label);
		ImGui.sameLine();
		final boolean value = ImGui.inputText("##" + m_label, m_string, flags);
		ImGui.popItemWidth();

		if (m_showErrorMsg) {

			ImGui.setCursorPosX(x);
			ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, m_errorMsg);
		}

		ImGui.endDisabled();

		return value;
	}

	/**
	 * Renders to screen the input text box
	 * @param width The box width
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses enter while the input text has focus
	 */
	public boolean render(float width) {

		return render(width, ImGui.getCursorPosX(), ImGui.getCursorPosY());
	}

	/**
	 * Renders to screen the input text box
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses enter while the input text has focus
	 */
	public boolean render() {

		return render(-1, ImGui.getCursorPosX(), ImGui.getCursorPosY());
	}

	/**
	 * No spaces allowed in the text box
	 * @param enable True to enable, false to disable
	 */
	public void setNoSpaces(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.CharsNoBlank;

		else
			flags &= ~ImGuiInputTextFlags.CharsNoBlank;
	}

	/**
	 * Converts every character into upper case
	 * @param enable True to enable, false to disable
	 */
	public void setAlwaysUpperCase(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.CharsUppercase;

		else
			flags &= ~ImGuiInputTextFlags.CharsUppercase;
	}

	/**
	 * No alphabetic/punctuation characters allowed in the text box
	 * @param enable True to enable, false to disable
	 */
	public void setNumbersOnly(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.CharsDecimal;

		else
			flags &= ~ImGuiInputTextFlags.CharsDecimal;
	}

	/**
	 * The input text box can only be read
	 * @param enable True to enable, false to disable
	 */
	public void setReadOnly(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.ReadOnly;

		else
			flags &= ~ImGuiInputTextFlags.ReadOnly;
	}

	/**
	 * When not set, the default behavior for the {@link #render}, {@link #render(float)} and {@link #render(float, float, float)} methods is to return true when the user types. When set, it returns true only if the user presses enter when the input text box has focus
	 * @param enable True to enable, false to disable
	 */
	public void setEnterReturnsTrue(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.EnterReturnsTrue;

		else
			flags &= ~ImGuiInputTextFlags.EnterReturnsTrue;
	}

	/**
	 * Converts every character into a "*" symbol. Useful for password input
	 * @param enable True to enable, false to disable
	 */
	public void setPassword(boolean enable) {

		if (enable)
			flags |= ImGuiInputTextFlags.Password;

		else
			flags &= ~ImGuiInputTextFlags.Password;
	}

	/**
	 * Sets if the error message should be seen
	 * @param enable True to enable, false to disable
	 */
	public void showErrorMsg(boolean enable) {

		m_showErrorMsg = enable;
	}

	/**
	 * Sets the error message
	 * @param msg The desired error message
	 */
	public void setErrorMsg(String msg) {

		m_errorMsg = msg;
	}

	/**
	 * If the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, the string will not be updated until the user presses enter in the input text box
	 * @return The string inside the input text box
	 */
	public String getString() {

		return m_string.toString();
	}

	private ImString m_string;
	private String m_label;
	private int flags = 0;
	private boolean m_showErrorMsg = false;
	private String m_errorMsg;
}
