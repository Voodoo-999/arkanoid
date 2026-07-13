import biuoop.DrawSurface;
/**
 * Sprite.
 */
public interface Sprite {
   // draw the sprite to the screen
/**
 * @param d a draw surface to draw to
 */
   void drawOn(DrawSurface d);
   // notify the sprite that time has passed
   /**
 * a method to inform that time has passed.
 */
void timePassed();
}
