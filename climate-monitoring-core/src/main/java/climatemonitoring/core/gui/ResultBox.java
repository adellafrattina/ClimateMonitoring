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
 * The result box class renders to screen a list box containing a specified String array
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class ResultBox {

	/**
	 * Initialize InputText fields
	 * @param label The result box label
	 * @param list The list of strings that will be rendered inside the list box
	 */
	public ResultBox(String label, String[] list) {

		m_label = label;
		m_list = list;
	}

	/**
	 * Initialize InputText fields
	 * @param label The result box label
	 */
	public ResultBox(String label) {

		m_label = label;
		m_list = null;
	}

	/**
	 * To render the result box
	 * @param width The box width. Inputting a -1 value will fill all the available space
	 * @param height The box height. Inputting a -1 value will fill all the available space
	 * @param x The box x position
	 * @param y The box y position
	 * @return The current selected item as an array index. -1 if no item was selected
	 */
	public int render(float width, float height, float x, float y) {

		if (m_list == null || m_list.length == 0)
			return -1;

		ImGui.setCursorPos(x, y);
		if (ImGui.beginListBox(m_label, width, height)) {

			for (int i = 0; i < m_list.length; i++) {

				final boolean isSelected = (m_currentItem == i);

				if (ImGui.selectable(m_list[i], isSelected, ImGuiSelectableFlags.AllowDoubleClick))
					m_currentItem = i;
			}

			ImGui.endListBox();
		}

		return m_currentItem;
	}

	/**
	 * To render the result box
	 * @param width The box width. Inputting a -1 value will fill all the available space
	 * @param height The box height. Inputting a -1 value will fill all the available space
	 * @return The current selected item as an array index. -1 if no item was selected
	 */
	public int render(float width, float height) {

		return render(width, height, ImGui.getCursorPosX(), ImGui.getCursorPosY());
	}

	/**
	 * To render the result box
	 * @return The current selected item as an array index. -1 if no item was selected
	 */
	public int render() {

		return render(-1, -1);
	}

	/**
	 * Sets a new string list
	 * @param list The desired list
	 */
	public void setList(String[] list) {

		m_list = list;
	}

	/**
	 * Sets the current selected item
	 * @param current_item The desired value for the current item index
	 */
	public void setCurrentItem(int current_item) {

		m_currentItem = current_item;
	}

	/**
	 * 
	 * @return The current selected item as an array index. -1 if no item was selected
	 */
	public int getCurrentItem() {

		return m_currentItem;
	}

	private String m_label;
	private String[] m_list;
	private int m_currentItem = -1;
}
