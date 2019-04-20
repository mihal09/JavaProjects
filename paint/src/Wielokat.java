import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Wielokat extends FiguraProstokatna {
    transient GeneralPath path;
    int xOffset, yOffset;
    double scaleX, scaleY;
    ArrayList<Point> dziwnePunkty = new ArrayList<>();
    public Wielokat(Point[] points) {
        super();
        addVertices(points);
    }
    public void addVertices(){
        System.out.println(getColor());
        if(dziwnePunkty.isEmpty())
            System.out.println("DZIWNE PUNKTY JEST EMPTY");
        else{
            System.out.println("DZIWNE PUNKTY NIE JEST NULL "+ dziwnePunkty.size());
            addVertices(dziwnePunkty.toArray(new Point[dziwnePunkty.size()]));
        }
    }
    public void addVertices(Point[] points){
        System.out.println("PUNKTY: "+points);
        if(points==null)
            return;

        vertices = new Point2D.Double[points.length];
        dziwnePunkty.clear();
        for (int i = 0; i < points.length; i++) {
            vertices[i] =  new Point2D.Double(points[i].getX(),points[i].getY());
            dziwnePunkty.add(points[i]);
        }
        System.out.println("XD: "+points[0]);
        xOffset = yOffset = 0;
        scaleX = scaleY = 1.0;
        path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.length);

        path.moveTo(points[0].getX(), points[0].getY());
        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i].getX(), points[i].getY());
        }
        path.closePath();
        bounds.setBounds(path.getBounds());
    }
    public void addVertices(Point2D.Double[] pointsDouble){
        if(pointsDouble==null)
            return;
        Point[] points = new Point[pointsDouble.length];
        for (int i = 0; i < pointsDouble.length; i++) {
            points[i] =  new Point((int)pointsDouble[i].getX(),(int)pointsDouble[i].getY());
        }
        addVertices(points);
    }

    @Override
    public void move(int dx, int dy){
        super.move(dx,dy);
        xOffset += dx;
        yOffset += dy;
    }

    public void moveVertice(int x, int y, int index) {
        int oldWidth = (int) bounds.getWidth();
        int oldHeight = (int) bounds.getHeight();
        int oldX = (int) bounds.getX();
        int oldY = (int) bounds.getY();
        super.moveVertice(x, y, index);

        double newWidth = bounds.getWidth();
        double newHeight =  bounds.getHeight();
        int newX = (int) bounds.getX();
        int newY = (int) bounds.getY();

        xOffset += newX-oldX;
        yOffset += newY-oldY;
        scaleX *= newWidth/oldWidth;
        scaleY *= newHeight/oldHeight;
        System.out.println(scaleX +":"+ scaleY);
    }

    @Override
    public DrawingType getDrawingType() {
        return DrawingType.POINT;
    }


    @Override
    public void draw(Graphics g) {
        if(path==null)
            return;
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setColor(getColor());
        AffineTransform initialTransform = g2D.getTransform();
//        g.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        final double centerX = bounds.getX();
        final double centerY = bounds.getY();
        g2D.translate(centerX,centerY);
        g2D.scale(scaleX, scaleY);
        g2D.translate(-centerX,-centerY);
        g2D.translate(xOffset, yOffset);

        g2D.draw(path);
        g2D.fill(path);

//        g2D.fillOval((int)path.getBounds().getCenterX(),(int)path.getBounds().getCenterY(), 20, 20);
        g2D.setTransform(initialTransform);
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
        g2D.setStroke(dashed);
        g2D.setColor(new Color(0,0,0, 48));
        g2D.drawRect( (int)bounds.getX(),(int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        g2D.dispose();
    }
}
