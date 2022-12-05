package code;
import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.Iterator;
// import java.util.Set;

public class State implements Cloneable {
    Cell currentCell;
    // Set<TrackRecord> exploredStates;
    ArrayList<Pair> exploredCells;
    Grid grid;
    State previousState;
    String currentPlan;
    String stateType;
    // int passengers;
    int depth;
    Cost cost;
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((currentCell == null) ? 0 : currentCell.hashCode());
        // result = prime * result + ((exploredCells == null) ? 0 : exploredCells.hashCode());
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        // result = prime * result + depth;
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
        // if (currentCell == null) {
        //     if (other.currentCell != null)
        //         return false;
        // } else if (!currentCell.equals(other.currentCell))
        //     return false;
        // if (exploredCells == null) {
        //     if (other.exploredCells != null)
        //         return false;
        // } else if (!exploredCells.equals(other.exploredCells))
        //     return false;
        if (grid == null) {
            if (other.grid != null)
                return false;
        } else if (!grid.equals(other.grid))
            return false;
        // if (depth != other.depth)
        //     return false;
        return true;
    }

    // public State(){}
    

    public State(Cell currentCell, Grid grid, State previousState, String currentPlan, 
    String stateType, ArrayList<Pair> exploredCells, int depth, Cost c){

        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        this.depth = depth;
        this.currentPlan = currentPlan;
        // this.exploredStates = exploredStates;
        this.stateType = stateType;
        this.exploredCells = exploredCells;
        this.cost =c;
    }

    public State(Cell currentCell, Grid grid, State previousState, String currentPlan, 
    String stateType, ArrayList<Pair> exploredCells, int depth){

        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        this.depth = depth;
        this.currentPlan = currentPlan;
        // this.exploredStates = exploredStates;
        this.stateType = stateType;
        this.exploredCells = exploredCells;
    }

    public State(Cell currentCell, Grid grid, State previousState, String currentPlan,  String stateType, ArrayList<Pair> exploredCells){
        this.currentCell = currentCell;
        this.grid= grid;
        this.previousState = previousState;
        depth = previousState.depth+1;
        this.currentPlan = currentPlan;
        // this.exploredStates = exploredStates;
        this.stateType = stateType;
        this.exploredCells = exploredCells;
    }

    // public State(Cell currentCell, Grid grid, State previousState, String currentPlan){
    //     this.currentCell = currentCell;
    //     this.grid= grid;
    //     this.previousState = previousState;
    //     depth = previousState.depth+1;
    //     this.currentPlan = currentPlan;
    //     // this.exploredStates = new HashSet<TrackRecord>();
    // }

    // public State(Cell currentCell, Grid grid){
    //     this.currentCell = currentCell;
    //     this.grid= grid;
    //     currentPlan="";
    // }

    public State clone() throws CloneNotSupportedException{

        State clonedState = new State(currentCell, grid, previousState, currentPlan, stateType, exploredCells, depth);

        clonedState.grid = clonedState.grid.clone();

        ArrayList<Pair> clonedExpCells = new ArrayList<Pair>();
        for (int index = 0; index < exploredCells.size(); index++) {
            clonedExpCells.add(exploredCells.get(index).clone());
        }

        // HashSet<TrackRecord> clonedExpStates = new HashSet<TrackRecord>();
        // Iterator<TrackRecord> it = clonedState.exploredStates.iterator();

        // while(it.hasNext()){
        //     clonedExpStates.add(it.next().clone());
        // }
        // clonedState.exploredStates = clonedExpStates;

        clonedState.exploredCells = clonedExpCells;
        

        clonedState.currentCell = clonedState.currentCell.clone();

        return clonedState;
    }

    //heuristic 1 implementation
    public double h1(){
        double cost=0;
        byte x = currentCell.positionX;
        byte y = currentCell.positionY;
        Ship closesetShip = grid.ships[0];
        Station closesetStation = grid.stations[0];

        for (int i = 0; i < grid.ships.length; i++) {
            byte distance = (byte) (Math.abs(grid.ships[i].y - y) + Math.abs(grid.ships[i].x - x));
            byte closesetDistance = (byte) (Math.abs(grid.ships[i].y - closesetShip.y) + Math.abs(grid.ships[i].x - closesetShip.x));
           
            if(distance < closesetDistance && grid.cells[grid.ships[i].y][grid.ships[i].x].ship.passengers - distance > 0)
                closesetShip = grid.ships[i];
        }

        for (int j = 0; j < grid.stations.length; j++) {
            byte distance = (byte) (Math.abs(grid.stations[j].y - y) + Math.abs(grid.stations[j].x - x));
            byte closesetDistance = (byte) (Math.abs(grid.stations[j].y - closesetStation.y) + Math.abs(grid.stations[j].x - closesetStation.x));

            if(distance < closesetDistance){
                closesetStation = grid.stations[j];
            }
        }

        byte shipDistance = (byte) (Math.abs(closesetShip.y - y) + Math.abs(closesetShip.x - x));
        byte stationDistance = (byte) (Math.abs(closesetStation.y - y) + Math.abs(closesetStation.x - x));
        
        byte numberOfBoxesLeft=0;
        int numberOfAlivePeople=0;
        for (int i = 0; i < grid.ships.length; i++) {
            if(!grid.cells[grid.ships[i].y][grid.ships[i].x].ship.blackBoxDestroyed){
                numberOfAlivePeople += grid.cells[grid.ships[i].y][grid.ships[i].x].ship.passengers;
                numberOfBoxesLeft++;
        }
    }

        cost = (stationDistance * 0.1 * numberOfBoxesLeft * numberOfAlivePeople) + ((0.01 * closesetShip.passengers)/(closesetShip.passengers-shipDistance)) + (0.1 * numberOfBoxesLeft);

        return cost;
    }

    //heuristic 2 implementation
    public int h2() {
        return 0;
    }
}
