package it.meneghin.abstracted;

import org.jetbrains.annotations.NotNull;

import java.nio.*;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer implements AutoCloseable, Bindable
{

	private final int rendererID; // unsigned int
	private final int count; // unsigned int

	private IndexBuffer(final int count)
	{
		this.count = count;
		this.rendererID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.rendererID);
	}




	public IndexBuffer(@NotNull final ByteBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(@NotNull final DoubleBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(@NotNull final FloatBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(@NotNull final IntBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(@NotNull final LongBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(@NotNull final ShortBuffer data)
	{
		this(data.capacity());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(final double @NotNull [] data)
	{
		this(data.length);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(final float @NotNull [] data)
	{
		this(data.length);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(final int @NotNull [] data)
	{
		this(data.length);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(final long @NotNull [] data)
	{
		this(data.length);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public IndexBuffer(final short @NotNull [] data)
	{
		this(data.length);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}



	@Override
	public void bind()
	{
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.rendererID);
	}

	@Override
	public void unbind()
	{
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public int getCount()
	{
		return count;
	}

	@Override
	public void close()
	{
		glDeleteBuffers(rendererID);
	}
}
