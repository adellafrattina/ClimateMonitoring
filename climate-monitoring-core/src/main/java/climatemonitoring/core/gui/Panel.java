/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * To group widgets together in another child window
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Panel extends Widget {

	/**
	 * Start the panel rendering
	 * @param title The panel title
	 * @param imGuiWindowFlags The window options
	 * @see imgui.flag.ImGuiWindowFlags
	 */
	public void begin(String title, int imGuiWindowFlags) {

		if (title != null) {

			m_title = new Text(validateLabel(title));
			m_title.setOriginX(m_title.getWidth() / 2.0f);
			m_title.setPositionX(getPositionX());
			m_title.render();
		}

		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		ImGui.beginChild("##" + title, getWidth(), getHeight(), true, imGuiWindowFlags);
	}

	/**
	 * Start the panel rendering
	 * @param title The panel title
	 */
	public void begin(String title) {

		begin(title, 0);
	}

	/**
	 * End the panel rendering
	 */
	public void end() {

		ImGui.endChild();
	}

	private Text m_title;
}
