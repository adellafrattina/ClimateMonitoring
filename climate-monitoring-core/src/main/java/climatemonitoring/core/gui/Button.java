/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

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

		return m_texture != 0 ? ImGui.imageButton(m_texture, getWidth(), getHeight()) : ImGui.button(m_label);
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
