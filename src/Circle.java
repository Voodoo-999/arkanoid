import java.awt.Color;
import biuoop.DrawSurface;

public class Circle implements Drawable{
    private int x;
    private int y;
    private int radius;
    private Color color;
    public Circle(int x, int y, int radius, Color color){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }
	@Override
	public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.drawCircle(this.x, this.y, this.radius);
	}
    
}
