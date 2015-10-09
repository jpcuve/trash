package graphic.maze;

import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jun 22, 2005
 * Time: 5:33:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Maze {
    public static final int CARVED = 1;
    public static final int HUNTED = 2;
    public static final int WALL_EAST = 4;
    public static final int WALL_WEST = 8;
    public static final int WALL_NORTH = 16;
    public static final int WALL_SOUTH = 32;
    private int width;
    private int height;
    private int[][] m;
    private Point[] move = new Point[] { new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1)};
    private Random random = new Random();

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.m = new int[width][height];
    }

    private void randomize(){
        for (int k = 0; k < 4; k++){
            int i = random.nextInt(4);
            int j = random.nextInt(4);
            Point p = move[i];
            move[i] = move[j];
            move[j] = p;
        }
    }

    private void kill(){
        Point p = new Point(0, 0);
        m[p.x][p.y] |= CARVED + WALL_EAST + WALL_WEST + WALL_NORTH + WALL_SOUTH;
        while (p != null){
            // move randomly until you cannot go further


        }

    }

    private Point hunt(){
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                if ((m[i][j] & CARVED) != 0 && (m[i][j] & HUNTED) == 0){
                    randomize();
                    for (int k = 0; k < 3; k++){
                        int x = i + move[k].x;
                        int y = j + move[k].y;
                        if (x >= 0 && x < width && y >= 0 && y < height && (m[x][y] & CARVED) == 0) return new Point(x, y);
                    }
                    m[i][j] |= HUNTED;
                }
            }
        }
        return null;
    }


}
