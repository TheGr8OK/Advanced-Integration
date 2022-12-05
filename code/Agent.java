package code;

import java.lang.Math;
// import java.util.ArrayList;

public class Agent implements Cloneable {
    // Grid grid;
    byte freeSpace;
    byte passengers;
    byte x;
    byte y;
    byte capacity;
    byte blackBoxesRetrieved;
    byte blackBoxesDestroyed;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + freeSpace;
        result = prime * result + passengers;
        result = prime * result + x;
        result = prime * result + y;
        // result = prime * result + capacity;
        result = prime * result + blackBoxesRetrieved;
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
        Agent other = (Agent) obj;
        // if (freeSpace != other.freeSpace)
        // return false;
        if (passengers != other.passengers)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        // if (capacity != other.capacity)
        // return false;
        if (blackBoxesRetrieved != other.blackBoxesRetrieved)
            return false;
        return true;
    }

    public Agent(Agent a) {
        // this.grid = a.grid;
        this.freeSpace = a.freeSpace;
        this.passengers = a.passengers;
        this.x = a.x;
        this.y = a.y;
        this.capacity = a.capacity;
        this.blackBoxesRetrieved = a.blackBoxesRetrieved;

    }

    public Agent(byte capacity, byte y, byte x) {
        this.freeSpace = capacity < 30 ? 30 : (capacity > 100) ? 100 : capacity;
        this.x = x;
        this.y = y;
        this.capacity = freeSpace;
        blackBoxesRetrieved = 0;
        passengers = 0;
    }

    public Agent(byte capacity, byte y, byte x, Grid grid) {
        this.freeSpace = capacity < 30 ? 30 : (capacity > 100) ? 100 : capacity;
        this.x = x;
        this.y = y;
        // this.grid = grid;
        this.capacity = freeSpace;
        blackBoxesRetrieved = 0;
        passengers = 0;
        // handle if x and y outside grid
    }

    // public void timeStep(State currentState){
    // // for (int i = 0; i < currentState.grid.ships.length; i++) {
    // currentState.grid.timeStep();
    // // }
    // }

    public State isPickUpPassengers(State currentState) throws CloneNotSupportedException {
        Ship ship = currentState.grid.cells[y][x].ship;

        // check if i am in the same cell as the ship
        if (!(ship.x == x) || !(ship.y == y)) {
            throw new Error("agent is not in the same position as the station!");
        }

        // check if I just picked up passengers on the previous state
        if (currentState.stateType.equals("pickup")) {
            return null;
        }

        // creating a new state to pickup passengers
        // System.out.println("added pickup state");
        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y][x], clonedState.grid,
                clonedState, currentState.currentPlan + "pickup,", "pickup", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        return newState;
    }

    public State isDropPassengers(State currentState) throws CloneNotSupportedException {
        Station station = currentState.grid.cells[y][x].station;

        // check if I am in the same cell as the station
        if (!(station.x == x) || !(station.y == y)) {
            throw new Error("agent is not in the same position as the station!");
        }

        // check if i just dropped passengers or there is no passengers to drop
        if (currentState.stateType.equals("drop") || currentState.grid.agent.passengers <= 0) {
            return null;
        }

        // creating a new state to to drop passengers
        // System.out.println("2nd clone");

        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y][x], clonedState.grid,
                clonedState, currentState.currentPlan + "drop,", "drop", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        return newState;
    }

    public State isRetrieveBlackBox(State currentState) throws CloneNotSupportedException {
        Ship ship = currentState.grid.cells[y][x].ship;

        // check if i am in the same cell as the wrecked ship
        if (!(ship.x == x) || !(ship.y == y)) {
            throw new Error("agent is not in the same position as the station!");
        }

        // check if I just retrieved the box
        if (currentState.stateType.equals("retrieve")) {
            return null;
        }

        // System.out.println("3rd clone");
        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y][x], clonedState.grid,
                clonedState, currentState.currentPlan + "retrieve,", "retrieve", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        return newState;
    }

    public State isMoveLeft(State currentState) throws CloneNotSupportedException {

        // check for out of bounds
        if (currentState.grid.agent.x - 1 < 0) {
            return null;
        }

        if (currentState.depth > 1 && currentState.stateType.equals("right")) {
            return null;
        }

        // creating a new state to move left
        // System.out.println("4th clone");
        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y][x - 1], clonedState.grid,
                clonedState, currentState.currentPlan + "left,", "left", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        // check if i visited this state already
        // TrackRecord isNew = new TrackRecord(newState);

        // System.out.println(check(isNew, currentState.exploredStates) +" left");
        // if (check(isNew, currentState.exploredStates)) {
        // return null;
        // } else {
        // newState.exploredStates.add(isNew);
        // }

        return newState;
    }

    public State isMoveUp(State currentState) throws CloneNotSupportedException {

        // check for out of bounds
        if (currentState.grid.agent.y - 1 < 0) {
            return null;
        }

        if (currentState.depth > 1 && currentState.stateType.equals("down")) {
            return null;
        }

        // creating a new state to move up
        // System.out.println("5th clone");
        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y - 1][x], clonedState.grid,
                clonedState, currentState.currentPlan + "up,", "up", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        // check if i visited this state already
        // TrackRecord isNew = new TrackRecord(newState);

        // System.out.println(check(isNew, currentState.exploredStates)+ " up");
        // if (check(isNew, currentState.exploredStates)) {
        // return null;
        // } else {
        // newState.exploredStates.add(isNew);
        // }

        return newState;
    }

    public State isMoveRight(State currentState) throws CloneNotSupportedException {

        // check for out of bounds
        if (currentState.grid.agent.x + 1 >= currentState.grid.m) {
            return null;
        }

        if (currentState.depth > 1 && currentState.stateType.equals("left")) {
            return null;
        }

        // creating a new state to move right
        // System.out.println("6th clone");
        State clonedState = (State) currentState.clone();

        State newState = new State(clonedState.grid.cells[y][x + 1], clonedState.grid,
                clonedState, currentState.currentPlan + "right,", "right", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        // check if i visited this state already
        // TrackRecord isNew = new TrackRecord(newState);
        // System.out.println(check(isNew, currentState.exploredStates)+ " right");
        // if (check(isNew, currentState.exploredStates)) {
        // return null;
        // } else {
        // newState.exploredStates.add(isNew);
        // }

        return newState;
    }

    public State isMoveDown(State currentState) throws CloneNotSupportedException {

        // check for out of bounds
        if (currentState.grid.agent.y + 1 >= currentState.grid.n) {
            return null;
        }

        if (currentState.depth > 1 && currentState.stateType.equals("up")) {
            return null;
        }

        // creating a new state to move down
        // System.out.println("7th clone");

        State clonedState = (State) currentState.clone();
        State newState = new State(clonedState.grid.cells[y + 1][x], clonedState.grid,
                clonedState, currentState.currentPlan + "down,", "down", clonedState.exploredCells);
        newState.cost = new Cost(newState);

        // check if i visited this state already
        // TrackRecord isNew = new TrackRecord(newState);
        // System.out.println(check(isNew, currentState.exploredStates)+ " down");
        // if (check(isNew, currentState.exploredStates)) {
        // return null;
        // } else {
        // newState.exploredStates.add(isNew);
        // }

        return newState;
    }

    // do action pickup passengers from ship
    public void pickUpPassengers(State currentState, int y, int x) {

        // check if I have enough free space for all passengers
        if (currentState.grid.agent.freeSpace >= currentState.grid.cells[y][x].ship.passengers) {
            // System.out.println("Saved kolo: " + currentState.grid.cells[y][x].ship);
            currentState.grid.agent.passengers += currentState.grid.cells[y][x].ship.passengers;
            currentState.grid.agent.freeSpace -= currentState.grid.cells[y][x].ship.passengers;
            currentState.grid.alivePassengers -= currentState.grid.cells[y][x].ship.passengers;
            currentState.grid.cells[y][x].ship.passengers = 0;
            currentState.grid.cells[y][x].ship.isSunk = true;
        } else {
            // System.out.println("Saved shwaya: " + currentState.grid.agent.freeSpace);
            currentState.grid.alivePassengers -= currentState.grid.agent.freeSpace;
            currentState.grid.cells[y][x].ship.passengers -= currentState.grid.agent.freeSpace;
            currentState.grid.agent.passengers = currentState.grid.agent.capacity;
            currentState.grid.agent.freeSpace = 0;
        }
        // System.out.println("During CurrentState Passengers: " +
        // currentState.grid.agent.passengers);
    }

    // do action drop passengers in station
    public void dropPassengers(State currentState) {
        // Station station = currentState.grid.cells[y][x].station;

        // System.out.println("I droped " + passengers + " passengers");

        // currentState.grid.saved += passengers;
        // currentState.grid.alivePassengers -= currentState.grid.agent.passengers;
        freeSpace = currentState.grid.agent.capacity;
        passengers = 0;

    }

    // do action retrieve a black box
    public void retrieveBlackBox(State currentState) {
        // Ship ship = (Ship) currentState.currentCell.occupant;

        // System.out.println("haretrieve");
        currentState.grid.agent.blackBoxesRetrieved += 1;
        currentState.grid.cells[y][x].ship.blackBoxDestroyed = true;
    }

    public String goToStation(State currentState) {
        String path = "";
        Station[] stations = currentState.grid.stations;
        Station nearestStation = stations[0];
        int nearestSteps = Math.abs(stations[0].y - currentState.grid.agent.y)
                + Math.abs(stations[0].x - currentState.grid.agent.x);

        for (int i = 0; i < stations.length; i++) {
            int stepsCalculated = Math.abs(stations[i].y - currentState.grid.agent.y)
                    + Math.abs(stations[0].x - currentState.grid.agent.x);
            if (stepsCalculated < nearestSteps) {
                nearestSteps = stepsCalculated;
                nearestStation = stations[i];
            }
        }

        int differenceY = nearestStation.y - currentState.grid.agent.y;
        int differenceX = nearestStation.x - currentState.grid.agent.x;

        while (differenceY != 0) {
            if (differenceY > 0) {
                currentState.grid.agent.y++;
                path += "down,";
                differenceY--;
            } else {
                currentState.grid.agent.y--;
                path += "up,";
                differenceY++;
            }
        }

        while (differenceX != 0) {
            if (differenceX > 0) {
                currentState.grid.agent.x++;
                path += "right,";
                differenceX--;
            } else {
                currentState.grid.agent.x--;
                path += "left,";
                differenceX++;
            }
        }
        path += "drop,";
        return path.substring(0, path.length());
    }

    public Agent clone() throws CloneNotSupportedException {
        return (Agent) super.clone();
    }

}
