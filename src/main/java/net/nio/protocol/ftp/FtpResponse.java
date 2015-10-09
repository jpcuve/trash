package net.nio.protocol.ftp;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2004
 * Time: 9:20:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class FtpResponse {
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private static final String EOL = "\r\n";
    private FtpHandler ftpHandler;
    private int code;
    private String[] lines;
    private ByteBuffer buffer;
    int index;

    public FtpResponse(FtpHandler ftpHandler) {
        this.ftpHandler = ftpHandler;
        reset();
    }

    public void setResponse(int code, String[] lines) {
        this.code = code;
        this.lines = lines;
    }

    public void setResponse(int code, String line) {
        setResponse(code, new String[]{line});
    }

    public void reset() {
        this.code = 0;
        this.lines = null;
        this.buffer = null;
        this.index = 0;
    }

    public void writeTo(ByteBuffer outputBuffer) {
        if (buffer == null){
            buffer = CHARSET_ASCII.encode(CharBuffer.wrap(toString()));
        }
        int l = Math.min(buffer.remaining(), outputBuffer.remaining());
        for (int i = 0; i < l; i++) outputBuffer.put(buffer.get());
        if (!buffer.hasRemaining()){
            ftpHandler.responseWritten(code, lines);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(1024);
        for (int i = 0; i < lines.length; i++){
            if (i == 0 && lines.length > 1){
                sb.append(code);
                sb.append('-');
            } else if (i == lines.length - 1){
                sb.append(code);
                sb.append(' ');
            } else {
                sb.append(' ');
            }
            sb.append(lines[i]);
            sb.append(EOL);
        }
        return sb.toString();
    }
}
