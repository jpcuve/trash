package lang.fn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class Parser {
	private HashMap symbols;

	public Parser(Interpreter ip) {
		symbols = new HashMap();
		for (int i = 0; i < ip.getBuiltIns().length; i++) {
			recSymbol(ip.getBuiltIns()[i]);
		}
	}

	public void recSymbol(Symbol sym){
		symbols.put(sym.stringValue(), sym);
	}

	public Symbol recSymbol(String s){
		Symbol sym = (Symbol)symbols.get(s);
		if(sym == null){
			sym = new Symbol(s);
			symbols.put(s , sym);
		}
		return sym;
	}

	public Expression read(Tokenizer t)
	throws FnException {
		return(read(t, 0));
	}

	public Expression read(Tokenizer t, int depth)
	throws FnException {
		do {
			t.nextToken();
		} while (t.ttype == Tokenizer.IGNORE);
		Expression e = readList(t, depth);
		if (e == null) e = readVector(t, depth);
		if (e == null) e = readSimple(t, depth);
		return(e);
	}

	public Expression readSimple(Tokenizer t, int depth)
	throws FnException {
		int d = depth + 1;
		switch (t.ttype) {
			case Tokenizer.EOF:
				return(null);
			case Tokenizer.UNKNOWN:
				throw new FnException(Error.SYNTAX, t.sval);
			case Tokenizer.RIGHT_PARENTHESIS:
				if (depth == 0) throw new FnException(Error.UNBALANCED_PARENTHESIS, t.sval);
				return(null);
			case Tokenizer.DOT:
				if (depth == 0) throw new FnException(Error.SYNTAX, t.sval);
				return(null);
			case Tokenizer.QUOTE:
				return(new Pair(Symbol.QUOTE, new Pair(read(t, d))));
			case Tokenizer.QUASIQUOTE:
				return(new Pair(Symbol.QUASIQUOTE, new Pair(read(t, d))));
			case Tokenizer.UNQUOTE:
				return(new Pair(Symbol.UNQUOTE, new Pair(read(t, d))));
			case Tokenizer.UNQUOTE_SPLICING:
				return(new Pair(Symbol.UNQUOTE_SPLICING, new Pair(read(t, d))));
			case Tokenizer.IDENTIFIER:
				return(recSymbol(t.sval));
			case Tokenizer.NUMBER:
				return((t.sval.indexOf('.') != -1 || t.sval.indexOf('e') != -1 || t.sval.indexOf('E') != -1) ? (Nb)new Real(new BigDecimal(t.sval)) : (Nb)new Int(new BigInteger(t.sval)));
			case Tokenizer.BOOLEAN:
				return((t.sval.charAt(1) == 't') ? Bool.TRUE : Bool.FALSE);
			case Tokenizer.CHARACTER:
				return((t.sval.length() == 3) ? new Char(t.sval.charAt(2)) : new Char(Integer.parseInt(t.sval.substring(2))));
			case Tokenizer.STRING:
				return(new Str(t.sval.substring(1, t.sval.length() - 1)));
			case Tokenizer.EMPTY:
				return(Empty.EMPTY);
		}
		return(null);
	}

	public Expression readList(Tokenizer t, int depth)
	throws FnException {
		if (t.ttype != Tokenizer.LEFT_PARENTHESIS) return(null);
		int d = depth + 1;
		Pair l = new Pair();
		Pair cur = l;
		Expression e = read(t, d);
		while (e != null) {
			cur.rplca(e);
			e = read(t, d);
			if (e != null) {
				Pair z = new Pair();
				cur.rplcd(z);
				cur = z;
			}
		}
		if (t.ttype == Tokenizer.DOT) {
			e = read(t, d);
			cur.rplcd(e);
			e = read(t, d);
		}
		if (t.ttype == Tokenizer.EOF) throw new FnException(Error.UNBALANCED_PARENTHESIS, t.sval);
		if (t.ttype != Tokenizer.RIGHT_PARENTHESIS) throw new FnException(Error.SYNTAX, t.sval);
		return(l);
	}

	public Expression readVector(Tokenizer t, int depth)
	throws FnException {
		if (t.ttype != Tokenizer.LEFT_VECTOR) return(null);
		int d = depth + 1;
		Vec v = new Vec();
		Expression e = read(t, d);
		while (e != null) {
			v.add(e);
			e = read(t, d);
		}
		if (t.ttype == Tokenizer.EOF) throw new FnException(Error.UNBALANCED_PARENTHESIS, t.sval);
		if (t.ttype != Tokenizer.RIGHT_PARENTHESIS) throw new FnException(Error.SYNTAX, t.sval);
		return(v);
	}

}