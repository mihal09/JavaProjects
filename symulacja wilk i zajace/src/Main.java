import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MyCanvas extends JPanel{
    Game game;
    public MyCanvas(Game game){
        this.game = game;
    }

    public void paintComponent(Graphics g) {
        game.draw(g);
    }
}

class MyJFrame extends JFrame {
    MyCanvas myCanvas;
    Game game;
    public MyJFrame(int rabbitNumber, int delay, int width, int height)
    {
        game = new Game(this,rabbitNumber,delay,width,height);
        myCanvas = new MyCanvas(game);
        setLayout(new BorderLayout());
        add(myCanvas,BorderLayout.CENTER);
        setSize(1200,800);
        setVisible(true);
        revalidate();
    }
}

public class Main {
    public static void main(String[] args) {
        int rabbitNumber = Integer.valueOf(args[0]);
        int delay = Integer.valueOf(args[1]);
        int width = Integer.valueOf(args[2]);
        int height = Integer.valueOf(args[3]);
        MyJFrame window = new MyJFrame(rabbitNumber,delay,width,height);
    }
}
