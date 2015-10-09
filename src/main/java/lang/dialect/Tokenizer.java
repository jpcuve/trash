package lang.dialect;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import static lang.dialect.Keyword.*;
import static lang.dialect.Token.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:25:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tokenizer implements Iterable<Token> {
    private final Reader reader;
    private Token token;
    private Keyword keyword;
    private String value;

    private static final int HALF_BUFFER_SIZE = 1024;
    private static final int BUFFER_SIZE = HALF_BUFFER_SIZE * 2;
    private static final char EOT = '\u0004';

    private StringBuilder sb;
    private int line;
    private char[] buf;
    private int fwd;
    private int bck;
    private int skip;

    public Tokenizer(Reader reader) {
        this.reader = reader;
        buf = new char[BUFFER_SIZE];
        fwd = BUFFER_SIZE - 1;
        buf[fwd] = EOT;
        line = 1;
        bck = fwd;
        skip = 0;
        sb = new StringBuilder(BUFFER_SIZE);
    }

    public void setOutput(Token token, Keyword keyword){
        this.token = token;
        this.keyword = keyword;
        this.value = sb.toString();
    }

    public Token getToken() {
        return token;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public String getValue(){
        return value;
    }

    public static String escape(String text){
        StringBuilder sb = new StringBuilder(text.length() * 2);
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            switch(c){
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case 0x08: sb.append("\\b"); break;
                case 0x09: sb.append("\\t"); break;
                case 0x0A: sb.append("\\n"); break;
                case 0x0C: sb.append("\\f"); break;
                case 0x0D: sb.append("\\r"); break;
                default: sb.append(c); break;
            }
        }
        return sb.toString();
    }


    private char readChar() throws IOException {
        char c = buf[fwd % BUFFER_SIZE];
        if (c == EOT && (fwd + 1) % HALF_BUFFER_SIZE == 0) {
            fwd++;
            int j = (fwd / HALF_BUFFER_SIZE) & 1;
            if (skip > 0) {
                assert skip == 1 : "character pushback length exceeds half buffer size";
                skip--;
            } else {
                int k;
                k = reader.read(buf, j * HALF_BUFFER_SIZE, HALF_BUFFER_SIZE - 1);
                int fl = (k != -1) ? (j * HALF_BUFFER_SIZE + k) : (fwd % BUFFER_SIZE);
                buf[fl] = EOT;
            }
            c = buf[fwd % BUFFER_SIZE];
        }
        fwd++;
        sb.append(c);
        return c;
    }

    private void escape(char c) {
        sb.setLength(sb.length() - 2);
        if (c != (char) 0) sb.append(c);
    }

    private void mark() {
        bck = fwd;
        sb.setLength(0);
    }

    private void reset() {
        skip += (fwd / HALF_BUFFER_SIZE - bck / HALF_BUFFER_SIZE);
        fwd = bck;
        sb.setLength(0);
    }

    private void back() {
        fwd--;
        if (fwd % HALF_BUFFER_SIZE == 0) {
            skip++;
            fwd--;
        }
        sb.setLength(sb.length() - 1);
    }

    public Iterator<Token> iterator() {
        return new Iterator<Token>(){
            public boolean hasNext() {
                return token != INVALID;
            }

            public Token next() {
                State state = State.INIT;
                try{
                    while (state != State.DONE) state = state.next(Tokenizer.this);
                } catch(IOException x){
                    token = INVALID;
                }
                return token;
            }

            public void remove() {
            }
        };
    }

    private enum State{
        INIT{
            public State next(Tokenizer t) throws IOException {
                char c;
                do {
                    t.mark();
                    c = t.readChar();
                } while (Character.isSpaceChar(c) || c == '\t' || c == '\r' || c == '\n'); // could change depending systems
                if (c == EOT) return INVALID0;
                return "+-".indexOf(c) != -1 ? SYMBOL0 : (Character.isJavaIdentifierStart(c) ? SYMBOL1 : NUMBER00);
            }
        },
        SYMBOL0{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER00;
                t.back();
                return SINGLE;
            }
        },
        SYMBOL1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isJavaIdentifierPart(c) && c != EOT) return SYMBOL1;
                t.back();
                t.setOutput(SYMBOL, LITERAL);
                Keyword keyword = Keyword.parse(t.getValue());
                if (keyword != null) t.setOutput(KEYWORD, keyword);
                return DONE;
            }
        },
        NUMBER00{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER01 : NUMBER02;
            }
        },
        NUMBER01{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER01;
                switch (c) {
                    case '.':
                        return NUMBER02;
                    case 'E':
                    case 'e':
                        return NUMBER04;
                }
                return NUMBER10;
            }
        },
        NUMBER02{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER03 : NUMBER10;
            }
        },
        NUMBER03{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER03;
                switch (c) {
                    case 'E':
                    case 'e':
                        return NUMBER04;
                }
                return NUMBER10;
            }
        },
        NUMBER04{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER06;
                switch (c) {
                    case '+':
                    case '-':
                        return NUMBER05;
                }
                return NUMBER10;
            }
        },
        NUMBER05{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER06 : NUMBER10;
            }
        },
        NUMBER06{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER06;
                t.back();
                t.setOutput(FLOATINGPOINT, LITERAL);
                return DONE;
            }
        },
        NUMBER10{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER11 : NUMBER20;
            }
        },
        NUMBER11{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER11;
                return c == '.' ? NUMBER12 : NUMBER20;
            }
        },
        NUMBER12{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER13 : NUMBER20;
            }
        },
        NUMBER13{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER13;
                t.back();
                t.setOutput(FLOATINGPOINT, LITERAL);
                return DONE;
            }
        },
        NUMBER20{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return Character.isDigit(c) ? NUMBER21 : STRING0;
            }
        },
        NUMBER21{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER21;
                t.back();
                t.setOutput(INTEGER, LITERAL);
                return DONE;
            }
        },
        STRING0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return c == '"' ? STRING1 : ERROR0;
            }
        },
        STRING1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case '\\':
                        return STRING2;
                    case EOT:
                        return INVALID0;
                    case '"':
                        t.setOutput(STRING, LITERAL);
                        return DONE;
                }
                return STRING1;
            }
        },
        STRING2{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case '"':
                        t.escape('"');
                        break;
                    case 'n':
                        t.escape('\n');
                        break;
                    case 't':
                        t.escape('\t');
                        break;
                    case 'r':
                        t.escape('\r');
                        break;
                    case 'b':
                        t.escape('\b');
                        break;
                    case 'f':
                        t.escape('\f');
                        break;
                    case '\\':
                        t.escape('\\');
                        break;
                    default:
                        t.escape((char) 0);
                        break;
                }
                return STRING1;
            }
        },
        ERROR0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return c == '%' ? ERROR1 : BOOLEAN0;
            }
        },
        ERROR1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case '\\':
                        return ERROR2;
                    case EOT:
                        return INVALID0;
                    case '%':
                        t.setOutput(ERROR, LITERAL);
                        return DONE;
                }
                return ERROR1;
            }
        },
        ERROR2{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case '"':
                        t.escape('"');
                        break;
                    case 'n':
                        t.escape('\n');
                        break;
                    case 't':
                        t.escape('\t');
                        break;
                    case 'r':
                        t.escape('\r');
                        break;
                    case 'b':
                        t.escape('\b');
                        break;
                    case 'f':
                        t.escape('\f');
                        break;
                    case '\\':
                        t.escape('\\');
                        break;
                    default:
                        t.escape((char) 0);
                        break;
                }
                return ERROR1;
            }
        },
        BOOLEAN0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return c == '#' ? BOOLEAN1 : DOUBLE0;
            }
        },
        BOOLEAN1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case 't':
                    case 'f':
                        t.setOutput(BOOLEAN, LITERAL);
                        break;
                    default:
                        t.back();
                        t.setOutput(NULL, LITERAL);
                        break;
                }
                return DONE;
            }
        },
        DOUBLE0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                switch(c){
                    case '=':
                        return DOUBLE1;
                    case '<':
                        return DOUBLE2;
                    case '>':
                        return DOUBLE3;
                    case '&':
                        return DOUBLE4;
                    case '|':
                        return DOUBLE5;
                    case '^':
                        return DOUBLE6;
                    case '/':
                        return DOUBLE7;
                }
                return SINGLE;
            }
        },
        DOUBLE1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '=') {
                    t.setOutput(RELOP, EQ);
                } else {
                    t.back();
                    t.setOutput(ASSIGN, LITERAL);
                }
                return DONE;
            }
        },
        DOUBLE2{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '>') {
                    t.setOutput(RELOP, NE);
                } else {
                    t.back();
                    t.setOutput(RELOP, LT);
                }
                return DONE;
            }
        },
        DOUBLE3{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '=') {
                    t.setOutput(RELOP, GE);
                } else {
                    t.back();
                    t.setOutput(RELOP, GT);
                }
                return DONE;
            }
        },
        DOUBLE4{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '&') {
                    t.setOutput(LOGOP, AND);
                } else {
                    t.back();
                    t.setOutput(BINOP, AND);
                }
                return DONE;
            }
        },
        DOUBLE5{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '|') {
                    t.setOutput(LOGOP, OR);
                } else {
                    t.back();
                    t.setOutput(BINOP, OR);
                }
                return DONE;
            }
        },
        DOUBLE6{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c == '^') {
                    t.setOutput(LOGOP, XOR);
                } else {
                    t.back();
                    t.setOutput(BINOP, XOR);
                }
                return DONE;

            }
        },
        DOUBLE7{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (c != '/'){
                    t.back();
                    t.setOutput(MULOP, DIV);
                    return DONE;
                }
                do{
                    c = t.readChar();
                } while (c != '\n' && c != EOT);
                return SYMBOL0;
            }
        },
        SINGLE{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                switch (c) {
                    case '(':
                        t.setOutput(LEFT_PARENTHESIS, LITERAL);
                        break;
                    case ')':
                        t.setOutput(RIGHT_PARENTHESIS, LITERAL);
                        break;
                    case '{':
                        t.setOutput(LEFT_BRACE, LITERAL);
                        break;
                    case '}':
                        t.setOutput(RIGHT_BRACE, LITERAL);
                        break;
                    case '[':
                        t.setOutput(LEFT_BRACKET, LITERAL);
                        break;
                    case ']':
                        t.setOutput(RIGHT_BRACKET, LITERAL);
                        break;
                    case ',':
                        t.setOutput(COMMA, LITERAL);
                        break;
                    case ';':
                        t.setOutput(SEMICOLON, LITERAL);
                        break;
                    case ':':
                        t.setOutput(COLON, LITERAL);
                        break;
                    case '.':
                        t.setOutput(DOT, LITERAL);
                        break;
                    case '?':
                        t.setOutput(UNDEFINED, LITERAL);
                        break;
                    case '@':
                        t.setOutput(FUNCTOR, LITERAL);
                        break;
                    case '!':
                        t.setOutput(MONOP, NOT);
                        break;
                    case '~':
                        t.setOutput(MONOP, LNOT);
                        break;
                    case '+':
                        t.setOutput(ADDOP, ADD);
                        break;
                    case '-':
                        t.setOutput(ADDOP, SUB);
                        break;
                    case '*':
                        t.setOutput(MULOP, MUL);
                        break;
                    default:
                        return INVALID0;
                }
                return DONE;
            }
        },
        INVALID0{
            public State next(Tokenizer t) throws IOException {
                t.setOutput(INVALID, LITERAL);
                return DONE;

            }
        },
        DONE{
            public State next(Tokenizer t) throws IOException {
                return null;
            }
        };

        public abstract State next(Tokenizer t) throws IOException;
    }



    public int lineno() {
        return line;
    }
}
