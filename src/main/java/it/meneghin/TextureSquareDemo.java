package it.meneghin;

import it.meneghin.abstracted.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL46.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL46.glVertexAttribPointer;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TextureSquareDemo
{

	public static void main(String[] args) throws Exception
	{

		long window;
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}


		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); // OpenGL 3.3
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // CORE (mi serve per usare vertex array)


		window = glfwCreateWindow(600, 600, "Hello World!" , NULL, NULL);
		if (window == NULL)
		{
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwMakeContextCurrent(window);

		GL.createCapabilities(); // Molto importante, senza di questo l'applicazione va in crash
		System.out.println("OpenGL version: " + GL11.glGetString(GL_VERSION));

		glfwSwapInterval(1); // VSync



		// Vertex Buffer Object
		final float[] vertices = {
				-0.5f, -0.5f, 0.0f, 0.0f, // 0
				0.5f, -0.5f, 1.0f, 0.0f, // 1
				0.5f, 0.5f, 1.0f, 1.0f, // 2
				-0.5f, 0.5f, 0.0f, 1.0f, // 3
		};

		// VBO related indices -> Index Buffer
		final int[] indices = {
				0,1,2, // 1st Triangle
				2,3,0 // 2nd Triangle
		};

		VertexArray va = new VertexArray();
		VertexBuffer vb = new VertexBuffer(vertices);

		//VB Attributes
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);

		VertexBufferLayout layout = new VertexBufferLayout();
		layout.pushFloat(2);
		layout.pushFloat(2);
		va.addBuffer(vb, layout);

		IndexBuffer ib = new IndexBuffer(indices);


		//Shaders
		Shader shader = new Shader("shader/BasicWithTexture.vert", "shader/BasicWithTexture.frag");
		shader.setUniform4f("u_Color", 0.6f, 0.2f, 0.8f, 1.0f);

		Texture texture = new Texture("texture/propngtools-random-img.png");
		shader.setUniform1i("u_Texture", 0);


		// unbind
		va.unbind();
		shader.unbind();
		vb.unbind();
		ib.unbind();

		Renderer renderer = new Renderer();

		float redChannel = 0.0f;
		float increment = 0.05f;
		while (!glfwWindowShouldClose(window))
		{
			renderer.clear();

			shader.bind();
			shader.setUniform4f("u_Color", redChannel, 0.2f, 0.8f, 1.0f);

			renderer.draw(va, ib, shader);

			if(redChannel > 1.0f)
			{
				increment = -0.05f;
			} else if (redChannel < 0.0f)
			{
				increment = 0.05f;
			}

			// TODO: clamp redChannel per non superare 0 e 1, in questo caso essendo un test non me ne preoccupo
			redChannel += increment;

			glfwSwapBuffers(window);

			glfwPollEvents();
		}

		vb.close();
		ib.close();

		shader.close();
		glfwTerminate();
	}

}
