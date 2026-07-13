// a BlockRemover is in charge of removing blocks from the game, as well as keeping count
// of the number of blocks that remain.
/**
 * BlockRemover.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * @param game the game to act upon
     * @param remainingBlocks counter of remaining points
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    // Blocks that are hit should be removed
    // from the game. Remember to remove this listener from the block
    // that is being removed from the game.
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(this.game);
        this.game.blockRemove(beingHit);
        this.remainingBlocks.decrease(1);
    }
}
