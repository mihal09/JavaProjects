import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    int n=8;
    int m=6;
    Board board;
    ArrayList<Rabbit> rabbits;
    Wolf wolf;
    int boxSide = 40;
    int boxGap = 2;

    public void draw(Graphics g){
        for(int y=0;y<m;y++){
            for(int x=0; x<n;x++){
                if(board.getField(x,y)==EnumType.EMPTY){
                    g.setColor(Color.GRAY);
//                    System.out.print("O");
                }
                else if(board.getField(x,y)==EnumType.RABBIT){
                    g.setColor(Color.BLUE);
//                    System.out.print("R");
                }
                else {
                    g.setColor(Color.RED);
//                    System.out.print("W");
                }
                g.fillRect(x*(boxSide+boxGap),y*(boxSide+boxGap),boxSide,boxSide);
            }
//            System.out.println();
        }
//        System.out.println("------------------------------------");
    }

    public Game(){
        int x = RandomGenerator.nextInt(n);
        int y = RandomGenerator.nextInt(m);
        rabbits = new ArrayList<>();
        board = new Board(n,m);
        wolf = new Wolf(x,y,board);
        board.setField(x,y,EnumType.WOLF);
        rabbits = new ArrayList<>();
        for(int i=0; i<15; i++){
            do {
                x = RandomGenerator.nextInt(n);
                y = RandomGenerator.nextInt(m);
            } while (board.isOccupied(x,y));
            rabbits.add(new Rabbit(x,y,board));
            board.setField(x,y,EnumType.RABBIT);
        }
        board.setWolf(wolf);
        board.setRabbits(rabbits);
    }

    public void tick(){
        for(Rabbit rabbit : rabbits)
            rabbit.move();
        wolf.move();
    }
}
