package parser.spindizzy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jpc on 22/11/2015.
 */
public class ScreenReader {
    private final ScreenHandler handler;

    public ScreenReader(ScreenHandler handler) {
        this.handler = handler;
    }

    public void parse(final InputStream is) throws IOException {
        int length = is.read();
        while (length > 0){
            int posy = is.read() & 0x7F;
            int posx = is.read() & 0x7F;
            int more = is.read() & 0x1F;
            handler.header(posx, posy, (more >> 4) == 0, more & 0xF);
            length -= 4;
            int b1 = is.read() & 0xFF;
            int b2 = is.read() & 0xFF;
            length -= 2;
            int base =  (b2 << 8) | b1;
            int y = (base >> 13) & 7;
            int x = (base >> 10) & 7;
            int z = (base >> 7) & 7;
            int t = base & 0x7F;
            switch(t) {
                case 1:
                    handler.floor(x, y, z, 0, "B", "SQ");
                    break;
                case 3:
                    handler.floor(x, y, z, 0, "B", "TR");
                    break;
                case 0x40:
                    handler.floor(x, y, z, 0, "B", "WA");
                    break;
                case 0x43:
                    handler.floor(x, y, z, 0, "B", "IC");
                    break;
                case 0x44:
                case 0x45:
                case 0x46:
                case 0x47:
                    handler.floor(x, y, z, t & 3, "B", "AR");
                    break;
                case 0x61:
                    handler.floor(x, y, z, 0, "C", "SQ");
                    break;
                case 0x63:
                    handler.floor(x, y, z, 0, "C", "TR");
                    break;
            }
            while(base != 0){
                b1 = is.read() & 0xFF;
                b2 = is.read() & 0xFF;
                length -= 2;
                base =  (b2 << 8) | b1;
                y = (base >> 13) & 7;
                x = (base >> 10) & 7;
                z = (base >> 7) & 7;
                if ((base & 0x1F) == 2){
                    int ref = is.read() & 0xFF;
                    length--;
                    handler.reference(x, y, z, (base >> 5) & 3, ref);
                } else {
                    t = base & 0x7F;
                    switch(t){
                        case 0:
                            break;
                        case 1:
                            handler.block(x, y, z, 0, "B", "SQ");
                            break;
                        case 3:
                            handler.block(x, y, z, 0, "B", "TR");
                            break;
                        case 0x40:
                            handler.block(x, y, z, 0, "B", "WA");
                            break;
                        case 0x43:
                            handler.block(x, y, z, 0, "B", "IC");
                            break;
                        case 0x44:
                        case 0x45:
                        case 0x46:
                        case 0x47:
                            handler.block(x, y, z, t & 3, "B", "AR");
                            break;
                        case 0x21:
                            handler.block(x, y, z, 0, "E", "SQ");
                            break;
                        case 0x23:
                            handler.block(x, y, z, 0, "E", "TR");
                            break;
                        case 0x61:
                            handler.block(x, y, z, 0, "C", "SQ");
                            break;
                        case 0x63:
                            handler.block(x, y, z, 0, "C", "TR");
                            break;
                        default:
                            int method = (t >> 5) & 3;
                            int type = (t >> 2) & 7;
                            switch (method){
                                case 0:
                                    handler.block(x, y, z, t & 3, "B", "B" + type);
                                    break;
                                case 1:
                                    handler.block(x, y, z, t & 3, "E", "B" + type);
                                    break;
                                case 2:
                                    handler.clue(x, y, z, t & 0xF);
                                    break;
                                case 3:
                                    handler.block(x, y, z, t & 3, "C", "B" + type);
                                    break;
                            }
                            break;
                    }
                }
            }
            while (length > 3){
                b1 = is.read() & 0xFF;
                b2 = is.read() & 0xFF;
                int b3 = is.read() & 0xFF;
                handler.object(b1, b2, b3);
                length -= 3;
            }
            is.read(); // last 0
            handler.end();
            length = is.read();
        }
    }

    public static void main(String[] args) throws IOException {
        final FileInputStream fis = new FileInputStream("etc/spindizzy/spindizzy_screens.bin");
        final ScreenReader screenReader = new ScreenReader(new ScreenHandler() {
            public void header(int posx, int posy, boolean slow, int colorScheme) {
                System.out.println("ScreenReader.header: " + String.format("(%s,%s)", posx, posy));
            }

            public void floor(int x, int y, int z, int r, String layout, String type) {
                System.out.println("ScreenReader.floor: " + String.format("(%s,%s,%s):%s %s %s", x, y, z, r, layout, type));

            }

            public void object(int b1, int b2, int b3) {
                System.out.println("ScreenReader.object: " + b1);

            }

            public void reference(int x, int y, int z, int r, int ref) {
                System.out.println("ScreenReader.reference: " + String.format("(%s,%s,%s):%s [%s]", x, y, z, r, ref));

            }

            public void block(int x, int y, int z, int r, String layout, String type) {
                System.out.println("ScreenReader.block: " + String.format("(%s,%s,%s):%s %s %s", x, y, z, r, layout, type));
            }

            public void clue(int x, int y, int z, int type) {
                System.out.println("ScreenReader.clue: " + type);
            }

            public void jewel(int x, int y, int z, int id) {

            }

            public void lift(int x, int y, int z, int minz, int maxz, int pause) {

            }

            public void enemy(int x, int y, int z, int type, int inertia) {

            }

            public void puzzle(int x, int y, int z, int r, int ref, int id) {

            }

            public void end() {
                System.out.println("ScreenReader.end");
            }
        });
        screenReader.parse(fis);
        fis.close();
    }
}
