/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

/**
 * To render a modal
 */
public class Modal {

	/**
	 * Constant value to indicate that the user presed the cancel button (returned from the {@link Modal#render()} method)
	 */
	public static final int CANCEL = 1;

	/**
	 * Constant value to indicate that the user presed the ok button (returned from the {@link Modal#render()} method)
	 */
	public static final int OK = 2;

	/**
	 * To create the modal
	 * @param label The modal's label
	 * @param msg The modal's message
	 */
	public Modal(String label, String msg) {

		m_label = label;
		m_msg = msg;
	}

	/**
	 * To render the modal
	 * @return 3 possible values:
	 * <p> 0 if the user does not press any button </p>
	 * <p> {@link Modal#CANCEL} if the user pressed the cancel button </p>
	 * <p> {@link Modal#OK} if the user pressed the ok button </p>
	 */
	public int render() {

		int result = 0;
		if (ImGui.beginPopupModal(m_label, ImGuiWindowFlags.AlwaysAutoResize)) {

			ImGui.text(m_msg);
			ImGui.separator();
			m_cancel.setWidth(ImGui.calcItemWidth() / 2.0f);
			if (m_cancel.render())
				result = CANCEL;
			m_ok.setPositionY(Widget.SAME_LINE_Y);
			m_ok.setWidth(ImGui.calcItemWidth() / 2.0f);
			if (m_ok.render())
				result = OK;

			if (result != 0)
				ImGui.closeCurrentPopup();

			ImGui.endPopup();
		}

		return result;
	}

	/**
	 * To open the modal
	 */
	public void open() {

		ImGui.openPopup(m_label);
	}

	private String m_label;
	private String m_msg;
	private Button m_cancel = new Button("Cancel");
	private Button m_ok = new Button("OK");
}
