package lang.lambda;

public abstract class ZProcedure extends ZExpression {
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return true; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public abstract boolean isPrimitive();
	public abstract boolean isClosure();
	
	public abstract ZExpression apply(ZInterpreter ip, ZExpression arg) throws ZException;
	
	public String toString(){
		return "[ZPr=" + super.toString() + "]";
	}

	public String write(){
		return "#<procedure>";
	}

}
