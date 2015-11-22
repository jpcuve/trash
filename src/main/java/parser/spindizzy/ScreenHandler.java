package parser.spindizzy;

/**
 * Created by jpc on 22/11/2015.
 */
public interface ScreenHandler {
    void header(int posx, int posy, boolean slow, int colorScheme);
    void floor(int x, int y, int z, int r, String apply, String type);
    void reference(int x, int y, int z, int r, int ref);
    void block(int x, int y, int z, int r, String layout, String type);
    void clue(int x, int y, int z, int type);
    void object(int b1, int b2, int b3);
    void jewel(int x, int y, int z, int id);
    void lift(int x, int y, int z, int minz, int maxz, int pause);
    void enemy(int x, int y, int z, int type, int inertia);
    void puzzle(int x, int y, int z, int r, int ref, int id);
    void end();
}
