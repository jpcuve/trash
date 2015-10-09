package lang.fn;

public class Empty extends Expression implements List {
	public static Empty EMPTY = new Empty();

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

	public boolean isList() { return true; }

	public Expression append(Expression z){
		return z;
	}
	
	public long length() {
		return(0);
	}
	
	public Expression convertToVector() {
		return(new Vec());
	}

	public String write(){
		return "()";
	}

	public String toString(){
		return "[Em]";
	}


}