import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


/*--------------------------------------------------------------------*/
//PLOTNO
class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {
    private final static int dragRadius = 15;
    private final static int closeCurveRadius = 20;
    MyJFrame myJFrame;
    ArrayList<Figura> figures;
    FiguraProstokatna selectedFigure = null;
    int selectedVertice = -1;
    private ActionState action = ActionState.NOTHING;
    Point paintingPivot;
    ArrayList<Point> paintingPoints = new ArrayList<>();
    String selectedFigureName = "Prostokat";
    private Color selectedColor = Color.DARK_GRAY;
    int prevX, prevY;

    private void setFigures(Figura[] figuresArray){
        figures.clear();
        if(figuresArray==null)
            return;
        for(Figura figure : figuresArray){
            figures.add(figure);
        }
    }
    private Figura[] getFigures(){
        return figures.toArray(new Figura[figures.size()]);
    }
    private File selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new MyFileFilter());
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        }
        return null;
    }
    public void showSavePanel(){
        File file = selectFile();
        if(file!=null) {
            setAction(ActionState.EDITING);
            FigureManager.save(file,getFigures());
            myJFrame.repaint();
        }
    }
    public void showLoadPanel(){
        File file = selectFile();
        if(file!=null) {
            setAction(ActionState.EDITING);
            setFigures(FigureManager.load(file));
            selectedFigure = null;
            myJFrame.repaint();
        }
    }
    public void startEditing(){
        if(getAction() == ActionState.NOTHING || getAction() == ActionState.CREATING)
            setAction(ActionState.EDITING);
        else if(getAction() == ActionState.CREATING || getAction() == ActionState.SELECTING_POINTS){
            stopDrawing(ActionState.EDITING);
        }
    }
    public void startCreating(){
        if(getAction() ==ActionState.NOTHING || getAction() ==ActionState.EDITING)
            setAction(ActionState.CREATING);
    }
    public void selectFigureName(String selectedFigureName){
        this.selectedFigureName = selectedFigureName;
        stopDrawing(ActionState.CREATING);
    }
    private void stopDrawing(ActionState newAction){
        if(newAction!=null)
            setAction(newAction);
        if(selectedFigure!=null)
            figures.add(selectedFigure);
        selectedFigure = null;
        myJFrame.validate();
        myJFrame.repaint();
    }

    @Override public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //jesli klikniety prawy przycisk myszy
       // System.out.println("Mouse clicked: "+x+","+y);
    }
    @Override public void mouseMoved(MouseEvent e){
        if(getAction() == ActionState.CREATING){
            myJFrame.setCursor(Cursor.DEFAULT_CURSOR);
            return;
        }
        else if(getAction() == ActionState.SELECTING_POINTS || getAction() ==  ActionState.PAINTING){
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
    @Override public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(!SwingUtilities.isRightMouseButton(e)) {
            paintingPivot = new Point(x, y);
            if (getAction() == ActionState.CREATING) { //dopiero cos ma zostac narysowane
                Debug();
                FiguraProstokatna temporaryFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, paintingPivot);
                temporaryFigure.setColor(getSelectedColor());
                if (temporaryFigure.getDrawingType() == DrawingType.POINT) {
                    setAction(ActionState.SELECTING_POINTS);

                    selectedFigure = temporaryFigure;
                    paintingPoints = new ArrayList<>();
                    paintingPoints.add(paintingPivot);
                } else
                    setAction(ActionState.PAINTING);
            } else if (getAction() == ActionState.PAINTING) { //aktualnie rysujemy figure
                paintingPivot = new Point(x, y);
            } else if (getAction() == ActionState.SELECTING_POINTS) { //aktualnie wybieramy punkty
                if (paintingPivot.distance(paintingPoints.get(0)) <= closeCurveRadius) {
                    ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
                    stopDrawing(ActionState.EDITING);
                } else {
                    paintingPoints.add(paintingPivot);
                    ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[paintingPoints.size()]));
                }

            } else if (getAction() == ActionState.EDITING) {
                for (Figura figure : figures) {
                    int nearest = figure.getNearestVertice(x, y, 20);
                    if (nearest != -1) { //mamy wierzcholek
                        setAction(ActionState.RESIZING);
                        selectedVertice = nearest;
                        selectedFigure = (FiguraProstokatna) figure;
                        selectedFigure.moveVertice(x, y, selectedVertice);
                        break;
                    } else if (figure.isPointInside(x, y)) {
                        setAction(ActionState.MOVING);
                        selectedFigure = (FiguraProstokatna) figure;
                        break;
                    }
                }
            }
        }
        else{ //prawy przycisk myszy klikniety zaznacza figurę
            if(getAction() == ActionState.EDITING){
                for (Figura figure : figures) {
                    if (figure.isPointInside(x, y)) {
                        selectedFigure = (FiguraProstokatna) figure;
                        break;
                    }
                }
            }
        }


        myJFrame.validate();
        myJFrame.repaint();
        prevX = x;
        prevY = y;
        System.out.println("Mouse pressed:"+x+","+y);
    }
    @Override public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if(SwingUtilities.isRightMouseButton(e) && (getAction() == ActionState.MOVING || getAction() == ActionState.RESIZING || getAction() == ActionState.EDITING)){
            if(selectedFigure!=null){
                Color color = JColorChooser.showDialog(this, "Wybierz kolor", selectedFigure.getColor());
                if(color!=null)
                    selectedFigure.setColor(color);
                action = ActionState.EDITING;
            }
            myJFrame.repaint();
        }

        if(!SwingUtilities.isRightMouseButton(e)) {
            if (getAction() == ActionState.PAINTING) {
                stopDrawing(ActionState.EDITING);
            } else if (getAction() == ActionState.MOVING) {
                setAction(ActionState.EDITING);
//                selectedFigure = null;
            } else if (getAction() == ActionState.RESIZING) {
                setAction(ActionState.EDITING);
//                selectedFigure = null;
            }
        }
        System.out.println("Mouse released:"+x+","+y);
    }
    @Override public void mouseDragged(MouseEvent e) {
        Debug();
        int x = e.getX();
        int y = e.getY();

        boolean shouldRepaint = false;
        if(getAction() ==ActionState.PAINTING){
            myJFrame.setCursor(Cursor.CROSSHAIR_CURSOR);
            Point p = new Point(x,y);
            selectedFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, p);
            selectedFigure.setColor(getSelectedColor());
            shouldRepaint=true;
        }
