public class Agent {
    Grid grid;
    int capacity;
    int x;
    int y;
    int originalcapacity;
    int blackBoxesRetrieved;

    public Agent(int capacity, int x, int y, Grid grid){
        this.capacity=capacity<30?30:(capacity>100)?100:capacity;
        this.x = x;
        this.y = y;
        this.grid = grid;
        originalcapacity = this.capacity;
        blackBoxesRetrieved = 0;
        //handle if x and y outside grid
    }

    public void timeStep(){

    }
    
    public void pickupPassengers(Ship ship){
        if (capacity >= ship.passengers){
            capacity -= ship.passengers;
        }
        else{
            ship.passengers -= capacity;
            capacity = 0;
        }
    }
    public void dropPassengers(Station station){
        station.passengersSaved += originalcapacity - capacity;
        capacity = originalcapacity;
    }
    public void retrieveBlackBox(Ship ship){
        if (ship.isSunk){
            if (!ship.blackBoxDestroyed){
                blackBoxesRetrieved += 1;
            }
        }
    }
    public void moveRight(){
        
    }
    public void moveLeft(){
        
    }
    public void moveUp(){
        
    }
    public void moveDown(){
        
    }
}
