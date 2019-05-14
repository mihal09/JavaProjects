import javafx.util.Pair;

import java.util.ArrayList;

public class Wolf extends Thread{
    private int x, y;
    private int k;
    private final int waitingTicks = 5;
    private int ticksLeft = 0;
    private final Board board;

    public Wolf(int x, int y, int k, Board board){
        this.board = board;
        this.k = k;
        this.x = x;
        this.y = y;
    }

    private void move() throws InterruptedException{
        while(true) {
            System.out.println(x+":"+y);
            if (ticksLeft > 0) {
                ticksLeft--;
            } else {
                ArrayList<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();
                double minimalDistance = Double.MAX_VALUE;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0)
                            continue;
                        int newX = x + dx;
                        int newY = y + dy;
                        if (board.isOutOfBound(newX, newY))
                            continue;
                        if (board.isCloserFromClosestRabbit(x, y, newX, newY)) {
                            Rabbit closestRabbit = board.getClosestRabbit(x, y);
                            double distance = Math.pow(closestRabbit.getX() - newX, 2) + Math.pow(closestRabbit.getY() - newY, 2);
                            if (distance < minimalDistance) {
                                minimalDistance = distance;
                                possibleMoves.clear();
                                possibleMoves.add(new Pair<>(newX, newY));
                            } else if (distance == minimalDistance) {
                                possibleMoves.add(new Pair<>(newX, newY));
                            }
                        }
                    }
                }
                if (possibleMoves.size() == 0) {
                    continue;
                }
                int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
                Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
                int oldX = x;
                int oldY = y;
                x = chosenPlace.getKey();
                y = chosenPlace.getValue();

                if (board.getField(x, y) == EnumType.RABBIT) {
                    ticksLeft = waitingTicks;
                    board.killRabbit(x, y);
                    board.checkGameOver();
                }

                board.setField(oldX, oldY, x, y, EnumType.WOLF);
                board.myJFrame.repaint();
            }
            int millisecondsToWait = (int) (RandomGenerator.nextInt(k + 1) + 0.5 * k);
            Thread.sleep(millisecondsToWait);
        }
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
        } catch (Exception ignored) {
        }
    }
}
