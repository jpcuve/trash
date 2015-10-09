package lang.fn;

public class Symbol extends Expression {
	public static final Symbol AND = new Symbol("and");
	public static final Symbol BEGIN = new Symbol("begin");
	public static final Symbol CASE = new Symbol("case");
	public static final Symbol COND = new Symbol("cond");
	public static final Symbol DEFINE = new Symbol("define");
	public static final Symbol DEFINE_SYNTAX = new Symbol("define-syntax");
	public static final Symbol DELAY = new Symbol("delay");
	public static final Symbol DO = new Symbol("do");
	public static final Symbol ELSE = new Symbol("else");
	public static final Symbol IF = new Symbol("if");
	public static final Symbol LAMBDA = new Symbol("lambda");
	public static final Symbol LET = new Symbol("let");
	public static final Symbol LET_STAR = new Symbol("let*");
	public static final Symbol LETREC = new Symbol("letrec");
	public static final Symbol LET_SYNTAX = new Symbol("let-syntax");
	public static final Symbol LETREC_SYNTAX = new Symbol("letrec-syntax");
	public static final Symbol OR = new Symbol("or");
	public static final Symbol QUOTE = new Symbol("quote");
	public static final Symbol QUASIQUOTE = new Symbol("quasiquote");
	public static final Symbol SET_EXCLAMATION = new Symbol("from!");
	public static final Symbol SYNTAX_RULES = new Symbol("syntax-rules");
	public static final Symbol UNQUOTE = new Symbol("unquote");
	public static final Symbol UNQUOTE_SPLICING = new Symbol("unquote-splicing");
	public static final Symbol RECEIPT = new Symbol("=>");
	public static final Symbol CONS = new Symbol("cons");
	public static final Symbol APPEND = new Symbol("append");

	private String value;

	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return true; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }

	public boolean isList(){ return false; }

	public Symbol(String s){
		value = s;
	}

	public String write() {
		return(value);
	}
	
	public String stringValue() {
		return(value);
	}

	public String toString() {
		return("[Sy=" + value + "]");
	}
}