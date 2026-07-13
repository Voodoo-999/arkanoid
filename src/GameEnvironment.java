import java.util.ArrayList;
import java.util.List;

/**
 * GameEnvironment.
 */
public class GameEnvironment {
    private List<Collidable> collidables;

    /**
     * initialize the game env.
     */
    public GameEnvironment() {
        collidables = new ArrayList<Collidable>();
    }

    // add the given collidable to the environment.
    /**
     * @param c add collidable
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * @param c the Collidable to remove
     */
    public void removeCollidable(Collidable c) {
        collidables.remove(c);
    }

    // Assume an object moving from line.start() to line.end().
    // If this object will not collide with any of the collidables
    // in this collection, return null. Else, return the information
    // about the closest collision that is going to occur.
    /**
     * @param trajectory a line that contionu the path of the ball
     * @return collision info class
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        CollisionInfo collisionInfo = null;
        Point collisionPoint = null;
        List<Collidable> sprites = new ArrayList<Collidable>(this.collidables);
        for (Collidable collidable : this.collidables) {
            // this is the closest point to the iterated object
            Point currentCollidePoint = trajectory.closestIntersectionToStartOfLine(collidable.getCollisionRectangle());
            if (currentCollidePoint == null) {
                continue;
            }
            if (collisionPoint == null) {
                collisionPoint = currentCollidePoint;
                collisionInfo = new CollisionInfo(collisionPoint, collidable);
            } else {
                // need to compare distance from the points to see who is closer
                double beforeDistance = trajectory.getStart().distance(collisionPoint);
                double currentDistance = trajectory.getStart().distance(currentCollidePoint);
                // the current point is closer:
                if (currentDistance > beforeDistance) {
                    collisionPoint = currentCollidePoint;
                    collisionInfo = new CollisionInfo(collisionPoint, collidable);
                }
            }
        }
        return collisionInfo;
    }

}
