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
            stopDrawing();
        }
    }
    public void startCreating(){
        if(action==ActionState.NOTHING || action==ActionState.EDITING)
            action = ActionState.CREATING;
    }
    public void selectFigureName(String selectedFigureName){
        this.selectedFigureName = selectedFigureName;
        action = ActionState.CREATING;
        stopDrawing();
    }
    private void stopDrawing(){
        if(selectedFigure!=null)
            figures.add(selectedFigure);
        selectedFigure = null;
        myJFrame.validate();
        myJFrame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //jesli klikniety prawy przycisk myszy
        System.out.println("Mouse clicked: "+x+","+y);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        if(action == ActionState.CREATING){
            myJFrame.setCursor(Cursor.DEFAULT_CURSOR);
            return;
        }
        else if(action == ActionState.SELECTING_POINTS || action ==  ActionState.PAINTING){
            myJFrame.setCursor(Cursor.CROSSHAIR_CURSOR);
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
            Debug();
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
            if(paintingPivot.distance(paintingPoints.get(0))<=closeCurveRadius){
                action = ActionState.CREATING;
                ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
                stopDrawing();
            }
            else {
                paintingPoints.add(paintingPivot);
                ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
            }

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

        if(SwingUtilities.isRightMouseButton(e) && (action == ActionState.MOVING || action == ActionState.RESIZING)){
            if(selectedFigure!=null)
                selectedFigure.setColor(JColorChooser.showDialog(this, "Wybierz kolor", Color.DARK_GRAY));
            myJFrame.repaint();
        }

        if(action == ActionState.PAINTING){
            action = ActionState.CREATING;
            stopDrawing();
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

    void Debug(){
        System.out.println(action +":"+selectedFigureName);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        Debug();
        int x = e.getX();
        int y = e.getY();

        boolean shouldRepaint = false;
        if(action==ActionState.PAINTING){
            myJFrame.setCursor(Cursor.CROSSHAIR_CURSOR);
            Point p = new Point(x,y);
            selectedFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, p);
            selectedFigure.setColor(selectedColor);
            shouldRepaint=true;
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
        Debug();
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
    MyCanvas myCanvas;
    public MyToolbar(MyJFrame myJFrame, MyCanvas myCanvas) {
        this.myJFrame = myJFrame;
        this.myCanvas = myCanvas;
        setLayout(new GridLayout(2,1));
        JButton bDraw = new JButton("Tryb rysowania");
        JButton bEdit = new JButton("Tryb edycji");
        JButton bInfo = new JButton("Info");
        JButton bProstokat = new JButton("Prostokat");
        JButton bWielokat = new JButton("Wielokat");
        JButton bKolo = new JButton("Kolo");
        JPanel buttonsPanel = new JPanel();
        JPanel figuresPanel = new JPanel();
        figuresPanel.setVisible(false);


        bDraw.addActionListener(e -> {
            bEdit.setBackground(new Color(243,245, 247));
            bDraw.setBackground(new Color(202,204, 206));
            figuresPanel.setVisible(true);
            myJFrame.validate();
            myCanvas.startCreating();
        });
        bEdit.addActionListener(e -> {
            bDraw.setBackground(new Color(243,245, 247));
            bEdit.setBackground(new Color(202,204, 206));
            figuresPanel.setVisible(false);
            myJFrame.validate();
            myCanvas.startEditing();
        });
        bInfo.addActionListener(e -> {
           myJFrame.showInfo();
        });
        bProstokat.addActionListener(e -> {
            myCanvas.selectFigureName("Prostokat");
            bKolo.setBackground(new Color(243,245, 247));
            bWielokat.setBackground(new Color(243,245, 247));
            bProstokat.setBackground(new Color(243,245, 247));

            bProstokat.setBackground(new Color(202,204, 206));
        });
        bKolo.addActionListener(e -> {
            myCanvas.selectFigureName("Kolo");
            bKolo.setBackground(new Color(243,245, 247));
            bWielokat.setBackground(new Color(243,245, 247));
            bProstokat.setBackground(new Color(243,245, 247));

            bKolo.setBackground(new Color(202,204, 206));
        });
        bWielokat.addActionListener(e -> {
            myCanvas.selectFigureName("Wielokat");
            bKolo.setBackground(new Color(243,245, 247));
            bWielokat.setBackground(new Color(243,245, 247));
            bProstokat.setBackground(new Color(243,245, 247));

            bWielokat.setBackground(new Color(202,204, 206));
        });


        figuresPanel.add(bProstokat);
        figuresPanel.add(bWielokat);
        figuresPanel.add(bKolo);

        buttonsPanel.add(bDraw);
        buttonsPanel.add(bEdit);
        buttonsPanel.add(bInfo);
        add(buttonsPanel);
        add(figuresPanel);
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
    public void showInfo(){
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

        myCanvas = new MyCanvas(figury,this);
        getContentPane().add(new MyToolbar(this, myCanvas),BorderLayout.PAGE_START);
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