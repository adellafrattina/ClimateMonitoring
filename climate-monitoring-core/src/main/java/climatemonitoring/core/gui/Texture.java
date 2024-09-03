/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import org.lwjgl.opengl.GL12;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

/**
 * Used to create an OpenGL texture ID
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Texture {

	/**
	 * To create a texture from memory. Must be called after a valid OpenGL context
	 * @param buffer The image's data
	 * @param width The image's width
	 * @param height The image's height
	 */
	public Texture(ByteBuffer buffer, int width, int height) {

		m_width = width;
		m_height = height;

		m_ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, m_ID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}

	/**
	 * 
	 * @return The texture ID (if it is 0, then it is invalid)
	 */
	public int getID() {

		return m_ID;
	}

	/**
	 * 
	 * @return The texture's width
	 */
	public int getWidth() {

		return m_width;
	}

	/**
	 * 
	 * @return The texture's height
	 */
	public int getHeight() {

		return m_height;
	}

	private int m_ID;
	private int m_width;
	private int m_height;
}
