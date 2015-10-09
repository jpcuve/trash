package lang.sim6502;

import java.io.IOException;
import java.io.Reader;

import static lang.sim6502.Keyword.*;
import static lang.sim6502.Token.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 4:24:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tokenizer {
    private Reader reader;

    public Token ttype;
    public String sval;
    public Keyword ival;

    private static final int HALF_BUFFER_SIZE = 1024;
    private static final int BUFFER_SIZE = HALF_BUFFER_SIZE * 2;

    private static final char EOF = '\u001C';

    private StringBuffer sb;
    private int line;
    private char[] buf;
    private int fwd;
    private int bck;
    private int skip;

    public Tokenizer(Reader reader) {
        this.reader = reader;
        buf = new char[BUFFER_SIZE];
        fwd = BUFFER_SIZE - 1;
        buf[fwd] = EOF;
        line = 1;
        bck = fwd;
        skip = 0;
        sb = new StringBuffer(BUFFER_SIZE);
    }

    public static String escape(String text){
        StringBuffer sb = new StringBuffer(text.length() * 2);
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
        if (c == EOF && (fwd + 1) % HALF_BUFFER_SIZE == 0) {
            fwd++;
            int j = (fwd / HALF_BUFFER_SIZE) & 1;
            if (skip > 0) {
                assert skip == 1 : "Character pushback length exceeds half buffer size";
                skip--;
            } else {
                int k = -1;
                k = reader.read(buf, j * HALF_BUFFER_SIZE, HALF_BUFFER_SIZE - 1);
                int fl = (k != -1) ? (j * HALF_BUFFER_SIZE + k) : (fwd % BUFFER_SIZE);
                buf[fl] = EOF;
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

    private static final int RESOLVE_WHITESPACE = 100;
    private static final int RESOLVE_COMMENT = 150;
    private static final int RESOLVE_SYMBOL = 200;
    private static final int RESOLVE_NUMBER_DECIMAL = 300;
    private static final int RESOLVE_NUMBER_HEXADECIMAL = 400;
    private static final int RESOLVE_DOUBLE = 800;
    private static final int RESOLVE_SINGLE = 900;

    public Token nextToken() throws IOException {
        char c;
        int state = RESOLVE_WHITESPACE;
        while (true) {
            switch (state) {
                case RESOLVE_WHITESPACE:
                    mark();
                    c = readChar();
                    state = Character.isWhitespace(c) ? RESOLVE_WHITESPACE + 1 : RESOLVE_COMMENT;
                    break;
                case RESOLVE_WHITESPACE + 1:
                    c = readChar();
                    if (!Character.isWhitespace(c)){
                        back();
                        ttype = WHITESPACE;
                        return ttype;
                    }
                    break;
                case RESOLVE_COMMENT:
                    reset();
                    c = readChar();
                    state = c == ';' ? RESOLVE_COMMENT + 1 : RESOLVE_SYMBOL;
                    break;
                case RESOLVE_COMMENT + 1:
                    c = readChar();
                    if (c == '\r'){
                        back();
                        sval = sb.toString();
                        ttype = COMMENT;
                        return ttype;
                    }
                    break;
                case RESOLVE_SYMBOL:
                    reset();
                    c = readChar();
                    state = ("+-".indexOf(c)) != -1 ? RESOLVE_SYMBOL + 1 : ((Character.isJavaIdentifierStart(c)) ? RESOLVE_SYMBOL + 2 : RESOLVE_NUMBER_DECIMAL);
                    break;
                case RESOLVE_SYMBOL + 1:
                    c = readChar();
                    if (Character.isDigit(c)) {
                        state = RESOLVE_NUMBER_DECIMAL;
                    } else if (c == '$') {
                        state = RESOLVE_NUMBER_HEXADECIMAL;
                    } else {
                        back();
                        state = RESOLVE_SINGLE;
                    }
                    break;
                case RESOLVE_SYMBOL + 2:
                    c = readChar();
                    if (!Character.isJavaIdentifierPart(c)) {
                        back();
                        sval = sb.toString();
                        ival = Keyword.parse(sval);
                        ttype = ival == null ? SYMBOL : KEYWORD;
                        return ttype;
                    }
                    break;
                case RESOLVE_NUMBER_DECIMAL:
                    reset();
                    c = readChar();
                    state = (Character.isDigit(c)) ? RESOLVE_NUMBER_DECIMAL + 1 : RESOLVE_DOUBLE;
                    break;
                case RESOLVE_NUMBER_DECIMAL + 1:
                    c = readChar();
                    if (!Character.isDigit(c)) {
                        back();
                        sval = sb.toString();
                        ttype = INTEGER;
                        return ttype;
                    }
                    break;
                case RESOLVE_NUMBER_HEXADECIMAL:
                    reset();
                    c = readChar();
                    state = (Character.isDigit(c) || "ABCDEFabcdef".indexOf(c) != -1) ? RESOLVE_NUMBER_HEXADECIMAL + 1 : RESOLVE_DOUBLE;
                    break;
                case RESOLVE_NUMBER_HEXADECIMAL + 1:
                    c = readChar();
                    if (!Character.isDigit(c) || "ABCDEFabcdef".indexOf(c) == -1) {
                        back();
                        sval = sb.toString();
                        ttype = INTEGER;
                        return ttype;
                    }
                    break;
                case RESOLVE_DOUBLE:
                    reset();
                    c = readChar();
                    switch(c){
                        case '=':
                            state = RESOLVE_DOUBLE + 1;
                            break;
                        case '<':
                            state = RESOLVE_DOUBLE + 2;
                            break;
                        case '>':
                            state = RESOLVE_DOUBLE + 3;
                            break;
                        case '&':
                            state = RESOLVE_DOUBLE + 4;
                            break;
                        case '|':
                            state = RESOLVE_DOUBLE + 5;
                            break;
                        case '^':
                            state = RESOLVE_DOUBLE + 6;
                            break;
                        case '/':
                            state = RESOLVE_DOUBLE + 7;
                            break;
                        default:
                            state = RESOLVE_SINGLE;
                            break;
                    }
                    break;
                case RESOLVE_DOUBLE + 1:
                    c = this.readChar();
                    if (c == '=') {
                        ival = EQ;
                        ttype = RELOP;
                    } else {
                        this.back();
                        ival = ASS;
                        ttype = BINOP;
                    }
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 2:
                    c = this.readChar();
                    if (c == '>') {
                        ival = NE;
                    } else {
                        this.back();
                        ival = LT;
                    }
                    ttype = RELOP;
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 3:
                    c = this.readChar();
                    if (c == '=') {
                        ival = GE;
                    } else {
                        this.back();
                        ival = GT;
                    }
                    ttype = RELOP;
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 4:
                    ival = ADD;
                    c = this.readChar();
                    if (c == '&') {
                        ttype = LOGOP;
                    } else {
                        this.back();
                        ttype = BINOP;
                    }
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 5:
                    ival = OR;
                    c = this.readChar();
                    if (c == '|') {
                        ttype = LOGOP;
                    } else {
                        this.back();
                        ttype = BINOP;
                    }
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 6:
                    ival = XOR;
                    c = this.readChar();
                    if (c == '^') {
                        ttype = LOGOP;
                    } else {
                        this.back();
                        ttype = BINOP;
                    }
                    sval = sb.toString();
                    return ttype;
                case RESOLVE_DOUBLE + 7:
                    c = this.readChar();
                    if (c != '/'){
                        this.back();
                        sval = sb.toString();
                        ival = DIV;
                        ttype = MULOP;
                        return ttype;
                    }
                    do{
                        c = this.readChar();
                    } while (c != '\n' && c != EOF);
                    state = RESOLVE_SYMBOL;
                    break;
                case RESOLVE_SINGLE:
                    this.reset();
                    c = this.readChar();
                    switch (c) {
                        case '(':
                            ttype = LEFT_PARENTHESIS;
                            break;
                        case ')':
                            ttype = RIGHT_PARENTHESIS;
                            break;
                        case ',':
                            ttype = COMMA;
                            break;
                        case ':':
                            ttype = COLON;
                            break;
                        case '!':
                            ival = NOT;
                            ttype = MONOP;
                            break;
                        case '~':
                            ival = LNOT;
                            ttype = MONOP;
                            break;
                        case '#':
                            ival = IMM;
                            ttype = MONOP;
                            break;
                        case '+':
                            ival = ADD;
                            ttype = ADDOP;
                            break;
                        case '-':
                            ival = SUB;
                            ttype = ADDOP;
                            break;
                        case '*':
                            ival = MUL;
                            ttype = MULOP;
                            break;
                        case '\r':
                            c = readChar();
                            if (c != '\n') back();
                            ttype = Token.EOL;
                            break;
                        case EOF:
                        case 0:
                            ttype = Token.EOF;
                            break;
                        default:
                            ttype = IGNORED;
                            break;
                    }
                    sval = sb.toString();
                    return ttype;
                default:
                    assert false : "lexer invalid state: " + state;
            }
        }
    }

    public int lineno() {
        return line;
    }
}
