package lang.smalltalk;

import java.io.IOException;
import java.io.Reader;

public class Tokenizer {
    public static final int TT_INVALID = 0;
    public static final int TT_EOF = 1;
    public static final int TT_LEFTPARENTHESIS = 2;
    public static final int TT_RIGHTPARENTHESIS = 3;
    public static final int TT_LEFTBRACKET = 4;
    public static final int TT_RIGHTBRACKET = 5;
    public static final int TT_DOT = 6;
    public static final int TT_SEMICOLON = 7;
    public static final int TT_COLON = 8;
    public static final int TT_SHARP = 9;
    public static final int TT_RETURN = 10;
    public static final int TT_EXCLAMATION = 11;
    public static final int TT_OR = 12;
    public static final int TT_AND = 13;
    public static final int TT_ASSIGN = 14;
    public static final int TT_LEFTARRAY = 15;


    public static final int TT_IDENTIFIER = 20;
    public static final int TT_INTEGER = 21;
    public static final int TT_FLOATINGPOINT = 22;
    public static final int TT_CHARACTER = 23;
    public static final int TT_STRING = 24;
    public static final int TT_KEYWORD = 25;
    public static final int TT_BINARYSELECTOR = 26;

    public static final String SELECTION = "+-*/~,<>=@?";

    public static int ttype;
	public static String sval;

	private static final int HALF_BUFFER_SIZE = 128;
	private static final int BUFFER_SIZE = HALF_BUFFER_SIZE * 2;

	private static final char EOF = '\u001C';
    private static final char EOL = '\n';

    private Reader r;
	private StringBuffer sb;
	private int line;
	private char[] buf;
	private int fwd;
	private int bck;
	private int skip;

	public Tokenizer(Reader r){
		this.r = r;
		buf = new char[BUFFER_SIZE];
		fwd = BUFFER_SIZE - 1;
		buf[fwd] = EOF;
		line = 1;
		bck = fwd;
		skip = 0;
		sb = new StringBuffer(BUFFER_SIZE);
	}

	private char readChar() {
		char c = buf[fwd % BUFFER_SIZE];
		if (c == EOF && (fwd + 1) % HALF_BUFFER_SIZE == 0){
			fwd++;
			int j = (fwd / HALF_BUFFER_SIZE) & 1;
			if (skip > 0){
				if (skip > 1) throw new RuntimeException("Character pushback length exceeds half buffer size");
				skip--;
			} else{
                int k = -1;
                try {
                    k = r.read(buf, j * HALF_BUFFER_SIZE, HALF_BUFFER_SIZE - 1);
                } catch(IOException ex){
                    throw new RuntimeException(ex.getMessage());
                }
                int fl = (k != -1) ? (j * HALF_BUFFER_SIZE + k) : (fwd % BUFFER_SIZE);
                buf[fl] = EOF;
			}
			c = buf[fwd % BUFFER_SIZE];
		}
		fwd++;
		sb.append(c);
		return c;
	}

	private void mark(){
		bck = fwd;
        sb.setLength(0);
	}

	private void reset(){
		skip += (fwd / HALF_BUFFER_SIZE - bck / HALF_BUFFER_SIZE);
		fwd = bck;
        sb.setLength(0);
	}

	private void back(){
		fwd--;
		if (fwd % HALF_BUFFER_SIZE == 0){
			skip++;
			fwd--;
		}
		sb.setLength(sb.length() - 1);
	}

	private static final int NUMBER = 200;
    public static final int NUMBER2 = 220;
    public static final int NUMBER3 = 240;
	private static final int CHARACTER = 300;
    private static final int STRING = 400;
    private static final int IDENTIFIER = 600;
    private static final int BINARYSELECTOR = 700;
    private static final int DOUBLE = 800;
    private static final int SINGLE = 900;

