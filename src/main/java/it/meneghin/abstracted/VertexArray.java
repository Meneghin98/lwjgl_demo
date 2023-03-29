package it.meneghin.abstracted;

import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.*;

public class VertexArray implements AutoCloseable
{

	private final int rendererID; // unsigned int

	public VertexArray()
	{
		this.rendererID = glGenVertexArrays();
		glBindBuffer(GL_ARRAY_BUFFER, this.rendererID);
	}

	public void addBuffer(final VertexBuffer vertexBuffer, final VertexBufferLayout layout)
	{
		this.bind();
		vertexBuffer.bind();
		List<VertexBufferLayout.LayoutElement> elements = layout.getElements();

		int offset = 0;
		for (int i = 0; i < elements.size(); i++)
		{
			VertexBufferLayout.LayoutElement element = elements.get(i);
			glEnableVertexAttribArray(i);
			glVertexAttribPointer(i, element.count(), element.type(), element.normalized(), layout.getStride(), offset);
			offset += element.count() * VertexBufferLayout.getSizeOfType(element.type());
		}


	}


	public void bind()
	{
		glBindVertexArray(this.rendererID);
	}

	public void unbind()
	{
		glBindVertexArray(0);
	}


	@Override
	public void close()
	{
		glDeleteVertexArrays(rendererID);
	}
}
