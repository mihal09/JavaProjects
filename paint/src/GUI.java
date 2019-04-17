import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {
    int prevX, prevY;
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("Mouse pressed: "+x+","+y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("Mouse released:"+x+","+y);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        Cursor cursor =  null;
        for(FiguraProstokatna figura : figury){
            int nearest = figura.getNearestVertice(x,y,20);
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
            else if(figura.isPointInside(x,y)){
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
        prevX = x;
        prevY = y;
        mouseUsed(x,y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseUsed(x,y);
//        figury[0].moveVertice(x,y,3);
//        validate();
//        removeAll();
//        myJFrame.doLayout();
//        repaint();
//        validate();
//        myJFrame.doLayout();
        // System.out.println("Mouse dragged:"+x+","+y);
    }

    void mouseUsed(int x, int y) {
        if (prevX == -1 || prevY == -1) {
            prevX = x;
            prevY = y;
        }
        int dx = x - prevX;
        int dy = y - prevY;

        for(FiguraProstokatna figura : figury) {
            int nearest = figura.getNearestVertice(x, y, 20);
            if (nearest != -1) { //mamy wierzcholek
                figura.moveVertice(x, y, nearest);
            } else if (figura.isPointInside(x, y)) {
                figura.move(dx, dy);
            }
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

    MyJFrame myJFrame;
    FiguraProstokatna[] figury;
    public MyCanvas(FiguraProstokatna[] figury, MyJFrame myJFrame){
        prevX = -1;
        prevY = -1;
        this.myJFrame = myJFrame;
        addMouseListener(this);
        addMouseMotionListener(this);
        this.figury = figury;
    }
    public void paintComponent(Graphics g) {
        for(FiguraProstokatna figura : figury){
            figura.draw(g);
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

    public MyJFrame(){
        FiguraProstokatna[] figury = new FiguraProstokatna[2];
        figury[0] = new Prostokat(100, 200, 200, 150);
//        figury[1] = new Kolo(400, 400, 50);
        Point[] punkty = new Point[3];
        punkty[0] = new Point(400,300);
        punkty[1] = new Point(500,300);
        punkty[2] = new Point(450,400);
        figury[1] = new Wielokat(punkty);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(30, 30, 800, 600);
        getContentPane().add(new MyCanvas(figury,this));
        setVisible(true);
    }
}

public class GUI {
    public static void main(String[] args){
        JFrame window = new MyJFrame();
    }
}