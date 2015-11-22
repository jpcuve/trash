package parser.spindizzy;

/**
 * Created by jpc on 22/11/2015.
 */
public interface BrushHandler {
    void header(int id);
    void reference(int x, int y, int z, int r, int ref);
    void block(int x, int y, int z, int r, String layout, String type);
    void clue(int x, int y, int z, int type);
    void end();
}
