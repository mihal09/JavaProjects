import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Rabbit {
    private int x, y;
    Board board;

    public Rabbit(int x, int y, Board board){
        this.x = x;
        this.y = y;
        this.board = board;
    }
    public void move(){
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
        if(possibleMoves.size()==0)
            return;
        int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
        Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
        board.setField(x,y,EnumType.EMPTY);
        x = chosenPlace.getKey();
        y = chosenPlace.getValue();
        board.setField(x,y,EnumType.RABBIT);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
