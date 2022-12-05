package code;

import java.util.ArrayList;
import java.util.HashSet;
// import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
// import java.util.Set;
import java.util.Stack;

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
        byte m = Byte.parseByte(gridSize[0]);
        byte n = Byte.parseByte(gridSize[1]);

        byte agentCapacity = Byte.parseByte(gridParts[1]);
        // System.out.println(agentCapacity + "Akhryyyy");

        String[] agentPosition = gridParts[2].split(",");
        byte agentX = Byte.parseByte(agentPosition[1]);
        byte agentY = Byte.parseByte(agentPosition[0]);
        Agent agent = new Agent(agentCapacity, agentY, agentX);

        String[] stringStations = gridParts[3].split(",");
        Station[] stations = new Station[stringStations.length / 2];

        String[] stringShips = gridParts[4].split(",");
        Ship[] ships = new Ship[stringShips.length / 3];

        for (int i = 0; i < stringShips.length; i += 3) {
            // System.out.println("Ship " + (i + 1) / 3);
            ships[(i + 1) / 3] = new Ship(Byte.parseByte(stringShips[i + 2]), Byte.parseByte(stringShips[i]),
                    Byte.parseByte(stringShips[i + 1]));
            alivePassengers += Integer.parseInt(stringShips[i + 2]);
        }

        for (int i = 0; i < stringStations.length; i += 2) {
            stations[(i + 1) / 2] = new Station(Byte.parseByte(stringStations[i]),
                    Byte.parseByte(stringStations[i + 1]));
        }

        // System.out.println("Ships: " + ships.length);
        // for (int i = 0; i < ships.length; i++) {
        // System.out.println(ships[i].passengers);
        // }
        // System.out.println("Stations: " + stations.length);
        // for (int i = 0; i < stations.length; i++) {
        // System.out.println(stations[i].y + " " + stations[i].x);
        // }
        // System.out.println("###### " + agent.capacity);
        Grid grid = new Grid(m, n, agent, stations, ships, alivePassengers);

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
        if (strategy.equals("ID")) {
            output = SolveID(grid);
        }
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
        // System.out.println(output+" out");
        return output;
    }

    private static String SolveID(Grid grid) throws CloneNotSupportedException {

        for (int i = 0;; i++) {
            HashSet<TrackRecord> exploredStates = new HashSet<TrackRecord>();
            Stack<State> stack = new Stack<State>();
            ArrayList<Pair> exploredCells = new ArrayList<Pair>();

            // adding the initial cell to the exploredCells
            exploredCells.add(new Pair(grid.agent.y, grid.agent.x));

            // Initial State of my problem
            State initialState = new State(grid.cells[grid.agent.y][grid.agent.x],
                    grid, null, "", "start", exploredCells, 0);

            // adding the initial state to my queue
            stack.push(initialState);

            int counter = 0;
            // int depth = 1000;

            while (!stack.isEmpty()) {

                // retrieving my current state from the queue
                State currentState = stack.pop();
                byte x = currentState.currentCell.positionX;
                byte y = currentState.currentCell.positionY;

                if (currentState.depth > i) {
                    continue;
                }

                // check if current state is a goal state
                if (isBreak(currentState)) {
                    String plan = currentState.currentPlan.substring(0, currentState.currentPlan.length() - 1);
                    String deaths = currentState.grid.deaths + "";
                    String retrieved = currentState.grid.agent.blackBoxesRetrieved + "";
                    String nodes = currentState.exploredCells.size() + "";

                    return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
                    // depth = currentState.depth;
                }

                if (counter > 1000000000) {
                    break;
                }

                // System.out.println("(" + y + "," + x + ")");
                TrackRecord isNew = new TrackRecord(new Pair(y, x), 
                        currentState.grid.agent.passengers,
                        currentState.grid.agent.blackBoxesRetrieved, currentState.grid.deaths,
                        currentState.depth,
                        currentState.exploredCells.size(), currentState.grid.ships);
                if (currentState.depth > 0 && !exploredStates.add(isNew)) {
                    // System.out.println("I AM PASSING!!!! " + currentState.currentPlan);
                    continue;
                }

                currentState.grid.agent.y = y;
                currentState.grid.agent.x = x;

                Pair p = new Pair(y, x);
                boolean addingNew = true;
                for (int i1 = 0; i1 < currentState.exploredCells.size(); i1++) {
                    if (currentState.exploredCells.get(i1).y == p.y && currentState.exploredCells.get(i1).x == p.x) {
                        addingNew = false;
                        break;
                    }
                }
                if (addingNew) {
                    currentState.exploredCells.add(p);
                }

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
                        stack.push(newState);
                    }
                }

                // check if the current cell is a ship
                if (currentState.grid.cells[y][x].isOccupied && currentState.grid.cells[y][x].isShip) {
                    Ship ship = currentState.grid.cells[y][x].ship;

                    // check if i can pickup passengers
                    if (!ship.isSunk) {
                        State newState = currentState.grid.agent.isPickUpPassengers(currentState);
                        if (newState != null) {
                            stack.push(newState);
                        }
                    } else {
                        // check if black box is retrievable
                        if (!ship.blackBoxDestroyed) {
                            State newState = currentState.grid.agent.isRetrieveBlackBox(currentState);
                            if (newState != null) {
                                stack.push(newState);
                            }
                        }
                    }
                }

                // adding the 4 movement directions
                // check if I can move Left
                State leftState = currentState.grid.agent.isMoveLeft(currentState);
                if (leftState != null) {
                    stack.push(leftState);
                }

                // check if I can move Up
                State upState = currentState.grid.agent.isMoveUp(currentState);
                if (upState != null) {
                    stack.push(upState);
                }

                // check if I can move Right
                State rightState = currentState.grid.agent.isMoveRight(currentState);
                if (rightState != null) {
                    stack.push(rightState);
                }

                // check if I can move Down
                State downState = currentState.grid.agent.isMoveDown(currentState);
                if (downState != null) {
                    stack.push(downState);
                }

                // System.out.println("Black boxes retrievd: " +
                // currentState.grid.agent.blackBoxesRetrieved);
                // System.out.println("Depth: " + currentState.depth);
                // System.out.println("OnBoard: " + currentState.grid.agent.passengers);
                // System.out.println("Ship Passengers: " +
                // currentState.grid.cells[3][2].ship.passengers);
                // System.out.println(
                // "BlackBox Destroyed: " +
                // currentState.grid.cells[3][2].ship.blackBoxDestroyed);
                // System.out.println("Alive Passengers: " + currentState.grid.alivePassengers);

                // System.out.println(currentState.currentPlan);

                counter++;
                // System.out.println("Explored: "+ currentState.exploredCells.size());

                // check if I reached a goal state and will I break

            }
        }

        // return "No way I reach this!!!";

    }

    private static String SolveDF(Grid grid) throws CloneNotSupportedException {
        HashSet<TrackRecord> exploredStates = new HashSet<TrackRecord>();
        Stack<State> stack = new Stack<State>();
        ArrayList<Pair> exploredCells = new ArrayList<Pair>();

        // adding the initial cell to the exploredCells
        exploredCells.add(new Pair(grid.agent.y, grid.agent.x));

        // Initial State of my problem
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x],
                grid, null, "", "start", exploredCells, 0);

        // adding the initial state to my queue
        stack.push(initialState);

        int counter = 0;
        int depth = 1000;
        ArrayList<State> goals = new ArrayList<State>();

        while (!stack.isEmpty() && stack.peek().depth <= depth) {

            // retrieving my current state from the queue
            State currentState = stack.pop();
            byte x = currentState.currentCell.positionX;
            byte y = currentState.currentCell.positionY;

            System.out.println("(" + y + "," + x + ")");
            TrackRecord isNew = new TrackRecord(new Pair(y, x), 
                    currentState.grid.agent.passengers,
                    currentState.grid.agent.blackBoxesRetrieved, currentState.grid.deaths,
                    currentState.depth,
                    currentState.exploredCells.size(), currentState.grid.ships);
            if (currentState.depth > 0 && !exploredStates.add(isNew)) {
                // System.out.println("I AM PASSING!!!! " + currentState.currentPlan);
                continue;
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
            if (addingNew) {
                currentState.exploredCells.add(p);
            }

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
                    stack.push(newState);
                }
            }

            // check if the current cell is a ship
            if (currentState.grid.cells[y][x].isOccupied && currentState.grid.cells[y][x].isShip) {
                Ship ship = currentState.grid.cells[y][x].ship;

                // check if i can pickup passengers
                if (!ship.isSunk) {
                    State newState = currentState.grid.agent.isPickUpPassengers(currentState);
                    if (newState != null) {
                        stack.push(newState);
                    }
                } else {
                    // check if black box is retrievable
                    if (!ship.blackBoxDestroyed) {
                        State newState = currentState.grid.agent.isRetrieveBlackBox(currentState);
                        if (newState != null) {
                            stack.push(newState);
                        }
                    }
                }
            }

            // adding the 4 movement directions
            // check if I can move Left
            State leftState = currentState.grid.agent.isMoveLeft(currentState);
            if (leftState != null) {
                stack.push(leftState);
            }

            // check if I can move Up
            State upState = currentState.grid.agent.isMoveUp(currentState);
            if (upState != null) {
                stack.push(upState);
            }

            // check if I can move Right
            State rightState = currentState.grid.agent.isMoveRight(currentState);
            if (rightState != null) {
                stack.push(rightState);
            }

            // check if I can move Down
            State downState = currentState.grid.agent.isMoveDown(currentState);
            if (downState != null) {
                stack.push(downState);
            }

            // System.out.println("Black boxes retrievd: " +
            // currentState.grid.agent.blackBoxesRetrieved);
            System.out.println("Depth: " + currentState.depth);
            // System.out.println("OnBoard: " + currentState.grid.agent.passengers);
            // System.out.println("Ship Passengers: " +
            // currentState.grid.cells[3][2].ship.passengers);
            // System.out.println(
            // "BlackBox Destroyed: " +
            // currentState.grid.cells[3][2].ship.blackBoxDestroyed);
            // System.out.println("Alive Passengers: " + currentState.grid.alivePassengers);

            // System.out.println(currentState.currentPlan);

            counter++;
            // System.out.println("Explored: "+ currentState.exploredCells.size());

            // check if I reached a goal state and will I break
            if (isBreak(currentState)) {
                goals.add(currentState);
                depth = currentState.depth;
            }

            if (counter > 1000000000) {
                break;
            }
        }

        if (goals.size() > 0) {
            State min = goals.get(0);
            for (int i = 1; i < goals.size(); i++) {
                if (goals.get(i).exploredCells.size() < min.exploredCells.size()) {
                    min = goals.get(i);
                }
            }

            String plan = min.currentPlan.substring(0, min.currentPlan.length() - 1);
            String deaths = min.grid.deaths + "";
            String retrieved = min.grid.agent.blackBoxesRetrieved + "";
            String nodes = min.exploredCells.size() + "";

            return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
        } else {
            return "No way I reach this!!!";
        }
    }

    private static String SolveBF(Grid grid) throws CloneNotSupportedException {
        HashSet<TrackRecord> exploredStates = new HashSet<TrackRecord>();
        Queue<State> queue = new LinkedList<>();
        ArrayList<Pair> exploredCells = new ArrayList<Pair>();

        // adding the initial cell to the exploredCells
        exploredCells.add(new Pair(grid.agent.y, grid.agent.x));

        // Initial State of my problem
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x],
                grid, null, "", "start", exploredCells, 0);

        // adding the initial state to my queue
        queue.add(initialState);

        // int counter = 0;
        // int depth = 1000;
        // State goal;

        while (!queue.isEmpty()) {

            // retrieving my current state from the queue
            State currentState = queue.remove();
            byte x = currentState.currentCell.positionX;
            byte y = currentState.currentCell.positionY;

            TrackRecord isNew = new TrackRecord(new Pair(y, x),
                    currentState.grid.agent.passengers,
                    currentState.grid.agent.blackBoxesRetrieved, currentState.grid.deaths,
                    currentState.depth,
                    currentState.exploredCells.size(), currentState.grid.ships);
            if (currentState.depth > 0 && !exploredStates.add(isNew)) {
                // System.out.println("I AM PASSING!!!! " + currentState.currentPlan);
                continue;
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
            if (addingNew) {
                currentState.exploredCells.add(p);
            }

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
                queue.add(leftState);
            }

            // check if I can move Up
            State upState = currentState.grid.agent.isMoveUp(currentState);
            if (upState != null) {
                queue.add(upState);
            }

            // check if I can move Right
            State rightState = currentState.grid.agent.isMoveRight(currentState);
            if (rightState != null) {
                queue.add(rightState);
            }

            // check if I can move Down
            State downState = currentState.grid.agent.isMoveDown(currentState);
            if (downState != null) {
                queue.add(downState);
            }

            // System.out.println("Black boxes retrievd: " +
            // currentState.grid.agent.blackBoxesRetrieved);
            // System.out.println("Depth: " + currentState.depth);
            // System.out.println("OnBoard: " + currentState.grid.agent.passengers);
            // System.out.println("Ship Passengers: " +
            // currentState.grid.cells[3][2].ship.passengers);
            // System.out.println(
            // "BlackBox Destroyed: " +
            // currentState.grid.cells[3][2].ship.blackBoxDestroyed);
            // System.out.println("Alive Passengers: " + currentState.grid.alivePassengers);

            // System.out.println(currentState.currentPlan);

            // counter++;
            // System.out.println("Explored: "+ currentState.exploredCells.size());

            // check if I reached a goal state and will I break
            if (isBreak(currentState)) {
                String plan = currentState.currentPlan.substring(0, currentState.currentPlan.length() - 1);
                String deaths = currentState.grid.deaths + "";
                String retrieved = currentState.grid.agent.blackBoxesRetrieved + "";
                String nodes = currentState.exploredCells.size() + "";

                return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
            }

            // if (counter > 10000000) {
            // break;
            // }
        }

        return "No way I reach this!!!";

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
                            isBreak = false;
                            break;
                        }
                }
            }
        }
        return isBreak;
    }

    // public static boolean check(TrackRecord comingPair, Set<TrackRecord>
    // exploredStates, State cState) {
    // // if(cState.currentPlan.equals("down,down,pickup,retrieve,up,")){
    // // System.out.println("The Record: "+ comingPair.currCell.y + " "+
    // // comingPair.currCell.x+ " "+ comingPair.alive+ " "+ comingPair.onBoard + "
    // // "+comingPair.dead+ " "+comingPair.saved);
    // // for(TrackRecord a:exploredStates){
    // // System.out.println(a.currCell.y+ " "+ a.currCell.x+ " "+ a.alive+"
    // // "+a.onBoard + " "+ a.dead + " "+ a.saved);
    // // }
    // // }
    // boolean result = false;
    // for (TrackRecord a : exploredStates) {
    // if (a.currCell.y == comingPair.currCell.y && a.currCell.x ==
    // comingPair.currCell.x
    // && a.alive == comingPair.alive && a.onBoard == comingPair.onBoard
    // && a.blackBoxes == comingPair.blackBoxes
    // && a.dead == comingPair.dead && a.saved == comingPair.saved) {

    // result = true;
    // break;
    // }
    // }
    // return result;
    // }

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println(solve("3,4;97;1,2;0,1;3,2,65;", "ID", false));
    }
}
