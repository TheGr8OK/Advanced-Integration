import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    int m;
    int n;
    Agent agent;
    Ship[] ships;
    Station[] stations;

    public Grid(int m, int n){
        //no items in the same cell
        this.m = m<5?5:(m>15)?15:m;
        this.n = n<5?5:(n>15)?15:n;

        //genertae Agent position
        int agentX = ThreadLocalRandom.current().nextInt(0, m+1);
        int agentY = ThreadLocalRandom.current().nextInt(0, n+1);
        int agentCapacity = ThreadLocalRandom.current().nextInt(30, 101);
        agent = new Agent(agentCapacity, agentX, agentY, this);
        
    }
}
