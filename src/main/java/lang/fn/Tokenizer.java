package lang.fn;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class Tokenizer {
	public static final int EOF = -1;
	public static final int UNKNOWN = 0;
	public static final int IGNORE = 1;
	public static final int LEFT_PARENTHESIS = 2;
	public static final int RIGHT_PARENTHESIS = 3;
	public static final int LEFT_VECTOR = 4;
	public static final int QUOTE = 5;
	public static final int QUASIQUOTE = 6;
	public static final int UNQUOTE = 7;
	public static final int UNQUOTE_SPLICING = 8;
	public static final int DOT = 9;
	public static final int IDENTIFIER = 10;
	public static final int NUMBER = 11;
	public static final int STRING = 12;
	public static final int BOOLEAN = 13;
	public static final int CHARACTER = 14;
	public static final int EMPTY = 15;

	private static final int BUFFER_SIZE = 255;
	private static final int MAX_LOOKAHEAD = 5;

	public String sval = null;
	public int ttype = UNKNOWN;

	private Reader r;
	private StringBuffer buffer = new StringBuffer();
	private int bufSize = 0;
	private int bufPtr = 0;
	private int bufMark = 0;

	public Tokenizer(Reader r) {
		this.r = r;
	}

	private void mark() {
		bufMark = bufPtr;
	}

	private void reset() {
		bufPtr = bufMark;
	}

	private char current() {
		return(buffer.charAt(bufPtr - 1));
	}

	public char pull()
	throws IOException {
		if (bufPtr >= bufSize) {
			char[] ca = new char[BUFFER_SIZE];
			int l = r.read(ca);
			if (l == -1) throw new EOFException();
			buffer.append(ca);
			bufSize += l;
		}
		char c = buffer.charAt(bufPtr);
		if (c == -1) throw new EOFException();
		bufPtr++;
		return(c);
	}

	public char push() {
		bufPtr--;
		return(buffer.charAt(bufPtr));
	}

	private void cut() {
		if (bufPtr != 0) {
			sval = buffer.substring(0, bufPtr);
			buffer.delete(0, bufPtr);
			bufSize -= bufPtr;
			buffer.setLength(bufSize);
			bufPtr = 0;
		} else {
			sval = "";
		}
	}

	public void nextToken() {
		nextToken1();
		cut();
	}

	private void nextToken1() {
		ttype = UNKNOWN;
		char c;
		try {
			c = pull();
		} catch(IOException ex) {
			ttype = EOF;
			return;
		}
		try {
			switch (c) {
				case ')':
					ttype = RIGHT_PARENTHESIS;
					return;
				case '\'':
					ttype = QUOTE;
					return;
				case '`':
					ttype = QUASIQUOTE;
					return;
				case '*':
				case '/':
					ttype = IDENTIFIER;
					return;
				case '(':
				case ',':
				case '<':
				case '>':
				case '#':
				case '=':
					nextToken2();
					return;
			}
			if (Character.isDigit(c) || "+-.".indexOf(c) != -1) {
				nextTokenNumber();
				return;
			}
			if (Character.isLetter(c) || ";\"".indexOf(c) != -1) {
				nextTokenN();
				return;
			}
			if (Character.isWhitespace(c) || c == 0) {
				ttype = IGNORE;
				return;
			}
		} catch (IOException ex) {
		}
		
	}

	private void nextToken2()
	throws IOException {
		switch (current()) {
			case '(':
				ttype = LEFT_PARENTHESIS;
				if (pull() == ')') {
					ttype = EMPTY;
					return;
				}
				push();
				return;
			case ',':
				ttype = UNQUOTE;
				if (pull() == '@') {
					ttype = UNQUOTE_SPLICING;
					return;
				}
				push();
				return;
			case '=':
				ttype = IDENTIFIER;
				if ("=>".indexOf(pull()) == -1) push();
				return;
			case '<':
				ttype = IDENTIFIER;
				if (">=".indexOf(pull()) == -1) push();
				return;
			case '>':
				ttype = IDENTIFIER;
				if (pull() != '=') push();
				return;
			case '#':
				char c = pull();
				switch (c) {
					case 't':
					case 'f':
						ttype = BOOLEAN;
						return;
					case '(':
						ttype = LEFT_VECTOR;
						return;
					case '\\':
						pull();
						if (Character.isDigit(current())) {
							while(Character.isDigit(pull()));
							push();
						}
						ttype = CHARACTER;
						return;
				}
				push();

		}
	}

	private void nextTokenN()
	throws IOException {
		char c;
		if (Character.isLetter(current())) {
			ttype = IDENTIFIER;
			do {
				c = pull();
			} while (Character.isLetter(c) || Character.isDigit(c) || ("!$%&*+-./:<=>?@^_~".indexOf(c) != -1));
			push();
			return;
		}

		if (current() == ';') {
			ttype = IGNORE;
			while (pull() != 10);
			push();
			return;
		}
		if (current() == '"') {
			ttype = STRING;
			while (pull() != '"');
			return;
		}
	}
	
	private void nextTokenNumber()
	throws IOException {
		char c;
		// if the number begins by response dot
		if (current() == '.') {
			ttype = DOT;
			c = pull();
			if (!Character.isDigit(c)) {
				push();
				return;
			}
			ttype = NUMBER;
			while(Character.isDigit(pull()));
			if ("Ee".indexOf(current()) != -1) {
				pull();
				if ("+-".indexOf(current()) != -1) pull();
				if (!Character.isDigit(current())) {
					push();
					ttype = IDENTIFIER;
					return;
				}
				while(Character.isDigit(pull()));
			}
			push();
			return;	
		}
		// if the number begins by response + or -
		if ("+-".indexOf(current()) != -1) {
			ttype = IDENTIFIER;
			pull();
			if (current() == '.') {
				c = pull();
				if (!Character.isDigit(c)) {
					push();
					return;
				}
				ttype = NUMBER;
				while(Character.isDigit(pull()));
				if ("Ee".indexOf(current()) != -1) {
					pull();
					if ("+-".indexOf(current()) != -1) pull();
					if (!Character.isDigit(current())) {
						push();
						ttype = IDENTIFIER;
						return;
					}
					while(Character.isDigit(pull()));
				}
				push();
				return;	
			}
			if (!Character.isDigit(current())) {
				ttype = IDENTIFIER;
				push();
				return;
			}
		}
		// if the number begins by response digit
		ttype = NUMBER;
		while(Character.isDigit(pull()));
		if (current() == '.') {
			pull();
			while(Character.isDigit(pull()));
		}
		if ("Ee".indexOf(current()) != -1) {
			pull();
			if ("+-".indexOf(current()) != -1) pull();
			if (!Character.isDigit(current())) {
				push();
				ttype = IDENTIFIER;
				return;
			}
			while(Character.isDigit(pull()));
		}
		push();
		return;	
	}

}