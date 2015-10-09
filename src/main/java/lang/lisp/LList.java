package lang.lisp;

public class LList extends LExpression{
	private LExpression a;
	private LExpression d;
	
	public LList(){
		this(LSymbol.NIL, LSymbol.NIL);
	}
	
	public LList(LExpression first){
		this(first, LSymbol.NIL);
	}
	
	public LList(LExpression first, LExpression rest){
		super();
		this.a = first;
		this.d = rest;
	}
	
	public void setD(LExpression l){
		this.d = l;
	}
	
	// lisp constucts
	
	public boolean atomp(){ return false; }
	public boolean listp(){ return true; }
	public boolean numberp(){ return false; }
	public boolean symbolp(){ return false;	}
	public boolean integerp(){ return false; }
	public boolean functionp(){ return this.a.eq(LSymbol.LAMBDA); }
	public boolean stringp(){ return false; }
	
	public boolean eq(LExpression l){ return false; }
	public boolean eql(LExpression l){ return false; }
	public boolean equal(LExpression l){
		if(l.atomp()) return false;
		LList ll = (LList)l;
		return (this.first().equal(ll.first()) && this.rest().equal(ll.rest()));
	}
	
	public void rplca(LExpression l){
		this.a = l;
	}
	
	public void rplcd(LExpression l){
		this.d = l;
	}
	
	public LExpression first(){
		return this.a;
	}
	
	public LExpression rest(){
		return this.d;
	}
	
	public long length(){
		LExpression l = this;
		long i = 0;
		while(!l.atomp()){
			i++;
			l = ((LList)l).rest();
		}
		return i;
	}
	
	public LExpression nthcdr(long n){
		LExpression cur = this;
		long i = n;
		while(cur.listp()){
			LList curl = (LList)cur;
			if(i == 0) return curl;
			i--;
			cur = curl.d;
		}
		return (i == 0) ? (LExpression)cur.cons(LSymbol.NIL) : LSymbol.NIL;
	}
	
	public LExpression nth(long n){
		LExpression cur = this;
		long i = n;
		while(cur.listp()){
			LList curl = (LList)cur;
			if(i == 0) return curl.first();
			i--;
			cur = curl.d;
		}
		return (i == 0) ? cur : LSymbol.NIL;
	}
	
	public void append(LExpression l){
		LList cur = this;
		while(cur.d.listp()) cur = (LList)cur.d;
		cur.d = new LList(l, cur.d);
	}

	public LExpression second(){
		LExpression l2 = this.rest();
		return (l2.atomp()) ? l2 : ((LList)l2).first();
	}
	
	public LExpression third(){
		return this.nth(2);
	}
	
	public LExpression fourth(){
		return this.nth(3);
	}
	
	public boolean xnullp(){
		boolean retVal = false;
		LExpression cur = this;
		while(cur.listp() && !retVal){
			LList curl = (LList)cur;
			if(curl.first().eq(LSymbol.NIL)) retVal = true;
			cur = curl.rest();
		}
		return retVal;
	}
	
	public LExpression xfirst(){
		LExpression retVal = (this.a.atomp()) ? (LExpression)this.a : (LExpression)((LList)this.a).first();
		if(this.d.atomp()) return retVal.cons(LSymbol.NIL);
		return retVal.cons(((LList)this.d).xfirst());
	}
	
	public LExpression xrest(){
		LExpression retVal = (this.a.atomp()) ? (LExpression)LSymbol.NIL : (LExpression)((LList)this.a).rest();
		if(this.d.atomp()) return retVal.cons(LSymbol.NIL);
		return retVal.cons(((LList)this.d).xrest());
	}
	
	
	// JAVA support
	
	public String toString(){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("(");
		
		LExpression cur = this;
		while(cur.listp()){
			LList curl = (LList)cur;
			sb.append(curl.first() + " ");
			cur = curl.d;
		}
		if(cur != LSymbol.NIL) sb.append(". " + cur + " ");
		sb.append(")");
		return sb.toString();
	}

}
