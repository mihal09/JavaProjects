import javafx.util.Pair;

import java.util.ArrayList;

public class Board {
    private int n, m;
    private EnumType isOccupied[][];
    private Wolf wolf;
    private ArrayList<Rabbit> rabbits;
    public Board(int n, int m){
        this.n = n;
        this.m = m;
        isOccupied = new EnumType[n][m];
        for(int x=0;x<n;x++){
            for(int y=0;y<m;y++)
                isOccupied[x][y] = EnumType.EMPTY;
        }
    }

    public void setWolf(Wolf wolf){this.wolf = wolf;}
    public void setRabbits(ArrayList<Rabbit> rabbits) { this.rabbits = rabbits;}

    public boolean isOccupied(int x, int y){
        return isOccupied[x][y]!= EnumType.EMPTY;
    }

    public boolean contains(int x, int y){
        return (x>=0)&&(x<n)&&(y>=0)&&(y<m);
    }
    public void setField(int x, int y, EnumType value){
        this.isOccupied[x][y] = value;
    }
    public EnumType getField(int x, int y){
        return isOccupied[x][y];
    }

    public void killRabbit(int x, int y){
        for(Rabbit rabbit : rabbits){
            if(rabbit.getX() == x && rabbit.getY() == y) {
                rabbits.remove(rabbit);
                return;
            }
        }
    }

    public boolean isFurtherFromWolf(int x1, int y1, int x2, int y2 ){
        return isFurtherFromPoint(x1,y1,x2,y2,wolf.getX(),wolf.getY())==1;
    }
    public boolean isCloserFromClosestRabbit(int x1, int y1, int x2, int y2){
        Pair<Integer, Integer> closestRabbit = getClosestRabbit(x1,y1);
        System.out.println(closestRabbit.getKey()+":"+closestRabbit.getValue());
        return isFurtherFromPoint(x1,y1,x2,y2,closestRabbit.getKey(),closestRabbit.getValue())==-1;
    }

    private int isFurtherFromPoint(int x1, int y1, int x2, int y2, int xPoint, int yPoint){
        double currentDistance = Math.pow(x1-xPoint,2)+Math.pow(y1-yPoint,2);
        double newDistance = Math.pow(x2-xPoint,2)+Math.pow(y2-yPoint,2);
        if(newDistance > currentDistance)
            return 1;
        else if(newDistance < currentDistance)
            return -1;
        else
            return 0;
    }



    public Pair<Integer,Integer> getClosestRabbit(int x, int y){
        System.out.println("krolikow: "+rabbits.size());
        double minDistance = Double.POSITIVE_INFINITY;
        int bestX=Integer.MAX_VALUE, bestY=Integer.MAX_VALUE;
        for(Rabbit rabbit : rabbits){
            double distance = Math.pow(rabbit.getX() - x,2)+Math.pow(rabbit.getY()-y,2);
            System.out.println("odleglosc: "+distance);
            if(distance<minDistance){
                minDistance = distance;
                bestX = rabbit.getX();
                bestY = rabbit.getY();
            }
        }
        return new Pair<>(bestX, bestY);
    }


//    public boolean getField(int x, int y){
//        return isOccupied[x][y];
//    }

}
