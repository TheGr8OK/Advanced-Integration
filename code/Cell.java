package code;

public class Cell implements Cloneable {
    int positionX;
    int positionY;
    boolean isOccupied;
    // boolean isExplored;
    Cell parentCell;
    Ship ship;
    Station station;
    boolean isShip;
    boolean isStation;

    public Cell(int x, int y, boolean isOccupied, Cell parent, Ship s, Station st, boolean isS, boolean isSt){
        positionX = x;
        positionY= y;
        this.isOccupied = isOccupied;
        parentCell = parent;
        ship = s;
        station = st;
        isShip = isS;
        isStation = isSt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + positionX;
        result = prime * result + positionY;
        result = prime * result + (isOccupied ? 1231 : 1237);
        result = prime * result + ((ship == null) ? 0 : ship.hashCode());
        result = prime * result + ((station == null) ? 0 : station.hashCode());
        result = prime * result + (isShip ? 1231 : 1237);
        result = prime * result + (isStation ? 1231 : 1237);
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
        Cell other = (Cell) obj;
        if (positionX != other.positionX)
            return false;
        if (positionY != other.positionY)
            return false;
        if (isOccupied != other.isOccupied)
            return false;
        if (ship == null) {
            if (other.ship != null)
                return false;
        } else if (!ship.equals(other.ship))
            return false;
        if (station == null) {
            if (other.station != null)
                return false;
        } else if (!station.equals(other.station))
            return false;
        if (isShip != other.isShip)
            return false;
        if (isStation != other.isStation)
            return false;
        return true;
    }

    public Cell(int y, int x) {
        positionX = x;
        positionY = y;
        isOccupied = false;
    }

    public Cell(int y, int x, Ship ship) {
        positionX = x;
        positionY = y;
        isOccupied = true;
        isShip =true;
        this.ship =ship;
    }
    public Cell(int y, int x, Station station) {
        positionX = x;
        positionY = y;
        isOccupied = true;
        isStation = true;
        this.station = station;
    }

    public Cell clone() throws CloneNotSupportedException {

        Cell clonedCell = new Cell(positionX, positionY, isOccupied, parentCell, ship, station, isShip, isStation);

        if (clonedCell.isShip) {
            clonedCell = new Cell(clonedCell.positionY, clonedCell.positionX, clonedCell.ship.clone());

        } else {
            if (clonedCell.isOccupied && clonedCell.isStation) 
                clonedCell.station = new Station(clonedCell.station.y, clonedCell.station.x);
        }

        return clonedCell;
    }

}
