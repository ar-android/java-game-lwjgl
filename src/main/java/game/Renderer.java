package game;

import engine.Utils;
import engine.Window;
import engine.graphic.Mesh;
import engine.graphic.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glViewport;

public class Renderer {

    private ShaderProgram shaderProgram;

    private static final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_NEAR = 0.0f;
    private final float Z_FAR = 1000;
    private Matrix4f projectionMatrix;

    public Renderer() {
    }

    public void init(Window window) throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();
        shaderProgram.createUniform("projectionMatrix");

        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        mesh.render();

        shaderProgram.unbind();

    }

    public void cleanup() {
        if (shaderProgram != null)
            shaderProgram.cleanup();
    }
}
