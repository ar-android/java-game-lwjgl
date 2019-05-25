package engine.graphic;

import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;

    public Texture(int id) {
        this.id = id;
    }

    public Texture(String filename) throws Exception {
        this(loadTexture(filename));
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanUp() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    private static int loadTexture(String filename) throws Exception{
        int width;
        int height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            URL resource = Texture.class.getResource(filename);
            File file = Paths.get(resource.toURI()).toFile();
            String fileAbsolutePath = file.getAbsolutePath();
            buffer = stbi_load(fileAbsolutePath, w, h, channels, 4);
            if (buffer == null){
                throw new Exception("Image file[" + fileAbsolutePath + "] not foound." + stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buffer);

        return textureId;
    }
}
