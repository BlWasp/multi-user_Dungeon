package Tools;

public enum Colors {

    reset("\u001B[0m"),
    black("\u001B[30m"),
    red("\u001B[31m"),
    green("\u001B[32m"),
    yellow("\u001B[33m"),
    blue("\u001B[34m"),
    purple("\u001B[35m"),
    cyan("\u001B[36m"),
    white("\u001B[37m"),
    bold("\033[0;1m");

    private String value;

    Colors(String content){
        value=content;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String blue(String text){
        return blue+text+reset;
    }

    public static String black(String text){
        return black+text+reset;
    }

    public static String red(String text){
        return red+text+reset;
    }

    public static String green(String text){
        return green+text+reset;
    }

    public static String yellow(String text){
        return yellow+text+reset;
    }

    public static String purple(String text){
        return purple+text+reset;
    }

    public static String cyan(String text){
        return cyan+text+reset;
    }

    public static String white(String text){
        return white+text+reset;
    }

    public static String bold(String text){
        return bold+text+reset;
    }

    public String getValue() {
        return value;
    }
}
