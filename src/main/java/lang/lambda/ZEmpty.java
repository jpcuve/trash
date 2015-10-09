package lang.lambda;

public class ZEmpty extends ZExpression {
	private String m_value;
	
	public static ZEmpty VOID = new ZEmpty("#void#");
	public static ZEmpty EMPTY = new ZEmpty("()");
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList() { return true; }
	
	public ZEmpty(String s){
		m_value = s;
	}
	
	public ZExpression append(ZExpression z){
		return z;
	}
	
	public String write(){
		return m_value;
	}
	
	public String toString(){
		return "[ZEm=" + m_value + "]";
	}
	
}
