package lang.interpreter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class Pair extends Expression implements List {
	private Expression a;
	private Expression d;
    private int references;

	public Pair(){
		this(Nil.NIL, Nil.NIL );
	}

	public Pair(Expression first){
		this(first, Nil.NIL );
	}

	public Pair(Expression first, Expression rest){
		a = first;
		d = rest;
        references = 0;
	}


	public boolean isEqual(Expression z){
		if(!(z instanceof Pair)) return false;
		Pair zl = (Pair)z;
		return (a.isEqual(zl.a) && d.isEqual(zl.d));
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
			if(fast == Nil.NIL) return true;
			if(!(slow instanceof Pair) || !(fast instanceof Pair)) return false;
			Pair slowl = (Pair)slow;
			Pair fastl = (Pair)fast;
			if(slowl == fastl.rest()) return false;
			slow = slowl.rest();
			fast = fastl.rest();
			if(fast == Nil.NIL) return true;
			if(!(fast instanceof Pair)) return false;
			fastl = (Pair)fast;
			fast = fastl.rest();
		}
	}

	public Expression nth(long n){
		Expression cur = this;
		long l = n;
		while(cur instanceof Pair){
			Pair curl = (Pair)cur;
			if(l == 0) return curl.a;
			l--;
			cur = curl.rest();
		}
		return Nil.NIL;
	}

	public Expression nthcdr(long n){
		Expression cur = this;
		long i = n;
		while(cur instanceof Pair){
			Pair curl = (Pair)cur;
			if(i == 0) return curl;
			i--;
			cur = curl.rest();
		}
		return Nil.NIL;
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
			if(fast == Nil.NIL) return l;
			if(!(fast instanceof Pair)) return -2;
			Pair fastl = (Pair)fast;
			if(fastl.rest() == Nil.NIL) return (l + 1);
			if(fast == slow && l > 0) return -1;
			if(!(fastl.rest() instanceof Pair)){
				l++;
				fast = fastl.rest();
			}else{
				if(!(slow instanceof Pair)) return -2;
				slow = ((Pair)slow).rest();
				fast = ((Pair)fastl.rest()).rest();
				l += 2;
			}
		}
	}

	public Expression append(Expression z){
		Pair curl = this;
		Pair retVal = new Pair(this.a);
		Pair outl = retVal;
		while(curl.rest() instanceof Pair){
			curl = (Pair)curl.rest();
			outl.rplcd(new Pair(curl.a));
			outl = (Pair)outl.rest();
		}
		outl.rplcd(z);
		return retVal;
	}

	public static Expression list(Expression z){
		return z.cons(Nil.NIL);
	}

	public static Expression list(Expression z1, Expression z2){
		return z1.cons(z2.cons(Nil.NIL));
	}

	public static Expression list(Expression z1, Expression z2, Expression z3){
		return z1.cons(z2.cons(z3.cons(Nil.NIL)));
	}

	public static Expression list(Expression z1, Expression z2, Expression z3, Expression z4){
		return z1.cons(z2.cons(z3.cons(z4.cons(Nil.NIL))));
	}

	// JAVA support

	public String toString(){
		return "[Pa=" + ((a instanceof Pair) ? "#pair#" : a.toString()) + " . " + ((d instanceof Pair) ? "#pair#" : d.toString()) + "]";
	}

    public String write(){
        Vector v = new Vector();
        circles(v);
        int count = 1;
        for (Iterator i = v.iterator(); i.hasNext();) {
            Pair p = (Pair)i.next();
            if (p.references > 0) {
                p.references = count;
                count++;
            }
            // System.out.println(p.toString() + " references=" + p.references);
        }
        // System.out.println("");
        return writeWithReferences();
    }

    private String writeWithReferences() {
        StringBuffer sb = new StringBuffer(1024);
        if (references < 0) {
            sb.append("#" + (-references) + "#");
        } else{
            if (references > 0) {
                sb.append("#" + references);
                sb.append('=');
                references = -references;
            }
            sb.append('(');
            boolean loop = true;
            Expression cur = this;
            do{
                Pair p = (Pair)cur;
                Expression cur1 = p.a;
                if (cur1 instanceof Pair) sb.append(((Pair)cur1).writeWithReferences());
                else sb.append(cur1.write());
                sb.append(' ');
                cur = p.d;
                if (cur instanceof Pair){
                    if (((Pair)cur).references != 0) {
                        sb.append(". " + ((Pair)cur).writeWithReferences());
                        loop = false;
                    }
                } else{
                    if(cur != Nil.NIL){
                        sb.append(". " + cur.write());
                    }
                    loop = false;
                }
            } while(loop);
            sb.append(')');
        }
        return sb.toString();
    }

    private void circles(Collection c) {
        references = 0;
        c.add(this);
        Pair p = null;
        if (a instanceof Pair) {
            p = (Pair)a;
            if (c.contains(p)) p.references++;
            else p.circles(c);
        }
        if (d instanceof Pair) {
            p = (Pair)d;
            if (c.contains(p)) p.references++;
            else p.circles(c);
        }
    }


}