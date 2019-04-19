import java.awt.*;
import java.awt.geom.Point2D;

public class Kolo extends FiguraProstokatna {
    public Kolo(int centerX, int centerY, int radius) {
        super(centerX-radius,centerY-radius,2*radius,2*radius);
        addVertices();
    }

    public Kolo(Point p1, Point p2) {
        super(p1, p2);
        addVertices();
    }

    void addVertices(){
        vertices = new Point2D.Double[1];
        vertices[0] = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        g.fillOval((int)(vertices[0].getX()-bounds.getWidth()/2),(int)(vertices[0].getY()-bounds.getHeight()/2), (int)bounds.getWidth(), (int)bounds.getHeight());
    }

    @Override
    public String toString() {
        return super.toString()+bounds.toString();
    }
}
