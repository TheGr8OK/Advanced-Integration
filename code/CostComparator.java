package code;

import java.util.Comparator;

public class CostComparator implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        if (o1.cost.getCost() < o2.cost.getCost())
            return -1;
        else if (o1.cost.getCost() > o2.cost.getCost())
            return 1;

        return 0;
    }

}
