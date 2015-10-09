package parser.pdf.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

import static parser.pdf.impl.Token.*;


public class Tokenizer implements Iterable<Token> {
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private static final String WHITESPACE = "\0\t\n\f\r ";
    private static final String DELIMITER = "()<>[]{}/%";
    private static final String HEXADECIMAL = "0123456789ABCDEFabcdef";
    private static final String OCTAL = "01234567";
    private final BufferedPushbackInputStream buffer;
    private final TokenizerIterator iterator;
    private int parenthesisLevel;
    private Token token;

    private static final HashMap<String, Token> keywords = new HashMap<String, Token>();

    static{
        keywords.put("null", NULL);
        keywords.put("true", BOOLEAN);
        keywords.put("false", BOOLEAN);
        keywords.put("stream", STREAM_INIT);
        keywords.put("endstream", STREAM_DONE);
        keywords.put("obj", OBJ_INIT);
        keywords.put("endobj", OBJ_DONE);
    }


    public Tokenizer(final InputStream inputStream){
        this.buffer = new BufferedPushbackInputStream(inputStream, 15);
        this.iterator = new TokenizerIterator();
    }

    public static String escape(final String s){
        return s;
    }

    public static String toString(Object o){
        return toString(o, -1);
    }

    public static String toString(Object o, int prettyPrint){
        if (o == null) return "null";
        if (o instanceof PdfDictionary) return ((PdfDictionary) o).toString(prettyPrint);
        if (o instanceof PdfArray) return ((PdfArray) o).toString(prettyPrint);
        if (o instanceof String) return String.format("/%s", o);
        if (o instanceof Number || o instanceof Boolean || o instanceof PdfReference) return o.toString();
        final StringBuilder sb = new StringBuilder();
        sb.append('(').append(escape(o.toString())).append(')');
        return o.toString();
    }

    public static void appendLine(final StringBuilder sb, int prettyPrint, final String s){
        if (prettyPrint >= 0) sb.append('\n');
        for (int i = 0; i < prettyPrint; i++) sb.append("  ");
        sb.append(s);
    }

    public class TokenizerIterator implements Iterator<Token> {
        private State state;
        private Token t;
        private byte[] value;
        private long position;
        private int length;

        private TokenizerIterator() {
            readNext();
        }

        public byte[] getValue() {
            return value;
        }

        public Token getToken(){
            return t;
        }

        public long getPosition() {
            return position;
        }

        private void readNext(){
            int pos = buffer.getPosition();
            try{
                for (state = State.INIT; state != State.NEXT && state != State.DONE; state = state.next(Tokenizer.this)){}
            } catch(IOException x){
                state = State.DONE;
            }
            length = buffer.getPosition() - pos;
        }

        public boolean hasNext() {
            return state != State.DONE;
        }

        public Token next() {
            this.value = buffer.getValue();
            this.position = buffer.getPosition() - length;
            this.t = token;
            readNext();
            return t;
        }

        public void remove() {
        }
    }

    public TokenizerIterator iterator() {
        return iterator;
    }

