/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.callback.ImListClipperCallback;
import imgui.flag.ImGuiSelectableFlags;

/**
 * The result box class renders to screen a list box containing a specified String array
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class ResultBox extends Widget {

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
	 * @return The current selected item as an array index. -1 if no item was selected
	 */
	public int render() {

		m_isAnyItemSelected = false;
		if (m_list == null || m_list.length == 0)
			return -1;

		ImGui.setCursorPos(getPositionX() - getOriginX(), getPositionY() - getOriginY());

		if (ImGui.beginListBox("##" + m_label, getWidth(), getHeight())) {

			ImGuiListClipper.forEach(m_list.length, new ImListClipperCallback() {

				@Override
				public void accept(int index) {

					final boolean isSelected = (m_currentItem == index);

					if (ImGui.selectable(m_list[index], isSelected, ImGuiSelectableFlags.AllowDoubleClick)) {

						m_currentItem = index;
						m_isAnyItemSelected = true;
					}
				}
			});

			ImGui.endListBox();
		}

		return m_currentItem;
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

	/**
	 * 
	 * @return True if an item in the list has been pressed, false if not (even if it was selected previously)
	 */
	public boolean isAnyItemSelected() {

		return m_isAnyItemSelected;
	}

	private String m_label;
	private String[] m_list;
	private int m_currentItem = -1;
	private boolean m_isAnyItemSelected = false;
}
