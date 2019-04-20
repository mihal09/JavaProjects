import java.awt.*;
import java.awt.geom.Point2D;

public class Prostokat extends FiguraProstokatna {
    public Prostokat(Point p1, Point p2) {
        super(p1, p2);
        addVertices();
    }

    public Prostokat(int x, int y, int width, int height) {
        super(x,y,width,height);
        addVertices();
    }
    void addVertices(){
        vertices = new Point2D.Double[1];
        vertices[0] = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setColor(getColor());
        g2D.fillRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());

        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
        g2D.setStroke(dashed);
        g2D.setColor(new Color(0,0,0, 48));
        g2D.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        g2D.dispose();
//        int r = 20;
//        g.drawOval((int)vertices[0].getX()-r,(int)vertices[0].getY()-r, 2*r, 2*r);
    }

    @Override
    public String toString() {
        return super.toString()+bounds.toString();
    }
}
