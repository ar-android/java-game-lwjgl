package engine.graphic;

import engine.Camera;
import engine.GameItem;
import engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private Matrix4f projectionMatrix;

    private Matrix4f modelViewMatrix;

    private Matrix4f viewMatrix;

    public Transformation() {
        this.projectionMatrix = new Matrix4f();
        this.modelViewMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov, float width, float height, float z_near, float z_far){
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, (width / height), z_near, z_far);
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Vector3f rotation = gameItem.getRotation();
        modelViewMatrix.set(viewMatrix)
                .translate(gameItem.getPosition())
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateY((float) Math.toRadians(-rotation.y))
                .rotateZ((float) Math.toRadians(-rotation.z))
                .scale(gameItem.getScale());
        return modelViewMatrix;
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }
}
