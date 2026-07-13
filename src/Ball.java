import biuoop.DrawSurface;

/**
 * A class of a ball.
 */
public class Ball implements Sprite, Drawable {
    private static final int TURN_WAIT = 0;
    private static final int MARGIN = 1;
    private static final double PI = 3.1415926535;
    private int wait = 0;
    private Point center;
    private int radius;
    private int leftBorder;
    private int topBorder;
    private int rightBorder;
    private int downBorder;
    private Velocity velocity;
    private GameEnvironment gameEnvironment;
    private Rectangle[] obstacle;
    private Paddle paddle = null;
    private int currentObstacle;
    private java.awt.Color color;

    public Point getCenter() {
        return new Point(this.center.getX(), this.center.getY());
    }

    // constructor
    /**
     * @param x     coordinate of point
     * @param y     coordinate of point
     * @param r     radius
     * @param color color
     */
    public Ball(int x, int y, int r, java.awt.Color color) {
        this.center = new Point(x, y);
        this.radius = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
        this.rightBorder = 800;
        this.downBorder = 800;
        this.leftBorder = 0;
        this.topBorder = 0;
        this.obstacle = new Rectangle[2];
        this.currentObstacle = 0;
        this.gameEnvironment = null;
    }

    /**
     * @param center point
     * @param r      radius
     * @param color  color
     */
    public Ball(Point center, int r, java.awt.Color color) {
        this.center = new Point(center.getX(), center.getY());
        this.radius = r;
        this.color = color;
        this.rightBorder = 800;
        this.downBorder = 800;
        this.leftBorder = 0;
        this.topBorder = 0;
        this.obstacle = new Rectangle[2];
        this.currentObstacle = 0;
        this.gameEnvironment = null;
    }

    /**
     * @param color the color to set to
     */
    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    /**
     * @param paddle a paddle to check hit with
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    /**
     * @param game the game to remove from
     */
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }

    /**
     * @param gameEnvironment the game environment to add to
     */
    public void setGameEnvironment(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * @param obstacle rectangle
     */
    public void setObstacle(Rectangle obstacle) {
        this.obstacle[this.currentObstacle] = new Rectangle(obstacle.getUpperLeft(), obstacle.getDownRight());
        this.currentObstacle++;
    }

    /**
     * @param velocity
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = new Velocity(velocity.getChangeX(), velocity.getChangeY());
    }

    /**
     * @param dx
     * @param dy
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * @return the current velocity
     */
    public Velocity getVelocity() {
        return new Velocity(this.velocity.getChangeX(), this.velocity.getChangeY());
    }

    // accessors
    /**
     * @return the center x value
     */
    public double getX() {
        return this.center.getX();
    }

    /**
     * @return true if the ball was inside the paddle
     */
    private boolean inPadlle() {
        if (this.paddle == null) {
            return false;
        }
        double leftPaddleWall = this.paddle.getCollisionRectangle().getUpperLeft().getX();
        double rightPaddleWall = this.paddle.getCollisionRectangle().getDownRight().getX();
        double currentX = this.center.getX();
        double currentY = this.center.getY();
        double halfRectangleWidth = this.paddle.getCollisionRectangle().getWidth() / 2;
        double topPaddleWall = this.paddle.getCollisionRectangle().getUpperLeft().getY();
        double lowerPaddleWall = this.paddle.getCollisionRectangle().getDownRight().getY();
        // check if the ball is inside, and if so to which side its the closest, and
        // then move to ball outside
        if (currentX <= rightPaddleWall && currentX >= leftPaddleWall && currentY <= lowerPaddleWall
                && currentY >= topPaddleWall) {
            if (currentX > leftPaddleWall + halfRectangleWidth) {
                // then move to the right side
                this.center = new Point(rightPaddleWall + this.radius, this.center.getY());
                return true;
            }
            this.center = new Point(leftPaddleWall - this.radius, this.center.getY());
            return true;
        }
        return false;
    }

    /**
     * move the ball one step, after checking the necessery things.
     */
    public void timePassed() {
        CollisionInfo collisionInfo = hitInfo();
        if (inPadlle()) {
            return;
        }
        if (null != collisionInfo) {
            // this changes the velocity using the collidable "hit" method
            this.velocity = hitInfo().collisionObject().hit(this, hitInfo().collisionPoint(), this.velocity);
            return;
        }
        this.center = this.velocity.applyToPoint(this.center);

    }

    /**
     * @param top    line at the top
     * @param bottom line of the bottom
     * @return true if between the two lines
     */
    private boolean isBetweenTwoHorizontalLines(Line top, Line bottom) {
        int topOfBAll = (int) this.center.getY() - this.radius;
        int bottomOfBAll = (int) this.center.getY() + this.radius;
        return (bottomOfBAll >= top.getStart().getY())
                && (topOfBAll <= bottom.getStart().getY());
    }

    /**
     * @param right
     * @param left
     * @return true if between the two lines
     */
    private boolean isBetweenTwoVerticalLines(Line right, Line left) {
        int rightOfBAll = (int) this.center.getX() + this.radius;
        int leftOfBAll = (int) this.center.getX() - this.radius;
        return ((rightOfBAll >= left.getStart().getX())
                && (leftOfBAll <= right.getStart().getX()));
    }

    /**
     * @param topBorder
     */
    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    /**
     * @param leftBorder
     */
    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     * @param rightBorder
     */
    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     * @param downBorder
     */
    public void setDownBorder(int downBorder) {
        this.downBorder = downBorder;
    }

    /**
     * @return the y value of center
     */
    public double getY() {
        return this.center.getY();
    }

    public int getRadius() {
        return (int)this.radius;
    }

    /**
     * @return the size of ball
     */
    public int getSize() {
        // need to calculate the size of the ball
        return (int) (this.radius * this.radius * PI);
    }

    /**
     * @return the color of ball
     */
    public java.awt.Color getColor() {
        return this.color;
    }

    // draw the ball on the given DrawSurface
    /**
     * @param surface
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle((int) this.center.getX(), (int) this.center.getY(), this.radius);
    }

    /**
     * @return a {@link CollisionInfo}
     */
    public CollisionInfo hitInfo() {
        double multiplyer = 2.3;
        Point endEndOfTrajectory = new Point(this.getX() + multiplyer * this.velocity.getChangeX(),
                this.getY() + multiplyer * this.velocity.getChangeY());
        Line trajectory = new Line(this.center, endEndOfTrajectory);
        if (gameEnvironment == null) {
            return null;
        }
        return gameEnvironment.getClosestCollision(trajectory);
    }

    /**
     * @param game the game to add to
     */
    public void addToGame(Game game) {
        game.addSprite(this);
    }
}
