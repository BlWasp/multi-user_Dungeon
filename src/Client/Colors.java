package Client;

public enum Colors {

    reset("\u001B[0m"),
    black("\u001B[30m"),
    red("\u001B[31m"),
    green("\u001B[32m"),
    yellow("\u001B[33m"),
    blue("\u001B[34m"),
    purple("\u001B[35m"),
    cyan("\u001B[36m"),
    white("\u001B[37m");

    private String value;

    Colors(String content){
        value=content;
    }

    @Override
    public String toString() {
        return value;
    }
}
