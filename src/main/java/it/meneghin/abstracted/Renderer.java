package it.meneghin.abstracted;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer
{


	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void draw(@NotNull final VertexArray va, @NotNull final IndexBuffer ib, @NotNull final Shader shader)
	{
		shader.bind();
		va.bind();
		ib.bind();

		glDrawElements(GL_TRIANGLES, ib.getCount(), GL_UNSIGNED_INT, NULL);
	}

}
