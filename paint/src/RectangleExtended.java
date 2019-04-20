import java.awt.*;

public class RectangleExtended extends Rectangle {
    public RectangleExtended(Point p1, Point p2){
        //ustawiamy lewy gorny
        super();
        if(p1 == null || p2 == null)
            return;

        int minX, maxX, minY, maxY;

        minX = Integer.min((int)p1.getX(), (int)p2.getX());
        maxX = Integer.max((int)p1.getX(), (int)p2.getX());
        minY = Integer.min((int)p1.getY(), (int)p2.getY());
        maxY = Integer.max((int)p1.getY(), (int)p2.getY());

        int width = maxX - minX;
        int height = maxY - minY;

        setSize(width, height);
        setLocation(minX, minY);
    }

    public RectangleExtended(int x, int y, int width, int height){
        super(x,y,width,height);
    }
    public RectangleExtended(){
        super();
    }

    public Point[] getVertices(){
        Point[] vertices = new Point[4];
        vertices[0] = new Point((int)getX(), (int)getY());
        vertices[1] = new Point((int)getX() + (int)getWidth(), (int)getY());
        vertices[2] = new Point((int)getX() , (int)getY() + (int)getHeight());
        vertices[3] = new Point((int)getX() + (int)getWidth(), (int)getY() + (int)getHeight());
        return vertices;
    }
    public Point getLowerRight(){
        Point p = new Point((int)getX()+(int)getWidth(),(int)getY()+(int)getHeight());
        return p;
    }
}
