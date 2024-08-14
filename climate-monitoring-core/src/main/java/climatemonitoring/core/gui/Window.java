/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core.gui;

import java.nio.IntBuffer;
import java.util.Objects;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * The class that creates the window through the use of the GLFW library and OpenGL
 * 
 * @author adellafrattina
 * @version 1.0-SNAPSHOT
 */
public class Window {

	/**
	 * The constructor initializes GLFW, creates the window and spawns it in the center on the monitor, sets some window settings and creates the OpenGL context
	 * @param spec The window specification
	 * @see WindowSpecification
	 */
	public Window(WindowSpecification spec) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {

			throw new IllegalStateException("Unable to initialize GLFW");
		}

		decideGlGlslVersions();

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		m_handle = GLFW.glfwCreateWindow(spec.width, spec.height, spec.title, MemoryUtil.NULL, MemoryUtil.NULL);

		if (m_handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		try (MemoryStack stack = MemoryStack.stackPush()) {

			final IntBuffer pWidth = stack.mallocInt(1); // int*
			final IntBuffer pHeight = stack.mallocInt(1); // int*

			GLFW.glfwGetWindowSize(m_handle, pWidth, pHeight);
			final GLFWVidMode vidmode = Objects.requireNonNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()));
			GLFW.glfwSetWindowPos(m_handle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		GLFW.glfwMakeContextCurrent(m_handle);
		GL.createCapabilities();

		if (spec.vSync)
			GLFW.glfwSwapInterval(1);

		else
			GLFW.glfwSwapInterval(0);

		if (spec.isFullscreen)
			GLFW.glfwMaximizeWindow(m_handle);

		else
			GLFW.glfwShowWindow(m_handle);
	}

	/**
	 * Destroys the window and terminates GLFW
	 */
	public void shutdown() {

		Callbacks.glfwFreeCallbacks(m_handle);
		GLFW.glfwDestroyWindow(m_handle);
		GLFW.glfwTerminate();
		Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
	}

	/**
	 * Checks whether the window was closed
	 * @return True if the user closed the window, false if not
	 */
	public boolean isClosed() {

		return GLFW.glfwWindowShouldClose(m_handle);
	}

	/**
	 * Swaps the window swap chain. Must be called at the end of an applicaton  main loop
	 */
	public void swap() {

		GLFW.glfwSwapBuffers(m_handle);
		GLFW.glfwPollEvents();
	}

	/**
	 * 
	 * @return The window handle
	 */
	public long getHandle() {

		return m_handle;
	}

	/**
	 * 
	 * @return The window width
	 */
	public int getWidth() {

		int[] width = new int[1], height = new int[1];
		GLFW.glfwGetWindowSize(m_handle, width, height);

		return width[0];
	}

	/**
	 * 
	 * @return The window height
	 */
	public int getHeight() {

		int[] width = new int[1], height = new int[1];
		GLFW.glfwGetWindowSize(m_handle, width, height);

		return height[0];
	}

	/**
	 * Utility function. Used to set the OpenGL version the window creation
	 */
	private void decideGlGlslVersions() {

		final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

		if (isMac) {

			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);		  // Required on Mac
		}

		else {

			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
		}
	}

	private long m_handle;
}
