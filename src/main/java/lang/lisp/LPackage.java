package lang.lisp;

public abstract class LPackage extends LExpression {
	protected int fID;
	public abstract String getInfo();
	public abstract LExpression f(LExpression arg, LEnvironment env) throws LException;
	public LPackage(){
		this.fID = 0;
	}
	public boolean getSpecial(){
		return false;
	}
	public LPackage clone(int n){
		LPackage l = (LPackage)super.clone();
		l.fID = n;
		return l;
	}
	public boolean atomp(){ return false; }
	public boolean listp(){ return false; }
	public boolean nullp(){ return false; }
	public boolean numberp(){ return false; }
	public boolean symbolp(){ return false; }
	public boolean integerp(){ return false; }
	public boolean functionp(){ return true; }
	public boolean stringp(){ return false; }
	
	public boolean eq(LExpression l){ return false; }
	public boolean eql(LExpression l){ return false; }
	public boolean equal(LExpression l){ return false; }
	
	public LList append(LExpression l){ return null; }
	
	public int getSize(){
		int save = this.fID;
		this.fID = 0;
		String s = this.getInfo();
		do{
			this.fID++;
		}while(!s.equals(this.getInfo()));
		int i = this.fID - 1;
		this.fID = save;
		return i;
	}
	
	protected void isArgNumberEqual(String s, LExpression arg, long n) throws LException {
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, s);
		LList argl = (LList)arg;
		if(argl.length() != n) throw new LException(LError.WRONG_NB_ARGS, s);
	}
	
	protected void isArgNumberBetween(String s, LExpression arg, long n1, long n2) throws LException {
		if(arg.atomp() && n1 != 0) throw new LException(LError.WRONG_NB_ARGS, s);
		LList argl = (LList)arg;
		long i = argl.length();
		if(i < n1 || i > n2) throw new LException(LError.WRONG_NB_ARGS, s);
	}
	
	protected void isArgNumberMoreOrEqual(String s, LExpression arg, long n) throws LException {
		if(arg.atomp() && n != 0) throw new LException(LError.WRONG_NB_ARGS, s);
		LList argl = (LList)arg;
		long i = argl.length();
		if(i < n) throw new LException(LError.WRONG_NB_ARGS, s);
	}
	

	
	public String toString(){
		return "#<" + this.getClass().getName() + ":" + this.fID + ":" + this.getInfo() + ">";
	}
}
