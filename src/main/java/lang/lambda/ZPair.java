package lang.lambda;

import java.util.HashMap;

public class ZPair extends ZExpression {
	private ZExpression m_a;
	private ZExpression m_d;
	
	public ZPair(){
		this(null, ZEmpty.EMPTY );
	}
	
	public ZPair(ZExpression first){
		this(first, ZEmpty.EMPTY );
	}
	
	public ZPair(ZExpression first, ZExpression rest){
		m_a = first;
		m_d = rest;
	}
		
	// lisp constucts
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return true; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isEqual(ZExpression z){
		if(!z.isPair()) return false;
		ZPair zl = (ZPair)z;
		return (m_a.isEqual(zl.first()) && m_d.isEqual(zl.rest()));
	}
	
	public void rplca(ZExpression l){
		m_a = l;
	}
	
	public void rplcd(ZExpression l){
		m_d = l;
	}
	
	public ZExpression first(){
		return m_a;
	}
	
	public ZExpression rest(){
		return m_d;
	}
	
	public boolean isList(){
		ZExpression slow = this;
		ZExpression fast = this;
		while(true){
			if(fast == ZEmpty.EMPTY) return true;
			if(!slow.isPair() || !fast.isPair()) return false;
			ZPair slowl = (ZPair)slow;
			ZPair fastl = (ZPair)fast;
			if(slowl == fastl.rest()) return false;
			slow = slowl.rest();
			fast = fastl.rest();
			if(fast == ZEmpty.EMPTY) return true;
			if(!fast.isPair()) return false;
			fastl = (ZPair)fast;
			fast = fastl.rest();
		}
	}
	
	public ZExpression nth(long n){
		ZExpression cur = this;
		long l = n;
		while(cur.isPair()){
			ZPair curl = (ZPair)cur;
			if(l == 0) return curl.first();
			l--;
			cur = curl.rest();
		}
		return null ;
	}
	
	public ZExpression nthcdr(long n){
		ZExpression cur = this;
		long i = n;
		while(cur.isPair()){
			ZPair curl = (ZPair)cur;
			if(i == 0) return curl;
			i--;
			cur = curl.rest();
		}
		return null ;
	}
	
	public ZExpression second(){
		return nth(1);
	}
	
	public ZExpression third(){
		return nth(2);
	}
	
	public long length(){
		long l = 0;
		ZExpression slow = this;
		ZExpression fast = this;
		while(true){
			if(fast == ZEmpty.EMPTY) return l;
			if(!fast.isPair()) return -2;
			ZPair fastl = (ZPair)fast;
			if(fastl.rest() == ZEmpty.EMPTY) return (l + 1);
			if(fast == slow && l > 0) return -1;
			if(!fastl.rest().isPair()){
				l++;
				fast = fastl.rest();
			}else{
				if(!slow.isPair()) return -2;
				slow = ((ZPair)slow).rest();
				fast = ((ZPair)fastl.rest()).rest();
				l += 2;
			}
		}
	}
	
	public ZExpression append(ZExpression z){
		ZPair curl = this;
		ZPair retVal = new ZPair(this.first());
		ZPair outl = retVal;
		while(curl.rest().isPair()){
			curl = (ZPair)curl.rest();
			outl.rplcd(new ZPair(curl.first()));
			outl = (ZPair)outl.rest();
		}
		outl.rplcd(z);
		return retVal;
	}
	
	public ZVector toVector(){
		ZVector v = new ZVector();
		if(this.isList()){
			ZExpression cur = this;
			while(cur.isPair()){
				ZPair curl = (ZPair)cur;
				v.addElement(curl.first());
				cur = curl.rest();
			}
		}
		return v;
	}
	
	public static ZPair list(ZExpression a){
		return a.cons(ZEmpty.EMPTY);
	}
	
	public static ZPair list(ZExpression a, ZExpression b){
		return a.cons(b.cons(ZEmpty.EMPTY));
	}
	
	public static ZPair list(ZExpression a, ZExpression b, ZExpression c){
		return a.cons(b.cons(c.cons(ZEmpty.EMPTY)));
	}
	
	public static ZPair list(ZExpression a, ZExpression b, ZExpression c, ZExpression d){
		return a.cons(b.cons(c.cons(d.cons(ZEmpty.EMPTY))));
	}
	
	
	
