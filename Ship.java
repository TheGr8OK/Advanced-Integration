public class Ship {
    int passengers;
    int blackBoxDamage;
    boolean blackBoxDestroyed;
    boolean isSunk;
    Grid grid;
    int y;
    int x;


    public Ship(int passengers, int y, int x, Grid grid) {
        blackBoxDamage = 0; // check public test 0 or 1
        this.passengers = passengers;
        isSunk = false;
        blackBoxDestroyed = false;
        this.y=y;
        this.x=x;
        this.grid = grid;
    }

    public void timeStep() {
        if (passengers > 0) {
            passengers--;
            grid.deaths++;
        } else {
            isSunk = true;
        }

        if (!blackBoxDestroyed && isSunk) {
            if (blackBoxDamage < 20) {
                blackBoxDamage++;
            } else {
                blackBoxDestroyed = true;
            }
        }
    }

}
