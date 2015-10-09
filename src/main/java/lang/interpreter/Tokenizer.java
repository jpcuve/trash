package lang.interpreter;

import java.io.IOException;
import java.io.Reader;

public class Tokenizer {
    public static final int TT_INVALID = 0;
    public static final int TT_EOF = 1;
    public static final int TT_LEFTPARENTHESIS = 2;
    public static final int TT_RIGHTPARENTHESIS = 3;
    public static final int TT_QUOTE = 4;
    public static final int TT_QUASIQUOTE = 5;
    public static final int TT_UNQUOTE = 6;
    public static final int TT_UNQUOTESPLICING = 7;
    public static final int TT_DOT = 8;
    public static final int TT_IDENTIFIER = 9;
    public static final int TT_BOOLEAN = 10;
    public static final int TT_NATURAL = 11;

    public static final String ALLOWED_LETTERS = "+-/*<>=?!";

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
                /*
                System.out.println("Buffer read: " + k + " characters");
                for(int expirationDate = 0; expirationDate < BUFFER_SIZE; expirationDate++) {
                    char cc = buf[expirationDate];
                    if (Character.isLetterOrDigit(cc) || "+-/<>=*.".indexOf(cc) != -1) System.out.print(" " + buf[expirationDate]);
                    else if (cc == EOF) System.out.print(" ~");
                    else if (cc == EOL) System.out.print(" -");
                    else System.out.print(" _");
                }
                System.out.println();
                */
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

	private static final int SY = 200;
	private static final int NB = 300;
    private static final int BO = 350;
    private static final int DB = 400;
	private static final int SG = 500;

	public int nextToken() {
		char c;
		int state = SY;
		while (true) {
			switch(state){
                case SY:
                    do{
						this.mark();
						c = this.readChar();
					} while (Character.isSpaceChar(c) || c == '\r' || c == '\n'); // could change depending systems
                    state = (Character.isLetter(c) || ALLOWED_LETTERS.indexOf(c) != -1) ? SY + 1 : NB;
					break;
				case SY + 1:
					c = this.readChar();
					if (Character.isLetterOrDigit(c) || ALLOWED_LETTERS.indexOf(c) != -1){
						state = SY + 1;
					} else{
						this.back();
                        sval = sb.toString();
						ttype = TT_IDENTIFIER;
						return ttype;
					}
					break;
                case NB:
                    this.reset();
                    c = this.readChar();
                    state = (Character.isDigit(c)) ? NB + 1 : BO;
                    break;
				case NB + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB + 1;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_NATURAL;
						return ttype;
					}
					break;
                case BO:
                    this.reset();
                    c = this.readChar();
                    state = (c == '#') ? BO + 1 : DB;
                    break;
                case BO + 1:
                    c = this.readChar();
                    switch(c) {
                        case 't':
                        case 'f':
                            ttype = TT_BOOLEAN;
                            break;
                        default:
                            ttype = TT_INVALID;
                            break;
                    }
                    sval = sb.toString();
                    return ttype;
                case DB:
                    this.reset();
                    c = this.readChar();
                    state = (c == ',') ? DB + 1 : SG;
                    break;
                case DB + 1:
                    c = this.readChar();
                    if (c == '@') {
                        ttype = TT_UNQUOTESPLICING;
                    } else {
                        this.back();
                        ttype = TT_UNQUOTE;
                    }
                    sval = sb.toString();
                    return ttype;
				case SG:
					this.reset();
					c = this.readChar();
					switch(c){
						case '(':
							ttype = TT_LEFTPARENTHESIS;
							break;
						case ')':
							ttype = TT_RIGHTPARENTHESIS;
							break;
						case '\'':
							ttype = TT_QUOTE;
							break;
                        case '`':
                            ttype = TT_QUASIQUOTE;
                            break;
                        case '.':
                            ttype = TT_DOT;
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