//        System.out.println("Mouse dragged:"+x+","+y);
//        mouseUsed(x,y);

        int dx = x - prevX;
        int dy = y - prevY;

        if(getAction() == ActionState.RESIZING){
            selectedFigure.moveVertice(x, y, selectedVertice);
            shouldRepaint=true;
        }
        else if(getAction() == ActionState.MOVING){
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
    @Override public void mouseEntered(MouseEvent e) {
        Debug();
    }
    @Override public void mouseExited(MouseEvent e) {

    }

    void Debug(){
        System.out.println(getAction());
    }

    public MyCanvas(Figura[] figury, MyJFrame myJFrame){
        setAction(ActionState.CREATING);
        selectedFigureName = "Wielokat";
        setSelectedColor(Color.BLACK);
        this.myJFrame = myJFrame;
        addMouseListener(this);
        addMouseMotionListener(this);
        prevX = prevY = -1;
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

    protected ActionState getAction() {
        return action;
    }

    protected void setAction(ActionState action) {
        this.action = action;
        try {
            if (action == ActionState.EDITING || action == ActionState.RESIZING || action == ActionState.MOVING) {
                myJFrame.myToolbar.selectMode(RunningMode.EDITITNG);
            } else if (action == ActionState.CREATING || action == ActionState.PAINTING || action == ActionState.SELECTING_POINTS) {
                myJFrame.myToolbar.selectMode(RunningMode.CREATING);
            }
        }
        catch(Exception e){}
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        if(action==ActionState.EDITING){
            if(selectedFigure!=null) {
                selectedFigure.setColor(selectedColor);
            }
        }
    }
}

/*--------------------------------------------------------------------*/
//GORNY PANEL
class MyToolbar extends JPanel{
    MyJFrame myJFrame;
    MyCanvas myCanvas;
    JButton bDraw, bEdit, bInfo, bProstokat, bWielokat, bKolo, bSave, bLoad, bColor;
    JPanel topPanel, bottomPanel, filePanel, buttonsPanel,  figuresPanel;
    private final static Color
            backgroundColor = new Color(220, 220, 220),
            activeColor = new Color(192,194, 196),
            unactiveColor = new Color(243,245, 247);
    public void selectMode(RunningMode mode){
        bEdit.setBackground(unactiveColor);
        bDraw.setBackground(unactiveColor);
        switch(mode){
            case CREATING:{
                bDraw.setBackground(activeColor);
                figuresPanel.setVisible(true);
                break;
            }
            case EDITITNG:{
                bEdit.setBackground(activeColor);
                figuresPanel.setVisible(false);
                break;
            }
        }

        myJFrame.validate();
    }
    public MyToolbar(MyJFrame myJFrame, MyCanvas myCanvas) {
        this.myJFrame = myJFrame;
        this.myCanvas = myCanvas;
        setLayout(new GridLayout(2,1));
        bDraw = new JButton("Tryb rysowania");
        bEdit = new JButton("Tryb edycji");
        bInfo = new JButton("Info");
        bSave = new JButton("Zapisz");
        bLoad = new JButton("Wczytaj");
        bProstokat = new JButton("Prostokat");
        bWielokat = new JButton("Wielokat");
        bKolo = new JButton("Kolo");
        bColor = new JButton("Zmień kolor");
        buttonsPanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        figuresPanel = new JPanel();
        filePanel = new JPanel();
        figuresPanel.setVisible(false);


        bDraw.addActionListener(e -> {
            selectMode(RunningMode.CREATING);
            myCanvas.startCreating();
        });
        bEdit.addActionListener(e -> {
            selectMode(RunningMode.EDITITNG);
            myCanvas.startEditing();
        });
        bInfo.addActionListener(e -> {
           myJFrame.showInfo();
        });
        bProstokat.addActionListener(e -> {
            myCanvas.selectFigureName("Prostokat");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bProstokat.setBackground(activeColor);
        });
        bKolo.addActionListener(e -> {
            myCanvas.selectFigureName("Kolo");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bKolo.setBackground(activeColor);
        });
        bWielokat.addActionListener(e -> {
            myCanvas.selectFigureName("Wielokat");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bWielokat.setBackground(activeColor);
        });
        bSave.addActionListener(e -> {
            myCanvas.showSavePanel();
        });
        bLoad.addActionListener(e -> {
            myCanvas.showLoadPanel();
        });
        bColor.addActionListener(e ->{
            Color c = JColorChooser.showDialog(this, "Wybierz kolor", bColor.getBackground());
            if(c!=null) {
                bColor.setBackground(c);
                myCanvas.setSelectedColor(c);
                myJFrame.repaint();
            }
        });




        figuresPanel.add(bProstokat);
        figuresPanel.add(bWielokat);
        figuresPanel.add(bKolo);

        filePanel.add(bSave);
        filePanel.add(bLoad);

        buttonsPanel.add(bDraw);
        buttonsPanel.add(bEdit);
        buttonsPanel.add(bInfo);

        topPanel.add(filePanel);
        topPanel.add(buttonsPanel);

        bottomPanel.add(figuresPanel);
        bottomPanel.add(bColor);
        add(topPanel);
        add(bottomPanel);

        topPanel.setLayout(new GridLayout(1,2));
        figuresPanel.setBackground(backgroundColor);
        buttonsPanel.setBackground(backgroundColor);
        filePanel.setBackground(backgroundColor);
        topPanel.setBackground(backgroundColor);
        bottomPanel.setBackground(backgroundColor);
        setBackground(backgroundColor);
        setBorder(BorderFactory.createLineBorder(Color.black));


        setVisible(true);
    }
}

/*--------------------------------------------------------------------*/
//GLOWNA KLATKA
class MyJFrame extends JFrame{
    MyCanvas myCanvas;
    MyToolbar myToolbar;
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
        FiguraProstokatna[] figury = new FiguraProstokatna[0];
//        figury[0] = new Prostokat(100, 200, 200, 150);
//        figury[2] = new Kolo(300, 200, 100);
////        System.out.println(figury[2].toJson());
////        System.out.println(figury[0]);
////        System.out.println(figury[2]);
//////        figury[0].fromJson(figury[2].toJson());
////        figury[2].setColor(Color.PINK);
////        figury[0].setColor(Color.BLUE);
////
////        JSONObject dataJObject;
////        Gson gson = new Gson();
////        String json = gson.toJson(figury[2]);
////
////        dataJObject = new JSONObject(json);
////        System.out.println(dataJObject);
////        figury[0] = gson.fromJson(dataJObject.toString(), Kolo.class );
////        figury[0].move(100,100);
//
//        System.out.println(figury[0]);
//        System.out.println(figury[2]);
//
//        figury[3] = new Kolo(300, 500, 100);
//        figury[3].setColor(Color.RED);
////        figury[1] = new Kolo(400, 400, 50);
//        Point[] punkty = new Point[3];
//        punkty[0] = new Point(400,300);
//        punkty[1] = new Point(500,300);
//        punkty[2] = new Point(450,400);
////        figury[1] = new Wielokat(punkty);
//        figury[1] = new Kolo(600, 500, 100);
//        figury[1].setColor(Color.GREEN);
//
////        FigureManager.load(FigureManager.save(figury));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(30, 30, 1200, 800);

        myCanvas = new MyCanvas(figury,this);
        myToolbar = new MyToolbar(this, myCanvas);
        getContentPane().add(myToolbar,BorderLayout.PAGE_START);
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