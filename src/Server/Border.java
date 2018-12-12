package Server;

import java.io.Serializable;

public abstract class Border implements Serializable {
    protected boolean crossable;
    protected int dest;
}
