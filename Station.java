public class Station {
    int passengersSaved;
    int y;
    int x;


    public Station(int y, int x) {
        passengersSaved = 0;
        this.y=y;
        this.x=x;
    }

    public void savePassengers(int saved){
        passengersSaved+= saved;
    }
}
