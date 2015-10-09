package lang.ql;

import java.io.IOException;
import java.util.Iterator;

import static lang.ql.Keyword.*;
import static lang.ql.Token.*;


/**
 * @author jpc
 */
public class Tokenizer  implements Iterable<Token> {
    private Token token;
    private Keyword keyword;
    private String value;

    private static final char EOT = '\u0004';

    private StringBuilder sb;
    private char[] buf;
    private int fwd;
    private int bck;

    public Tokenizer(final String expression) {
        buf = (expression + EOT).toCharArray();
        fwd = bck = 0;
        sb = new StringBuilder();
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


    private char readChar() throws IOException {
        char c = buf[fwd++];
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
        fwd = bck;
        sb.setLength(0);
    }

    private void back() {
        fwd--;
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
                return "+-".indexOf(c) != -1 ? SYMBOL0 : (Character.isLetter(c) ? SYMBOL1 : NUMBER00);
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
                if ((Character.isLetterOrDigit(c) || "_".indexOf(c) != -1) && c != EOT) return SYMBOL1;
                t.back();
                t.setOutput(SYMBOL, LITERAL);
                return DONE;
            }
        },
        NUMBER00{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                if ("+-".indexOf(c) != -1) c = t.readChar();
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
                t.setOutput(NUMBER, LITERAL);
                return DONE;
            }
        },
        NUMBER10{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                if ("+-".indexOf(c) != -1) c = t.readChar();
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
                t.setOutput(NUMBER, LITERAL);
                return DONE;
            }
        },
        NUMBER20{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                if ("+-".indexOf(c) != -1) c = t.readChar();
                return Character.isDigit(c) ? NUMBER21 : DESCRIPTOR0;
            }
        },
        NUMBER21{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                if (Character.isDigit(c)) return NUMBER21;
                t.back();
                t.setOutput(NUMBER, LITERAL);
                return DONE;
            }
        },
        DESCRIPTOR0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return c == '%' ? DESCRIPTOR1 : STRING0;
            }
        },
        DESCRIPTOR1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch (c) {
                    case EOT:
                        return INVALID0;
                    case '%':
                        t.setOutput(DESCRIPTOR, LITERAL);
                        return DONE;
                }
                return DESCRIPTOR1;
            }
        },
        STRING0{
            public State next(Tokenizer t) throws IOException {
                t.reset();
                char c = t.readChar();
                return c == '"' ? STRING1 : BOOLEAN0;
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
                        t.setOutput(RELOP, NULL);
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
                    case '!':
                        return DOUBLE1;
                    case '<':
                        return DOUBLE2;
                    case '>':
                        return DOUBLE3;
                }
                return SINGLE;
            }
        },
        DOUBLE1{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch(c){
                    case '@':
                        t.setOutput(RELOP, NBETWEEN);
                        return DONE;
                    case '$':
                        t.setOutput(RELOP, NIN);
                        return DONE;
                    case '#':
                        t.setOutput(RELOP, NNULL);
                        return DONE;
                    case '~':
                        t.setOutput(RELOP, NLIKE);
                        return DONE;
                }
                t.back();
                t.setOutput(NOTOP, NOT);
                return DONE;
            }
        },
        DOUBLE2{
            public State next(Tokenizer t) throws IOException {
                char c = t.readChar();
                switch(c){
                    case '>':
                        t.setOutput(RELOP, NE);
                        return DONE;
                    case '=':
                        t.setOutput(RELOP, LE);
                        return DONE;
                }
                t.back();
                t.setOutput(RELOP, LT);
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
                    case '=':
                        t.setOutput(RELOP, EQ);
                        break;
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
                    case '.':
                        t.setOutput(DOT, LITERAL);
                        break;
                    case ':':
                        t.setOutput(COLON, LITERAL);
                        break;
                    case '|':
                        t.setOutput(OROP, OR);
                        break;
                    case '&':
                        t.setOutput(ANDOP, AND);
                        break;
                    case '!':
                        t.setOutput(NOTOP, NOT);
                        break;
                    case '~':
                        t.setOutput(RELOP, LIKE);
                        break;
                    case '@':
                        t.setOutput(RELOP, BETWEEN);
                        break;
                    case '$':
                        t.setOutput(RELOP, IN);
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
                    case '/':
                        t.setOutput(MULOP, DIV);
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
}
