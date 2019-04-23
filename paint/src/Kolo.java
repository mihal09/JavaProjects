import java.awt.*;
import java.awt.geom.Point2D;

/**
 * figura Koło rozszerzająca FiguraProstokatna
 * @see FiguraProstokatna
 */
public class Kolo extends FiguraProstokatna {
    /**
     * tworzy Koło z podaniem współrzędnych jego środka i promienia
     * @param centerX współrzędna x środka
     * @param centerY współrzędna y środka
     * @param radius promień
     */
    public Kolo(int centerX, int centerY, int radius) {
        super(centerX-radius,centerY-radius,2*radius,2*radius);
        addVertices();
    }

    /**
     * tworzy Koło o danych dwóch rogach
     * @param p1 wierzchołek pierwszy
     * @param p2 wierzchołek drugi
     */
    public Kolo(Point p1, Point p2) {
        super(p1, p2);
        addVertices();
    }

    public void addVertices(){
        vertices = new Point2D.Double[1];
        vertices[0] = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setColor(getColor());
//        g2D.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        g2D.fillOval((int)(vertices[0].getX()-bounds.getWidth()/2),(int)(vertices[0].getY()-bounds.getHeight()/2), (int)bounds.getWidth(), (int)bounds.getHeight());

        if(paintBorders) {
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
            g2D.setStroke(dashed);
            g2D.setColor(new Color(0, 0, 0, 48));
            g2D.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        }
        g2D.dispose();
    }

}
