import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;


class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {
    private final static int dragRadius = 15;
    private final static int closeCurveRadius = 20;
    MyJFrame myJFrame;
    ArrayList<Figura> figures;
    FiguraProstokatna activeFigure = null;
    int selectedVertice = -1;
    ActionState action = ActionState.NOTHING;
    Point paintingPivot;
    ArrayList<Point> paintingPoints = new ArrayList<>();
    String selectedFigureName = "Prostokat";
    Color selectedColor = Color.DARK_GRAY;
    int prevX, prevY;

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("Mouse pressed: "+x+","+y);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        Cursor cursor =  null;
        for(Figura figure : figures){
            int nearest = figure.getNearestVertice(x,y,dragRadius);
            if(nearest!=-1){ //mamy wierzcholek
                if(nearest==0)
                    cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
                else if(nearest==1)
                    cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
                else if(nearest==2)
                    cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
                else if(nearest==3)
                    cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
            }
            else if(figure.isPointInside(x,y)){
                cursor = new Cursor(Cursor.MOVE_CURSOR);
            }
            if(cursor!=null){
                break;
            }
        }
        if(cursor==null)
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        myJFrame.setCursor(cursor);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        paintingPivot = new Point(x,y);
        if(action==ActionState.CREATING){ //dopiero cos ma zostac narysowane

            FiguraProstokatna temporaryFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, paintingPivot);
            temporaryFigure.setColor(selectedColor);
            if(temporaryFigure.getDrawingType()==DrawingType.POINT){
                action = ActionState.SELECTING_POINTS;

                activeFigure = temporaryFigure;
                paintingPoints = new ArrayList<>();
                paintingPoints.add(paintingPivot);
            }
            else
                action = ActionState.PAINTING;

        }
        else if(action==ActionState.PAINTING){ //aktualnie rysujemy figure
            paintingPivot = new Point(x,y);
        }
        else if(action==ActionState.SELECTING_POINTS){ //aktualnie wybieramy punkty
            paintingPoints.add(paintingPivot);
            ((Wielokat) activeFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
            if(paintingPivot.distance(paintingPoints.get(0))<=closeCurveRadius){
                action = ActionState.CREATING;
                figures.add(activeFigure);
                activeFigure = null;
            }
            myJFrame.validate();
            myJFrame.repaint();
        }


        prevX = x;
        prevY = y;
        System.out.println("Mouse pressed:"+x+","+y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(action==ActionState.PAINTING){
            figures.add(activeFigure);
            activeFigure = null;
            myJFrame.validate();
            myJFrame.repaint();
        }
        System.out.println("Mouse released:"+x+","+y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(action==ActionState.PAINTING){
            Point p = new Point(x,y);
            activeFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, p);
            activeFigure.setColor(selectedColor);
        }
//        System.out.println("Mouse dragged:"+x+","+y);
        mouseUsed(x,y);
    }

    void mouseUsed(int x, int y) {
        if (prevX == -1 || prevY == -1) {
            prevX = x;
            prevY = y;
        }
        int dx = x - prevX;
        int dy = y - prevY;

        for(Figura figure : figures) {
            int nearest = figure.getNearestVertice(x, y, 20);
            if (nearest != -1) { //mamy wierzcholek
                figure.moveVertice(x, y, nearest);
            } else if (figure.isPointInside(x, y)) {
                figure.move(dx, dy);
            }
            myJFrame.validate();
            myJFrame.repaint();
            prevX = x;
            prevY = y;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public MyCanvas(Figura[] figury, MyJFrame myJFrame){
        action = ActionState.CREATING;
        selectedFigureName = "Wielokat";
        selectedColor = Color.GREEN;
        prevX = -1;
        prevY = -1;
        this.myJFrame = myJFrame;
        addMouseListener(this);
        addMouseMotionListener(this);
        figures = new ArrayList<>(figury.length);
        for(int i=0; i<figury.length; i++){
            figures.add(figury[i]);

        }
    }
    public void paintComponent(Graphics g) {
        if(activeFigure !=null)
            activeFigure.draw(g);
        for(Figura figure : figures){
            figure.draw(g);
        }
    }
}


//class MyListener extends MouseInputAdapter {
//    public void mousePressed(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        System.out.println("Mouse pressed: "+x+","+y);
//    }
//
//    public void mouseDragged(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        System.out.println("Mouse dragged:"+x+","+y);
//    }
//
//    public void mouseReleased(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        System.out.println("Mouse released:"+x+","+y);
//    }
//
//    public void mouseMoved(MouseEvent e){
//        int x = e.getX();
//        int y = e.getY();
//        System.out.println("Mouse moved:"+x+","+y);
//    }
//}

/*--------------------------------------------------------------------*/
//GLOWNA KLATKA
class MyJFrame extends JFrame{
    void showInfo(){
        JDialog d = new JDialog(this, "Informacje", true );
        d.setLayout(new GridLayout(3,2,8,8));
        JLabel lab1 = new JLabel("Nazwa programu: FiGURU", SwingConstants.CENTER);
        JLabel lab2 = new JLabel("Autor: Michał Janik", SwingConstants.CENTER);
        JLabel lab3 = new JLabel("<html>Przeznaczenie: Rysowanie figur geometrycznych, ich skalowanie i przesuwanie. <br>Możliwość zapisu/odczytu z pliku.</html>", SwingConstants.CENTER);

        lab1.setVerticalAlignment(SwingConstants.CENTER);
        lab2.setVerticalAlignment(SwingConstants.CENTER);
        lab3.setVerticalAlignment(SwingConstants.CENTER);
        lab1.setBorder(BorderFactory.createLineBorder(Color.black));
        lab2.setBorder(BorderFactory.createLineBorder(Color.black));
        lab3.setBorder(BorderFactory.createLineBorder(Color.black));
        d.add(lab1);
        d.add(lab2);
        d.add(lab3);
        d.setSize(600, 300);
        d.show();
    }
    public MyJFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("FiGURU");
        FiguraProstokatna[] figury = new FiguraProstokatna[4];
        figury[0] = new Prostokat(100, 200, 200, 150);
        figury[2] = new Kolo(300, 200, 100);
//        System.out.println(figury[2].toJson());
//        System.out.println(figury[0]);
//        System.out.println(figury[2]);
////        figury[0].fromJson(figury[2].toJson());
//        figury[2].setColor(Color.PINK);
//        figury[0].setColor(Color.BLUE);
//
//        JSONObject dataJObject;
//        Gson gson = new Gson();
//        String json = gson.toJson(figury[2]);
//
//        dataJObject = new JSONObject(json);
//        System.out.println(dataJObject);
//        figury[0] = gson.fromJson(dataJObject.toString(), Kolo.class );
//        figury[0].move(100,100);

        System.out.println(figury[0]);
        System.out.println(figury[2]);

        figury[3] = new Kolo(300, 500, 100);
        figury[3].setColor(Color.RED);
//        figury[1] = new Kolo(400, 400, 50);
        Point[] punkty = new Point[3];
        punkty[0] = new Point(400,300);
        punkty[1] = new Point(500,300);
        punkty[2] = new Point(450,400);
//        figury[1] = new Wielokat(punkty);
        figury[1] = new Kolo(600, 500, 100);
        figury[1].setColor(Color.GREEN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(30, 30, 800, 600);
        getContentPane().add(new MyCanvas(figury,this));
        setVisible(true);
        showInfo();
    }
}

public class GUI {
    public static void main(String[] args){
        JFrame window = new MyJFrame();
    }
}