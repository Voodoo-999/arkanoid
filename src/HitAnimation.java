import java.awt.Color;
import biuoop.DrawSurface;
import biuoop.Sleeper;

public class HitAnimation implements HitListener {

    private Game game;

    public HitAnimation(Game game) {
        this.game = game;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        this.game.hitAnimation(hitter.getCenter(), hitter.getColor(), 15, 15);

    }
}