	public int nextToken() {
		char c;
		int state = CHARACTER;
		while (true) {
			switch(state){
                case CHARACTER:
                    do{
						this.mark();
						c = this.readChar();
					} while (Character.isSpaceChar(c) || c == '\r' || c == '\n'); // could change depending systems
                    state = (c == '$') ? CHARACTER + 1 : STRING;
					break;
                case CHARACTER + 1:
                    c = this.readChar();
                    sval = sb.toString();
                    ttype = TT_CHARACTER;
                    return ttype;
                case STRING:
                    this.reset();
                    c = this.readChar();
                    state = (c == '\'') ? STRING + 1 : IDENTIFIER;
                    break;
                case STRING + 1:
                    c = this.readChar();
                    if (c != '\'') {
                        state = STRING + 1;
                    } else {
                        sval = sb.toString();
                        ttype = TT_STRING;
                        return ttype;
                    }
                    break;
                case IDENTIFIER:
                    this.reset();
                    c = this.readChar();
                    if (Character.isLetter(c)) {
                        state = IDENTIFIER + 1;
                    } else {
                        state = NUMBER;
                    }
                    break;
                case IDENTIFIER + 1:
                    c = this.readChar();
                    if (Character.isLetterOrDigit(c) || ":".indexOf(c) != -1) {
                        state = IDENTIFIER + 1;
                    } else {
                        this.back();
                        sval = sb.toString();
                        if (sb.charAt(sb.length() - 1) == ':') {
                            ttype = TT_KEYWORD;
                        } else {
                            ttype = TT_IDENTIFIER;
                        }
                        return ttype;
                    }
                    break;
                case NUMBER:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER + 1 : NUMBER2;
					break;
				case NUMBER + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER + 1;
					} else{
						switch(c){
							case '.':
								state = NUMBER + 2;
								break;
							case 'E':
							case 'e':
								state = NUMBER + 4;
								break;
							default:
								state = NUMBER2;
								break;
						}
					}
					break;
				case NUMBER + 2:
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER + 3 : NUMBER2;
					break;
				case NUMBER + 3:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER + 3;
					} else{
						switch(c){
							case 'E':
							case 'e':
								state = NUMBER + 4;
								break;
							default:
								state = NUMBER2;
								break;
						}
					}
					break;
				case NUMBER + 4:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER + 6;
					} else{
						switch(c){
							case '+':
							case '-':
								state = NUMBER + 5;
								break;
							default:
								state = NUMBER2;
								break;
						}
					}
					break;
				case NUMBER + 5:
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER + 6 : NUMBER2;
					break;
				case NUMBER + 6:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER + 6;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_FLOATINGPOINT;
						return ttype;
					}
					break;
				case NUMBER2:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER2 + 1 : NUMBER3;
					break;
				case NUMBER2 + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER2 + 1;
					} else{
						switch(c){
							case '.':
								state = NUMBER2 + 2;
								break;
							default:
								state = NUMBER3;
								break;
						}
					}
					break;
				case NUMBER2 + 2:
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER2 + 3 : NUMBER3;
					break;
				case NUMBER2 + 3:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER2 + 3;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_FLOATINGPOINT;
						return ttype;
					}
					break;
				case NUMBER3:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NUMBER3 + 1 : BINARYSELECTOR;
					break;
				case NUMBER3 + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NUMBER3 + 1;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_INTEGER;
						return ttype;
					}
					break;
                case BINARYSELECTOR:
                    this.reset();
                    c = this.readChar();
                    state = (SELECTION.indexOf(c) != -1) ? BINARYSELECTOR + 1 : DOUBLE;
                    break;
                case BINARYSELECTOR + 1:
                    c = this.readChar();
                    if (SELECTION.indexOf(c) != -1) {
                        state = BINARYSELECTOR + 1;
                    } else {
                        this.back();
                        sval = sb.toString();
                        ttype = TT_BINARYSELECTOR;
                        return ttype;
                    }
                    break;
                case DOUBLE:
                    this.reset();
                    c = this.readChar();
                    switch(c) {
                        case '#':
                            state = DOUBLE + 1;
                            break;
                        case ':':
                            state = DOUBLE + 2;
                            break;
                        default:
                            state = SINGLE;
                            break;
                    }
                    break;
                case DOUBLE + 1:
                    c = this.readChar();
                    if (c != '(') {
                        state = SINGLE;
                    } else {
                        sval = sb.toString();
                        ttype = TT_LEFTARRAY;
                        return ttype;
                    }
                    break;
                case DOUBLE + 2:
                    c = this.readChar();
                    if (c != '=') {
                        state = SINGLE;
                    } else {
                        sval = sb.toString();
                        ttype = TT_ASSIGN;
                        return ttype;
                    }
                    break;
                case SINGLE:
					this.reset();
					c = this.readChar();
					switch(c){
						case '(':
							ttype = TT_LEFTPARENTHESIS;
							break;
						case ')':
							ttype = TT_RIGHTPARENTHESIS;
							break;
						case '[':
							ttype = TT_LEFTBRACKET;
							break;
                        case ']':
                            ttype = TT_RIGHTBRACKET;
                            break;
                        case '.':
                            ttype = TT_DOT;
                            break;
                        case ';':
                            ttype = TT_SEMICOLON;
                            break;
                        case ':':
                            ttype = TT_COLON;
                            break;
                        case '#':
                            ttype = TT_SHARP;
                            break;
                        case '^':
                            ttype = TT_RETURN;
                            break;
                        case '!':
                            ttype = TT_EXCLAMATION;
                            break;
                        case '|':
                            ttype = TT_OR;
                            break;
                        case '&':
                            ttype = TT_AND;
                            break;
						case EOF:
                        case 0:
							ttype = TT_EOF;
							break;
						default:
							ttype = TT_INVALID;
							break;
					}
					sval = sb.toString();
					return ttype;
				default:
					throw new RuntimeException("Internal error: lexer invalid state: " + state);
			}
		}
    }

	public int lineno(){
		return line;
	}

}