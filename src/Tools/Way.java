package Tools;

public enum Way {
    S,
    N,
    E,
    W;

    public static boolean isAWay(String obj){
        for(Way w: Way.values()){
            if(obj.equals(w.toString()))return true;
        }
        return false;
    }

    public static String allWay(){
        String all = "";
        for(Way w: Way.values()){
            all = all+w+" ";
        }
        return all;
    }
}
