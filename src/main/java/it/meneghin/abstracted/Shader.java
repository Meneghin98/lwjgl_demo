package it.meneghin.abstracted;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class Shader implements AutoCloseable
{
	private final Map<String, Integer> uniformLocationCache = new HashMap<>();
	private final String vertexShaderPath_debug;
	private final int rendererID; // unsigned int
	private final String fragmentShaderPath_debug;

	public Shader(@NotNull final String vertexShaderPath, @NotNull final String fragmentShaderPath)
	{
		this.vertexShaderPath_debug = vertexShaderPath;
		this.fragmentShaderPath_debug = fragmentShaderPath;

		String vertexShader = loadShader(vertexShaderPath);
		String fragmentShader = loadShader(fragmentShaderPath);
		this.rendererID = createShader(vertexShader, fragmentShader);
		this.bind();
	}


	public void bind()
	{
		glUseProgram(this.rendererID);
	}

	public void unbind()
	{
		glUseProgram(0);
	}

	public void setUniform1i(final String name, final int i0)
	{
		int location = getUniformLocation(name);
		glUniform1i(location, i0);
	}

	public void setUniform4f(final String name, final float f0, final float f1, final float f2, final float f3)
	{
		int location = getUniformLocation(name);
		glUniform4f(location, f0, f1, f2, f3);
	}



	private int getUniformLocation(final String name)
	{
		if (uniformLocationCache.containsKey(name))
		{
			return uniformLocationCache.get(name);
		}

		int location = glGetUniformLocation(rendererID, name);

		if (location == -1)
		{
			System.out.printf("WARN: uniform %s not found", name);
		}

		uniformLocationCache.put(name, location);
		return location;
	}

	@Override
	public void close()
	{
		glDeleteProgram(rendererID);
	}

	private static int createShader(String vertexShader, String fragmentShader)
	{
		int programId = glCreateProgram();
		int vShaderId = compileShader(GL_VERTEX_SHADER, vertexShader);
		int fShaderId = compileShader(GL_FRAGMENT_SHADER, fragmentShader);

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

	private static int compileShader(int type, String source)
	{
		int id = glCreateShader(type);
		glShaderSource(id, source);
		glCompileShader(id);

		IntBuffer result = BufferUtils.createIntBuffer(1);
		glGetShaderiv(id, GL_COMPILE_STATUS, result); // iv sono i tipi dei parametri, i: identifica che vogliamo un INT, v: identifica che vuole un VECTOR (pointer)
		if (result.get() == GL_FALSE)
		{
			String errorMessage = glGetShaderInfoLog(id);
			System.err.printf("Failed to compile %s shader.%n", type == GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT"); // Possono essere piú di 2 tipi quindi non é consigliato scrivere in questo modo il messaggio
			System.err.println(errorMessage);
			glDeleteShader(id);
			throw new IllegalStateException("Shader compilation failed");
		}

		return id;
	}

	private static String loadShader(String fileName)
	{
		try (InputStream in = ClassLoader.getSystemResourceAsStream(fileName))
		{
			assert in != null;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
			{
				return reader.lines().collect(Collectors.joining("\n"));
			}

		} catch (IOException e)
		{
			throw new IllegalStateException("Failed to read shader source file", e); //
		}
	}
}
