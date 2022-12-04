package code;

public class TrackRecord implements Cloneable {
    Pair currCell;
    // int numberOfAlivePeople;
    int depth;
    int onBoard;
    int alive;
    int dead;
    int saved;
    int blackBoxes;
    Ship[] ships;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currCell == null) ? 0 : currCell.hashCode());
        result = prime * result + depth;
        result = prime * result + onBoard;
        result = prime * result + alive;
        result = prime * result + dead;
        result = prime * result + saved;
        result = prime * result + blackBoxes;
        result = prime * result + nodes;
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
        TrackRecord other = (TrackRecord) obj;
        if (currCell == null) {
            if (other.currCell != null)
                return false;
        } else if (!currCell.equals(other.currCell))
            return false;
        if (depth != other.depth)
            return false;
        if (onBoard != other.onBoard)
            return false;
        if (alive != other.alive)
            return false;
        if (dead != other.dead)
            return false;
        if (saved != other.saved)
            return false;
        if (blackBoxes != other.blackBoxes)
            return false;
        if (nodes != other.nodes)
            return false;
        return true;
    }

    int nodes;
    // number of alive passengers

    public TrackRecord(State currState) {
        currCell = new Pair(currState.currentCell.positionY, currState.currentCell.positionX);
        onBoard = currState.grid.agent.passengers;
        alive = currState.grid.alivePassengers;
        dead = currState.grid.deaths;
        saved = currState.grid.saved;
        blackBoxes = currState.grid.agent.blackBoxesRetrieved;
        nodes = currState.exploredCells.size();
        depth = currState.depth;
        ships = currState.grid.ships;
    }

    public TrackRecord(Pair p, int a, int o, int b, int d, int s, int depth, int nodes, Ship[] ships) {
        currCell = p;
        onBoard = o;
        blackBoxes = b;
        alive =a;
        saved = s;
        dead = d;
        this.depth =depth;
        this.nodes =nodes;
        this.ships = ships;
    }

    public TrackRecord clone() throws CloneNotSupportedException {
        TrackRecord cloned = (TrackRecord) super.clone();
        cloned.currCell = cloned.currCell.clone();

        return cloned;
    }
}
