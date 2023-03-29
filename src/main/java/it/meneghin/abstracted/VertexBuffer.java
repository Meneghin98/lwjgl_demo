package it.meneghin.abstracted;

import org.jetbrains.annotations.NotNull;

import java.nio.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;

public class VertexBuffer implements AutoCloseable, Bindable
{

	private final int rendererID; // unsigned int

	private VertexBuffer()
	{
		this.rendererID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.rendererID);
	}




	public VertexBuffer(@NotNull final ByteBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(@NotNull final DoubleBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(@NotNull final FloatBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(@NotNull final IntBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(@NotNull final LongBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(@NotNull final ShortBuffer data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(final double @NotNull [] data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(final float @NotNull [] data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(final int @NotNull [] data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(final long @NotNull [] data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public VertexBuffer(final short @NotNull [] data)
	{
		this();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}




	@Override
	public void bind()
	{
		glBindBuffer(GL_ARRAY_BUFFER, this.rendererID);
	}

	@Override
	public void unbind()
	{
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void close()
	{
		glDeleteBuffers(rendererID);
	}
}
