import java.util.List;

/**
 * A class of a line.
 */
public class Line {
    private static final double COMPARISON_THRESHOLD = 0.00001;
    private Point start;
    private Point end;

    // constructors
    /**
     * constructon with 2 points.
     * @param start the start point
     * @param end   the end point
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * constructor with 4 values.
     * @param x1 the x value of the start point
     * @param x2 the x value of the end point
     * @param y1 the y value of the start point
     * @param y2 the y value of the end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * a method to return the length of the line.
     * @return the length of the line
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
     * method to return the middle point of the line.
     * @return the middle point of the line
     */
    public Point middle() {
        double middleX = (this.start.getX() + this.end.getX()) / 2;
        double middleY = (this.start.getY() + this.end.getY()) / 2;
        return new Point(middleX, middleY);
    }

    /**
     * method to return the start point.
     * @return the start point of the line
     */
    public Point getStart() {
        return new Point(start.getX(), start.getY());
    }

    /**
     * method to return the end point.
     * @return the end point of the line
     */
    public Point getEnd() {
        return new Point(end.getX(), end.getY());
    }

    /**
     * Returns true if the lines intersect, false otherwise.
     * @param other the other line to check intersection with
     * @return true if intersect, falses otherwise
     */
    public boolean isIntersecting(Line other) {
        Point ownStartPoint = this.start;
        Point ownEndPoint = this.end;
        Point otherStartPoint = other.getStart();
        Point otherEndPoint = other.getEnd();

        double ownSlope = calcSlope(ownStartPoint, ownEndPoint);
        double otherSlope = calcSlope(otherStartPoint, otherEndPoint);

        // If the lines have the same slope, they are parallel and will never intersect
        // we need to check 2 things, if they are overlaping or not
        if (doubleEquals(ownSlope, otherSlope)) {
            // check if overlap
            // i do this by checking if one point is on the other line
            // check if they are on the same line
            if (doubleEquals(ownStartPoint.getY(), otherSlope * ownStartPoint.getX()
                    + calculateIntercept(otherStartPoint, otherSlope))) {
                // here we enter if they are on the same line, and we check if they are at the
                // same "segment"
                if (isBetween(otherStartPoint, otherEndPoint, ownStartPoint.getX(), ownStartPoint.getY())
                        || isBetween(otherStartPoint, otherEndPoint, ownEndPoint.getX(), ownEndPoint.getY())
                        || isBetween(ownStartPoint, ownEndPoint, otherStartPoint.getX(), otherStartPoint.getY())
                        || isBetween(ownStartPoint, ownEndPoint, otherEndPoint.getX(), otherEndPoint.getY())) {
                    return true;
                }
            }
            // If the lines are parallel but don't overlap
            return false;
        }

        double xIntersect, yIntersect;

        // Check for vertical lines (slope = Double.POSITIVE_INFINITY)
        if (Double.isInfinite(ownSlope)) { // Own line is vertical
            xIntersect = ownStartPoint.getX();
            yIntersect = otherSlope * xIntersect + calculateIntercept(otherStartPoint, otherSlope);
        } else if (Double.isInfinite(otherSlope)) { // Other line is vertical
            xIntersect = otherStartPoint.getX();
            yIntersect = ownSlope * xIntersect + calculateIntercept(ownStartPoint, ownSlope);
        } else { // General case
            double b1 = calculateIntercept(ownStartPoint, ownSlope);
            double b2 = calculateIntercept(otherStartPoint, otherSlope);
            xIntersect = (b2 - b1) / (ownSlope - otherSlope); // Solve for X
            yIntersect = ownSlope * xIntersect + b1;
        }

        // Check if the intersection point is within both line segments
        if (isBetween(ownStartPoint, ownEndPoint, xIntersect, yIntersect)
                && isBetween(otherStartPoint, otherEndPoint, xIntersect, yIntersect)) {
            return true;
        }

        return false; // Intersection is outside the line segments
    }

    // If this line does not intersect with the rectangle, return null.
    // Otherwise, return the closest intersection point to the
    // start of the line.

