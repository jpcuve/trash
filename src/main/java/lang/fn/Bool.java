package lang.fn;

public class Bool extends Expression {
	public boolean isBoolean() { return true; }
	public boolean isSymbol() { return false; }
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
	
	public static Bool TRUE = new Bool();
	public static Bool FALSE = new Bool();
	
	public String toString(){
		return (this == Bool.FALSE) ? "[Bo=#f]" : "[Bo=#t]";
	}
	
	public String write(){
		return (this == Bool.FALSE) ? "#f" : "#t";
	}
	
	public static Bool tf(boolean b){
		return (b) ? Bool.TRUE : Bool.FALSE;
	}
	
	public boolean booleanValue() {
		return(this == Bool.TRUE);
	}										   
	
}