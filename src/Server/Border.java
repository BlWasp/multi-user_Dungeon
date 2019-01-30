package Server;

import java.io.Serializable;

/**
 * Représente une frontière (entre case, entre plateau, etc)
 * Classe mère de Wall, Door
 */
public abstract class Border implements Serializable {
    protected boolean crossable;
    protected int dest;
}
