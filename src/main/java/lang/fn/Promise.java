package lang.fn;

public class Promise extends Expression {
	private Expression delay;
	private Expression value;
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return true; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }

	public boolean isList() { return true; }
	
	public Promise(Expression z) {
		delay = z;
		value = null;
	}
	
	public Expression force(Interpreter ip, Environment env)
	throws FnException {
		if (value == null) value = ip.eval(delay, env);
		return(value);
	}

	public String write() {
		return("#<promise>");
	}
}