/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * To render a tooltip
 * 
 * @author adellafrattina
 * @verison 1.0-SNAPSHOT
 */
public class Tooltip {

	/**
	 * To create a tooltip
	 * @param label The tooltip's label
	 * @param desc The tooltip's description
	 */
	public Tooltip(String label, String desc) {

		m_label = label;
		m_desc = desc;
	}

	/**
	 * To render a tooltip
	 */
	public void render() {

		ImGui.textDisabled(m_label);
		if (ImGui.isItemHovered()) {

			ImGui.beginTooltip();
			ImGui.pushTextWrapPos(ImGui.getFontSize() * 35.0f);
			ImGui.textUnformatted(m_desc);
			ImGui.popTextWrapPos();
			ImGui.endTooltip();
		}
	}

	private String m_label;
	private String m_desc;
}
