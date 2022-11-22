import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    int m;
    int n;
    int deaths;
    Cell[][] cells;
    Agent agent;
    Ship[] ships;
    Station[] stations;

    public Grid(int m, int n, Agent agent, Ship[] ships, Station[] stations){
        this.m = m;
        this.n = n;
        this.agent= agent;
        this.ships= ships;
        this.stations = stations;
        
        cells = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public Grid(){
        //no items in the same cell
        m = ThreadLocalRandom.current().nextInt(5, 16);
        n = ThreadLocalRandom.current().nextInt(5, 16);

        deaths =0; 

        cells = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        //genertae Agent position
        int agentX = ThreadLocalRandom.current().nextInt(0, m);
        int agentY = ThreadLocalRandom.current().nextInt(0, n);
        int agentCapacity = ThreadLocalRandom.current().nextInt(30, 101);
        agent = new Agent(agentCapacity, agentY, agentX, this);
        cells[agentY][agentX].isOccupied = true;
        cells[agentY][agentX].occupant = agent;

        int numberOfShips = ThreadLocalRandom.current().nextInt(1, n*m +1);
        ships = new Ship[numberOfShips];

        //generate ships on random locations
        for (int i = 0; i < ships.length; i++) {
            int numberOfPassengers = ThreadLocalRandom.current().nextInt(1, 101);
            while(true){
                int shipPositionX = ThreadLocalRandom.current().nextInt(0, m);
                int shipPositionY = ThreadLocalRandom.current().nextInt(0, n);
                if(!cells[shipPositionY][shipPositionX].isOccupied){
                    ships[i] = new Ship(numberOfPassengers, shipPositionY, shipPositionX, this);
                    cells[shipPositionY][shipPositionX].isOccupied = true;
                    cells[shipPositionY][shipPositionX].occupant= ships[i];
                    break;
                }
            }
        }

        int numberOfStations = ThreadLocalRandom.current().nextInt(1, n*m +1);
        stations = new Station[numberOfStations];

        for (int i = 0; i < stations.length; i++) {
            while(true){
                int stationPositionX = ThreadLocalRandom.current().nextInt(0, m);
                int stationPositionY = ThreadLocalRandom.current().nextInt(0, n);
                if(!cells[stationPositionY][stationPositionX].isOccupied){
                    stations[i] = new Station(stationPositionY, stationPositionX);
                    cells[stationPositionY][stationPositionX].isOccupied = true;
                    cells[stationPositionY][stationPositionX].occupant= stations[i];
                    break;
                }
            }
        }

    }
}
