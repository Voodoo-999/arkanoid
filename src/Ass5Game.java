
/**
 * BouncingBallAnimation.
 */
public class Ass5Game {
    private static final int SCREEN_SIZE_X = 1300;
    private static final int SCREEN_SIZE_Y = 900;


    /**
     * @param args
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.run();
    }
}
