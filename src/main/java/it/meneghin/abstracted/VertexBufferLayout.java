package it.meneghin.abstracted;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class VertexBufferLayout
{
	public record LayoutElement(int type, int count, boolean normalized) {}

	public static int getSizeOfType(int type)
	{
		Integer size = SIZE_OF_TYPE.get(type);
		Objects.requireNonNull(size);
		return size;
	}

	private final List<LayoutElement> elements = new ArrayList<>();
	private int stride;

	public VertexBufferLayout()
	{
		this.stride = 0;
	}

	public void pushFloat(int count)
	{
		elements.add(new LayoutElement(GL_FLOAT, count, false));
		stride += getSizeOfType(GL_FLOAT);
	}
	public void pushInt(int count)
	{
		elements.add(new LayoutElement(GL_UNSIGNED_INT, count, false));
		stride += getSizeOfType(GL_UNSIGNED_INT);
	}
	public void pushByte(int count)
	{
		elements.add(new LayoutElement(GL_UNSIGNED_BYTE, count, true));
		stride += getSizeOfType(GL_UNSIGNED_BYTE);
	}

	public int getStride()
	{
		return this.stride;
	}

	public List<LayoutElement> getElements()
	{
		return Collections.unmodifiableList(elements);
	}

	private static final Map<Integer, Integer> SIZE_OF_TYPE = Map.of(
			GL_FLOAT, Float.BYTES,
			GL_UNSIGNED_INT, Integer.BYTES,
			GL_UNSIGNED_BYTE, Byte.BYTES
	);
}
