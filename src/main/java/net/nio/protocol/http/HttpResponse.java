package net.nio.protocol.http;

import net.nio.MemoryBuffer;

import java.io.IOException;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:21:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpResponse extends HttpMessage {
    private int versionMajor;
    private int versionMinor;
    private int code;
    private String message;

    public HttpResponse(HttpHandler httpHandler) throws IOException {
        super(httpHandler);
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void decodeLine() throws Exception {
        if (line == null) throw new ProtocolException("not status line");
        int ht1 = line.indexOf("HTTP/");
        if (ht1 == -1) throw new ProtocolException("http protocol version not found");
        int dt1 = line.indexOf('/', ht1 + 1);
        if (dt1 == -1) throw new ProtocolException("http protocol major version number not found");
        int dt2 = line.indexOf('.', dt1 + 1);
        if (dt2 == -1) throw new ProtocolException("http protocol minor version number not found");
        int sp1 = line.indexOf(' ', dt2);
        if (sp1 == -1) throw new ProtocolException("http response code not found");
        int sp2 = line.indexOf(' ', sp1 + 1);
        if (sp2 == -1) throw new ProtocolException("http response message not found");
        versionMajor = Integer.parseInt(line.substring(dt1 + 1, dt2));
        versionMinor = Integer.parseInt(line.substring(dt2 + 1, sp1));
        code = Integer.parseInt(line.substring(sp1 + 1, sp2));
        message = line.substring(sp2 + 1);
    }

    public void encodeLine() throws Exception {
        line = String.format("HTTP/%d.%d %d %s", versionMajor, versionMinor, code, message);
    }

    public void setStatus(int code, String message, int majorVersion, int minorVersion){
        this.code = code;
        this.message = message;
        this.versionMajor = majorVersion;
        this.versionMinor = minorVersion;
    }

    public void setTextResponse(String response, Charset charset, String mimeType) {
        ByteBuffer byteBuffer = charset.encode(response);
        setLength(byteBuffer.limit());
        setType(String.format("%s;charset=\"%s\"", mimeType, charset));
        setRbc(new MemoryBuffer(byteBuffer));
    }

    public void setDataResponse(ReadableByteChannel readableByteChannel, int length, String mimeType){
        setLength(length);
        setType(mimeType);
        setRbc(readableByteChannel);
    }
}
