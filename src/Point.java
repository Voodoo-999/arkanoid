
/**
 *A class which creates a Point.
 */
public class Point {
    private double x;
    private double y;
    /**
     *a constructor.
     @param x is the x value of the point
     @param y is the y value of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *calculate the distance.
     @param other is the other point to check distance to
     @return the distance between @ points
     */
    public double distance(Point other) {
        double insideTheSqrX = (this.x - other.getX()) * (this.x - other.getX());
        double insideTheSqrY = (this.y - other.getY()) * (this.y - other.getY());
        double distance = Math.sqrt(insideTheSqrX + insideTheSqrY);
        return distance;
    }

    /**
     *check for equality.
     @param other is the other point to check equality to
     @return true if the point are equal, false otherwise
     */
    public boolean equals(Point other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    /**
     *get the X value.
     @return the x value
     */
    public double getX() {
        return this.x;
    }
    /**
     *get the Y value.
     @return the y value
     */
    public double getY() {
        return this.y;
    }
}
