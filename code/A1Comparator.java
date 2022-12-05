package code;

import java.util.Comparator;

public class A1Comparator implements Comparator<State>{
    
    public int compare(State o1, State o2) {
        if (o1.h1() + o1.cost.getCost() < o2.h1() + o2.cost.getCost())
            return -1;
        else if (o1.h1() + o1.cost.getCost() < o2.h1() + o2.cost.getCost())
            return 1;

        return 0;
    }

}
