import java.awt.*;
import java.awt.geom.Point2D;


abstract class FiguraProstokatna implements Figura {
    static boolean paintBorders = true;
    RectangleExtended bounds;
    Point2D.Double[] vertices;
    private Color color = Color.darkGray;

    private final static int minHeight=20, minWidth=20;

    public void move(int dx, int dy){
        //przesuwamy ramkę
        bounds.setLocation((int)bounds.getX()+dx,(int)bounds.getY()+dy);
        //przesuwamy punkty
        if(vertices!=null) {
            for (Point2D.Double vertex : vertices) {
                vertex.setLocation(vertex.getX() + dx, vertex.getY() + dy);
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
            for (Point2D.Double vertex : vertices) {
                double xPercentage = signumSelectedX * (vertex.getX() - pivot.getX()) / bounds.getWidth();
                double yPercentage = signumSelectedY * (vertex.getY() - pivot.getY()) / bounds.getHeight();

                double newX = xPercentage * newWidth + pivot.getX();
                double newY = yPercentage * newHeight + pivot.getY();

                vertex.setLocation(newX, newY);
            }
        }
        bounds = new RectangleExtended(pivot, selectedChanged);
    }

    public void setColor(Color color) {
            this.color = color;
    }
    Color getColor(){
        return this.color;
    }
    public DrawingType getDrawingType() {
        return DrawingType.RECTANGLE;
    }


    FiguraProstokatna(Point p1, Point p2){
        bounds = new RectangleExtended(p1, p2);
    }
    FiguraProstokatna(int x, int y, int width, int height){
        bounds = new RectangleExtended(x,y,width,height);
    }
    FiguraProstokatna(){
        bounds = new RectangleExtended();
        setColor(null);
    }

    /**
     * odswieza dane o punktach
     */
    public void addVertices(){}
    public boolean isPointInside(int x, int y){
        return bounds.contains(x,y);
    }
    @Override public String toString(){
        return "szerokość: "+bounds.getWidth()+", wysokość: "+bounds.getHeight()+"; x: "+bounds.getCenterX()+" y: "+bounds.getCenterY();
    }
}