    private enum State{
        INIT{
            public State next(Tokenizer t) throws IOException {
                t.token = UNKNOWN;
                int b;
                do {
                    t.buffer.mark();
                    b = t.buffer.read();
                } while (WHITESPACE.indexOf(b) != -1);
                if (b == -1) return DONE;
                return  "+-".indexOf(b) != -1 ? SYMBOL0 : (Character.isLetter(b) ? SYMBOL1 : REFOBJ0);
            }},
        SYMBOL0{
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                return Character.isDigit(b) || b == '.' ? NUMBER10 : SYMBOL1;
            }},
        SYMBOL1{
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (DELIMITER.indexOf(b) != -1 || WHITESPACE.indexOf(b) != -1 || b == -1){
                    if (b != -1) t.buffer.back();
                    final byte[] bytes = t.buffer.getValue();
                    final Token code = keywords.get(new String(bytes, CHARSET_ASCII).toLowerCase());
                    t.token = (code != null ? code : OPERATOR);
                    if (code == STREAM_INIT){
                        for (int i = 0; i < 2; i++){
                            b = t.buffer.read();
                            if (b == '\n') break;
                        }
                    }
                    return NEXT;
                }
                return SYMBOL1;
            }},
        REFOBJ0 {
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                return Character.isDigit(b) ? REFOBJ1 : NUMBER10;
            }},
        REFOBJ1 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (WHITESPACE.indexOf(b) != -1) return REFOBJ2;
                return Character.isDigit(b) ? REFOBJ1 : NUMBER10;
            }},
        REFOBJ2 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                while (WHITESPACE.indexOf(b) != -1 && b != -1) b = t.buffer.read();
                return Character.isDigit(b) ? REFOBJ3 : NUMBER10;
            }},
        REFOBJ3 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (WHITESPACE.indexOf(b) != -1) return REFOBJ4;
                return Character.isDigit(b) ? REFOBJ3 : NUMBER10;
            }},
        REFOBJ4 {
            public State next(Tokenizer t) throws IOException {
                int b;
                do b = t.buffer.read(); while(WHITESPACE.indexOf(b) != -1 && b != -1);
                if (b == 'R'){
                    t.token = REFERENCE;
                    return NEXT;
                }
                for (byte l: "obj".getBytes()){
                    if (b != l) return NUMBER10;
                    b = t.buffer.read();
                }
                t.buffer.back();
                t.token = OBJ_INIT;
                return NEXT;
            }},
        NUMBER10{
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                if ("+-".indexOf(b) != -1) b = t.buffer.read();
                if (b == '.') return NUMBER11;
                return Character.isDigit(b) ? NUMBER12 : NUMBER20;
            }},
        NUMBER11 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                return Character.isDigit(b) ? NUMBER13 : NAME0;
            }},
        NUMBER12 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (Character.isDigit(b)) return NUMBER12;
                return b == '.' ? NUMBER13 : NUMBER20;
            }},
        NUMBER13 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (!Character.isDigit(b)){
                    t.buffer.back();
                    t.token = NUMBER;
                    return NEXT;
                }
                return NUMBER14;
            }},
        NUMBER14 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (Character.isDigit(b)) return NUMBER14;
                t.buffer.back();
                t.token = NUMBER;
                return NEXT;
            }},
        NUMBER20{
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                if ("+-".indexOf(b) != -1) b = t.buffer.read();
                return Character.isDigit(b) ? NUMBER21 : NAME0;
            }},
        NUMBER21{
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (!Character.isDigit(b)){
                    t.buffer.back();
                    t.token = NUMBER;
                    return NEXT;
                }
                return NUMBER21;
            }},
        NAME0 {
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                return b == '/' ? NAME1 : STRING0;
            }},
        NAME1 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (b == '#') {
                    b = t.buffer.read();
                    int i = HEXADECIMAL.indexOf(b);
                    if (i != -1){
                        if (i > 15) i-=6;
                        b = t.buffer.read();
                        int j = HEXADECIMAL.indexOf(b);
                        if (j != -1){
                            if (j > 15) j-=6;
                            t.buffer.escape(3, (byte) ((i << 4) + j));
                            return NAME1;
                        }
                    }
                }
                if (DELIMITER.indexOf(b) != -1 || WHITESPACE.indexOf(b) != -1 || b == -1){
                    if (b != -1) t.buffer.back();
                    t.token = NAME;
                    return NEXT;
                }
                return NAME1;
            }},
        STRING0 {
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                return b == '<' ? STRING1 : STRING3;
            }},
        STRING1 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (b == '<'){
                    t.token = DICTIONARY_INIT;
                    return NEXT;
                }
                t.buffer.back();
                return STRING2;
            }},
        STRING2 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (b == '>' || b == -1){
                    t.token = STRING;
                    return NEXT;
                }
                if (HEXADECIMAL.indexOf(b) == -1 && WHITESPACE.indexOf(b) == -1) return INVALID;
                return STRING2;
            }},
        STRING3 {
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                if (b == '('){
                    t.parenthesisLevel++;
                    return STRING4;
                }
                return DOUBLE0;
            }},
        STRING4 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                switch(b){
                    case -1:
                    case ')':
                        t.parenthesisLevel--;
                        if (t.parenthesisLevel <= 0 || b == -1){
                            t.parenthesisLevel = 0;
                            t.token = STRING;
                            return NEXT;
                        }
                        break;
                    case '(':
                        t.parenthesisLevel++;
                        break;
                    case '\\':
                        return STRING5;
                }
                return STRING4;
            }},
        STRING5 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                switch(b){
                    case 'n':
                        t.buffer.escape('\n');
                        break;
                    case 'r':
                        t.buffer.escape('\r');
                        break;
                    case 't':
                        t.buffer.escape('\t');
                        break;
                    case 'b':
                        t.buffer.escape('\b');
                        break;
                    case 'f':
                        t.buffer.escape('\f');
                        break;
                    case '(':
                        t.buffer.escape('(');
                        break;
                    case ')':
                        t.buffer.escape(')');
                        break;
                    case '\\':
                        t.buffer.escape('\\');
                        break;
                    case 10:
                        t.buffer.escape(0);
                        break;
                    case 13:
                        b = t.buffer.read();
                        if (b != 10){
                            t.buffer.back();
                            t.buffer.escape(0);
                        } else{
                            t.buffer.escape(3, 0);
                        }
                        break;
                    default:
                        if (OCTAL.indexOf(b) != -1){
                            int count = 0;
                            int total = 0;
                            while (OCTAL.indexOf(b) != -1 && b != -1 && count < 3){
                                total = total * 8 + OCTAL.indexOf(b);
                                b = t.buffer.read();
                                count++;
                            }
                            t.buffer.back();
                            t.buffer.escape(count + 1, total);
                        }
                        break;
                }
                return STRING4;
            }},
        DOUBLE0{
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                return b == '>' ? DOUBLE1 : SINGLE;
            }},
        DOUBLE1 {
            public State next(Tokenizer t) throws IOException {
                int b = t.buffer.read();
                if (b == '>'){
                    t.token = DICTIONARY_DONE;
                    return NEXT;
                }
                return INVALID;
            }},
        SINGLE{
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                switch(b){
                    case '[':
                        t.token = LEFT_BRACKET;
                        return NEXT;
                    case ']':
                        t.token = RIGHT_BRACKET;
                        return NEXT;
                }
                return INVALID;
            }},
        INVALID{
            public State next(Tokenizer t) throws IOException {
                t.buffer.reset();
                int b = t.buffer.read();
                return b == -1 ? DONE : NEXT;
            }},
        NEXT{
            public State next(Tokenizer t) throws IOException {
                return null;
            }},
        DONE {
            public State next(Tokenizer t) throws IOException {
                return null;
            }};

        public abstract State next(Tokenizer t) throws IOException;
    }
}
