import engine.GameEngine;
import engine.IGameLogic;
import game.DummyGame;

public class Game {

    public static void main(String[] args) {
        try {
            boolean vSyc = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 500, vSyc, gameLogic);
            gameEngine.start();
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}