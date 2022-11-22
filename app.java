import java.util.LinkedList;
import java.util.Queue;

public class app{


    public  static String GenGrid(){
        String rep = "";
        Grid grid = new Grid();
        rep += Integer.toString(grid.m) + ",";
        rep += Integer.toString(grid.n) + ";";
        rep += Integer.toString(grid.agent.capacity) + ";";
        rep += Integer.toString(grid.agent.x) + ",";
        rep += Integer.toString(grid.agent.y) + ";";
        for (int i = 0; i < grid.stations.length; i++){
            rep += Integer.toString(grid.stations[i].x) + ",";
            rep += Integer.toString(grid.stations[i].y) + ",";
        }
        rep=rep.substring(0, rep.length()-1);
        rep += ";";
        for (int i = 0; i< grid.ships.length; i++){
            rep += Integer.toString(grid.ships[i].x) + ",";
            rep += Integer.toString(grid.ships[i].y) + ",";
            rep += Integer.toString(grid.ships[i].passengers) + ",";
        }
        rep=rep.substring(0, rep.length()-1);
        rep += ";";
        return rep;
    }
    public static String Solve(String stringGrid, String strategy, Boolean visualize){
        String[] gridParts = stringGrid.split(";");

        String[] gridSize = gridParts[0].split(","); 
        int m = Integer.parseInt(gridSize[0]);
        int n = Integer.parseInt(gridSize[1]);

        int agentCapacity = Integer.parseInt(gridParts[1]);

        String[] agentPosition = gridParts[2].split(",");
        int agentX = Integer.parseInt(agentPosition[0]);
        int agentY = Integer.parseInt(agentPosition[1]);
        Agent agent = new Agent(agentCapacity, agentY, agentX);
       
        String[] stringStations = gridParts[3].split(",");
        Station[] stations = new Station[stringStations.length/2];

        
        String[] stringShips = gridParts[4].split(",");
        Ship[] ships = new Ship[stringShips.length/3];

        for (int i = 0; i < stringShips.length; i+=3) {
            System.out.println("Ship "+ (i+1)/3);
            ships[(i+1)/3] = new Ship(Integer.parseInt(stringShips[i+2]), Integer.parseInt(stringShips[i+1]), Integer.parseInt(stringShips[i]));
        }

        for (int i = 0; i < stringStations.length; i+=2) {
            stations[(i+1)/2] = new Station(Integer.parseInt(stringStations[i+1]), Integer.parseInt(stringStations[i]));
        }

        System.out.println("Ships: " + ships.length);
        for (int i = 0; i < ships.length; i++) {
            System.out.println(ships[i].passengers);
        }
        System.out.println("Stations: " + stations.length);
        for (int i = 0; i < stations.length; i++) {
            System.out.println(stations[i].y+ " "+ stations[i].x);
        }
        Grid grid = new Grid(m, n, agent, ships, stations);

        Cell[][] cells= grid.cells;

        System.out.println("Y: "+cells.length);
System.out.println("X: "+ cells[0].length);
        for (int i = 0; i < ships.length; i++) {
            ships[i].grid = grid;
            cells[ships[i].y][ships[i].x]= new Cell(ships[i].y, ships[i].x, ships[i]);
        }

        for (int i = 0; i < stations.length; i++) {
            cells[stations[i].y][stations[i].x]=  new Cell(stations[i].y, stations[i].x, stations[i]);
        }

        cells[agent.y][agent.x] = new Cell(agent.y, agent.x);




        
        String output = "";
        if (strategy.equals("BF")){
            SolveBF(grid);
        }
        if (strategy.equals("DF")){
            SolveDF();
        }
        if (strategy.equals("ID")){
            SolveID();
        }
        if (strategy.equals("GR1")){
            SolveGR(1);
        }
        if (strategy.equals("GR2")){
            SolveGR(2);
        }
        if (strategy.equals("AS1")){
            SolveAS(1);
        }
        if (strategy.equals("AS2")){
            SolveAS(2);
        }
        return output;
    }

    public static void SolveBF(Grid grid){
        Queue<Cell> queue = new LinkedList<>();
        List<Cell> stationsPositions= new List<Cell>();

        //initial position for agent and set the initial node to explored
        queue.add(grid.cells[grid.agent.y][grid.agent.x]);
        grid.cells[grid.agent.y][grid.agent.x].isExplored = true; 

        while(!queue.isEmpty()){
            //retrieve the initial agent position
            Cell currentCell = queue.peek();
            int x = currentCell.positionX;
            int y = currentCell.positionY;

            if(currentCell.isOccupied){

                if(currentCell.occupant instanceof Station){
                    stationsPositions[stationsPositions.length]= currentCell;
                }

                if(currentCell.occupant instanceof Ship){
                    Ship currentShip = (Ship) currentCell.occupant;
                    grid.agent.pickUpPassengers(currentShip);
                 }
            }
            System.out.println("(" + x + "," + y + ")");
            Boolean[] directions= isValid(grid.cells, currentCell, grid.m, grid.n);
            queue.remove();
            
            //go to adjacent cells
            //add left cell to the queue
            if(directions[0]){
                queue.add(grid.cells[y][x-1]);
                grid.cells[y][x-1].isExplored= true;
            }
            //add top cell to the queue
            if(directions[1]){
                queue.add(grid.cells[y-1][x]);
                grid.cells[y-1][x].isExplored= true;
            }
            //add right cell to the queue
            if(directions[2]){
                queue.add(grid.cells[y][x+1]);
                grid.cells[y][x+1].isExplored= true;
            }
            //add bottom cell to the queue
            if(directions[3]){
                queue.add(grid.cells[y+1][x]);
                grid.cells[y+1][x].isExplored= true;
            }
        }

    }

    public static void SolveDF(){
        
    }

    public static void SolveID(){
        
    }

    public static void SolveGR(int i){
        
    }

    public static void SolveAS(int i){
        
    }

    public static Boolean[] isValid(Cell[][] cells,Cell cell, int m, int n){
        Boolean[] directions = new Boolean[4];

        //is left move valid
        if(cell.positionX - 1 < 0){
            directions[0] = false;
        }
        else{
            directions[0] = true;
        }

        //is up move valid
        if(cell.positionY -1 < 0){
            directions[1] = false;
        }
        else{
            directions[1] = true;
        }

        //is right move valid
        if(cell.positionX + 1 >= m){
            directions[2] = false;
        }
        else{
            directions[2] = true;
        }

        //is down move valid
        if(cell.positionY +1 >=n){
            directions[3] = false;
        }
        else{
            directions[3] = true;
        }
        
        //is left cell explored already
        if(directions[0] && cells[cell.positionY][cell.positionX-1].isExplored){
            directions[0] = false;
        }

        //is top cell explored already
        if(directions[1] && cells[cell.positionY-1][cell.positionX].isExplored){
            directions[1] = false;
        }

        //is right cell explored already 
        if(directions[2] && cells[cell.positionY][cell.positionX+1].isExplored){
            directions[2] = false;
        }

        if(directions[3] && cells[cell.positionY+1][cell.positionX].isExplored){
            directions[3] = false;
        }
        
        return directions;
    }

    public static void main(String[] args) {
        String genGrid= GenGrid();
        System.out.println(genGrid);
        Solve(genGrid, "BF", false);    
    }
    }