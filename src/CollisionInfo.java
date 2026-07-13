/**
 * CollisionInfo.
 */
public class CollisionInfo {
    private Point collisionPoint;
    private Collidable collisionObject;


    /**
     * @param pointOfCollision the point of collison
     * @param collisionObject the object with which colliding
     */
    public CollisionInfo(Point pointOfCollision, Collidable collisionObject) {
        this.collisionPoint = new Point(pointOfCollision.getX(), pointOfCollision.getY());
        this.collisionObject = collisionObject;
    }

    // the point at which the collision occurs.
    /**
     * @return the point of collision
     */
    public Point collisionPoint() {
        return new Point(this.collisionPoint.getX(), this.collisionPoint.getY());
    }

    // the collidable object involved in the collision.
    /**
     * @return the collidable onject that colliding with
     */
    public Collidable collisionObject() {
        return this.collisionObject;
    }

    /**
     * @param collisionPoint set the point of collisioin
     */
    public void setCollisionPoint(Point collisionPoint) {
        this.collisionPoint = new Point(collisionPoint.getX(), collisionPoint.getY());
    }

    /**
     * @param collisionObject set the object that will collide with
     */
    public void setCollisionObject(Collidable collisionObject) {
        this.collisionObject = collisionObject;
    }
}
