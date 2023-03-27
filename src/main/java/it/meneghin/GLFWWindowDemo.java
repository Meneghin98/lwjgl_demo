package it.meneghin;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindowDemo
{

	private static final String VERTEX_SHADER = """
			#version 330 core
            
			layout(location = 0) in vec4 position;
            
			void main ()
			{
				gl_Position = position;
			}
			""";
	private static final String FRAGMENT_SHADER = """
			#version 330 core
            
			layout(location = 0) out vec4 color;
            
			void main ()
			{
				color = vec4(1.0, 0.0, 1.0, 1.0);
			}
			""";



	private static int compileShader(int type, String source)
	{
		int id = glCreateShader(type);
		glShaderSource(id, source);
		glCompileShader(id);

		IntBuffer result = BufferUtils.createIntBuffer(1);
		glGetShaderiv(id, GL_COMPILE_STATUS, result); // iv sono i tipi dei parametri, i: identifica che vogliamo un INT, v: identifica che vuole un VECTOR (pointer)
		if(result.get() == GL_FALSE)
		{
			String errorMessage = glGetShaderInfoLog(id);
			System.err.printf("Failed to compile %s shader.%n", type == GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT"); // Possono essere piú di 2 tipi quindi non é consigliato scrivere in questo modo il messaggio
			System.err.println(errorMessage);
			glDeleteShader(id);
			throw new IllegalStateException("Shader compilation failed");
		}

		return id;
	}

	private static int createShader()
	{
		int programId = glCreateProgram();
		int vShaderId = compileShader(GL_VERTEX_SHADER, GLFWWindowDemo.VERTEX_SHADER);
		int fShaderId = compileShader(GL_FRAGMENT_SHADER, GLFWWindowDemo.FRAGMENT_SHADER);

		glAttachShader(programId, vShaderId);
		glAttachShader(programId, fShaderId);

		glLinkProgram(programId);
		glValidateProgram(programId);

		// Non sono necessari da pulire, é pure consigliato lasciarli in memoria per poter eseguire debug
		// Per production code va bene anche li lasciarli in memoria in quanto non occupano molto spazio in ogni caso, va peró eseguito il Detach
		glDeleteShader(vShaderId);
		glDeleteShader(fShaderId);

		return programId;
	}

	public static void main(String[] args)
	{

		long window;
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		window = glfwCreateWindow(600, 600, "Hello World!", NULL, NULL);
		if (window == NULL)
		{
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwMakeContextCurrent(window);

		GL.createCapabilities(); // Molto importante, senza di questo l'applicazione va in crash

		//VBO
		final float[] vertices = {
				-0.5f, -0.5f,
				0.0f, 0.5f,
				0.5f, -0.5f
		};

		int vbo = glGenBuffers(); // Creare un buffer nella GPU
		glBindBuffer(GL_ARRAY_BUFFER, vbo); // Impostare il contesto sul buffer generato
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW); // Impostare i dati dentro il buffer

		//VB Attributes
		glEnableVertexAttribArray(0); // Ricordarsi di abilitare l'attributo prima di impostarne le informazioni
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 2* Float.BYTES, 0); // informazioni dell'attributo

		//Shaders
		int shaderProgram = createShader(); // Creo Vertex e Fragment Shader
		glUseProgram(shaderProgram); // Dico di utilizzare le shader create

		while (!glfwWindowShouldClose(window))
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Pulisco dalla finestra quello che c'era il frame prima

			glDrawArrays(GL_TRIANGLES, 0, 3); // Draw Call

			glfwSwapBuffers(window);

			glfwPollEvents();
		}

		glfwTerminate();
	}

}
