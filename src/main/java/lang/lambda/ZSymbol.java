package lang.lambda;

public class ZSymbol extends ZExpression {
	
	private String m_value;
	
	public static ZSymbol AND = new ZSymbol("and");
	public static ZSymbol BEGIN = new ZSymbol("begin");
	public static ZSymbol CASE = new ZSymbol("case");
	public static ZSymbol COND = new ZSymbol("cond");
	public static ZSymbol DEFINE = new ZSymbol("define");
	public static ZSymbol DEFINE_SYNTAX = new ZSymbol("define-syntax");
	public static ZSymbol DELAY = new ZSymbol("delay");
	public static ZSymbol DO = new ZSymbol("do");
	public static ZSymbol ELSE = new ZSymbol("else");
	public static ZSymbol IF = new ZSymbol("if");
	public static ZSymbol LAMBDA = new ZSymbol("lambda");
	public static ZSymbol LET = new ZSymbol("let");
	public static ZSymbol LET_STAR = new ZSymbol("let*");
	public static ZSymbol LETREC = new ZSymbol("letrec");
	public static ZSymbol LET_SYNTAX = new ZSymbol("let-syntax");
	public static ZSymbol LETREC_SYNTAX = new ZSymbol("letrec-syntax");
	public static ZSymbol OR = new ZSymbol("or");
	public static ZSymbol QUOTE = new ZSymbol("quote");
	public static ZSymbol QUASIQUOTE = new ZSymbol("quasiquote");
	public static ZSymbol SET_EXCLAMATION = new ZSymbol("set!");
	public static ZSymbol SYNTAX_RULES = new ZSymbol("syntax-rules");
	public static ZSymbol UNQUOTE = new ZSymbol("unquote");
	public static ZSymbol UNQUOTE_SPLICING = new ZSymbol("unquote-splicing");
	public static ZSymbol RECEIPT = new ZSymbol("=>");
	
	public static ZSymbol CONS = new ZSymbol("cons");
	public static ZSymbol APPEND = new ZSymbol("append");
	
	public static void register(ZParser zp){
		zp.recSymbol(AND);
		zp.recSymbol(BEGIN);
		zp.recSymbol(CASE);
		zp.recSymbol(COND);
		zp.recSymbol(DEFINE);
		zp.recSymbol(DEFINE_SYNTAX);
		zp.recSymbol(DELAY);
		zp.recSymbol(DO);
		zp.recSymbol(ELSE);
		zp.recSymbol(IF);
		zp.recSymbol(LAMBDA);
		zp.recSymbol(LET);
		zp.recSymbol(LET_STAR);
		zp.recSymbol(LETREC);
		zp.recSymbol(LET_SYNTAX);
		zp.recSymbol(LETREC_SYNTAX);
		zp.recSymbol(OR);
		zp.recSymbol(QUOTE);
		zp.recSymbol(QUASIQUOTE);
		zp.recSymbol(SET_EXCLAMATION);
		zp.recSymbol(UNQUOTE);
		zp.recSymbol(UNQUOTE_SPLICING);
		zp.recSymbol(RECEIPT);
		zp.recSymbol(CONS);
		zp.recSymbol(APPEND);
	}

	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return true; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }

	public boolean isEqv(ZExpression z){
		if(!z.isSymbol()) return false;
		return (this == z);
	}
	
	public ZSymbol(String s){
		m_value = s;
	}
	
	public String getName(){
		return m_value;
	}
	
	public String write(){
		return m_value;
	}

	// JAVA support
	
	public String toString(){
		return "[ZSy=" + m_value + "]";
	}
	
	public int hashCode(){
		return m_value.hashCode();
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ZSymbol)) return false;
		return m_value.equals(((ZSymbol)o).getName());
	}
}
