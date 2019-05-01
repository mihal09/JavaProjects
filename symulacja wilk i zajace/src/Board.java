import java.util.List;

class Board {
    private int n, m;
    private EnumType[][] isOccupied;
    private Wolf wolf;
    private List<Rabbit> rabbits;
    MyJFrame myJFrame;
    Board(int n, int m, MyJFrame myJFrame){
        this.myJFrame = myJFrame;
        this.n = n;
        this.m = m;
        isOccupied = new EnumType[n][m];
        for(int x=0;x<n;x++){
            for(int y=0;y<m;y++)
                isOccupied[x][y] = EnumType.EMPTY;
        }
    }

    public void setWolf(Wolf wolf){this.wolf = wolf;}
    void setRabbits(List<Rabbit> rabbits) { this.rabbits = rabbits;}

    boolean isOccupied(int x, int y){
        return isOccupied[x][y]!= EnumType.EMPTY;
    }

    boolean isOutOfBound(int x, int y){
        return (x < 0) || (x >= n) || (y < 0) || (y >= m);
    }
    synchronized void setField(int oldX, int oldY, int newX, int newY, EnumType value){
        this.isOccupied[oldX][oldY] = EnumType.EMPTY;
        this.isOccupied[newX][newY] = value;
    }

    void setField(int newX, int newY, EnumType value){
        this.isOccupied[newX][newY] = value;
    }

    EnumType getField(int x, int y){
        return isOccupied[x][y];
    }

    void killRabbit(int x, int y){
        for(Rabbit rabbit : rabbits){
            if(rabbit.getX() == x && rabbit.getY() == y) {
                rabbit.kill();
                rabbits.remove(rabbit);
//                setField(x,y,x,y,EnumType.WOLF);
                return;
            }
        }
    }

    boolean isFurtherFromWolf(int x1, int y1, int x2, int y2){
        return isFurtherFromPoint(x1,y1,x2,y2,wolf.getX(),wolf.getY())==1;
    }
    boolean isCloserFromClosestRabbit(int x1, int y1, int x2, int y2){
        Rabbit closestRabbit = getClosestRabbit(x1,y1);
        return isFurtherFromPoint(x1,y1,x2,y2,closestRabbit.getX(),closestRabbit.getY())==-1;
    }

    private int isFurtherFromPoint(int x1, int y1, int x2, int y2, int xPoint, int yPoint){
        double currentDistance = Math.pow(x1-xPoint,2)+Math.pow(y1-yPoint,2);
        double newDistance = Math.pow(x2-xPoint,2)+Math.pow(y2-yPoint,2);
        return Double.compare(newDistance, currentDistance);
    }



    Rabbit getClosestRabbit(int x, int y){
        double minDistance = Double.POSITIVE_INFINITY;
        Rabbit closestRabbit = null;
        for(Rabbit rabbit : rabbits){
            double distance = Math.pow(rabbit.getX() - x,2)+Math.pow(rabbit.getY()-y,2);
            if(distance<minDistance){
                minDistance = distance;
                closestRabbit = rabbit;
            }
        }
        return closestRabbit;
    }

    void checkGameOver() {
        if(rabbits.size()==0)
            System.exit(0);
    }

}
