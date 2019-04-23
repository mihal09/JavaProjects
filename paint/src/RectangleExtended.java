import java.awt.*;

/**
 * klasa rozszerzająca klasę Rectangle
 * @see Rectangle
 */
public class RectangleExtended extends Rectangle {
    /**
     * konstruktor z podaniem dwóch rogow
     * @param p1 rog 1
     * @param p2 rog 2
     */
    RectangleExtended(Point p1, Point p2){
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

    /**
     * konstruktor z podaniem wspolrzednych lewego gornego rogu, szerokosci i dlugosci
     * @param x wspolrzedna x lewego gornego rogu
     * @param y wspolrzedna y lewego gornego rogu
     * @param width szerokosc
     * @param height wysokosc
     */
    RectangleExtended(int x, int y, int width, int height){
        super(x,y,width,height);
    }
    RectangleExtended(){
        super();
    }

    /**
     * zwraca listę wierzchołków tego prostokąta
     * @return lista wierchołków
     */
    public Point[] getVertices(){
        Point[] vertices = new Point[4];
        vertices[0] = new Point((int)getX(), (int)getY());
        vertices[1] = new Point((int)getX() + (int)getWidth(), (int)getY());
        vertices[2] = new Point((int)getX() , (int)getY() + (int)getHeight());
        vertices[3] = new Point((int)getX() + (int)getWidth(), (int)getY() + (int)getHeight());
        return vertices;
    }

    /**
     * zwraca współrzędne prawego dolnego wiercholka
     * @return współrzędne prawego dolnego wiercholka
     */
    public Point getLowerRight(){
        return new Point((int)getX()+(int)getWidth(),(int)getY()+(int)getHeight());
    }
}
