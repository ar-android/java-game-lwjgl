package engine;

public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(Window window);

    void render(Window window);

    void update(float interval);

    void cleanup();

}
