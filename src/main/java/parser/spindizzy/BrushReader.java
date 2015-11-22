package parser.spindizzy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jpc on 22/11/2015.
 */
public class BrushReader {
    private final BrushHandler handler;

    public BrushReader(BrushHandler handler) {
        this.handler = handler;
    }

    public void parse(final InputStream is) throws IOException {
        int id = is.read();
        while (id >= 0){
            handler.header(id);
            int length = is.read();
            length -= 2;
            while(length > 0){
                int b1 = is.read() & 0xFF;
                int b2 = is.read() & 0xFF;
                length -= 2;
                int base =  (b2 << 8) | b1;
                int y = (base >> 13) & 7;
                int x = (base >> 10) & 7;
                int z = (base >> 7) & 7;
                if ((base & 0x1F) == 2){
                    int ref = is.read() & 0xFF;
                    length--;
                    handler.reference(x, y, z, (base >> 5) & 3, ref);
                } else {
                    int t = base & 0x7F;
                    switch(t){
                        case 0:
                            handler.end();
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
            id = is.read();
        }
    }

    public static void main(String[] args) throws IOException {
        final FileInputStream fis = new FileInputStream("etc/spindizzy/spindizzy_brushes.bin");
        final BrushReader brushReader = new BrushReader(new BrushHandler() {
            public void header(int id) {
                System.out.println("BrushReader.header: " + id);
            }

            public void reference(int x, int y, int z, int r, int ref) {
                System.out.println("BrushReader.reference: " + String.format("(%s,%s,%s):%s [%s]", x, y, z, r, ref));

            }

            public void block(int x, int y, int z, int r, String layout, String type) {
                System.out.println("BrushReader.block: " + String.format("(%s,%s,%s):%s %s %s", x, y, z, r, layout, type));
            }

            public void clue(int x, int y, int z, int type) {
                System.out.println("BrushReader.clue: " + type);
            }

            public void end() {
                System.out.println("BrushReader.end");
            }
        });
        brushReader.parse(fis);
        fis.close();
    }
}
