import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.omg.CORBA.Bounds;

import java.awt.*;
import java.awt.geom.Point2D;


public abstract class FiguraProstokatna implements Figura {
    protected RectangleExtended bounds;
    protected Point2D.Double[] vertices;
    protected Color color = Color.darkGray;

    private final static int minHeight=50, minWidth=50;

    public void move(int dx, int dy){
        //przesuwamy ramkę
        bounds.setLocation((int)bounds.getX()+dx,(int)bounds.getY()+dy);
        //przesuwamy punkty
        if(vertices!=null) {
            for (int i = 0; i < vertices.length; i++) {
                vertices[i].setLocation(vertices[i].getX() + dx, vertices[i].getY() + dy);
            }
        }
    }
    public int getNearestVertice(int x, int y, int radius){
        double minDistance = Double.MAX_VALUE;
        int bestIndex = -1;
        Point[] boundsVertices = bounds.getVertices();
        for(int i=0; i<boundsVertices.length; i++){
            double distance = boundsVertices[i].distance(x,y);
            if(distance <= radius && distance < minDistance){
                bestIndex = i;
                minDistance = distance;
            }
        }
        return bestIndex;
    }
    public void moveVertice(int x, int y, int index) {
        Point[] boundsVertices = bounds.getVertices();
        Point selected = new Point(boundsVertices[index]);
        Point selectedChanged = new Point(x,y);
        Point pivot = new Point(boundsVertices[3-index]);
        double signumSelectedX = Math.signum(selected.getX()-pivot.getX());
        double signumSelectedY = Math.signum(selected.getY()-pivot.getY());

        int newWidth = (int)((selectedChanged.getX() - pivot.getX())*signumSelectedX);
        newWidth = Integer.max(newWidth, minWidth);
        newWidth *= signumSelectedX;
        int newHeight = (int)((selectedChanged.getY() - pivot.getY())*signumSelectedY);
        newHeight = Integer.max(newHeight, minHeight);
        newHeight *= signumSelectedY;
        selectedChanged.setLocation(pivot.getX()+newWidth, pivot.getY()+newHeight);

        //przeskalowanie punktow
        if(vertices!=null) {
            for (int i = 0; i < vertices.length; i++) {
                double xPercentage = signumSelectedX*(vertices[i].getX() - pivot.getX()) / bounds.getWidth();
                double yPercentage = signumSelectedY*(vertices[i].getY() - pivot.getY()) / bounds.getHeight();

                double newX = xPercentage * newWidth + pivot.getX() ;
                double newY = yPercentage * newHeight + pivot.getY();

                vertices[i].setLocation(newX, newY);
            }
        }
        bounds = new RectangleExtended(pivot, selectedChanged);
    }

    public void setColor(Color color) {
            this.color = color;
    }
    public Color getColor(){
        return this.color;
    }
    public DrawingType getDrawingType() {
        return DrawingType.RECTANGLE;
    }


    public FiguraProstokatna(Point p1, Point p2){
        bounds = new RectangleExtended(p1, p2);
    }
    public FiguraProstokatna(int x, int y, int width, int height){
        bounds = new RectangleExtended(x,y,width,height);
    }
    public FiguraProstokatna(){
        bounds = new RectangleExtended();
        setColor(null);
    }

    public boolean isPointInside(int x, int y){
        return bounds.contains(x,y);
    }
}
