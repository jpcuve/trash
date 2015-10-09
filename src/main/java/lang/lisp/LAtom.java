package lang.lisp;

public class LAtom extends LExpression{
	
	public LAtom(){
		super();
	}
		
	// lisp constructs
	
	public boolean atomp(){	return true; }
	public boolean listp(){ return false; }
	public boolean numberp(){ return false;	}
	public boolean symbolp(){ return false;	}
	public boolean integerp(){ return false; }
	public boolean functionp(){ return false; }
	public boolean stringp(){ return false; }
	
	public boolean eq(LExpression l){ return this == l;	}
	public boolean eql(LExpression l){ return this.eq(l); }
	public boolean equal(LExpression l){ return this.eql(l); }
	
	
}
