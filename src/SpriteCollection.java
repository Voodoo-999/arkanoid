import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;

/**
 * SpriteCollection.
 */
public class SpriteCollection {
    private ArrayList<Sprite> sprites;

    /**
     * construnctor for new sprite collection.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<Sprite>();
    }

    /**
     * @param s a sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * @param s its the sprite to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }

    /**
     * notify all the spites in collection that time passed.
     */
    public void notifyAllTimePassed() {
        List<Sprite> sprites = new ArrayList<Sprite>(this.sprites);
        for (Sprite sprite : sprites) {
            sprite.timePassed();
        }
    }

    /**
     * @param d a drawsurface from buioop to draw the sprites to
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : sprites) {
            sprite.drawOn(d);
        }
    }
}