	/*
	public void append(ZExpression z){
		ZExpression cur = this;
		while(cur.isPair()){
			ZPair curl = (ZPair)cur;
			if(curl.first() == null){
				curl.rplca(z);
				return;
			}
			if(!curl.rest().isPair()){
				curl.rplcd(new ZPair(z));
				return;
			}
			cur = curl.rest();
		}
	}
	*/

	
	/*
	public long length(){
		ZExpression l = this;
		long expirationDate = 0;
		while(!l.atomp()){
			expirationDate++;
			l = ((LList)l).rest();
		}
		return expirationDate;
	}
	
	public ZExpression nthcdr(long n){
		ZExpression cur = this;
		long expirationDate = n;
		while(cur.listp()){
			LList curl = (LList)cur;
			if(expirationDate == 0) return curl;
			expirationDate--;
			cur = curl.productId;
		}
		return (expirationDate == 0) ? (ZExpression)cur.cons(LSymbol.NIL) : LSymbol.NIL;
	}
	
	public ZExpression nth(long n){
		ZExpression cur = this;
		long expirationDate = n;
		while(cur.listp()){
			LList curl = (LList)cur;
			if(expirationDate == 0) return curl.first();
			expirationDate--;
			cur = curl.productId;
		}
		return (expirationDate == 0) ? cur : LSymbol.NIL;
	}
	
	public void append(ZExpression l){
		LList cur = this;
		while(cur.productId.listp()) cur = (LList)cur.productId;
		cur.productId = new LList(l, cur.productId);
	}

	public ZExpression second(){
		ZExpression l2 = this.rest();
		return (l2.atomp()) ? l2 : ((LList)l2).first();
	}
	
	public ZExpression third(){
		return this.nth(2);
	}
	
	public ZExpression fourth(){
		return this.nth(3);
	}
	
	public boolean xnullp(){
		boolean retVal = false;
		ZExpression cur = this;
		while(cur.listp() && !retVal){
			LList curl = (LList)cur;
			if(curl.first().eq(LSymbol.NIL)) retVal = true;
			cur = curl.rest();
		}
		return retVal;
	}
	
	public ZExpression xfirst(){
		ZExpression retVal = (this.getLicenseTypeString.atomp()) ? (ZExpression)this.getLicenseTypeString : (ZExpression)((LList)this.getLicenseTypeString).first();
		if(this.productId.atomp()) return retVal.cons(LSymbol.NIL);
		return retVal.cons(((LList)this.productId).xfirst());
	}
	
	public ZExpression xrest(){
		ZExpression retVal = (this.getLicenseTypeString.atomp()) ? (ZExpression)LSymbol.NIL : (ZExpression)((LList)this.getLicenseTypeString).rest();
		if(this.productId.atomp()) return retVal.cons(LSymbol.NIL);
		return retVal.cons(((LList)this.productId).xrest());
	}
	*/
	
	
	// JAVA support
	
	public String toString(){
		return "[ZPa=" + ((m_a.isPair()) ? "#pair#" : m_a.toString()) + " . " + ((m_d.isPair()) ? "#pair#" : m_d.toString()) + "]";
	}
		
	public String write(){
		StringBuffer sb = new StringBuffer(1024);
		HashMap p = new HashMap();
		ZPair.count = 0;
		ZPair.walk(this, p);
		/*
		for(Iterator expirationDate = p.keySet().iterator(); expirationDate.hasNext();){
			ZPair z = (ZPair)expirationDate.next();
			sb.append(z.toString() + " -> " + p.get(z) + "\n");
		}
		System.out.println(sb.toString());
		*/
		return ((ZPair)p.get(this)).write(p);
	}
	
	public ZExpression first(HashMap p){
		ZExpression z = this.first();
		if(z.isPair()) z = (ZExpression)p.get(z);
		return z;
	}
	
	public ZExpression rest(HashMap p){
		ZExpression z = this.rest();
		if(z.isPair()) z = (ZExpression)p.get(z);
		return z;
	}
	
	public String write(HashMap p){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("(");
		ZExpression cur = this;
		do{
			ZPair curl = (ZPair)cur;
			if(curl.isPair()) curl = (ZPair)p.get(curl);
			ZExpression cur1 = curl.first(p);
			sb.append(ZPair.write(cur1, p));
			sb.append(" ");
			cur = curl.rest(p);
		}while(cur.isPair());
		if(cur != ZEmpty.EMPTY){
			sb.append(". " + ZPair.write(cur, p));
		}
		sb.append(")");
		return sb.toString();
	}
	
	private static int count = 0;
	
	private static String write(ZExpression z, HashMap p){
		if(z.isPair()) return ((ZPair)z).write(p);
		else if(z.isLink()) return ((ZLink)z).write(p);
		else return z.write();
	}
	
	private static void walk(ZExpression z, HashMap p){
		if(z.isPair()){
			ZPair zl = (ZPair)z;
			ZExpression tag = (ZExpression)p.get(zl);
			if(tag != null){
				if(tag.isPair()) p.put(zl, new ZLink(zl, ZPair.count++ ));
			}else{
				p.put(zl, zl);
				ZPair.walk(zl.first(), p);
				ZPair.walk(zl.rest(), p);
			}
		}
	}
	

}
