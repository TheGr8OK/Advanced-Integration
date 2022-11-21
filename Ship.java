public class Ship {
    int passengers;
    int blackBoxDamage;
    boolean blackBoxDestroyed;
    boolean isSunk;

    public Ship(int passengers) {
        blackBoxDamage = 0; // check public test 0 or 1
        this.passengers = passengers;
        isSunk = false;
        blackBoxDestroyed = false;
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
