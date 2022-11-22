public class Agent {
    Grid grid;
    int freeSpace;
    int x;
    int y;
    int capacity;
    int blackBoxesRetrieved;

    public Agent(int capacity, int y, int x, Grid grid){
        this.freeSpace=capacity<30?30:(capacity>100)?100:capacity;
        this.x = x;
        this.y = y;
        this.grid = grid;
        capacity = this.freeSpace;
        blackBoxesRetrieved = 0;
        //handle if x and y outside grid
    }

    public void timeStep(){

    }
    
    public void pickUpPassengers(Ship ship){
        if(!(ship.x==x) || !(ship.y ==y)){
            throw new Error("agent is not in the same position as the ship!");
        }

        timeStep();
        if (freeSpace >= ship.passengers){
            freeSpace -= ship.passengers;
            ship.passengers=0;
            ship.isSunk=true;
        }
        else{
            ship.passengers -= freeSpace;
            freeSpace = 0;
        }
        
    }


    public void dropPassengers(Station station){
        if(!(station.x==x) || !(station.y ==y)){
            throw new Error("agent is not in the same position as the station!");
        }
        timeStep();
        station.passengersSaved += capacity - freeSpace;
        freeSpace = capacity;
    }


    public void retrieveBlackBox(Ship ship){
        if(!(ship.x==x) || !(ship.y ==y)){
            throw new Error("agent is not in the same position as the ship!");
        }
        timeStep();
        if (ship.isSunk){
            if (!ship.blackBoxDestroyed){
                blackBoxesRetrieved += 1;
                ship.blackBoxDestroyed=true;
            }
        }
    }


    public void moveRight(){
        if(x == grid.m-1){
            throw new Error("you can not move right");
        }
        else{
            x++;
        }
    }

    public void moveLeft(){
        if(x==0){
            throw new Error("you can not move left");
        }
        else{
            x--;
        }
    }

    public void moveUp(){
        if(y==0){
            throw new Error("you can not move up");
        }
        else{
            y--;
        }
    }

    public void moveDown(){
        if(y== grid.n-1){
            throw new Error("you can not move down");
        }
        else{
            y++;
        }
    }
}
