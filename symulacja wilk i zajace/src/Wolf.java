import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Wolf {
    private int x, y;
    private final int waitingTicks = 5;
    private int ticksLeft = 0;
    private Board board;

    public Wolf(int x, int y, Board board){
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public void move(){
        if(ticksLeft>0){
            ticksLeft--;
            return;
        }
        ArrayList<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();
        double minimalDistance = Double.MAX_VALUE;
        for(int dx=-1; dx<=1; dx++){
            for(int dy=-1; dy<=1; dy++){
                if(dx==0 && dy==0)
                    continue;
                int newX = x+dx;
                int newY = y+dy;
                if(!board.contains(newX, newY))
                    continue;
                if(board.isCloserFromClosestRabbit(x,y,newX,newY)) {
                    Rabbit closestRabbit = board.getClosestRabbit(x,y);
                    double distance = Math.pow(closestRabbit.getX()-newX,2)+Math.pow(closestRabbit.getY()-newY,2);
                    if(distance<minimalDistance) {
                        minimalDistance = distance;
                        possibleMoves.clear();
                        possibleMoves.add(new Pair<>(newX, newY));
                    }
                    else if(distance == minimalDistance)
                    {
                        possibleMoves.add(new Pair<>(newX, newY));
                    }
                }
            }
        }
        if(possibleMoves.size()==0) {
            return;
        }
        int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
        Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
        if( board.getField(chosenPlace.getKey(),chosenPlace.getValue())==EnumType.RABBIT) {
            ticksLeft = waitingTicks;
            board.killRabbit(chosenPlace.getKey(), chosenPlace.getValue());
        }

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
