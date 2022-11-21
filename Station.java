public class Station {
    int passengersSaved;

    public Station() {
        passengersSaved = 0;
    }

    public void savePassengers(int saved){
        passengersSaved+= saved;
    }
}
