/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 13-May-02
 * Time: 18:15:40
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import java.io.IOException;
import java.io.Reader;

public class Tokenizer {
    public static final int TT_INVALID = 0;
    public static final int TT_EOF = 1;
    public static final int TT_EOL = 2;
    public static final int TT_ASSIGN = 3;
    public static final int TT_EXCLAMATION = 4;
    public static final int TT_QUESTION = 5;
    public static final int TT_COLON = 6;
    public static final int TT_EQ = 7;
    public static final int TT_NE = 8;
    public static final int TT_LT = 9;
    public static final int TT_LE = 10;
    public static final int TT_GT = 11;
    public static final int TT_GE = 12;
    public static final int TT_LEFTPARENTHESIS = 13;
    public static final int TT_RIGHTPARENTHESIS = 14;
    public static final int TT_ADD = 15;
    public static final int TT_SUB = 16;
    public static final int TT_MUL = 17;
    public static final int TT_DIV = 18;
    public static final int TT_FLOATINGPOINT = 19;
    public static final int TT_INTEGER = 20;
    public static final int TT_SYMBOL = 21;

    public static int ttype;
	public static String sval;

	private static final int HALF_BUFFER_SIZE = 8;
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

	private final int CP = 100;
	private final int SY = 200;
	private final int NB = 300;
	private final int NB2 = 310;
	private final int NB3 = 320;
	private final int SG = 400;

	public int nextToken() {
		char c;
		int state = CP;
		while (true) {
			switch(state){
				case CP:
					do{
						this.mark();
						c = this.readChar();
					} while (Character.isSpaceChar(c) || c == '\r'); // could change depending systems
					switch(c){
						case '<':
							state = CP + 1;
							break;
						case '=':
							state = CP + 2;
							break;
						case '>':
							state = CP + 3;
							break;
						default:
							state = SY;
							break;
					}
					break;
				case CP + 1:
					c = this.readChar();
					switch(c){
						case '=':
							ttype = TT_LE;
							sval = sb.toString();
							return ttype;
						case '>':
							ttype = TT_NE;
							sval = sb.toString();
							return ttype;
						default:
							this.back();
							ttype = TT_LT;
							sval = sb.toString();
							return ttype;
					}
				case CP + 2:
					c = this.readChar();
					switch(c){
						case '=':
							sval = sb.toString();
							ttype = TT_EQ;
							return ttype;
						default:
							this.back();
							sval = sb.toString();
							ttype = TT_ASSIGN;
							return ttype;
					}
				case CP + 3:
					c = this.readChar();
					switch(c){
						case '=':
							sval = sb.toString();
							ttype = TT_GE;
							return ttype;
						default:
							this.back();
							sval = sb.toString();
							ttype = TT_GT;
							return ttype;
					}
				case SY:
					this.reset();
					c = this.readChar();
                    state = Character.isLetter(c) ? SY + 1 : NB;
					break;
				case SY + 1:
					c = this.readChar();
					if (Character.isLetterOrDigit(c)){
						state = SY + 1;
					} else{
						this.back();
                        sval = sb.toString();
						ttype = TT_SYMBOL;
						return ttype;
					}
					break;
				case NB:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NB + 1 : NB2;
					break;
				case NB + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB + 1;
					} else{
						switch(c){
							case '.':
								state = NB + 2;
								break;
							case 'E':
							case 'e':
								state = NB + 4;
								break;
							default:
								state = NB2;
								break;
						}
					}
					break;
				case NB + 2:
					c = this.readChar();
					state = Character.isDigit(c) ? NB + 3 : NB2;
					break;
				case NB + 3:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB + 3;
					} else{
						switch(c){
							case 'E':
							case 'e':
								state = NB + 4;
								break;
							default:
								state = NB2;
								break;
						}
					}
					break;
				case NB + 4:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB + 6;
					} else{
						switch(c){
							case '+':
							case '-':
								state = NB + 5;
								break;
							default:
								state = NB2;
								break;
						}
					}
					break;
				case NB + 5:
					c = this.readChar();
					state = Character.isDigit(c) ? NB + 6 : NB2;
					break;
				case NB + 6:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB + 6;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_FLOATINGPOINT;
						return ttype;
					}
					break;
				case NB2:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NB2 + 1 : NB3;
					break;
				case NB2 + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB2 + 1;
					} else{
						switch(c){
							case '.':
								state = NB2 + 2;
								break;
							default:
								state = NB3;
								break;
						}
					}
					break;
				case NB2 + 2:
					c = this.readChar();
					state = Character.isDigit(c) ? NB2 + 3 : NB3;
					break;
				case NB2 + 3:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB2 + 3;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_FLOATINGPOINT;
						return ttype;
					}
					break;
				case NB3:
					this.reset();
					c = this.readChar();
					state = Character.isDigit(c) ? NB3 + 1 : SG;
					break;
				case NB3 + 1:
					c = this.readChar();
					if (Character.isDigit(c)){
						state = NB3 + 1;
					} else{
						this.back();
						sval = sb.toString();
						ttype = TT_INTEGER;
						return ttype;
					}
					break;
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
						case '+':
							ttype = TT_ADD;
							break;
						case '-':
							ttype = TT_SUB;
							break;
						case '/':
							ttype = TT_DIV;
							break;
						case '*':
							ttype = TT_MUL;
							break;
						case '!':
							ttype = TT_EXCLAMATION;
							break;
						case '?':
							ttype = TT_QUESTION;
							break;
						case ':':
							ttype = TT_COLON;
							break;
						case EOF:
							ttype = TT_EOF;
							break;
						case EOL:
							line++;
							ttype = TT_EOL;
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
