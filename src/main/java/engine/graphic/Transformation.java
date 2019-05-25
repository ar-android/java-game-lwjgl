package engine.graphic;

import engine.Window;
import org.joml.Matrix4f;

public class Transformation {

    private Matrix4f projectionMatrix;

    public Transformation() {
        this.projectionMatrix = new Matrix4f();
    }

    public void setProjectionMatrix(Window window, float fov, float z_near, float z_far) {
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, z_near, z_far);
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }
}
