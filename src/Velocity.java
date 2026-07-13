// Velocity specifies the change in position on the `x` and the `y` axes.
/**
 * Velocity.
 */
public class Velocity {
    private double changeX;
    private double changeY;

    // constructor
    /**
     * @param dx
     * @param dy
     */
    public Velocity(double dx, double dy) {
        this.changeX = dx;
        this.changeY = dy;
    }

    /**
     * @return the dx value
     */
    public double getChangeX() {
        return this.changeX;
    }

    /**
     * @param dx
     */
    public void setChangeX(double dx) {
        this.changeX = dx;
    }

    /**
     * @return the dy value
     */
    public double getChangeY() {
        return this.changeY;
    }

    /**
     * @param dy
     */
    public void setChangeY(double dy) {
        this.changeY = dy;
    }


    /**
     * Calculate and return the speed based on dx and dy.
     *
     * @return the speed (magnitude of velocity)
     */
    public double getSpeed() {
        return Math.sqrt(this.changeX * this.changeX + this.changeY * this.changeY);
    }

    /**
     * @param angle
     * @param speed
     * @return new velocity with dx and dy instead of speed and angel(converting to
     *         them)
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radians = Math.toRadians(angle);
        double dx = speed * Math.sin(radians);
        double dy = -speed * Math.cos(radians); // minus because the y is from the buttom
        return new Velocity(dx, dy);
    }

    // Take a point with position (x,y) and return a new point
    // with position (x+dx, y+dy)
    /**
     * @param p
     * @return the point after the new velocity applayed
     */
    public Point applyToPoint(Point p) {
        Point newPoint = new Point(p.getX() + this.changeX, p.getY() + this.changeY);
        return newPoint;
    }
}
