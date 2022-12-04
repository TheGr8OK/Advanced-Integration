package code;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class State implements Cloneable {
    Cell currentCell;
    Set<TrackRecord> exploredStates;
    ArrayList<Pair> exploredCells;
    Grid grid;
    State previousState;
    boolean isInitialState;
    String currentPlan;
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentCell == null) ? 0 : currentCell.hashCode());
        result = prime * result + ((exploredCells == null) ? 0 : exploredCells.hashCode());
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        result = prime * result + ((previousState == null) ? 0 : previousState.hashCode());
        result = prime * result + depth;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (currentCell == null) {
            if (other.currentCell != null)
                return false;
        } else if (!currentCell.equals(other.currentCell))
            return false;
        if (exploredCells == null) {
            if (other.exploredCells != null)
                return false;
        } else if (!exploredCells.equals(other.exploredCells))
            return false;
        if (grid == null) {
            if (other.grid != null)
                return false;
        } else if (!grid.equals(other.grid))
            return false;
        if (previousState == null) {
            if (other.previousState != null)
                return false;
        } else if (!previousState.equals(other.previousState))
            return false;
        if (depth != other.depth)
            return false;
        return true;
    }

    String stateType;
    // int passengers;
    int depth;
    

    public State(){}
    
    public State(Cell currentCell, Set<TrackRecord> exploredStates, Grid grid, State previousState, String currentPlan, 
    String stateType, ArrayList<Pair> exploredCells, int depth){

        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        this.depth = depth;
        isInitialState= false;
        this.currentPlan = currentPlan;
        this.exploredStates = exploredStates;
        this.stateType = stateType;
        this.exploredCells = exploredCells;
    }

    public State(Cell currentCell, Set<TrackRecord> exploredStates, Grid grid, State previousState, String currentPlan,  String stateType, ArrayList<Pair> exploredCells){
        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        depth = previousState.depth+1;
        isInitialState= false;
        this.currentPlan = currentPlan;
        this.exploredStates = exploredStates;
        this.stateType = stateType;
        this.exploredCells = exploredCells;
    }

    public State(Cell currentCell, Grid grid, State previousState, String currentPlan){
        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        depth = previousState.depth+1;
        isInitialState= false;
        this.currentPlan = currentPlan;
        this.exploredStates = new HashSet<TrackRecord>();
    }

    public State(Cell currentCell, Grid grid){
        this.currentCell = currentCell;
        this.grid= grid;
        isInitialState=true;
        currentPlan="";
        this.exploredStates = new HashSet<TrackRecord>();
    }

    public State clone() throws CloneNotSupportedException{

        State clonedState = new State(currentCell, exploredStates, grid, previousState, currentPlan, stateType, exploredCells, depth);

        clonedState.grid = clonedState.grid.clone();

        ArrayList<Pair> clonedExpCells = new ArrayList<Pair>();
        for (int index = 0; index < exploredCells.size(); index++) {
            clonedExpCells.add(exploredCells.get(index).clone());
        }

        HashSet<TrackRecord> clonedExpStates = new HashSet<TrackRecord>();
        Iterator<TrackRecord> it = clonedState.exploredStates.iterator();

        while(it.hasNext()){
            clonedExpStates.add(it.next().clone());
        }
        // clonedState.exploredStates = clonedExpStates;

        clonedState.exploredCells = clonedExpCells;
        

        clonedState.currentCell = clonedState.currentCell.clone();

        return clonedState;
    }
}
