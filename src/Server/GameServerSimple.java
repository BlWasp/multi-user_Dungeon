package Server;

import Client.Avatar;

import java.util.*;

public class GameServerSimple {
    private int available;
    private Grid gGrid;
    private Map<Integer, List<String>> positionMap;
    protected GameServerSimple(){
        int size = 8;
        available=1;
        gGrid = new Grid(size);
        gGrid.displayGrid();
        positionMap = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            positionMap.put(i, new ArrayList<String>());
        }
    }

    public GameServerSimple(int state){
        available=state;
    }

    public int connection(String avUsed, Integer position) {

        if(available==0)
            return available;
        List<String> user = positionMap.get(position);
        user.add(avUsed);
        List<String> test = positionMap.get(position);
        System.out.println(test);
        return available;

    }

    public void escape(Avatar avUsed, int position, String goTo) {

    }

    public void move(Avatar avUsed, int position, String goTo) {

    }

    public void displayGameInfo() {

    }

    public int getPosition(Avatar av){
        return 0;
    }

    public static void main(String args[]) throws Exception {

    }
}
