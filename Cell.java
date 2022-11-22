public class Cell {
    int positionX;
    int positionY;
    boolean isOccupied;
    boolean isExplored;
    Object occupant;

    public Cell(int y, int x, Object occupant){
        positionX=x;
        positionY=y;
        isOccupied = true;
        isExplored = false;
        this.occupant = occupant;
    }
    public Cell(int y, int x){
        positionX=x;
        positionY=y;
        isExplored=false;
        isOccupied = false;
    }


}
