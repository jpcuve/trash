package parser.spindizzy;

/**
 * Created by jpc on 11/23/15.
 */
@FunctionalInterface
public interface Block {
    void block(int x, int y, int z, int r, String layout, String type);
}
