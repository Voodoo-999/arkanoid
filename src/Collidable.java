/**
 * Collidable.
 */
public interface Collidable {
   // Return the "collision shape" of the object.
   /**
 * @return the rectangle that will collide with
 */
Rectangle getCollisionRectangle();

   // Notify the object that we collided with it at collisionPoint with
   // a given velocity.
   // The return is the new velocity expected after the hit (based on
   // the force the object inflicted on us).
   /**
 * @param collisionPoint the poin of collision
 * @param hitter the ball that hited
 * @param currentVelocity the velocity of the moment of collision
 * @return the new Velocity to apply to ball
 */
Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
