/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * To render some plain text
 * 
 * @author adellfrattina
 * @version 1.0-SNAPSHOT
 * @see Widget
 */
public class Text extends Widget {

	/**
	 * To create a text
	 * @param str The text's string
	 */
	public Text(String str) {

		m_str = str;
	}

	/**
	 * To render the text
	 */
	public void render() {

		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		ImGui.textColored(color[0], color[1], color[2], color[3], m_str);
	}

	/**
	 * To set the text's string
	 * @param str The text's string
	 */
	public void setString(String str) {

		m_str = str;
	}

	/**
	 * 
	 * @return The text's string
	 */
	public String getString() {

		return m_str;
	}

	/**
	 * To set the text's color
	 * @param r The red value (between 0 and 255)
	 * @param g The green value (between 0 and 255)
	 * @param b The blue value (between 0 and 255)
	 * @param a The alpha value (between 0 and 255)
	 */
	public void setColor(int r, int g, int b, int a) {

		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}

	@Override
	public float getWidth() {

		return ImGui.calcTextSize(m_str).x;
	}

	@Override
	public float getHeight() {

		return ImGui.calcTextSize(m_str).y;
	}

	private String m_str;
	private final int[] color = { 255, 255, 255, 255 };
}
