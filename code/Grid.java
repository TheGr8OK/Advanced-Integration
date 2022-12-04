package code;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Grid implements Cloneable {
    int m;
    int n;
    int deaths;
    int alivePassengers;
    int saved;
    Cell[][] cells;
    Agent agent;
    Ship[] ships;
    // int[][] shipsPassengers;
    Station[] stations;

    // public Grid(Grid g) {
    // this.m = g.m;
    // this.n = g.n;
    // this.deaths = g.deaths;
    // this.alivePassengers = g.alivePassengers;
    // this.cells = g.cells;
    // this.agent = new Agent(g.agent);
    // this.ships = g.ships;
    // // this.shipsPassengers = g.shipsPassengers;
    // this.stations = g.stations;
    // }

   
    public Grid(int m, int n, int deaths, Agent agent, Station[] stations, int alivePassengers, Cell[][] cells,
            Ship[] ships, int saved) {
        this.m = m;
        this.n = n;
        this.agent = agent;
        this.ships = ships;
        this.stations = stations;
        this.alivePassengers = alivePassengers;
        this.deaths = deaths;
        this.cells = cells;
        this.saved = saved;
    }

   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + saved;
        result = prime * result + ((agent == null) ? 0 : agent.hashCode());
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
        Grid other = (Grid) obj;
        if (saved != other.saved)
            return false;
        if (agent == null) {
            if (other.agent != null)
                return false;
        } else if (!agent.equals(other.agent))
            return false;
        return true;
    }


    public Grid(int m, int n, Agent agent, Station[] stations, Ship[] ships, int alivePassengers) {
        this.m = m;
        this.n = n;
        this.agent = agent;
        this.ships = ships;
        this.stations = stations;
        this.alivePassengers = alivePassengers;

        deaths = 0;
        saved =0;
        // shipsPassengers = new int[n][m];

        // for (int i = 0; i < ships.length; i++) {
        // shipsPassengers[ships[i].y][ships[i].x] = ships[i].passengers;
        // alivePassengers += ships[i].passengers;
        // }

        cells = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public Grid() throws CloneNotSupportedException {
        // no items in the same cell
        m = ThreadLocalRandom.current().nextInt(5, 16);
        n = ThreadLocalRandom.current().nextInt(5, 16);

        deaths = 0;

        cells = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        // genertae Agent position
        int agentX = ThreadLocalRandom.current().nextInt(0, m);
        int agentY = ThreadLocalRandom.current().nextInt(0, n);
        int agentCapacity = ThreadLocalRandom.current().nextInt(30, 101);
        agent = new Agent(agentCapacity, agentY, agentX, this);
        cells[agentY][agentX].isOccupied = true;
        // cells[agentY][agentX].occupant = agent;

        int numberOfShips = ThreadLocalRandom.current().nextInt(1, n * m + 1);
        ships = new Ship[numberOfShips];

        // generate ships on random locations
        for (int i = 0; i < numberOfShips; i++) {
            int numberOfPassengers = ThreadLocalRandom.current().nextInt(1, 101);
            while (true) {
                int shipPositionX = ThreadLocalRandom.current().nextInt(0, m);
                int shipPositionY = ThreadLocalRandom.current().nextInt(0, n);
                if (!cells[shipPositionY][shipPositionX].isOccupied) {
                    cells[shipPositionY][shipPositionX].ship = new Ship(numberOfPassengers, shipPositionY,
                            shipPositionX);
                    cells[shipPositionY][shipPositionX].isOccupied = true;
                    cells[shipPositionY][shipPositionX].isShip = true;
                    break;
                }
            }
        }

        int numberOfStations = ThreadLocalRandom.current().nextInt(1, n * m + 1);
        stations = new Station[numberOfStations];

        for (int i = 0; i < stations.length; i++) {
            while (true) {
                int stationPositionX = ThreadLocalRandom.current().nextInt(0, m);
                int stationPositionY = ThreadLocalRandom.current().nextInt(0, n);
                if (!cells[stationPositionY][stationPositionX].isOccupied) {
                    stations[i] = new Station(stationPositionY, stationPositionX);
                    cells[stationPositionY][stationPositionX].isOccupied = true;
                    cells[stationPositionY][stationPositionX].station = stations[i];
                    break;
                }
            }
        }

    }

    public Grid clone() throws CloneNotSupportedException {
        Grid clonedGrid = new Grid(m, n, deaths, agent, stations, alivePassengers, cells, ships, saved);

        Cell newCells[][] = new Cell[n][m];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                newCells[i][j] = clonedGrid.cells[i][j].clone();

            }
        }

        clonedGrid.cells = newCells;

        clonedGrid.agent = agent.clone();

        return clonedGrid;
    }

    public void timeStep() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {

                if (cells[i][j].isShip) {
                    if (cells[i][j].ship.passengers > 0) {
                        cells[i][j].ship.passengers--;
                        deaths++;
                        alivePassengers--;
                        cells[i][j].ship.isSunk = cells[i][j].ship.passengers ==0?true:false;
                    } else 
                        cells[i][j].ship.isSunk = true;

                    if (!(cells[i][j].ship.blackBoxDestroyed) && cells[i][j].ship.isSunk) {
                        if (cells[i][j].ship.blackBoxDamage < 20) {
                            cells[i][j].ship.addBlackBoxDamage();
                            cells[i][j].ship.blackBoxDestroyed = cells[i][j].ship.blackBoxDamage ==20 ? true: false;
                        }
                         else 
                            cells[i][j].ship.blackBoxDestroyed = true;
                    }
                }
            }
        }
    }

}
