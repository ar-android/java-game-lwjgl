package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.Window;
import engine.graphic.Mesh;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private int direction = 0;
    private float color = 0.0f;
    private final Renderer renderer;
    private GameItem[] gameItems;

    private int displyInc;
    private int displxInc;
    private int displzInc;
    private int scaleInc;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float[] positions = new float[]{
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, 0-.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new  int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5
        };

        Mesh mesh = new Mesh(positions, colors, indices);

        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0,0,-2);
        gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;

        if (window.isKeyPressed(GLFW_KEY_UP)){
            displyInc = 1;
        }else if (window.isKeyPressed(GLFW_KEY_DOWN)){
            displyInc = -1;
        }else if (window.isKeyPressed(GLFW_KEY_LEFT)){
            displxInc = 1;
        }else if (window.isKeyPressed(GLFW_KEY_RIGHT)){
            displxInc = -1;
        }else if (window.isKeyPressed(GLFW_KEY_A)){
            displzInc = -1;
        }else if (window.isKeyPressed(GLFW_KEY_Q)){
            displzInc = 1;
        }else if (window.isKeyPressed(GLFW_KEY_Z)){
            scaleInc = -1;
        }else if (window.isKeyPressed(GLFW_KEY_Y)){
            scaleInc = 1;
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameItems);
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            Vector3f position = gameItem.getPosition();
            float posx = position.x + displxInc * 0.01f;
            float posy = position.y + displyInc * 0.01f;
            float posz = position.z + displzInc * 0.01f;
            gameItem.setPosition(posx, posy, posz);

            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0){
                scale = 0;
            }
            gameItem.setScale(scale);

            float rotation = gameItem.getRotation().x + 1.5f;
            if (rotation > 360){
                rotation = 0;
            }
            gameItem.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
