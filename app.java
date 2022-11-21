import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
public class app{

    public static void main(String[] args) {
        System.out.println(ThreadLocalRandom.current().nextInt(0, 4));
        System.out.println(ThreadLocalRandom.current().nextInt(0, 4));
        System.out.println(ThreadLocalRandom.current().nextInt(0, 4));
        System.out.println(ThreadLocalRandom.current().nextInt(0, 4));
    }
    }