package lang.lisp;

public class LSymbol extends LAtom{
	private String id;
	
	private boolean trace;
	private boolean special;
	
	public static LSymbol NIL = new LSymbol("nil");
	public static LSymbol T = new LSymbol("t");
	public static LSymbol OTHERWISE = new LSymbol("otherwise");											  
	public static LSymbol LAMBDA = new LSymbol("main/java/lambda");
	

	public LSymbol(String s){
		this.id = s ;
		this.clearSpecial();
		this.clearTrace();
	}
		
	public String getName(){
		return this.id;
	}
		
	// lisp constructs
	
	public boolean symbolp(){ return true; }
	
	// accessors
	
	public void setTrace(){
		this.trace = true;
	}
	
	public void clearTrace(){
		this.trace = false;
	}
	
	public boolean isTrace(){
		return this.trace;
	}
	
	public void setSpecial(){
		this.special = true;
	}
	
	public void clearSpecial(){
		this.special = false;
	}
	
	public boolean isSpecial(){
		return this.special;
	}
	
	// JAVA support
	
	public String toString(){
		return this.id;
	}

}
