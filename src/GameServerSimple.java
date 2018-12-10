

public class GameServerImpl {
    private boolean available;
    protected GameServerImpl(){
        available=true;
    }

    public GameServerImpl(boolean state){
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
