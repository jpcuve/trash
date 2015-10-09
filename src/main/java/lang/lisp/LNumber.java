package lang.lisp;

public abstract class LNumber extends LAtom{
	
	public abstract boolean integerp();
	public abstract boolean floatp();
	
	public static LNumber zero(){
		return new LInteger("0");
	}
	
	public static LNumber one(){
		return new LInteger("1");
	}
	
	// number methods
	
	public abstract int compareTo(LNumber n);
	public abstract int signum();
	public abstract LNumber random();
	public abstract LNumber max(LNumber n);
	public abstract LNumber min(LNumber n);
	public abstract LNumber floaT();
	public abstract LNumber round();
	public abstract LNumber add(LNumber n);
	public abstract LNumber sub(LNumber n);
	public abstract LNumber mul(LNumber n);
	public abstract LNumber div(LNumber n);
	

	
	// lisp constructs
	
	public boolean numberp(){
		return true;
	}
			
	// JAVA support
	
	public String toString(){
		return "number[?]";
	}
	
}
