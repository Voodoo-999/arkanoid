/**
 * ScoreTrackingListener.
 */
public class ScoreTrackingListener implements HitListener {
   private Counter currentScore;

   /**
 * @param scoreCounter the counter to count keep trach to
 */
public ScoreTrackingListener(Counter scoreCounter) {
      this.currentScore = scoreCounter;
   }

    @Override
   public void hitEvent(Block beingHit, Ball hitter) {
        this.currentScore.increase(5);
   }
}
