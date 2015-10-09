package lang.lambda;

public class ZChar extends ZExpression {
	
	public static ZChar SPACE = new ZChar(' ');
	public static ZChar NEWLINE = new ZChar('\n');
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return true; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public boolean isEqv(ZExpression z){
		if(!z.isChar()) return false;
		return (m_value == ((ZChar)z).getValue());
	}
	
	private char m_value;
	
	public ZChar(char c){
		m_value = c;
	}
	
	public char getValue(){
		return m_value;
	}
	
	public String toString(){
		return "[ZCh=" + m_value + "]";
	}
	
	public String write(){
		if(this == ZChar.SPACE) return "#\\space";
		if(this == ZChar.NEWLINE) return "#\\newline";
		return "#\\" + m_value;
	}			   

}
