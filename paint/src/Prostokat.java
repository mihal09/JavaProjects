import java.awt.*;
import java.awt.geom.Point2D;

/**
 * figura Prostokąt rozszerzająca FiguraProstokatna
 * @see FiguraProstokatna
 */
public class Prostokat extends FiguraProstokatna {
    /**
     * tworzy prostokąt o danych dwóch wierzchołkach
     * @param p1 wierzchołek pierwszy
     * @param p2 wierzchołek drugi
     */
    public Prostokat(Point p1, Point p2) {
        super(p1, p2);
        addVertices();
    }

    /**
     * tworzy prostokąt z podaniem wspolrzednych lewego gornego rogu, szerokosci i dlugosci
     * @param x wspolrzedna x lewego gornego rogu
     * @param y wspolrzedna y lewego gornego rogu
     * @param width szerokosc
      @param height wysokosc
     */
    public Prostokat(int x, int y, int width, int height) {
        super(x,y,width,height);
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
        g2D.fillRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());

        if(paintBorders) {
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
            g2D.setStroke(dashed);
            g2D.setColor(new Color(0, 0, 0, 48));
            g2D.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        }
        g2D.dispose();
    }

}
