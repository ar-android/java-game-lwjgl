package game;

import engine.*;
import engine.graphic.Mesh;
import engine.graphic.OBJLoader;
import engine.graphic.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Renderer renderer;
    private GameItem[] gameItems;
    private Camera camera;
    private Vector3f cameraInc;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        Texture texture = new Texture("/textures/grassblock.png");
//        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
//        mesh.setTexture(texture);

        gameItems = new GameItem[]{
                new GameItem(mesh)
                        .setScale(0.5f)
                        .setPosition(0,0,-3)
        };
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y *CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()){
            Vector2f displayVector = mouseInput.getDisplayVector();
            camera.movePosition(displayVector.x * MOUSE_SENSITIVITY, displayVector.y * MOUSE_SENSITIVITY, 0);
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
