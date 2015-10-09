package lang.lambda;

public class ZBoolean extends ZExpression {
	
	public boolean isBoolean() { return true; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public static ZBoolean TRUE = new ZBoolean();
	public static ZBoolean FALSE = new ZBoolean();
	
	public String toString(){
		return (this == ZBoolean.FALSE) ? "[ZBo=#f]" : "[ZBo=#t]";
	}
	
	public String write(){
		return (this == ZBoolean.FALSE) ? "#f" : "#t";
	}
	
	public static ZBoolean tf(boolean b){
		return (b) ? ZBoolean.TRUE : ZBoolean.FALSE;
	}
											   

}
