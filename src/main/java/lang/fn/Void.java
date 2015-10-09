package lang.fn;

public class Void extends Expression {
	public static Void VOID = new Void();

	public boolean isBoolean() { return false; }
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

	public boolean isList() { return false; }

	public String write(){
		return "";
	}

	public String toString(){
		return "[Vo]";
	}


}