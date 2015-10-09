package lang.lambda;

public class ZPort extends ZExpression {
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return true; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public String toString(){
		return "[ZPo=]";
	}
	
	public String write(){
		return this.toString();
	}			   


}
