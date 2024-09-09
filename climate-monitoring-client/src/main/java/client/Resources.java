/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package client;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import climatemonitoring.core.gui.Texture;

/**
 * The resources manager
 * 
 * @author
 * @version 1.0-SNAPSHOT
 */
public class Resources {

	/**
	 * The gear icon
	 */
	public static final String GEAR = "gear.png";

	/**
	 * The map to render as background
	 */
	public static final String MAP = "map.png";

	/**
	 * To initialize and load the resources. Must be called after a valid OpenGL context
	 */
	public static void init() {

		s_instance.m_textures.put(GEAR, loadTexture(GEAR));
		s_instance.m_textures.put(MAP, loadTexture(MAP));
	}

	/**
	 * To retrieve a specific texture
	 * @param type The texture name
	 * @return The actual OpenGL texture
	 */
	public static Texture getTexture(String type) {

		return s_instance.m_textures.get(type);
	}

	/**
	 * To load the data from the jar or from a folder
	 * @param path The texture's file path
	 * @return The loaded texture
	 * @throws RuntimeException If the file is not found
	 */
	private static Texture loadTexture(String path) {

		ByteBuffer buffer = null;
		int[] x = new int[1];
		int[] y = new int[1];

		try {

			byte[] rawBytes = s_instance.getClass().getResourceAsStream("/" + path).readAllBytes();
			ByteBuffer tmp = BufferUtils.createByteBuffer(rawBytes.length);
			tmp.put(rawBytes);
			tmp.flip();
			buffer = STBImage.stbi_load_from_memory(tmp, x, y, new int[4], STBImage.STBI_rgb_alpha);
		}

		catch (Exception e) {

			buffer = STBImage.stbi_load(RESOURCES_PATH + "/" + path, x, y, new int[4], STBImage.STBI_rgb_alpha);
		}

		if (buffer == null) {

			throw new RuntimeException("Failed to load '" + RESOURCES_PATH + "/" + path + "'");
		}

		return new Texture(buffer, x[0], y[0]);
	}

	/**
	 * To be used in an IDE
	 */
	private static final String RESOURCES_PATH = "resources";

	private static Resources s_instance = new Resources();

	private HashMap<String, Texture> m_textures = new HashMap<String, Texture>();
}
