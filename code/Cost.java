package code;

public class Cost {
    int blackBoxDestroyed;
    int deaths;

    public Cost(State curState){
        blackBoxDestroyed = curState.grid.agent.blackBoxesDestroyed;
        deaths = curState.grid.deaths;
    }

    public Cost(int bbd, int d){
        blackBoxDestroyed = bbd;
        deaths=d;
    }

    public int getCost(){
        return this.deaths*250 + this.blackBoxDestroyed;
    }

    
}
