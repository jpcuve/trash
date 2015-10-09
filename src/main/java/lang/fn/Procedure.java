package lang.fn;

public abstract class Procedure extends Expression {
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return true; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }
	
	public abstract boolean isPrimitive();
	public abstract boolean isClosure();
	
	public abstract Expression apply(Interpreter ip, Expression arg, Environment env) throws FnException;
	
	public String toString(){
		return "[Pr=" + super.toString() + "]";
	}

	public String write(){
		return "#<procedure>";
	}
}