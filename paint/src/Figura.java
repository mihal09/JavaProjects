import java.awt.*;

public interface Figura {
    void move(int dx, int dy);
    void moveVertice(int dx, int dy, int index);

    void draw(Graphics g);
    void setColor(Color c);

    boolean isPointInside(int x, int y);
    int getNearestVertice(int x, int y, int radius);

    DrawingType getDrawingType();
}
