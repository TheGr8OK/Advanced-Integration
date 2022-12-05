package code;

import java.util.Comparator;

public class A2Comparator implements Comparator<State>{
    @Override
    public int compare(State o1, State o2) {
        if (o1.h2() + o1.cost.getCost() < o2.h2() + o2.cost.getCost())
            return -1;
        else if (o1.h2() + o1.cost.getCost() < o2.h2() + o2.cost.getCost())
            return 1;

        return 0;
    }

}
