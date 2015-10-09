package net.nio.protocol.http;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:27:53 PM
 * To change this template use File | Settings | File Templates.
 */
public enum HttpMethod {
    OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT;

    public static HttpMethod parse(String text){
        if (text.length() > 0){
            switch(text.charAt(0)){
                case 'O': return OPTIONS;
                case 'G': return GET;
                case 'H': return HEAD;
                case 'P':
                    if (text.length() > 1){
                        switch(text.charAt(1)){
                            case 'O': return POST;
                            case 'U': return PUT;
                        }
                    }
                    return null;
                case 'D': return DELETE;
                case 'T': return TRACE;
                case 'C': return CONNECT;
            }
        }
        return null;
    }
}
