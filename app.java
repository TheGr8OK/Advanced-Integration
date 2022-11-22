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
    public static String solve(Grid grid, String strategy, String visualize){
        String output = "";
        return output;
    }

    public static void main(String[] args) {
        System.out.println(GenGrid());
    }
    }