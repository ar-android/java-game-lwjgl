package game;

import org.lwjgl.opengl.GL11;

public class Renderer {

    public Renderer() {
    }

    public void init() {

    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
