package it.meneghin;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindowDemo
{


	public static void main(String[] args)
	{

		long window;
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if (window == NULL)
		{
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwMakeContextCurrent(window);

		GL.createCapabilities(); // Molto importante, senza di questo l'applicazione va in crash

		while (!glfwWindowShouldClose(window))
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glfwSwapBuffers(window);

			glfwPollEvents();
		}

		glfwTerminate();
	}

}
