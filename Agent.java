public class Agent {
    Grid grid;
    int capacity;
    int x;
    int y;

    public Agent(int capacity, int x, int y, Grid grid){
        this.capacity=capacity<30?30:(capacity>100)?100:capacity;
        this.x = x;
        this.y = y;
        this.grid = grid;

        //handle if x and y outside grid
    }

    public void timeStep(){

    }
    
}
