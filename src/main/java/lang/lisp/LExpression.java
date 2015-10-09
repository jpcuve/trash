package lang.lisp;

public abstract class LExpression extends Object implements Cloneable {

	// lisp constructs

	public abstract boolean atomp();
	public abstract boolean listp();
	// public abstract boolean nullp();
	public abstract boolean numberp();
	public abstract boolean symbolp();
	public abstract boolean functionp();
	public abstract boolean stringp();
	
	public abstract boolean eq(LExpression l);
	public abstract boolean eql(LExpression l);
	public abstract boolean equal(LExpression l);
	
	public LList cons(LExpression l){
		return new LList(this, l);
	}
		
	// JAVA support
	
	public Object clone(){
		LExpression l = null;
		try{
			l = (LExpression)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return l;
	}
	

}
