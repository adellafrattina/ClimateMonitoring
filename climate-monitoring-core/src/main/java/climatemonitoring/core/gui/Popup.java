/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.flag.ImGuiSelectableFlags;

/**
 * To render a pop-up
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Popup {

	/**
	 * To create the pop-up
	 * @param label The pop-up label
	 * @param selectables The selectable list
	 */
	public Popup(String label, String[] selectables) {

		m_label = label;
		m_selectables = selectables;
	}

	/**
	 * To render the pop-up
	 * @return The current selected item's index
	 */
	public int render() {

		if (m_label == null)
			m_label = "##";

		if (ImGui.beginPopup(m_label, 0)) {

			if (!m_label.equals("##")) {

				ImGui.text(m_label);
				ImGui.separator();
			}

			for (int i = 0; i < m_selectables.length; i++) {

				final boolean isSelected = (m_currentItem == i);

				if (ImGui.selectable(m_selectables[i], isSelected, ImGuiSelectableFlags.AllowDoubleClick))
					m_currentItem = i;
			}

			ImGui.endPopup();
		}

		return m_currentItem;
	}

	/**
	 * To open the pop-up
	 */
	public void open() {

		ImGui.openPopup(m_label);
		m_currentItem = -1;
	}

	private String m_label;
	private int m_currentItem = -1;
	private String[] m_selectables;
}
