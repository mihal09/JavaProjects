import java.util.Random;

public class RandomGenerator {
    private Random random;
    private static RandomGenerator instance = new RandomGenerator();
    private RandomGenerator(){
        random = new Random();
    }
    public static int nextInt(int n){
        return instance.random.nextInt(n);
    }
}