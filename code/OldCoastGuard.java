package code;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class OldCoastGuard {

    public static String GenGrid() {
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

    public static String solve(String stringGrid, String strategy, Boolean visualize) {
        String[] gridParts = stringGrid.split(";");

        String[] gridSize = gridParts[0].split(",");
        int m = Integer.parseInt(gridSize[0]);
        int n = Integer.parseInt(gridSize[1]);

        int agentCapacity = Integer.parseInt(gridParts[1]);
        System.out.println(agentCapacity +"Akhryyyy");

        String[] agentPosition = gridParts[2].split(",");
        int agentX = Integer.parseInt(agentPosition[1]);
        int agentY = Integer.parseInt(agentPosition[0]);
        Agent agent = new Agent(agentCapacity, agentY, agentX);

        String[] stringStations = gridParts[3].split(",");
        Station[] stations = new Station[stringStations.length / 2];

        String[] stringShips = gridParts[4].split(",");
        Ship[] ships = new Ship[stringShips.length / 3];

        for (int i = 0; i < stringShips.length; i += 3) {
            System.out.println("Ship " + (i + 1) / 3);
            ships[(i + 1) / 3] = new Ship(Integer.parseInt(stringShips[i + 2]), Integer.parseInt(stringShips[i]),
                    Integer.parseInt(stringShips[i + 1]));
        }

        for (int i = 0; i < stringStations.length; i += 2) {
            stations[(i + 1) / 2] = new Station(Integer.parseInt(stringStations[i]),
                    Integer.parseInt(stringStations[i + 1]));
        }

        System.out.println("Ships: " + ships.length);
        for (int i = 0; i < ships.length; i++) {
            System.out.println(ships[i].passengers);
        }
        System.out.println("Stations: " + stations.length);
        for (int i = 0; i < stations.length; i++) {
            System.out.println(stations[i].y + " " + stations[i].x);
        }
        System.out.println("###### "+ agent.capacity );
        Grid grid = new Grid(m, n, agent, ships, stations);

        Cell[][] cells = grid.cells;

        for (int i = 0; i < ships.length; i++) {
            // ships[i].grid = grid;
            cells[ships[i].y][ships[i].x] = new Cell(ships[i].y, ships[i].x, ships[i]);
        }

        for (int i = 0; i < stations.length; i++) {
            cells[stations[i].y][stations[i].x] = new Cell(stations[i].y, stations[i].x, stations[i]);
        }

        cells[agent.y][agent.x] = new Cell(agent.y, agent.x);

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
        if (strategy.equals("GR1")) {
            output = SolveGR(1, grid);
        }
        if (strategy.equals("GR2")) {
            output = SolveGR(2, grid);
        }
        if (strategy.equals("AS1")) {
            output = SolveAS(1, grid);
        }
        if (strategy.equals("AS2")) {
            output = SolveAS(2, grid);
        }
        return output;
    }

    public static String SolveBF(Grid grid) {
        Queue<State> queue = new LinkedList<>();
        // Cell agentIntialCell = grid.cells[grid.agent.y][grid.agent.x];

        // initial position for agent and set the initial node to explored
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x], grid);
        initialState.stateType = "move";
        // initialState.exploredStates.add(new TrackRecord(initialState));
        queue.add(initialState);

        while (!queue.isEmpty()) {
            // retrieve the initial agent position
            State currentState = queue.peek();
            int x = currentState.currentCell.positionX;
            int y = currentState.currentCell.positionY;

            currentState.grid.agent.y = y;
            currentState.grid.agent.x = x;


            System.out.println("(" + y + "," + x + ")");
            System.out.println(currentState.currentPlan);

            
                if (currentState.currentCell.isOccupied) {

                    if (currentState.currentCell.occupant instanceof Station) {
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                        new Grid(currentState.grid),
                        currentState, currentState.currentPlan + "drop,", currentState.nodesCounter, "drop");
                        newState.stateType = "drop";
                        if(!currentState.exploredStates.contains(new TrackRecord(newState))){
                        currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }
                    if (currentState.currentCell.occupant instanceof Ship
                            && !(((Ship) currentState.currentCell.occupant).isSunk)) {
                        System.out.println("PIckkkkkinnnggggggg");
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                        new Grid(currentState.grid),
                                currentState, currentState.currentPlan + "pickup,", currentState.nodesCounter, "pickup");
                        newState.stateType = "pick";
                        if(!currentState.exploredStates.contains(new TrackRecord(newState))){
                            currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }}
                    if (currentState.currentCell.occupant instanceof Ship
                            && ((Ship) currentState.currentCell.occupant).isSunk
                            && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                        new Grid(currentState.grid),
                                currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter, "retrieve");
                        newState.stateType = "retrieve";
                        if(!currentState.exploredStates.contains(new TrackRecord(newState))){
                            currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }}

                // go to adjacent cells
                // add left cell to the queue
                if (currentState.grid.agent.moveLeft(currentState)) {
                    State newState = new State(currentState.grid.cells[y][x - 1], currentState.exploredStates,
                    new Grid(currentState.grid),
                            currentState, currentState.currentPlan + "left,", currentState.nodesCounter + 1);
                    Boolean isLeft = true;
                    newState.stateType = "move";
                    for (int i = 0; i < currentState.exploredStates.size(); i++) {
                        if (currentState.exploredStates.contains(new TrackRecord(newState))) {
                            isLeft = false;
                            break;
                        }
                    }
                    if (isLeft && !currentState.exploredStates.contains(new TrackRecord(newState))) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }

                // add top cell to the queue
                if (currentState.grid.agent.moveUp(currentState)) {
                    State newState = new State(currentState.grid.cells[y - 1][x], currentState.exploredStates,
                    new Grid(currentState.grid),
                            currentState, currentState.currentPlan + "up,", currentState.nodesCounter + 1);
                    Boolean isUp = true;
                    newState.stateType = "move";
                    for (int i = 0; i < currentState.exploredStates.size(); i++) {
                        if (currentState.exploredStates.contains(new TrackRecord(newState))) {
                            isUp = false;
                            break;
                        }
                    }
                    if (isUp && !currentState.exploredStates.contains(new TrackRecord(newState))) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }
                // add right cell to the queue
                if (currentState.grid.agent.moveRight(currentState)) {
                    State newState = new State(currentState.grid.cells[y][x + 1], currentState.exploredStates,
                    new Grid(currentState.grid),
                            currentState, currentState.currentPlan + "right,", currentState.nodesCounter + 1);
                    Boolean isRight = true;
                    newState.stateType = "move";
                    for (int i = 0; i < currentState.exploredStates.size(); i++) {
                        if (currentState.exploredStates.contains(new TrackRecord(newState))) {
                            isRight = false;
                            break;
                        }
                    }
                    if (isRight&& !currentState.exploredStates.contains(new TrackRecord(newState))) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }

                // add bottom cell to the queue
                if (currentState.grid.agent.moveDown(currentState)) {
                    State newState = new State(currentState.grid.cells[y + 1][x], currentState.exploredStates,
                    new Grid(currentState.grid),
                            currentState, currentState.currentPlan + "down,", currentState.nodesCounter + 1);
                    Boolean isDown = true;
                    newState.stateType = "move";
                    for (int i = 0; i < currentState.exploredStates.size(); i++) {
                        if (currentState.exploredStates.contains(new TrackRecord(newState))) {
                            isDown = false;
                            break;
                        }
                    }
                    if (isDown&& !currentState.exploredStates.contains(new TrackRecord(newState))) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        queue.add(newState);
                    }
                }
                // currentState.grid.agent.timeStep(currentState);
                if (currentState.stateType.equals("pick")) {
                    currentState.grid.agent.pickUpPassengers(currentState);
                }
                if (currentState.currentCell.occupant instanceof Ship
                        && ((Ship) currentState.currentCell.occupant).isSunk
                        && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                    State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                    new Grid(currentState.grid),
                            currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter);
                    newState.stateType = "retrieve";
                    if(!currentState.exploredStates.contains(new TrackRecord(newState))){
                    currentState.exploredStates.add(new TrackRecord(newState));
                    queue.add(newState);
                }}
                if (currentState.stateType.equals("retrieve")) {
                    currentState.grid.agent.retrieveBlackBox(currentState);
                }

                if (currentState.stateType.equals("drop")) {
                    currentState.grid.agent.dropPassengers(currentState);
                }            

            queue.remove();

            Boolean isBreak = false;
            System.out.println("Passengers on board "+ currentState.grid.agent.passengers);
            System.out.println("Alive Passengers:  " + currentState.grid.alivePassengers);

            if (currentState.grid.alivePassengers == 0) {
                Ship[] ships = currentState.grid.ships;
                isBreak = true;

                // System.out.println("SHIPS: " + ships.length);
                // System.out.println(ships[0].blackBoxDestroyed);

                for (int i = 0; i < ships.length; i++) {
                    if (ships[i].isSunk && ships[i].blackBoxDestroyed == false) {
                        isBreak = false;
                        break;
                    }
                }

                // System.out.println("Breaking: " + isBreak);
                if (isBreak && currentState.grid.agent.passengers==0) {
                    // System.out.println("Current number of passengers: "+ currentState.grid.agent.passengers);
                    // if (currentState.grid.agent.passengers != 0) {
                    //     // System.out.println("going to station");
                    //     currentState.currentPlan += currentState.grid.agent.goToStation(currentState);
                    // }
                    String plan = currentState.currentPlan.substring(0, currentState.currentPlan.length() - 1);
                    String deaths = currentState.grid.deaths + "";
                    String retrieved = currentState.grid.agent.blackBoxesRetrieved + "";
                    String nodes = currentState.nodesCounter + "";

                    return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
                }
            }
        }
        return "No way I reach this!";

    }

    public static String SolveDF(Grid grid) {
        Stack<State> stack = new Stack<State>();
        // Cell agentIntialCell = grid.cells[grid.agent.y][grid.agent.x];

        // initial position for agent and set the initial node to explored
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x], grid);
        initialState.stateType = "move";
        // initialState.exploredStates.add(new TrackRecord(initialState));
        stack.push(initialState);

        while (!stack.isEmpty()) {
            // retrieve the initial agent position
            State currentState = stack.pop();
            int x = currentState.currentCell.positionX;
            int y = currentState.currentCell.positionY;

            currentState.grid.agent.y = y;
            currentState.grid.agent.x = x;
            System.out.println(currentState.stateType);

            if (currentState.stateType.equals("move")) {

                if (currentState.currentCell.isOccupied) {

                    if (currentState.currentCell.occupant instanceof Station) {
                        currentState.grid.agent.dropPassengers(currentState);
                    }

                    if (currentState.currentCell.occupant instanceof Ship
                            && !(((Ship) currentState.currentCell.occupant).isSunk)) {
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                                currentState.grid,
                                currentState, currentState.currentPlan + "pickup,", currentState.nodesCounter);
                        newState.stateType = "pick";
                        System.out.println("ADDED A PICK TO THE STACK!");
                        currentState.exploredStates.add(new TrackRecord(newState));

                        stack.push(newState);
                    }
                    if (currentState.currentCell.occupant instanceof Ship
                            && ((Ship) currentState.currentCell.occupant).isSunk
                            && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                                currentState.grid,
                                currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter);
                        newState.stateType = "retrieve";
                        System.out.println("ADDED A RETRIEVE TO THE STACK!");
                        currentState.exploredStates.add(new TrackRecord(newState));

                        stack.push(newState);
                    }
                }
                System.out.println("(" + y + "," + x + ")");

                // go to adjacent cells
                // add left cell to the queue
                State newState = new State();
                if (x - 1 >= 0) {
                    newState = new State(currentState.grid.cells[y][x - 1], currentState.exploredStates,
                            currentState.grid,
                            currentState, currentState.currentPlan + "left,", currentState.nodesCounter + 1);
                    newState.stateType = "move";
                }
                if (currentState.grid.agent.moveLeft(currentState)) {
                    System.out.println("ADDED A LEFT MOVE TO THE STACK!");
                    currentState.exploredStates.add(new TrackRecord(newState));

                    stack.push(newState);
                }

                // add top cell to the queue
                if (y - 1 >= 0) {
                    newState = new State(currentState.grid.cells[y - 1][x], currentState.exploredStates,
                            currentState.grid,
                            currentState, currentState.currentPlan + "up,", currentState.nodesCounter + 1);
                    newState.stateType = "move";
                }
                if (currentState.grid.agent.moveUp(currentState)) {
                    System.out.println("ADDED A UP MOVE TO THE STACK!");

                    currentState.exploredStates.add(new TrackRecord(newState));
                    stack.push(newState);
                }
                // add right cell to the queue
                newState = new State(currentState.grid.cells[y][x + 1], currentState.exploredStates,
                        currentState.grid,
                        currentState, currentState.currentPlan + "right,", currentState.nodesCounter + 1);
                newState.stateType = "move";
                if (currentState.grid.agent.moveRight(currentState)) {
                    System.out.println("ADDED A RIGHT MOVE TO THE STACK!");

                    currentState.exploredStates.add(new TrackRecord(newState));
                    stack.push(newState);
                }
                // add bottom cell to the queue
                newState = new State(currentState.grid.cells[y + 1][x], currentState.exploredStates,
                        currentState.grid,
                        currentState, currentState.currentPlan + "down,", currentState.nodesCounter + 1);
                newState.stateType = "move";
                if (currentState.grid.agent.moveDown(currentState)) {
                    System.out.println("ADDED A DOWN MOVE TO THE STACK!");

                    currentState.exploredStates.add(new TrackRecord(newState));
                    stack.push(newState);
                }

            } else {
                if (currentState.stateType.equals("pick")) {
                    System.out.println("pickup state!!");
                    currentState.grid.agent.pickUpPassengers(currentState);
                }
                if (currentState.currentCell.occupant instanceof Ship
                        && ((Ship) currentState.currentCell.occupant).isSunk
                        && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                    State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                            currentState.grid,
                            currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter);
                    newState.stateType = "retrieve";
                    currentState.exploredStates.add(new TrackRecord(newState));
                    stack.push(newState);
                }
                if (currentState.stateType.equals("retrieve")) {
                    currentState.grid.agent.retrieveBlackBox(currentState);
                }
            }
            System.out.println(
                    "Items left instack: " + stack.size() + " and the next state is " + stack.peek().stateType);

            Boolean isBreak = false;
            System.out.println("Alive Passengers:  " + currentState.grid.alivePassengers);

            if (currentState.grid.alivePassengers == 0) {
                Ship[] ships = currentState.grid.ships;
                isBreak = true;

                System.out.println("SHIPS: " + ships.length);
                System.out.println(ships[0].blackBoxDestroyed);

                for (int i = 0; i < ships.length; i++) {
                    if (ships[i].isSunk && ships[i].blackBoxDestroyed == false) {
                        isBreak = false;
                        break;
                    }
                }

                System.out.println("Breaking: " + isBreak);
                if (isBreak) {
                    if (currentState.grid.agent.passengers != 0) {
                        System.out.println("going to station");
                        currentState.currentPlan += currentState.grid.agent.goToStation(currentState);
                    }
                    String plan = currentState.currentPlan.substring(0, currentState.currentPlan.length() - 1);
                    String deaths = currentState.grid.deaths + "";
                    String retrieved = currentState.grid.agent.blackBoxesRetrieved + "";
                    String nodes = currentState.nodesCounter + "";

                    return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
                }
            }
        }
        return "No way I reach this!";

    }

    public static String SolveID(Grid grid) {
        Stack<State> stack = new Stack<State>();
        Set<TrackRecord> exploredStates = new HashSet<TrackRecord>();

        // initial position for agent and set the initial node to explored
        State initialState = new State(grid.cells[grid.agent.y][grid.agent.x], grid);
        initialState.stateType = "move";
        initialState.depth = 0;
        Boolean isBreak = false;

        for (int i = 0;; i++) {
            exploredStates = new HashSet<TrackRecord>();
            // for (int j = 0; j < initialState.grid.cells.length; j++) {
            // for (int j2 = 0; j2 < initialState.grid.cells[j].length; j2++) {
            // initialState.grid.cells[j][j2].isExplored = false;
            // }
            // }
            // Cell agentIntialCell = grid.cells[grid.agent.y][grid.agent.x];
            initialState.exploredStates = exploredStates;
            stack.push(initialState);
            while (!stack.isEmpty()) {
                // retrieve the initial agent position
                State currentState = stack.pop();
                int x = currentState.currentCell.positionX;
                int y = currentState.currentCell.positionY;

                currentState.grid.agent.y = y;
                currentState.grid.agent.x = x;

                if (currentState.stateType.equals("move")) {

                    if (currentState.currentCell.isOccupied) {

                        if (currentState.currentCell.occupant instanceof Station) {
                            currentState.grid.agent.dropPassengers(currentState);
                        }

                        if (currentState.depth + 1 <= i && currentState.currentCell.occupant instanceof Ship
                                && !(((Ship) currentState.currentCell.occupant).isSunk)) {
                            State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                                    currentState.grid,
                                    currentState, currentState.currentPlan + "pickup,", currentState.nodesCounter);
                            newState.stateType = "pick";

                            currentState.exploredStates.add(new TrackRecord(newState));
                            stack.push(newState);
                            System.out.println("ADDED A PICK TO THE STACK!");
                        }
                        if (currentState.depth + 1 <= i && currentState.currentCell.occupant instanceof Ship
                                && ((Ship) currentState.currentCell.occupant).isSunk
                                && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                            State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                                    currentState.grid,
                                    currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter);
                            newState.stateType = "retrieve";
                            currentState.exploredStates.add(new TrackRecord(newState));
                            stack.push(newState);
                            System.out.println("ADDED A RETRIEVE TO THE STACK!");
                        }
                    }
                    System.out.println("(" + y + "," + x + ")");

                    // go to adjacent cells
                    // add left cell to the queue
                    State newState = new State();
                    if (x - 1 >= 0) {
                        newState = new State(currentState.grid.cells[y][x - 1], currentState.exploredStates,
                                currentState.grid,
                                currentState, currentState.currentPlan + "left,", currentState.nodesCounter + 1);
                        newState.stateType = "move";
                    }
                    if (currentState.depth + 1 <= i && currentState.grid.agent.moveLeft(currentState)) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        stack.push(newState);
                        System.out.println("ADDED A LEFT MOVE TO THE STACK! ");

                    }

                    // add top cell to the queue
                    if (y - 1 >= 0) {
                        newState = new State(currentState.grid.cells[y - 1][x], currentState.exploredStates,
                                currentState.grid,
                                currentState, currentState.currentPlan + "up,", currentState.nodesCounter + 1);
                        newState.stateType = "move";
                    }
                    if (currentState.depth + 1 <= i && currentState.grid.agent.moveUp(currentState)) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        stack.push(newState);
                        System.out.println("ADDED A UP MOVE TO THE STACK!");
                    }

                    // add right cell to the queue
                    newState = new State(currentState.grid.cells[y][x + 1], currentState.exploredStates,
                            currentState.grid,
                            currentState, currentState.currentPlan + "right,", currentState.nodesCounter + 1);
                    newState.stateType = "move";
                    if (currentState.depth + 1 <= i && currentState.grid.agent.moveRight(currentState)) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        stack.push(newState);
                        System.out.println("ADDED A RIGHT MOVE TO THE STACK!");
                    }

                    // add bottom cell to the queue
                    newState = new State(currentState.grid.cells[y + 1][x], currentState.exploredStates,
                            currentState.grid,
                            currentState, currentState.currentPlan + "down,", currentState.nodesCounter + 1);
                    newState.stateType = "move";
                    if (currentState.depth + 1 <= i && currentState.grid.agent.moveDown(currentState)) {
                        currentState.exploredStates.add(new TrackRecord(newState));
                        stack.push(newState);
                        System.out.println("ADDED A DOWN MOVE TO THE STACK!");
                    }

                } else {
                    if (currentState.stateType.equals("pick")) {
                        System.out.println(currentState.currentPlan);
                        currentState.grid.agent.pickUpPassengers(currentState);
                    }

                    if (currentState.depth + 1 <= i && currentState.currentCell.occupant instanceof Ship
                            && ((Ship) currentState.currentCell.occupant).isSunk
                            && !(((Ship) currentState.currentCell.occupant).blackBoxDestroyed)) {
                        State newState = new State(currentState.grid.cells[y][x], currentState.exploredStates,
                                currentState.grid,
                                currentState, currentState.currentPlan + "retrieve,", currentState.nodesCounter);
                        newState.stateType = "retrieve";
                        currentState.exploredStates.add(new TrackRecord(newState));
                        stack.push(newState);
                        System.out.println("ADDED A Retrieve MOVE TO THE STACK!");
                    }
                    if (currentState.stateType.equals("retrieve")) {
                        currentState.grid.agent.retrieveBlackBox(currentState);
                    }
                }

                System.out.println("Alive Passengers:  " + currentState.grid.alivePassengers);

                if (currentState.grid.alivePassengers == 0) {
                    Ship[] ships = currentState.grid.ships;
                    isBreak = true;

                    System.out.println("SHIPS: " + ships.length);
                    System.out.println(ships[0].blackBoxDestroyed);

                    for (int j = 0; j < ships.length; j++) {
                        if (ships[j].isSunk && ships[j].blackBoxDestroyed == false) {
                            isBreak = false;
                            break;
                        }
                    }

                    System.out.println("Breaking: " + isBreak);
                    if (isBreak) {
                        if (currentState.grid.agent.passengers != 0) {
                            System.out.println("going to station");
                            currentState.currentPlan += currentState.grid.agent.goToStation(currentState);
                        }
                        String plan = currentState.currentPlan.substring(0, currentState.currentPlan.length() - 1);
                        String deaths = currentState.grid.deaths + "";
                        String retrieved = currentState.grid.agent.blackBoxesRetrieved + "";
                        String nodes = currentState.nodesCounter + "";

                        return plan + ";" + deaths + ";" + retrieved + ";" + nodes;
                    }
                }
            }
            if (isBreak || i > 100) {
                break;
            } else {
                System.out.println("Iterate: " + i);
            }

        }
        return "No way I reach this!";

    }

    public static String SolveGR(int i, Grid grid) {
        return "";

    }

    public static String SolveAS(int i, Grid grid) {
        return "";

    }

    public static void main(String[] args) {
        // String genGrid= GenGrid();
        // System.out.println(genGrid);
        System.out.println(solve("2,2;30;0,1;0,0;1,1,60;", "BF", false));
    }
}