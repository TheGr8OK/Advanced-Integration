import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    int m;
    int n;
    Cell[][] cells;
    Agent agent;
    Ship[] ships;
    Station[] stations;

    public Grid(){
        //no items in the same cell
        m = ThreadLocalRandom.current().nextInt(5, 16);
        n = ThreadLocalRandom.current().nextInt(5, 16);

        cells = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        //genertae Agent position
        int agentX = ThreadLocalRandom.current().nextInt(0, m+1);
        int agentY = ThreadLocalRandom.current().nextInt(0, n+1);
        int agentCapacity = ThreadLocalRandom.current().nextInt(30, 101);
        agent = new Agent(agentCapacity, agentY, agentX, this);
        cells[agentY][agentX].isOccupied = true;

        int numberOfShips = ThreadLocalRandom.current().nextInt(1, (n*m/4) +1);
        ships = new Ship[numberOfShips];

        //generate ships on random locations
        for (int i = 0; i < ships.length; i++) {
            int numberOfPassengers = ThreadLocalRandom.current().nextInt(1, 101);
            while(true){
                int shipPositionX = ThreadLocalRandom.current().nextInt(0, m+1);
                int shipPositionY = ThreadLocalRandom.current().nextInt(0, n+1);
                if(!cells[shipPositionY][shipPositionX].isOccupied){
                    ships[i] = new Ship(numberOfPassengers, shipPositionY, shipPositionX);
                    cells[shipPositionY][shipPositionX].isOccupied = true;
                    cells[shipPositionY][shipPositionX].occupant= ships[i];
                    break;
                }
            }
        }

        int numberOfStations = ThreadLocalRandom.current().nextInt(1, (n*m/4) +1);
        stations = new Station[numberOfStations];

        for (int i = 0; i < stations.length; i++) {
            while(true){
                int stationPositionX = ThreadLocalRandom.current().nextInt(0, m+1);
                int stationPositionY = ThreadLocalRandom.current().nextInt(0, n+1);
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
