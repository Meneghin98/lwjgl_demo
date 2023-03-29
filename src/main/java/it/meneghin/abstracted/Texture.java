package it.meneghin.abstracted;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements AutoCloseable
{

	private final int rendererID;
	private final String filepath;
	private int width = 0, height = 0, bpp = 0;

	public Texture(@NotNull final String filepath)
	{
		stbi_set_flip_vertically_on_load(true);

		int[] w = {0}, h = {0}, b = {0};
		ByteBuffer buffer = null;

		try (InputStream stream = ClassLoader.getSystemResourceAsStream(filepath)) {
			// Leggi i dati dell'immagine PNG dallo stream
			assert stream != null;
			byte[] imageData = stream.readAllBytes();

			// Carica i dati dell'immagine PNG usando STB Image
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
			imageBuffer.put(imageData);
			imageBuffer.rewind();

			buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, b, 4);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (buffer == null) {
			throw new IllegalStateException("Failed to read image");
		}

		this.width = w[0];
		this.height = h[0];
		this.bpp = b[0];


		this.filepath = filepath;
		this.rendererID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, rendererID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		this.bind();

		stbi_image_free(buffer);
	}



	public void bind(final int slot)
	{
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, rendererID);
	}
	public void bind()
	{
		bind(0);
	}
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getBpp()
	{
		return bpp;
	}

	@Override
	public void close() throws Exception
	{
		glDeleteTextures(rendererID);
	}
}
