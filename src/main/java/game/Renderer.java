package game;

import engine.GameItem;
import engine.Utils;
import engine.Window;
import engine.graphic.Mesh;
import engine.graphic.ShaderProgram;
import engine.graphic.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glViewport;

public class Renderer {

    private ShaderProgram shaderProgram;

    private static final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_NEAR = 0.0f;
    private final float Z_FAR = 1000;

    private Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();


        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        for (GameItem gameItem : gameItems) {
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale()
            );
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();

    }

    public void cleanup() {
        if (shaderProgram != null)
            shaderProgram.cleanup();
    }
}
