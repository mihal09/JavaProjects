import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Wielokat extends FiguraProstokatna {
    GeneralPath path;
    public Wielokat(Point[] points) {
        super();
        addVertices(points);
    }
    void addVertices(Point[] points){
        path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.length);

        path.moveTo(points[0].getX(), points[0].getY());
        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i].getX(), points[i].getY());
        }
        path.closePath();
        bounds.setBounds(path.getBounds());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.fill(path);
        g.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        g2D.draw(path);
    }
}
