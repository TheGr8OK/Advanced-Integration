package code;
public class Ship implements Cloneable {
    int passengers;
    // int dead;
    int blackBoxDamage;
    boolean blackBoxDestroyed;
    boolean isSunk;
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + passengers;
        result = prime * result + blackBoxDamage;
        result = prime * result + (blackBoxDestroyed ? 1231 : 1237);
        result = prime * result + (isSunk ? 1231 : 1237);
        result = prime * result + y;
        result = prime * result + x;
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
        Ship other = (Ship) obj;
        if (passengers != other.passengers)
            return false;
        if (blackBoxDamage != other.blackBoxDamage)
            return false;
        if (blackBoxDestroyed != other.blackBoxDestroyed)
            return false;
        if (isSunk != other.isSunk)
            return false;
        if (y != other.y)
            return false;
        if (x != other.x)
            return false;
        return true;
    }

    // Grid grid;
    int y;
    int x;


    public Ship(int passengers, int blackBoxDamage, boolean isSunk, boolean blackBoxDestroyed, int y, int x){
        this.passengers = passengers;
        this.blackBoxDamage = blackBoxDamage;
        this.isSunk = isSunk;
        this.blackBoxDestroyed = blackBoxDestroyed;
        this.y=y;
        this.x=x;
    } 
    
    public Ship(int passengers, int y, int x){
        blackBoxDamage = 0; // check public test 0 or 1
        this.passengers = passengers;
        isSunk = false;
        blackBoxDestroyed = false;
        this.y=y;
        this.x=x;
    }

    // public Ship(int passengers, int y, int x) {
    //     blackBoxDamage = 0; // check public test 0 or 1
    //     this.passengers = passengers;
    //     isSunk = false;
    //     blackBoxDestroyed = false;
    //     this.y=y;
    //     this.x=x;
    //     // this.grid = grid;
    // }

    // public void timeStep() {
    //     if (passengers > 0) {
    //         passengers--;
    //         grid.deaths++;
    //         grid.alivePassengers--;
    //     } else {
    //         isSunk = true;
    //     }

    //     if (!blackBoxDestroyed && isSunk) {
    //         if (blackBoxDamage < 20) {
    //             blackBoxDamage++;
    //         } else {
    //             blackBoxDestroyed = true;
    //         }
    //     }
    // }

    public Ship clone() throws CloneNotSupportedException{
        Ship ship = new Ship(passengers, blackBoxDamage, isSunk, blackBoxDestroyed , y, x);

        return ship;
    }

    public void addBlackBoxDamage() {
        blackBoxDamage++;
        // System.out.println("INCCREASED THE BLACKBOX DAMAGE!!! "+ blackBoxDamage);
    }

}
