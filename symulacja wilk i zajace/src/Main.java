import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
    Game game;
    public MyCanvas(){
        game = new Game();
        System.out.println("tworze mycanvas");
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        game.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("click");
        game.tick();
        repaint();
//        System.out.println("click");
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        game.tick();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

class MyJFrame extends JFrame {
    MyCanvas myCanvas;
    public MyJFrame() {
        setSize(1200,800);
        setVisible(true);
        myCanvas = new MyCanvas();
        setLayout(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(myCanvas,BorderLayout.CENTER);
        revalidate();
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame window = new MyJFrame();
    }


}
