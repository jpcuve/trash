package lang.interpreter;

public class Bool extends Expression {
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