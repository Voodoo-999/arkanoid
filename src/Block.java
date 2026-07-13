import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;

class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle collisionRectangle;
    private Color color = Color.darkGray;
    private List<HitListener> hitListeners;

    /**
     * @param rect the rectangle shape
     */
    Block(Rectangle rect) {
        this.collisionRectangle = new Rectangle(rect.getUpperLeft(), rect.getDownRight());
        this.hitListeners = new ArrayList<>();
    }

    /**
     * @param color the color of the block
     */
    public void setColor(Color color) {
        this.color = new Color(color.getRGB());
    }

    /**
     * @return the collision rectange
     */
    public Rectangle getCollisionRectangle() {
        return new Rectangle(this.collisionRectangle.getUpperLeft(), this.collisionRectangle.getDownRight());
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        // if (!ballColorMatch(hitter)) {
        this.notifyHit(hitter);
        // }
        double rightX = this.collisionRectangle.getDownRight().getX();
        double leftX = this.collisionRectangle.getUpperLeft().getX();
        double upperY = this.collisionRectangle.getUpperLeft().getY();
        double lowerY = this.collisionRectangle.getDownRight().getY();
        double dx = currentVelocity.getChangeX();
        double dy = currentVelocity.getChangeY();
        double reverseDx = -dx;
        double reverseDy = -dy;
        if (collisionPoint.getX() == rightX) {
            return new Velocity(reverseDx, dy);
        }
        if (collisionPoint.getX() == leftX) {
            return new Velocity(reverseDx, dy);
        }
        if (collisionPoint.getY() == upperY) {
            return new Velocity(dx, reverseDy);
        }
        if (collisionPoint.getY() == lowerY) {
            return new Velocity(dx, reverseDy);
        }
        return new Velocity(dx, dy);
    }

    public void drawOn(DrawSurface d) {
        double x = this.collisionRectangle.getUpperLeft().getX();
        double y = this.collisionRectangle.getUpperLeft().getY();
        double width = this.collisionRectangle.getWidth();
        double height = this.collisionRectangle.getHeight();
        d.setColor(this.color);
        d.fillRectangle((int) x, (int) y, (int) width, (int) height);
        d.setColor(Color.BLACK);
        d.drawRectangle((int) x, (int) y, (int) width, (int) height);
    }

    public void timePassed() {
        return;
    }

    public void addToGame(Game game) {
        game.addSprite(this);
        game.addCollidable(this);
    }

    public boolean ballColorMatch(Ball ball) {
        return this.color.equals(ball.getColor());
    }

    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
            if (!this.color.equals(Color.darkGray)) {
                hitter.setColor(this.color);
            }
        }
    }

    public Point getTopLeft() {
        return this.collisionRectangle.getUpperLeft();
    }

    public Point getBottomRight() {
        return this.collisionRectangle.getDownRight();
    }

    public Color getColor() {
        return this.color;
    }
}
