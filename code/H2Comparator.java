package code;

import java.util.Comparator;

public class H2Comparator implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        if (o1.h2() < o2.h2())
            return -1;
        else if (o1.h2() > o2.h2())
            return 1;

        return 0;
    }

}
