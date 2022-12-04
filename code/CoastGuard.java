package code;

import java.util.ArrayList;
import java.util.HashSet;
// import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CoastGuard {
    public static String GenGrid() throws CloneNotSupportedException {
        String rep = "";
        Grid grid = new Grid();
        rep += Integer.toString(grid.m) + ",";
        rep += Integer.toString(grid.n) + ";";
        rep += Integer.toString(grid.agent.capacity) + ";";
        rep += Integer.toString(grid.agent.x) + ",";
        rep += Integer.toString(grid.agent.y) + ";";
        for (int i = 0; i < grid.stations.length; i++) {
            rep += Integer.toString(grid.stations[i].x) + ",";
            rep += Integer.toString(grid.stations[i].y) + ",";
        }
        rep = rep.substring(0, rep.length() - 1);
        rep += ";";
        for (int i = 0; i < grid.ships.length; i++) {
            rep += Integer.toString(grid.ships[i].x) + ",";
            rep += Integer.toString(grid.ships[i].y) + ",";
            rep += Integer.toString(grid.ships[i].passengers) + ",";
        }
        rep = rep.substring(0, rep.length() - 1);
        rep += ";";
        return rep;
    }

    public static String solve(String stringGrid, String strategy, boolean visualize)
            throws CloneNotSupportedException {
        int alivePassengers = 0;
        String[] gridParts = stringGrid.split(";");

        String[] gridSize = gridParts[0].split(",");
        int m = Integer.parseInt(gridSize[0]);
        int n = Integer.parseInt(gridSize[1]);

        int agentCapacity = Integer.parseInt(gridParts[1]);
        // System.out.println(agentCapacity + "Akhryyyy");

        String[] agentPosition = gridParts[2].split(",");
        int agentX = Integer.parseInt(agentPosition[1]);
        int agentY = Integer.parseInt(agentPosition[0]);
        Agent agent = new Agent(agentCapacity, agentY, agentX);

        String[] stringStations = gridParts[3].split(",");
        Station[] stations = new Station[stringStations.length / 2];

        String[] stringShips = gridParts[4].split(",");
        Ship[] ships = new Ship[stringShips.length / 3];

        for (int i = 0; i < stringShips.length; i += 3) {
            // System.out.println("Ship " + (i + 1) / 3);
            ships[(i + 1) / 3] = new Ship(Integer.parseInt(stringShips[i + 2]), Integer.parseInt(stringShips[i]),
                    Integer.parseInt(stringShips[i + 1]));
            alivePassengers += Integer.parseInt(stringShips[i + 2]);
        }

        for (int i = 0; i < stringStations.length; i += 2) {
            stations[(i + 1) / 2] = new Station(Integer.parseInt(stringStations[i]),
                    Integer.parseInt(stringStations[i + 1]));
        }

        // System.out.println("Ships: " + ships.length);
        // for (int i = 0; i < ships.length; i++) {
        //     System.out.println(ships[i].passengers);
        // }
        // System.out.println("Stations: " + stations.length);
        // for (int i = 0; i < stations.length; i++) {
        //     System.out.println(stations[i].y + " " + stations[i].x);
        // }
        // System.out.println("###### " + agent.capacity);
        Grid grid = new Grid(m, n, agent, stations, alivePassengers);

        for (int i = 0; i < ships.length; i++) {
            // ships[i].grid = grid;
            grid.cells[ships[i].y][ships[i].x] = new Cell(ships[i].y, ships[i].x, ships[i]);
            // System.out.println(grid.cells[ships[i].y][ships[i].x].ship+ " ???");
        }

        for (int i = 0; i < stations.length; i++) {
            grid.cells[stations[i].y][stations[i].x] = new Cell(stations[i].y, stations[i].x, stations[i]);
        }

        grid.cells[agent.y][agent.x] = new Cell(agent.y, agent.x);

        String output = "";
        if (strategy.equals("BF")) {
            output = SolveBF(grid);
        }
        if (strategy.equals("DF")) {
        output = SolveDF(grid);
        }
        // if (strategy.equals("ID")) {
        // output = SolveID(grid);
        // }
        // if (strategy.equals("GR1")) {
        // output = SolveGR(1, grid);
        // }
        // if (strategy.equals("GR2")) {
        // output = SolveGR(2, grid);
        // }
        // if (strategy.equals("AS1")) {
        // output = SolveAS(1, grid);
        // }
        // if (strategy.equals("AS2")) {
        // output = SolveAS(2, grid);
        // }
        // System.out.println(output+"  out");
        return output;
    }

    private static String SolveDF(Grid grid) {
        return null;
    }

    private static String SolveBF(Grid grid) throws CloneNotSupportedException {
        Queue<State> queue = new LinkedList<>();
        ArrayList<Pair> exploredCells = new ArrayList<Pair>();

        HashSet<TrackRecord> expStates = new HashSet<TrackRecord>();
        expStates.add(new TrackRecord(new Pair(grid.agent.y, grid.agent.x), grid.alivePassengers, grid.agent.passengers, grid.agent.blackBoxesRetrieved, grid.deaths ,grid.saved, 0, 1));
        // adding the initial cell to the exploredCells
        exploredCells.add(new Pair(grid.agent.y, grid.agent.x));

        // Initial State of my problem
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x], expStates,
                grid, null, "", "start", exploredCells, 0);

        // adding the initial state to my queue
        queue.add(initialState);

        int counter = 0;
        int depth = 1000; 
        ArrayList<State> goals = new ArrayList<State>();

        while (!queue.isEmpty() && queue.peek().depth <= depth) {

            // retrieving my current state from the queue
            State currentState = queue.remove();
            int x = currentState.currentCell.positionX;
            int y = currentState.currentCell.positionY;

            TrackRecord isNew = new TrackRecord(currentState);
            if (check(isNew, currentState.exploredStates, currentState) && currentState.previousState != null && !isBreak(currentState)
            && ((currentState.stateType.equals("left")) || (currentState.stateType.equals("right")) || (currentState.stateType.equals("up")) || 
            (currentState.stateType.equals("down")))) {
                // System.out.println("I AM PASSING!!!! " + currentState.currentPlan);
                continue;
            }
            else{
                // System.out.println("Adding: "+ isNew.currCell.y + " "+isNew.currCell.x +" "+ isNew.numberOfAlivePeople + " "+ isNew.onBoard + " "+isNew.blackBoxes + " "+isNew.nodes);
                currentState.exploredStates.add(isNew);
            }

            currentState.grid.agent.y = y;
            currentState.grid.agent.x = x;

            Pair p = new Pair(y, x);
            boolean addingNew = true;
            for (int i = 0; i < currentState.exploredCells.size(); i++) {
                if (currentState.exploredCells.get(i).y == p.y && currentState.exploredCells.get(i).x == p.x) {
                    addingNew = false;
                    break;
                }
            }
            if(addingNew){
                currentState.exploredCells.add(p);
            }

            // currentState.grid.cells[currentState.currentCell.positionY][currentState.currentCell.positionX]
            // = currentState.currentCell;

            // perform the action coming on queue
            switch (currentState.stateType) {
                case "pickup":
                    // check if the ship still has passengers
                    if (currentState.grid.cells[y][x].ship.passengers > 0) {
                        currentState.grid.agent.pickUpPassengers(currentState, y, x);
                    }

                    break;

                case "retrieve":
                    currentState.grid.agent.retrieveBlackBox(currentState);
                    currentState.grid.cells[y][x].ship.blackBoxDestroyed = true;

                    break;

                case "drop":
                    currentState.grid.agent.dropPassengers(currentState);

                    break;

                default:
                    break;
            }

            // timestep
            if (!(currentState.depth == 0)) {
                currentState.grid.timeStep();
            }
            // check if the current cell is a station
            if (currentState.grid.cells[y][x].isOccupied
                    && !currentState.grid.cells[y][x].isShip) {
                State newState = currentState.grid.agent.isDropPassengers(currentState);
                if (newState != null) {
                    queue.add(newState);
                }
            }

            // check if the current cell is a ship
            if (currentState.grid.cells[y][x].isOccupied && currentState.grid.cells[y][x].isShip) {
                Ship ship = currentState.grid.cells[y][x].ship;

                // check if i can pickup passengers
                if (!ship.isSunk) {
                    State newState = currentState.grid.agent.isPickUpPassengers(currentState);
                    if (newState != null) {
                        queue.add(newState);
                    }
                } else {
                    // check if black box is retrievable
                    if (!ship.blackBoxDestroyed) {
                        State newState = currentState.grid.agent.isRetrieveBlackBox(currentState);
                        if (newState != null) {
                            queue.add(newState);
                        }
                    }
                }
            }

            // adding the 4 movement directions
            // check if I can move Left
            State leftState = currentState.grid.agent.isMoveLeft(currentState);
            if (leftState != null) {
                // currentState.grid.timeStep(currentState);
                queue.add(leftState);
                // System.out.println(leftState.grid.cells[1][1].ship+ " left");
                // System.out.println(queue.peek().grid.cells[1][1].ship +"klbivkcyh");
            }

            // check if I can move Up
            State upState = currentState.grid.agent.isMoveUp(currentState);
            if (upState != null) {
                // currentState.grid.timeStep(currentState);
                // System.out.println("up is null");
                queue.add(upState);
            }

            // check if I can move Right
            State rightState = currentState.grid.agent.isMoveRight(currentState);
            if (rightState != null) {
                // currentState.grid.timeStep(currentState);
                queue.add(rightState);
                // System.out.println(rightState.grid.cells[1][1].ship+ " right");
                // System.out.println(queue.peek().grid.cells[1][1].ship +"klbivkcyh");
            }

            // check if I can move Down
            State downState = currentState.grid.agent.isMoveDown(currentState);
            if (downState != null) {
                // currentState.grid.timeStep(currentState);
                queue.add(downState);
            }

            // System.out.println("Black boxes retrievd: " + currentState.grid.agent.blackBoxesRetrieved);
            System.out.println("Depth: " + currentState.depth);
            // System.out.println("OnBoard: " + currentState.grid.agent.passengers);
            // System.out.println("Ship Passengers: " + currentState.grid.cells[3][2].ship.passengers);
            // System.out.println(
            //         "BlackBox Destroyed:   " + currentState.grid.cells[3][2].ship.blackBoxDestroyed);
            // System.out.println("Alive Passengers: " + currentState.grid.alivePassengers);

            // System.out.println(currentState.currentPlan);

            // queue.remove();
            counter++;
            // System.out.println("Explored: "+ currentState.exploredCells.size());

            // timestep
            // currentState.grid.timeStep(currentState);

            // check if I reached a goal state and will I break
            if (isBreak(currentState)) {
                goals.add(currentState);
                depth = currentState.depth;
            }

            if (counter > 1000000) {

                // System.out.println("kkkkwqkdbievfuk2qwb "+ currentState.exploredStates.size());
                // Iterator<TrackRecord> it =currentState.exploredStates.iterator();
                // while(it.hasNext()){
                //     System.out.println(it.next().currCell.y + " "+ it.next().currCell.x + " "+ it.next().numberOfAlivePeople + " "+ it.next().onBoard);
                // }
                break;
            }
        }

        if(goals.size() > 0){
        State min = goals.get(0);
        for (int i = 1; i < goals.size(); i++) {
            if(goals.get(i).exploredCells.size() < min.exploredCells.size()){
                min = goals.get(i);
            }
        }
        // for (int i = 0; i < min.exploredCells.size(); i++) {
        //     System.out.println(min.exploredCells.get(i).y + " " + min.exploredCells.get(i).x);
        // }
    
        String plan = min.currentPlan.substring(0, min.currentPlan.length() - 1);
        String deaths = min.grid.deaths + "";
        String retrieved = min.grid.agent.blackBoxesRetrieved + "";
        String nodes = min.exploredCells.size() + "";
    
        return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
        }
        else{
        return "No way I reach this!!!";
    }
}

   
    private static boolean isBreak(State currentState) {
        boolean isBreak = false;

        // check if there is no passengers alive
        if (currentState.grid.alivePassengers <= 0 && currentState.grid.agent.passengers <= 0) {
            // Ship[] ships = currentState.grid.ships;
            isBreak = true;

            for (int i = 0; i < currentState.grid.cells.length; i++) {
                for (int j = 0; j < currentState.grid.cells[i].length; j++) {
                    if (currentState.grid.cells[i][j].isShip)
                        if (currentState.grid.cells[i][j].ship.passengers > 0 ||
                                !currentState.grid.cells[i][j].ship.blackBoxDestroyed) {
                            // System.out.println("YADYYYY ELNEEELAAAA");
                            // System.out.println(currentState.grid.cells[i][j].ship.passengers);
                            // System.out.println(currentState.grid.cells[i][j].ship.blackBoxDestroyed + " "
                                    // + i + " " + j);
                            isBreak = false;
                            break;
                        }
                }
            }
            // check if there is balck boxes left
            // for (int i = 0; i < ships.length; i++) {
            // System.out.println(ships[i].blackBoxDestroyed);
            // if (ships[i].isSunk && !ships[i].blackBoxDestroyed) {
            // System.out.println("Blackbox destoryed: " + ships[i].blackBoxDestroyed);
            // isBreak = false;
            // break;
            // }
            // }
        }
        return isBreak;
    }


    public static boolean check(TrackRecord comingPair, Set<TrackRecord> exploredStates, State cState){
        // if(cState.currentPlan.equals("down,down,pickup,retrieve,up,")){
        //     System.out.println("The Record: "+ comingPair.currCell.y + " "+ comingPair.currCell.x+ " "+ comingPair.alive+ " "+ comingPair.onBoard + " "+comingPair.dead+ " "+comingPair.saved);
        //     for(TrackRecord a:exploredStates){
        //         System.out.println(a.currCell.y+ " "+ a.currCell.x+ " "+ a.alive+" "+a.onBoard + " "+ a.dead + " "+ a.saved);
        //     }
        // }
        boolean result = false;
        for(TrackRecord a : exploredStates) {
            if(a.currCell.y == comingPair.currCell.y && a.currCell.x== comingPair.currCell.x 
            && a.alive == comingPair.alive && a.onBoard == comingPair.onBoard && a.blackBoxes == comingPair.blackBoxes
            && a.dead == comingPair.dead && a.saved ==comingPair.saved) {
                
                result=true;
                break;
            }
          }
          return result;
    }
    
    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println(solve("3,4;97;1,2;0,1;3,2,65;", "BF", false));
    }
}
