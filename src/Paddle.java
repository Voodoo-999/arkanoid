import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.GUI;

/**
 * Paddle.
 */
public class Paddle implements Sprite, Collidable {
    final public Color color = new Color(103, 136, 250);
    final public Color other = new Color(255, 100, 50);
    final public Color orange = new Color(220, 20, 60);
    private int height;
    private Game game;
    private int width;
    private double wait = 0;
    private double DX = 5;
    private biuoop.KeyboardSensor keyboard;
    private Rectangle rectangle;
    private GUI gui;

    /**
     * @param gui  a gui
     * @param rect a Rectangle
     */
    public Paddle(GUI gui, Rectangle rect, int height, int width, Game game) {
        this.height = height;
        this.game = game;
        this.width = width;
        this.gui = gui;
        this.keyboard = gui.getKeyboardSensor();
        this.rectangle = new Rectangle(rect.getUpperLeft(), rect.getDownRight());
    }

    /**
     * @return the current gui
     */
    public GUI getGUI() {
        return this.gui;
    }

    /**
     * moves left.
     */
    public void moveLeft() {
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();
        double newX = this.rectangle.getUpperLeft().getX() - DX;
        double newY = this.rectangle.getUpperLeft().getY();
        Point newUpperLeft = new Point(newX, newY);
        this.rectangle = new Rectangle(newUpperLeft, width, height);
    }

    public Color getColor() {
        return this.color;
    }

    /**
     * moves right.
     */
    public void moveRight() {
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();
        double newX = this.rectangle.getUpperLeft().getX() + DX;
        double newY = this.rectangle.getUpperLeft().getY();
        Point newUpperLeft = new Point(newX, newY);
        this.rectangle = new Rectangle(newUpperLeft, width, height);

    }

    /**
     * do something when pass time.
     */
    public void timePassed() {
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();
        double newY = this.rectangle.getUpperLeft().getY();
        if (this.wait > 0) {
            --this.wait;
            return;
        }
        if (this.keyboard.isPressed(biuoop.KeyboardSensor.RIGHT_KEY)) {
            if (this.rectangle.getUpperRight().getX() >= this.width) {
                double newX = 0;
                Point newUpperLeft = new Point(newX, newY);
                this.rectangle = new Rectangle(newUpperLeft, width, height);
                return;
            }
            this.game.rightMoveAnimation();
            moveRight();
        }
        if (this.keyboard.isPressed(biuoop.KeyboardSensor.LEFT_KEY)) {
            if (this.rectangle.getUpperLeft().getX() < 0) {
                double newX = this.width - width;
                Point newUpperLeft = new Point(newX, newY);
                this.rectangle = new Rectangle(newUpperLeft, width, height);
                return;
            }
            this.game.leftMoveAnimation();
            moveLeft();
        }
    }

    /**
     * @param d a drawsurface to draw to
     */
    public void drawOn(DrawSurface d) {
        double x = this.rectangle.getUpperLeft().getX();
        double y = this.rectangle.getUpperLeft().getY();
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();
        d.setColor(this.color);
        d.fillRectangle((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * get the collision rectangle.
     * 
     * @return a the rectangle that collide with
     */
    public Rectangle getCollisionRectangle() {
        return new Rectangle(this.rectangle.getUpperLeft(), this.rectangle.getDownRight());
    }

    /**
     * @param collisionPoint  the collision point
     * @param currentVelocity the current velocity of the ball
     * @return a velocity to implement to the ball after the hit
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.game.hitAnimation(collisionPoint, this.color, hitter.getRadius(), 15);
        double rightX = this.rectangle.getDownRight().getX();
        double leftX = this.rectangle.getUpperLeft().getX();
        double upperY = this.rectangle.getUpperLeft().getY();
        double lowerY = this.rectangle.getDownRight().getY();
        double width = this.rectangle.getWidth();
        double height = this.rectangle.getHeight();
        double fifthWidth = width / 5;
        double firstDividingX = rightX - (4 * fifthWidth);
        double secondDividingX = rightX - (3 * fifthWidth);
        double thirdDividingX = rightX - (2 * fifthWidth);
        double fourthDividingX = rightX - (1 * fifthWidth);
        double dx = currentVelocity.getChangeX();
        double dy = currentVelocity.getChangeY();
        double speed = currentVelocity.getSpeed();
        double reverseDx = -dx;
        double reverseDy = -dy;
        // here it matters where it hits in the regions
        if (collisionPoint.getY() == upperY) {
            if (collisionPoint.getX() < firstDividingX) {
                return Velocity.fromAngleAndSpeed(300, speed);
            }
            if (collisionPoint.getX() < secondDividingX) {
                return Velocity.fromAngleAndSpeed(330, speed);
            }
            if (collisionPoint.getX() < thirdDividingX) {
                return new Velocity(dx, -dy);
            }
            if (collisionPoint.getX() < fourthDividingX) {
                return Velocity.fromAngleAndSpeed(30, speed);
            }
            // the fifth case:
            return Velocity.fromAngleAndSpeed(60, speed);
        }
        if (collisionPoint.getY() == lowerY) {
            return new Velocity(dx, reverseDy);
        }
        if (collisionPoint.getX() == rightX) {
            if (dx > 0) {
                // muiltiply because this is a situation that the paddle go over the ball from
                // the side
                if (dx > 2 * DX) {
                    return currentVelocity;
                }
                dx = dx * 1.5;
                return new Velocity(dx, dy);
            }
            this.wait = 0;
            return new Velocity(reverseDx, dy);
        }
        if (collisionPoint.getX() == leftX) {
            if (dx <= 0) {
                // muiltiply because this is a situation that the paddle go over the ball from
                // the side
                if (dx < -2 * DX) {
                    // check if too fast already
                    return currentVelocity;
                }
                dx = dx * 1.5;
                return new Velocity(dx, dy);
            }
            this.wait = 0;
            return new Velocity(reverseDx, dy);
        }
        return new Velocity(dx, dy);
    }

    // Add this paddle to the game.
    /**
     * @param g a game to add to
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    public int getMiddleHeight() {
        return (int) this.rectangle.getDownLeft().getY() - (int) this.rectangle.getHeight() / 2;
    }

    public int getLeftX() {
        return (int) this.rectangle.getDownLeft().getX();
    }

    public int getRightX() {
        return (int) this.rectangle.getDownRight().getX();
    }

    public int getWidth() {
        return (int) this.rectangle.getWidth();
    }

    public int getHeight() {
        return (int) this.rectangle.getHeight();
    }

    public double getDX() {
        return this.DX;
    }

    public void setDX(double dx) {
        this.DX = dx;
    }
}
