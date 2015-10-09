package lang.fn;

import java.util.HashMap;

public class Pair extends Expression implements List {
	private Expression a;
	private Expression d;

	public Pair(){
		this(null, Empty.EMPTY );
	}

	public Pair(Expression first){
		this(first, Empty.EMPTY );
	}

	public Pair(Expression first, Expression rest){
		a = first;
		d = rest;
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
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }

	public boolean isEqual(Expression z){
		if(!z.isPair()) return false;
		Pair zl = (Pair)z;
		return (a.isEqual(zl.first()) && d.isEqual(zl.rest()));
	}

	public void rplca(Expression z){
		a = z;
	}

	public void rplcd(Expression z){
		d = z;
	}

	public Expression first(){
		return a;
	}

	public Expression rest(){
		return d;
	}

	public boolean isList(){
		Expression slow = this;
		Expression fast = this;
		while(true){
			if(fast == Empty.EMPTY) return true;
			if(!slow.isPair() || !fast.isPair()) return false;
			Pair slowl = (Pair)slow;
			Pair fastl = (Pair)fast;
			if(slowl == fastl.rest()) return false;
			slow = slowl.rest();
			fast = fastl.rest();
			if(fast == Empty.EMPTY) return true;
			if(!fast.isPair()) return false;
			fastl = (Pair)fast;
			fast = fastl.rest();
		}
	}

	public Expression nth(long n){
		Expression cur = this;
		long l = n;
		while(cur.isPair()){
			Pair curl = (Pair)cur;
			if(l == 0) return curl.first();
			l--;
			cur = curl.rest();
		}
		return Empty.EMPTY ;
	}

	public Expression nthcdr(long n){
		Expression cur = this;
		long i = n;
		while(cur.isPair()){
			Pair curl = (Pair)cur;
			if(i == 0) return curl;
			i--;
			cur = curl.rest();
		}
		return Empty.EMPTY ;
	}

	public Expression second(){
		return nth(1);
	}

	public Expression third(){
		return nth(2);
	}

	public long length(){
		long l = 0;
		Expression slow = this;
		Expression fast = this;
		while(true){
			if(fast == Empty.EMPTY) return l;
			if(!fast.isPair()) return -2;
			Pair fastl = (Pair)fast;
			if(fastl.rest() == Empty.EMPTY) return (l + 1);
			if(fast == slow && l > 0) return -1;
			if(!fastl.rest().isPair()){
				l++;
				fast = fastl.rest();
			}else{
				if(!slow.isPair()) return -2;
				slow = ((Pair)slow).rest();
				fast = ((Pair)fastl.rest()).rest();
				l += 2;
			}
		}
	}

	public Expression append(Expression z){
		Pair curl = this;
		Pair retVal = new Pair(this.first());
		Pair outl = retVal;
		while(curl.rest().isPair()){
			curl = (Pair)curl.rest();
			outl.rplcd(new Pair(curl.first()));
			outl = (Pair)outl.rest();
		}
		outl.rplcd(z);
		return retVal;
	}

	public Expression convertToVector(){
		Vec v = new Vec();
		if(this.isList()){
			Expression cur = this;
			while(cur.isPair()){
				Pair curl = (Pair)cur;
				v.add(curl.first());
				cur = curl.rest();
			}
		}
		return v;
	}

	public static Expression list(Expression z){
		return z.cons(Empty.EMPTY);
	}

	public static Expression list(Expression z1, Expression z2){
		return z1.cons(z2.cons(Empty.EMPTY));
	}

	public static Expression list(Expression z1, Expression z2, Expression z3){
		return z1.cons(z2.cons(z3.cons(Empty.EMPTY)));
	}

	public static Expression list(Expression z1, Expression z2, Expression z3, Expression z4){
		return z1.cons(z2.cons(z3.cons(z4.cons(Empty.EMPTY))));
	}

	// JAVA support

	public String toString(){
		return "[Pa=" + ((a.isPair()) ? "#pair#" : a.toString()) + " . " + ((d.isPair()) ? "#pair#" : d.toString()) + "]";
	}

	public String write(){
		StringBuffer sb = new StringBuffer(1024);
		HashMap p = new HashMap();
		Pair.count = 0;
		Pair.walk(this, p);

		// for(Iterator expirationDate = p.keySet().iterator(); expirationDate.hasNext();){
		// 	ZPair z = (ZPair)expirationDate.next();
		// 	sb.append(z.toString() + " -> " + p.response(z) + "\n");
		// }
		// System.out.println(sb.toString());

		return ((Pair)p.get(this)).write(p);
	}

	public Expression first(HashMap p){
		Expression z = this.first();
		if(z.isPair()) z = (Expression)p.get(z);
		return z;
	}

	public Expression rest(HashMap p){
		Expression z = this.rest();
		if(z.isPair()) z = (Expression)p.get(z);
		return z;
	}

	public String write(HashMap p){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("(");
		Expression cur = this;
		do{
			Pair curl = (Pair)cur;
			if(curl.isPair()) curl = (Pair)p.get(curl);
			Expression cur1 = curl.first(p);
			sb.append(Pair.write(cur1, p));
			sb.append(" ");
			cur = curl.rest(p);
		}while(cur.isPair());
		if(cur != Empty.EMPTY){
			sb.append(". " + Pair.write(cur, p));
		}
		sb.append(")");
		return sb.toString();
	}

	private static int count = 0;

	private static String write(Expression z, HashMap p){
		if(z.isPair()) return ((Pair)z).write(p);
		else if(z.isLink()) return ((Link)z).write(p);
		else return z.write();
	}

	private static void walk(Expression z, HashMap p){
		if(z.isPair()){
			Pair zl = (Pair)z;
			Expression tag = (Expression)p.get(zl);
			if(tag != null){
				if(tag.isPair()) p.put(zl, new Link(zl, Pair.count++ ));
			}else{
				p.put(zl, zl);
				Pair.walk(zl.first(), p);
				Pair.walk(zl.rest(), p);
			}
		}
	}

}