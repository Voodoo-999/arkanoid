import java.awt.Color;
import biuoop.DrawSurface;

public class DrawRectangle implements Drawable{
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    public DrawRectangle(int x, int y, int width, int height, Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
	@Override
	public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.drawRectangle(this.x, this.y, this.width, this.height);
	}
    
}
