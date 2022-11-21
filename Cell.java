public class Cell {
    int positionX;
    int positionY;
    boolean isOccupied;
    Object occupant;

    public Cell(int y, int x, Object occupant){
        positionX=x;
        positionY=y;
        isOccupied = false;
        this.occupant = occupant;
    }
    public Cell(int y, int x){
        positionX=x;
        positionY=y;
        isOccupied = false;
    }

}
