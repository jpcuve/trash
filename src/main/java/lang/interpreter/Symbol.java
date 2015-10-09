package lang.interpreter;

public class Symbol extends Expression {
	public static final Symbol AND = new Symbol("and");
	public static final Symbol BEGIN = new Symbol("begin");
	public static final Symbol CASE = new Symbol("case");
	public static final Symbol COND = new Symbol("cond");
	public static final Symbol DEFINE = new Symbol("define");
	public static final Symbol DO = new Symbol("do");
	public static final Symbol ELSE = new Symbol("else");
	public static final Symbol IF = new Symbol("if");
	public static final Symbol LAMBDA = new Symbol("lambda");
	public static final Symbol LET = new Symbol("let");
	public static final Symbol LETSTAR = new Symbol("let*");
	public static final Symbol LETREC = new Symbol("letrec");
	public static final Symbol OR = new Symbol("or");
	public static final Symbol QUOTE = new Symbol("quote");
	public static final Symbol QUASIQUOTE = new Symbol("quasiquote");
	public static final Symbol SETEXCLAMATION = new Symbol("from!");
	public static final Symbol UNQUOTE = new Symbol("unquote");
	public static final Symbol UNQUOTESPLICING = new Symbol("unquote-splicing");
	public static final Symbol RECEIPT = new Symbol("=>");

    public static final Symbol[] BUILTIN = {
        AND,
        BEGIN,
        CASE,
        COND,
        DEFINE,
        DO,
        ELSE,
        IF,
        LAMBDA,
        LET,
        LETSTAR,
        LETREC,
        OR,
        QUOTE,
        QUASIQUOTE,
        SETEXCLAMATION,
        UNQUOTE,
        UNQUOTESPLICING,
        RECEIPT
    };

	private String value;

	public Symbol(String s){
		value = s;
	}

    public Symbol() {
        this(null);
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