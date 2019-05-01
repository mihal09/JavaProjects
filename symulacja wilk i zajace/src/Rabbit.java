import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Rabbit extends Thread{
    private int x, y;
    private int k;
    private boolean isAlive = true;
    Board board;

    public Rabbit(int x, int y, int k, Board board){
        this.k = k;
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public void kill(){
        isAlive = false;
    }

    public void move() throws InterruptedException{
        if(!isAlive)
            return;
        ArrayList<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();
        for(int dx=-1; dx<=1; dx++){
            for(int dy=-1; dy<=1; dy++){
                if(dx==0 && dy==0)
                    continue;
                int newX = x+dx;
                int newY = y+dy;
                if(!board.contains(newX, newY) || board.isOccupied(newX, newY))
                    continue;
                if(board.isFurtherFromWolf(x, y, newX, newY))
                    possibleMoves.add(new Pair<>(newX, newY));
            }
        }
        if(possibleMoves.size()!=0) {
            int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
            Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
            board.setField(x, y, EnumType.EMPTY);
            x = chosenPlace.getKey();
            y = chosenPlace.getValue();
            board.setField(x, y, EnumType.RABBIT);
            board.myJFrame.repaint();
        }
        int millisecondsToWait = (int)(RandomGenerator.nextInt(k+1)+0.5*k);
        Thread.sleep(millisecondsToWait);
        move();
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public void run() {
        try {
            move();
        } catch (Exception e) {
        }
    }
}
