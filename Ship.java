public class Ship {
    int passengers;
    int blackBoxDamage;
    boolean blackBoxDestroyed;
    boolean isSunk;
    int y;
    int x;


    public Ship(int passengers, int y, int x) {
        blackBoxDamage = 0; // check public test 0 or 1
        this.passengers = passengers;
        isSunk = false;
        blackBoxDestroyed = false;
        this.y=y;
        this.x=x;

        
    }

    public void timeStep() {
        if (passengers > 0) {
            passengers--;
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
