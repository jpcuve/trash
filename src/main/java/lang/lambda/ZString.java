package lang.lambda;

public class ZString extends ZExpression {
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return true; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }

	public boolean isEqual(ZExpression z){
		if(!z.isString()) return false;
		return (m_value.equals(((ZString)z).getValue()));
	}
	
	private String m_value;
	
	public ZString(String s){
		m_value = s;
	}
	
	public String getValue(){
		return m_value;
	}
	
	public String toString(){
		return "[ZSt=" + m_value + "]";
	}
	
	public String write(){
		return "\"" + m_value + "\"";
	}


}