    /**
     * @param rect a rectangle to check the closest collision with
     * @return the closest point of collision
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        Line mySelf = new Line(this.getStart(), this.getEnd());
        List<Point> intersectionList = rect.intersectionPoints(mySelf);
        Point closestPoint = null;
        double shortestDistance = 1000;
        for (Point intersectPoint : intersectionList) {
            double currentDistance = intersectPoint.distance(this.start);
            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestPoint = new Point(intersectPoint.getX(), intersectPoint.getY());
            }
        }
        return closestPoint;
    }

    /**
     * Returns true if the lines intersect with both lines, false otherwise.
     * @param other1 the first line to check intersection with
     * @param other2 the second line to check intersection with
     * @return true if intersect with both, falses otherwise
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return isIntersecting(other1) && isIntersecting(other2);
    }

    /**
     * Returns true if the lines intersect , false otherwise.
     * @param other the other line to check intersection with
     * @return the intesection point if intersect, false otherwise
     */
    public Point intersectionWith(Line other) {
        Point ownStartPoint = this.start;
        Point ownEndPoint = this.end;
        Point otherStartPoint = other.getStart();
        Point otherEndPoint = other.getEnd();

        double ownSlope = calcSlope(ownStartPoint, ownEndPoint);
        double otherSlope = calcSlope(otherStartPoint, otherEndPoint);

        // If the lines have the same slope, they are parallel and will never intersect
        if (doubleEquals(ownSlope, otherSlope)) {
            return null;
        }

        double xIntersect, yIntersect;

        // Check for vertical lines (slope = Double.POSITIVE_INFINITY)
        if (Double.isInfinite(ownSlope)) { // Own line is vertical
            xIntersect = ownStartPoint.getX();
            yIntersect = otherSlope * xIntersect + calculateIntercept(otherStartPoint, otherSlope);
        } else if (Double.isInfinite(otherSlope)) { // Other line is vertical
            xIntersect = otherStartPoint.getX();
            yIntersect = ownSlope * xIntersect + calculateIntercept(ownStartPoint, ownSlope);
        } else { // General case
            double b1 = calculateIntercept(ownStartPoint, ownSlope);
            double b2 = calculateIntercept(otherStartPoint, otherSlope);
            xIntersect = (b2 - b1) / (ownSlope - otherSlope); // Solve for X
            yIntersect = ownSlope * xIntersect + b1;
        }

        // Check if the intersection point is within both line segments
        if (isBetween(ownStartPoint, ownEndPoint, xIntersect, yIntersect)
                && isBetween(otherStartPoint, otherEndPoint, xIntersect, yIntersect)) {
            return new Point(xIntersect, yIntersect);
        }

        return null; // Intersection is outside the line segments
    }

    /**
     * Calculate y-intercept (b) given a point and slope.
     * @param p     the point
     * @param slope the slope of the equation
     * @return the intercept
     */
    private double calculateIntercept(Point p, Double slope) {
        return p.getY() - slope * p.getX();
    }

    /**
     * Calculate the slope of a line segment given two points.
     * @param start the start point
     * @param end   the end point
     * @return the slope of the equation
     */
    private Double calcSlope(Point start, Point end) {
        if (doubleEquals(start.getX(), end.getX())) {
            return Double.POSITIVE_INFINITY; // Represent vertical lines
        }
        // eqauation for finding slope:
        return (end.getY() - start.getY()) / (end.getX() - start.getX());
    }

    /**
     * Check if a point (x, y) is within a given segment.
     * @param start the staring point
     * @param end   the end point
     * @param x     the x value to check if in the line
     * @param y     the y value to check if in the line
     * @return true if the values in the line, false otherwise
     */
    private boolean isBetween(Point start, Point end, double x, double y) {
        return x >= Math.min(start.getX(), end.getX()) && x <= Math.max(start.getX(), end.getX())
                && y >= Math.min(start.getY(), end.getY()) && y <= Math.max(start.getY(), end.getY());
    }

    /**
     * equals -- return true if the lines are equal, false otherwise.
     * @param other the other line to check equality to
     * @return true if th line equals, false otherwise
     */
    public boolean equals(Line other) {
        return (this.start.equals(other.getStart()) && this.end.equals(other.getEnd())
                || (this.start.equals(other.getEnd()) && this.end.equals(other.getStart())));
    }

    /**
     * chck equal by threshhold.
     * @param a the first number
     * @param b the second number
     * @return true if equal up to the threshold
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < COMPARISON_THRESHOLD;
    }
}
