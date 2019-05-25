package engine;

public class GameEngine implements Runnable{

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Window window;
    private final Thread gameLoopThread;
    private final Timer timer;
    private final IGameLogic gameLogic;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        this.window = new Window(windowTitle, width, height, vSync);
        this.gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        this.timer = new Timer();
        this.gameLogic = gameLogic;
        this.mouseInput = new MouseInput();
    }

    public void start(){
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")){
            gameLoopThread.run();
        }else{
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cleanup();
        }
    }

    private void init() throws Exception{
        window.init();
        mouseInput.init(window);
        timer.init();
        gameLogic.init(window);
    }

    private void gameLoop(){

        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.requestClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();
            while (accumulator >= interval){
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;

        while (timer.getTime() < endTime){
            try{
                Thread.sleep(1);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    private void render() {
        gameLogic.render(window);
        window.update();
    }

    private void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    private void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    public void cleanup() {
        gameLogic.cleanup();
    }
}
