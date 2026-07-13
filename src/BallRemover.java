// a BlockRemover is in charge of removing blocks from the game, as well as keeping count
// of the number of blocks that remain.
/**
 * BallRemover.
 */
public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    /**
     * @param game           the game to act upon
     * @param remainingBalls the counter of remaining balls
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    // Blocks that are hit should be removed
    // from the game. Remember to remove this listener from the block
    // that is being removed from the game.
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        this.remainingBalls.decrease(1);
        this.game.ballDestroyAnimation(hitter);
        hitter.removeFromGame(this.game);
    }
}
