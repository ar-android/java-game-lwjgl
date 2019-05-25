package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
    private final Vector2d previousPosition;
    private final Vector2d currentPosition;
    private final Vector2f displayVector;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0,0);
        displayVector = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (__, xpos, ypos) -> {
            currentPosition.x = xpos;
            currentPosition.y = ypos;
        });

        glfwSetCursorEnterCallback(window.getWindowHandle(), (__, entered) -> {
           inWindow = entered;
        });

        glfwSetMouseButtonCallback(window.getWindowHandle(), (__, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }

    public void input(Window window) {
        displayVector.x = 0;
        displayVector.y = 0;
        if (previousPosition.x > 0 & previousPosition.y > 0 && inWindow){
            double deltax = currentPosition.x - previousPosition.x;
            double deltay = currentPosition.y - previousPosition.y;
            boolean rotatex = deltax != 0;
            boolean rotatey = deltay != 0;
            if (rotatex){
                displayVector.y = (float) deltax;
            }
            if (rotatey){
                displayVector.x = (float) deltay;
            }
            previousPosition.x = currentPosition.x;
            previousPosition.y = currentPosition.y;
        }
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
