package engine.graphic;

import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private final int vertexCount;
    private final int vaoId;
    private final int vboId;
    private final int idxVboId;
    private final int vcoId;

    public Mesh(float[] positions, float[] colors, int[] indices) {
        FloatBuffer verticesBuffer = null;
        FloatBuffer colorsBuffers = null;
        IntBuffer indicesBuffer = null;

        try{
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0,3,GL_FLOAT, false, 0,0);

            colorsBuffers = MemoryUtil.memAllocFloat(colors.length);
            colorsBuffers.put(colors).flip();
            vcoId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vcoId);
            glBufferData(GL_ARRAY_BUFFER, colorsBuffers, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }finally {
            if (verticesBuffer != null){
                MemoryUtil.memFree(verticesBuffer);
            }

            if (verticesBuffer != null){
                MemoryUtil.memFree(indicesBuffer);
            }

            if (colorsBuffers != null){
                MemoryUtil.memFree(colorsBuffers);
            }
        }

    }

    public void render() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(vcoId);
        glDeleteBuffers(idxVboId);

        glBindVertexArray(0);
        glDeleteBuffers(vaoId);
    }
}
