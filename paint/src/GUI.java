import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {
    private final static int dragRadius = 15;
    private final static int closeCurveRadius = 20;
    MyJFrame myJFrame;
    ArrayList<Figura> figures;
    FiguraProstokatna selectedFigure = null;
    int selectedVertice = -1;
    ActionState action = ActionState.NOTHING;
    Point paintingPivot;
    ArrayList<Point> paintingPoints = new ArrayList<>();
    String selectedFigureName = "Prostokat";
    Color selectedColor = Color.DARK_GRAY;
    int prevX, prevY;

    public void startEditing(){
        if(action==ActionState.NOTHING || action==ActionState.CREATING)
            action = ActionState.EDITING;
        else if(action==ActionState.CREATING || action==ActionState.SELECTING_POINTS){
            action = ActionState.EDITING;
            figures.add(selectedFigure);
            selectedFigure = null;
            myJFrame.validate();
            myJFrame.repaint();
        }
    }

    public void startCreating(){
        if(action==ActionState.NOTHING || action==ActionState.EDITING)
            action = ActionState.CREATING;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(SwingUtilities.isRightMouseButton(e));
        System.out.println("Mouse pressed: "+x+","+y);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        if(action == ActionState.CREATING || action ==  ActionState.PAINTING || action ==  ActionState.SELECTING_POINTS){
            myJFrame.setCursor(Cursor.DEFAULT_CURSOR);
            return;
        }
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

                selectedFigure = temporaryFigure;
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
            ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
            if(paintingPivot.distance(paintingPoints.get(0))<=closeCurveRadius){
                action = ActionState.CREATING;
                figures.add(selectedFigure);
                selectedFigure = null;
            }
            myJFrame.validate();
            myJFrame.repaint();
        }
        else if(action==ActionState.EDITING){
            for(Figura figure : figures) {
                int nearest = figure.getNearestVertice(x, y, 20);
                if (nearest != -1) { //mamy wierzcholek
                    action = ActionState.RESIZING;
                    selectedVertice = nearest;
                    selectedFigure = (FiguraProstokatna)figure;
                    selectedFigure.moveVertice(x, y, selectedVertice);
                    break;
                } else if (figure.isPointInside(x, y)) {
                    action = ActionState.MOVING;
                    selectedFigure = (FiguraProstokatna)figure;
                    break;
                }
            }
        }


        myJFrame.validate();
        myJFrame.repaint();
        prevX = x;
        prevY = y;
        System.out.println("Mouse pressed:"+x+","+y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(action == ActionState.PAINTING){
            figures.add(selectedFigure);
            selectedFigure = null;
            myJFrame.validate();
            myJFrame.repaint();
        }
        else if(action == ActionState.MOVING){
            action = ActionState.EDITING;
            selectedFigure = null;
        }
        else if(action == ActionState.RESIZING){
            action = ActionState.EDITING;
            selectedFigure = null;
        }
        System.out.println("Mouse released:"+x+","+y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        boolean shouldRepaint = false;
        if(action==ActionState.PAINTING){
            Point p = new Point(x,y);
            selectedFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, p);
            selectedFigure.setColor(selectedColor);
        }
//        System.out.println("Mouse dragged:"+x+","+y);
//        mouseUsed(x,y);

        int dx = x - prevX;
        int dy = y - prevY;

        if(action == ActionState.RESIZING){
            selectedFigure.moveVertice(x, y, selectedVertice);
            shouldRepaint=true;
        }
        else if(action == ActionState.MOVING){
            selectedFigure.move(dx,dy);
            shouldRepaint=true;
        }

        if(shouldRepaint){
            myJFrame.validate();
            myJFrame.repaint();
        }
        prevX = x;
        prevY = y;
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

    @Override public void mouseEntered(MouseEvent e) {

    }
    @Override public void mouseExited(MouseEvent e) {

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
        if(selectedFigure !=null)
            selectedFigure.draw(g);
        for(Figura figure : figures){
            figure.draw(g);
        }
    }
}

class MyToolbar extends JPanel{
    MyJFrame myJFrame;
    public MyToolbar(MyJFrame myJFrame) {
        this.myJFrame = myJFrame;
        setLayout(new GridLayout(1,2));
        JButton b1 = new JButton("Tryb rysowania");
        JButton b2 = new JButton("Tryb edycji");

        b1.addActionListener(e -> {
            b2.setBackground(new Color(243,245, 247));
            b1.setBackground(new Color(202,204, 206));
            myJFrame.startCreating();
        });
        b2.addActionListener(e -> {
            b1.setBackground(new Color(243,245, 247));
            b2.setBackground(new Color(202,204, 206));
            myJFrame.startEditing();
        });
        add(b1);
        add(b2);
        setVisible(true);
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
    MyCanvas myCanvas;
    public void startCreating(){
        myCanvas.startCreating();
    }
    public void startEditing(){
        myCanvas.startEditing();
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

        getContentPane().add(new MyToolbar(this),BorderLayout.PAGE_START);
        myCanvas = new MyCanvas(figury,this);
        getContentPane().add(myCanvas);
        setVisible(true);
//        showInfo();
    }
}

public class GUI {
    public static void main(String[] args){
        JFrame window = new MyJFrame();
    }
}