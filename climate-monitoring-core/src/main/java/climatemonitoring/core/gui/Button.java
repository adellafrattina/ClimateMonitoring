/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;

/**
 * To render a button
 * 
 * @author adellfrattina
 * @version 1.0-SNAPSHOT
 */
public class Button extends Widget {

	/**
	 * Create the button
	 * @param label The button label
	 */
	public Button(String label) {

		m_label = label;
	}

	/**
	 * To render the button
	 * @return True if the button gets pressed, false if not
	 */
	public boolean render() {

		if (m_width == DEFAULT_WIDTH) {

			ImVec2 size = ImGui.calcTextSize(m_label);
			setWidth(size.x + ImGui.getStyle().getFramePaddingX());
		}

		if (m_height == DEFAULT_HEIGHT) {

			ImVec2 size = ImGui.calcTextSize(m_label);
			setHeight(size.y + ImGui.getStyle().getFramePaddingY());
		}

		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 0);
		final boolean value = m_texture != 0 ? ImGui.imageButton(m_texture, getWidth(), getHeight()) : ImGui.button(m_label, getWidth(), getHeight());
		ImGui.popStyleVar();
		return value;
	}

	/**
	 * To set a texture renderer ID
	 * @param texture
	 */
	void setTexture(int texture) {

		m_texture = texture;
	}

	private String m_label;
	private int m_texture = 0;
}
