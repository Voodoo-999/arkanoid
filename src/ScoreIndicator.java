import java.awt.Color;
import biuoop.DrawSurface;

/**
 * ScoreIndicator.
 */
public class ScoreIndicator implements Sprite {
    private Counter score;

    /**
     * @param c the counter to indicate from
     */
    public ScoreIndicator(Counter c) {
        this.score = c;
    }

    @Override
    public void drawOn(DrawSurface d) {
        return;

    }

    public void betterDrawOn(DrawSurface d, int x, int y, int size) {
        d.setColor(Color.green);
        d.drawText(x, y, "スコア: " + this.score.getValue()+" / 245", size);
    }

    @Override
    public void timePassed() {
    }

    /**
     * @param game the game to add to
     */
    public void addToGame(Game game) {
        game.addSprite(this);
    }

}
