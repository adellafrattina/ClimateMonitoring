/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.flag.ImGuiComboFlags;
import imgui.flag.ImGuiSelectableFlags;

/**
 * To render a drop-down menu
 * 
 * @author adellfrattina
 * @version 1.0-SNAPSHOT
 */
public class Dropdown extends Widget {

	/**
	 * To create a drop-down menu
	 * @param label The drop down label
	 * @param selectables The selectables list
	 */
	public Dropdown(String label, String[] selectables) {

		m_label = new Text(label);
		m_selectables = selectables;
	}

	/**
	 * To render the drop-down menu
	 * @return The current selected item's index
	 */
	public int render() {

		m_isAnyItemSelected = false;
		m_label.setPosition(getPositionX() - getOriginX(), getPositionY() - getOriginY());
		m_label.render();
		float w = ImGui.calcTextSize(m_selectables[m_currentItem]).x;
		ImGui.pushItemWidth(w + 50);
		ImGui.setCursorPos(m_label.getWidth() + getPositionX() - getOriginX(), m_label.getPositionY() - m_label.getOriginY() - ImGui.getStyle().getFramePaddingY()); //, getPositionY() - getOriginY() - ImGui.getStyle().getFramePaddingY()
		if (ImGui.beginCombo("##" + m_label, m_selectables[m_currentItem], ImGuiComboFlags.HeightRegular)) {

			for (int i = 0; i < m_selectables.length; i++) {

				final boolean isSelected = (m_currentItem == i);

				if (ImGui.selectable(m_selectables[i], isSelected, ImGuiSelectableFlags.AllowDoubleClick)) {

					m_currentItem = i;
					m_isAnyItemSelected = true;
				}
			}

			ImGui.endCombo();
		}

		ImGui.popItemWidth();

		return m_currentItem;
	}

	/**
	 * To set the current item
	 * @param current_item The current item
	 */
	public void setCurrentItem(int current_item) {

		m_currentItem = current_item;
	}

	/**
	 * 
	 * @return The drop-down's selectable list
	 */
	public String[] getList() {

		return m_selectables;
	}

	/**
	 * 
	 * @return True if an item in the list has been pressed, false if not (even if it was selected previously)
	 */
	public boolean isAnyItemSelected() {

		return m_isAnyItemSelected;
	}

	@Override
	public float getWidth() {

		return m_label.getWidth() + super.getWidth();
	}

	@Override
	public float getHeight() {

		return m_label.getHeight();
	}

	private Text m_label;
	private final String[] m_selectables;
	private int m_currentItem = 0;
	private boolean m_isAnyItemSelected = false;
}
