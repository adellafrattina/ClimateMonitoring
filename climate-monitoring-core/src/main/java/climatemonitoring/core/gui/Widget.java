/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import imgui.ImGui;

/**
 * To group all the common properties of a widget
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Widget {

	/**
	 * To set the widget with the default x position
	 */
	public static final float DEFAULT_X = -1.0f;

	/**
	 * To set the widget with the default y position
	 */
	public static final float DEFAULT_Y = -1.0f;

	/**
	 * To set the widget with the default width
	 */
	public static final float DEFAULT_WIDTH = -1.0f;

	/**
	 * To set the widget with the default height
	 */
	public static final float DEFAULT_HEIGHT = -1.0f;

	/**
	 * To set the widget on the same line as the previous one
	 */
	public static final float SAME_LINE_Y = -3.0f;

	/**
	 * To set the widget's origin
	 * @param x The widget's x origin
	 * @param y The widget's y origin
	 */
	public void setOrigin(float x, float y) {

		m_originX = x;
		m_originY = y;
	}

	/**
	 * To set the widget's x origin
	 * @param x The widget's x origin
	 */
	public void setOriginX(float x) {

		m_originX = x;
	}

	/**
	 * To set the widget's y origin
	 * @param y The widget's y origin
	 */
	public void setOriginY(float y) {

		m_originY = y;
	}

	/**
	 * To set the widget's position
	 * @param x The widget's x position
	 * @param y The widget's y position
	 */
	public void setPosition(float x, float y) {

		m_x = x;
		m_y = y;
	}

	/**
	 * To set the widget's x position
	 * @param x The widget's x position
	 */
	public void setPositionX(float x) {

		m_x = x;
	}

	/**
	 * To set the widget's y position
	 * @param y The widget's y position
	 */
	public void setPositionY(float y) {

		m_y = y;
	}

	/**
	 * To set the widget's size
	 * @param width The widget's width
	 * @param height The widget's height
	 */
	public void setSize(float width, float height) {

		m_width = width;
		m_height = height;
	}

	/**
	 * To set the widget's width
	 * @param width The widget's width
	 */
	public void setWidth(float width) {

		m_width = width;
	}

	/**
	 * To set the widget's height
	 * @param height The widget's height
	 */
	public void setHeight(float height) {

		m_height = height;
	}

	/**
	 * 
	 * @return The widget's x origin
	 */
	public float getOriginX() {

		return m_originX;
	}

	/**
	 * 
	 * @return The widget's y origin
	 */
	public float getOriginY() {

		return m_originY;
	}

	/**
	 * 
	 * @return The widget's x position
	 */
	public float getPositionX() {

		if (m_y == SAME_LINE_Y) {

			ImGui.sameLine();
		}

		return (m_x == DEFAULT_X ? ImGui.getCursorPosX() : m_x);
	}

	/**
	 * 
	 * @return The widget's y position
	 */
	public float getPositionY() {

		if (m_y == SAME_LINE_Y) {

			ImGui.sameLine();
			m_y = DEFAULT_Y;
		}

		return (m_y == DEFAULT_Y ? ImGui.getCursorPosY() : m_y);
	}

	/**
	 * 
	 * @return The widget's width
	 */
	public float getWidth() {

		float width = m_width;
		if (m_width == DEFAULT_WIDTH) {

			ImGui.pushItemWidth(m_width);
			width = ImGui.calcItemWidth();
			ImGui.popItemWidth();
		}

		return width;
	}

	/**
	 * 
	 * @return The widget's height
	 */
	public float getHeight() {

		return m_height;
	}

	protected String validateLabel(String label) {

		return label != null ? label : "";
	}

	private float m_originX = 0.0f;
	private float m_originY = 0.0f;
	protected float m_x = DEFAULT_X;
	protected float m_y = DEFAULT_Y;
	protected float m_width = DEFAULT_WIDTH;
	protected float m_height = DEFAULT_HEIGHT;
}
