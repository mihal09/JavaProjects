import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Wolf {
    private int x, y;
    private Board board;

    public Wolf(int x, int y, Board board){
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public void move(){
        ArrayList<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();
        for(int dx=-1; dx<=1; dx++){
            for(int dy=-1; dy<=1; dy++){
                if(dx==0 && dy==0)
                    continue;
                int newX = x+dx;
                int newY = y+dy;
                if(!board.contains(newX, newY))
                    continue;
                if(board.isCloserFromClosestRabbit(x,y,newX,newY))
                    possibleMoves.add(new Pair<>(newX, newY));
            }
        }
        if(possibleMoves.size()==0) {
            return;
        }
        int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
        Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
        if( board.getField(chosenPlace.getKey(),chosenPlace.getValue())==EnumType.RABBIT)
            board.killRabbit(chosenPlace.getKey(),chosenPlace.getValue());

        board.setField(x,y,EnumType.EMPTY);
        x = chosenPlace.getKey();
        y = chosenPlace.getValue();
        board.setField(x,y,EnumType.WOLF);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
