package engine.graphic;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private int programId;
    private int vertexId;
    private int fragmentId;

    public ShaderProgram() throws Exception{
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create shader.");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0)
            throw new Exception("Error creating shader. Type: " + shaderType);

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling  shader code :" + glGetShaderInfoLog(shaderId, 1024));

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new Exception("Error linking Shader Code: " + glGetProgramInfoLog(programId, 1024));

        if (vertexId != 0)
            glDetachShader(programId, vertexId);

        if (fragmentId != 0)
            glDetachShader(programId, fragmentId);

        glValidateProgram(programId);

//        Used when debugging mode
//        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
//            System.err.println("Warning validating shader code: "+ glGetProgramInfoLog(programId, 1024));
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0)
            glDeleteProgram(programId);
    }
}
