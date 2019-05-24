package engine.graphic;

import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Mesh {
    private final int vaoId;
    private final int vboId;
    private final int vertextCount;

    public Mesh(float[] positions) {
        FloatBuffer verticesBuffer = null;
        try{
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            vertextCount = positions.length / 3;
            verticesBuffer.put(positions).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0,3,GL_FLOAT, false, 0,0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }finally {
            if (verticesBuffer != null){
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVboId() {
        return vboId;
    }

    public int getVertextCount() {
        return vertextCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        glDeleteBuffers(vboId);

        glBindVertexArray(0);
        glDeleteBuffers(vaoId);
    }
}
