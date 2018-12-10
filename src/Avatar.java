import java.io.Serializable;

public class Avatar implements Serializable {

    private static final long serialVersionUID = 1548468510L;

    private final String name;
    private int lifePoint;

    public Avatar(String name) {
        this.name = name;
        lifePoint=10;
    }

    public String getName() {
        return name;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public void setLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }
}
