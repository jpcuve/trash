package parser.spindizzy;

/**
 * Created by jpc on 22/11/2015.
 */
public interface ScreenHandler {
    void header(int posx, int posy, boolean slow, int colorScheme);
    void floor(int x, int y, int z, int r, String layout, String type);
    void reference(int x, int y, int z, int r, int ref);
    void block(int x, int y, int z, int r, String layout, String type);
    void clue(int x, int y, int z, int type);
    void object(int x, int y, int z, String type, int data);
    void end();
}
