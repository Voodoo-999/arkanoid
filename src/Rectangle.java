import java.util.List;
import java.util.ArrayList;

/**
 * A class of a rectangle.
 */
public class Rectangle {
    private double width;
    private double height;
    private Point upperRight;
    private Point downLeft;
    private Point upperLeft;
    private Point downRight;

    // constructors
    /**
     * constructon with 2 points.
     * @param downRight the downRight point
     * @param upperLeft the upperLeft point
     */
    public Rectangle(Point upperLeft, Point downRight) {
        this.upperLeft = new Point(upperLeft.getX(), upperLeft.getY());
        this.downRight = new Point(downRight.getX(), downRight.getY());
        this.downLeft = new Point(upperLeft.getX(), downRight.getY());
        this.upperRight = new Point(downRight.getX(), upperLeft.getY());
        this.width = downRight.getX() - upperLeft.getX();
        this.height = downRight.getY() - upperLeft.getY();
    }

    /**
     * @param upperLeft point
     * @param width     width
     * @param height    height
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = new Point(upperLeft.getX(), upperLeft.getY());
        this.width = width;
        this.height = height;
        this.downRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
        this.downLeft = new Point(upperLeft.getX(), downRight.getY());
        this.upperRight = new Point(downRight.getX(), upperLeft.getY());
    }

    // Return a (possibly empty) List of intersection points
    // with the specified line.
    /**
     * @param line with this line we check intersection
     * @return a possibly empty list of intersection points
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        List<Point> intersectionList = new ArrayList<Point>();
        Line upperLine = new Line(this.upperLeft, this.upperRight);
        Line lowerLine = new Line(this.downLeft, this.downRight);
        Line rightLine = new Line(this.upperRight, this.downRight);
        Line leftLine = new Line(this.upperLeft, this.downLeft);
        Point upperIntesect = upperLine.intersectionWith(line);
        Point lowerIntersect = lowerLine.intersectionWith(line);
        Point rightIntersect = rightLine.intersectionWith(line);
        Point leftIntersectin = leftLine.intersectionWith(line);

        if (upperIntesect != null) {
            intersectionList.add(upperIntesect);
        }
        if (lowerIntersect != null) {
            intersectionList.add(lowerIntersect);
        }
        if (rightIntersect != null) {
            intersectionList.add(rightIntersect);
        }
        if (leftIntersectin != null) {
            intersectionList.add(leftIntersectin);
        }
        return intersectionList;
    }

    // Return the width and height of the rectangle

    /**
     * @return the loweset rightest point
     */
    public Point getDownRight() {
        return new Point(this.downRight.getX(), this.downRight.getY());
    }

    /**
     * @param downRight point
     */
    public void setDownRight(Point downRight) {
        this.downRight = new Point(downRight.getX(), downRight.getY());
    }

    /**
     * @return the top left point
     */
    public Point getUpperLeft() {
        return new Point(this.upperLeft.getX(), this.upperLeft.getY());
    }

    /**
     * @param upperLeft
     */
    public void setUpLeft(Point upperLeft) {
        this.upperLeft = new Point(upperLeft.getX(), upperLeft.getY());
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the uprer right point
     */
    public Point getUpperRight() {
        return new Point(this.upperRight.getX(), this.upperRight.getY());
    }

    /**
     * @return the down left point
     */
    public Point getDownLeft() {
        return new Point(this.downLeft.getX(), this.downLeft.getY());
    }

}
