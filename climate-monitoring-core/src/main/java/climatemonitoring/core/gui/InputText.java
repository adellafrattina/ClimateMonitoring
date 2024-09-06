/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImString;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;

/**
 * The InputText class renders to screen a box to input text and holds the written string.
 * It is also possible to set an error message that will appear under the input box.
 * The error message can be turned on or off however the user likes
 * It can also enforce some conditions, such as disabling spaces or making the input box read only
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class InputText extends Widget {

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 * @param max_chars The maximum number of characters that can be typed in the input text box
	 */
	public InputText(String label, String str, String error_msg, int max_chars) {

		m_label = new Text(label);
		m_string = new ImString(str, max_chars);
		m_maxChars = max_chars;
		m_errorMsg = error_msg;
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 * @param error_msg The error message that will be shown when the {@link #showErrorMsg(boolean)} method will enable the error message rendering
	 */
	public InputText(String label, String str, String error_msg) {

		m_label = new Text(label);
		m_string = new ImString(str, 1024);
		m_errorMsg = error_msg;
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 * @param str The string that will be rendered inside the box for the first time
	 */
	public InputText(String label, String str) {

		m_label = new Text(label);
		m_string = new ImString(str, 1024);
		m_errorMsg = null;
	}

	/**
	 * Initialize InputText fields
	 * @param label The input text box label
	 */
	public InputText(String label) {

		m_label = new Text(label);
		m_string = new ImString(1024);
		m_errorMsg = null;
	}

	/**
	 * Renders to screen the input text box
	 * @return True when the user is typing by default, but if the {@link #setEnterReturnsTrue(boolean)} method set the flag to true, then the function will return true only if the user presses enter while the input text has focus
	 */
	public boolean render() {

		begin();
		ImGui.pushItemWidth(getWidth());
		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		final boolean value = ImGui.inputText("##" + validateLabel(m_label.getString()), m_string, m_flags);
		ImGui.popItemWidth();

		return end(value);
	}

	/**
	 * No spaces allowed in the text box
	 * @param enable True to enable, false to disable
	 */
	public void setNoSpaces(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.CharsNoBlank;

		else
			m_flags &= ~ImGuiInputTextFlags.CharsNoBlank;
	}

	/**
	 * Converts every character into upper case
	 * @param enable True to enable, false to disable
	 */
	public void setAlwaysUpperCase(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.CharsUppercase;

		else
			m_flags &= ~ImGuiInputTextFlags.CharsUppercase;
	}

	/**
	 * No alphabetic/punctuation characters allowed in the text box
	 * @param enable True to enable, false to disable
	 */
	public void setNumbersOnly(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.CharsDecimal;

		else
			m_flags &= ~ImGuiInputTextFlags.CharsDecimal;
	}

	/**
	 * The input text box can only be read
	 * @param enable True to enable, false to disable
	 */
	public void setReadOnly(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.ReadOnly;

		else
			m_flags &= ~ImGuiInputTextFlags.ReadOnly;
	}

	/**
	 * When not set, the default behavior for the {@link #render} method is to return true when the user types. When set, it returns true only if the user presses enter when the input text box has focus
	 * @param enable True to enable, false to disable
	 */
	public void setEnterReturnsTrue(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.EnterReturnsTrue;

		else
			m_flags &= ~ImGuiInputTextFlags.EnterReturnsTrue;
	}

	/**
	 * Converts every character into a "*" symbol. Useful for password input
	 * @param enable True to enable, false to disable
	 */
	public void setPassword(boolean enable) {

		if (enable)
			m_flags |= ImGuiInputTextFlags.Password;

		else
			m_flags &= ~ImGuiInputTextFlags.Password;
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
	 * To reset the string to be rendered inside the input text box
	 * @param str The string inside the input text box
	 */
	public void setString(String str) {

		m_string = new ImString(str, m_maxChars);
	}

	/**
	 * 
	 * @return The string inside the input text box
	 */
	public String getString() {

		return m_string.toString();
	}

	/**
	 * 
	 * @return The maximum number of characters
	 */
	public int getMaxCharacters() {

		return m_maxChars;
	}

	protected void begin() {

		ImGui.beginDisabled(((m_flags & ImGuiInputTextFlags.ReadOnly) == ImGuiInputTextFlags.ReadOnly));

		if (m_label.getString() != null && !m_label.getString().isEmpty()) {

			m_label.setOriginX(m_label.getWidth() / 2.0f);
			m_label.setPosition(getPositionX() - getOriginX() + getWidth() / 2.0f, m_y == DEFAULT_Y ? getPositionY() - getOriginY() : getPositionY() - getOriginY() - m_label.getHeight() - ImGui.getStyle().getFramePaddingY());
			m_label.render();
		}

		m_enterReturnsTrue = ((m_flags & ImGuiInputTextFlags.EnterReturnsTrue) == ImGuiInputTextFlags.EnterReturnsTrue);
		m_flags &= ~ImGuiInputTextFlags.EnterReturnsTrue;
	}

	protected boolean end(boolean value) {

		if (!ImGui.isItemFocused())
			m_active = false;

		if (ImGui.isItemActive())
			m_active = true;

		if (m_enterReturnsTrue) {

			boolean b = m_active && !ImGui.isItemActive() && ImGui.isKeyPressed(ImGuiKey.Enter);
			if (b)
				m_active = false;

			value = b;
		}

		else {

			m_active = false;
		}

		if (m_showErrorMsg) {

			ImVec2 size = ImGui.calcTextSize(m_errorMsg);
			ImGui.setCursorPosX(getPositionX() - getOriginX() + getWidth() / 2.0f - size.x / 2.0f);
			ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, m_errorMsg);
		}

		ImGui.endDisabled();

		return value;
	}

	protected ImString m_string;
	protected Text m_label;
	protected int m_flags = 0;
	protected boolean m_showErrorMsg = false;
	protected String m_errorMsg;
	private boolean m_active = false;
	private boolean m_enterReturnsTrue = false;
	private int m_maxChars = ImString.DEFAULT_LENGTH;
}
