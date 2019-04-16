import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Figura {
    public abstract void move(int dx, int dy);
    public abstract void moveVertice(int dx, int dy, int index);
    public abstract void draw(Graphics g);
    public abstract boolean isPointInside(int x, int y);
    public abstract int getNearestVertice(int x, int y, int radius);

    //protected Point2D.Double[] vertices;
}
