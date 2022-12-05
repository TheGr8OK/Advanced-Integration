package code;
public class Station implements Cloneable {
    int passengersSaved;
    byte y;
    byte x;


    public Station(byte y, byte x) {
        passengersSaved = 0;
        this.y=y;
        this.x=x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + passengersSaved;
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
        Station other = (Station) obj;
        if (passengersSaved != other.passengersSaved)
            return false;
        if (y != other.y)
            return false;
        if (x != other.x)
            return false;
        return true;
    }

    public void savePassengers(int saved){
        passengersSaved+= saved;
    }

    public Station clone() throws CloneNotSupportedException{
        return (Station) super.clone();
    }
}
