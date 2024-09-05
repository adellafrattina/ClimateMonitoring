/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import java.util.LinkedList;

import imgui.ImGui;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;

/**
 * To render a table
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Table extends Widget {

	/**
	 * To create a table with pre-defined columns
	 * @param label The table's label
	 * @param columns The table's columns
	 */
	public Table(String label, String[] columns) {

		m_label = label;
		m_columns = columns;
	}

	/**
	 * To render the table
	 */
	public void render() {

		ImGui.setCursorPos(getPositionX(), getPositionY());
		int flags = 0;
		flags |= ImGuiTableFlags.Borders;
		flags |= ImGuiTableFlags.RowBg;
		flags |= ImGuiTableFlags.SizingFixedSame;
		flags |= ImGuiTableFlags.Resizable;
		flags |= ImGuiTableFlags.ScrollY;

		boolean render = false;
		if (m_height == DEFAULT_HEIGHT)
			render = ImGui.beginTable(m_label, m_columns.length, flags);

		else
			render = ImGui.beginTable(m_label, m_columns.length, flags, -1, getHeight());

		if (render) {

			ImGui.tableSetupScrollFreeze(0, 1);

			for (int i = 0; i < m_columns.length; i++)
				ImGui.tableSetupColumn(m_columns[i], ImGuiTableColumnFlags.WidthStretch);

			ImGui.tableHeadersRow();

			for (int i = 0; i < m_rows.size(); i++) {

				ImGui.tableNextRow();
				for (int j = 0; j < m_columns.length; j++) {

					ImGui.tableSetColumnIndex(j);
					ImGui.textWrapped(m_rows.get(i)[j]);
				}
			}

			ImGui.endTable();
		}
	}

	/**
	 * 
	 * @return All the available columns
	 */
	public String[] getColumns() {

		return m_columns;
	}

	/**
	 * To add a new row
	 * @param row_data The row data
	 */
	public void addRow(String[] row_data) {

		if (row_data == null || row_data.length == 0)
			return;

		int minSize = m_columns.length > row_data.length ? row_data.length : m_columns.length;

		String[] tmp = new String[m_columns.length];
		int i = 0;
		for (; i < minSize; i++)
			tmp[i] = row_data[i];

		for (; i < tmp.length; i++)
			tmp[i] = new String("");

		m_rows.add(tmp);
	}

	private String m_label;
	private final String[] m_columns;
	private final LinkedList<String[]> m_rows = new LinkedList<String[]>();
}
