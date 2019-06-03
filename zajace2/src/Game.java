import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Game {
    private int n,m;
    private Board board;
    private List<Rabbit> rabbits;
    private Wolf wolf;
    private int boxSide = 20;
    private int boxGap = 2;

    void draw(Graphics g){
            for (int y = 0; y < m; y++) {
                for (int x = 0; x < n; x++) {
                    if (board.getField(x, y) == EnumType.EMPTY) {
                        g.setColor(Color.GRAY);
                    } else if (board.getField(x, y) == EnumType.RABBIT) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(x * (boxSide + boxGap), y * (boxSide + boxGap), boxSide, boxSide);
                }
            }
    }

    Game(MyJFrame myJFrame, int rabbitNumber, int delay, int width, int height){
        n = width;
        m = height;

        rabbitNumber = Math.min(n*m-1, rabbitNumber);

        rabbits = Collections.synchronizedList(new ArrayList<>());
        board = new Board(n,m, myJFrame);

        //spawn wolf
        int x = RandomGenerator.nextInt(n);
        int y = RandomGenerator.nextInt(m);
        wolf = new Wolf(x,y, delay,board);
        board.setField(x,y,EnumType.WOLF);
        rabbits = new ArrayList<>();


        //spawn rabbits
        for(int i=0; i<rabbitNumber; i++){
            do {
                x = RandomGenerator.nextInt(n);
                y = RandomGenerator.nextInt(m);
            } while (board.isOccupied(x,y));
            rabbits.add(new Rabbit(x,y, delay,board));
            board.setField(x,y,EnumType.RABBIT);
        }

        //send wolf and rabbits to board object
        board.setWolf(wolf);
        board.setRabbits(rabbits);

        //start threads
        for(Rabbit rabbit : rabbits){
            rabbit.start();
        }
        wolf.start();
    }
}
