package Server;

import Board.Grid;
import Client.Avatar;

public class GameServerSimple {
    private boolean available;
    private Grid gGrid;
    protected GameServerSimple(){
        available=true;
        gGrid = new Grid(8);
        gGrid.displayGrid();
    }

    public GameServerSimple(boolean state){
        available=state;
    }

    public boolean connection(String uid, String avUsed) {
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
