package code;

import java.util.Comparator;

public class H1Comparator implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        if (o1.h1() < o2.h1())
            return -1;
        else if (o1.h1() > o2.h1())
            return 1;

        return 0;
    }

}
