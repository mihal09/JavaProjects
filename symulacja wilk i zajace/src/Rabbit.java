import javafx.util.Pair;

import java.util.ArrayList;

class Rabbit extends Thread{
    private int x, y;
    private int k;
    private boolean isAlive = true;
    private final Board board;

    Rabbit(int x, int y, int k, Board board){
        this.k = k;
        this.x = x;
        this.y = y;
        this.board = board;
    }

    void kill(){
        isAlive = false;
    }

    private void move() throws InterruptedException{
        while(true) {
            if (!isAlive)
                return;
            ArrayList<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0)
                        continue;
                    int newX = x + dx;
                    int newY = y + dy;
                    if (board.isOutOfBound(newX, newY) || board.isOccupied(newX, newY))
                        continue;
                    if (board.isFurtherFromWolf(x, y, newX, newY))
                        possibleMoves.add(new Pair<>(newX, newY));
                }
            }
            if (possibleMoves.size() != 0) {
                int chosenIndex = RandomGenerator.nextInt(possibleMoves.size());
                Pair<Integer, Integer> chosenPlace = possibleMoves.get(chosenIndex);
                int oldX = x;
                int oldY = y;
                x = chosenPlace.getKey();
                y = chosenPlace.getValue();
                board.setField(oldX, oldY, x, y, EnumType.RABBIT);
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
