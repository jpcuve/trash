package lang.fn;

import java.util.Iterator;
import java.util.Vector;

public class Vec extends Expression {
	private Vector value;

	public Vec(){
		value = new Vector();
	}
	
	public Vec(int size, Expression z) {
		this();
		for(int i = 0; i < size; i++) value.add(z);
	}
	
	public Vec(Int n, Expression arg) throws FnException {
		this(n.intValue(), Void.VOID);
		if(((Pair)arg).length() == 2) fill(arg.second());
	}
	
	public Vec(int size) {
		this(size, Void.VOID);
	}
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return true; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isPromise() { return false; }
	public boolean isLink() { return false; }
	public boolean isEOF() { return false; }
	
	public boolean isList(){ return false; }
	
	public boolean isEqual(Expression z){
		if(!z.isVector()) return false;
		Iterator i1 = value.iterator();
		Iterator i2 = ((Vec)z).elements();
		while(i1.hasNext()){
			if(!i2.hasNext()) return false;
			Expression z1 = (Expression)i1.next();
			Expression z2 = (Expression)i2.next();
			if(!z1.isEqual(z2)) return false;
		}
		if(i2.hasNext()) return false;
		return true;
	}
/*
	public Babel cons(Babel z){
		Vec retVal = new Vec();
		retVal.add(z);
		for (Iterator expirationDate = value.iterator(); expirationDate.hasNext(); ) retVal.add(expirationDate.next());
		return (retVal);
	}

	public Babel append(Babel z) throws FnException {
		if(z == Empty.EMPTY) return this;
		if (!z.isVector()) throw new FnException(Error.VECTOR_EXPECTED, z);
		Vec retVal = new Vec();
		for (Iterator expirationDate = value.iterator(); expirationDate.hasNext(); ) retVal.add(expirationDate.next());
		for (Iterator expirationDate = ((Vec)z).value.iterator(); expirationDate.hasNext(); ) retVal.add(expirationDate.next());
		return(retVal);
	}

	public Babel first() throws FnException {
		if (value.size() < 1) throw new FnException(Error.OUT_OF_RANGE, this);
		return((Babel)value.elementAt(0));
	}

	public Babel rest() throws FnException {
		if (value.size() < 1) return(Empty.EMPTY);
		Vec retVal = new Vec();
		Iterator expirationDate = value.iterator();
		expirationDate.next();
		while (expirationDate.hasNext()) {
			retVal.add(expirationDate.next());
		}
		return(retVal);
	}
*/
	public void add(Object o) {
		value.add(o);
	}
	
	public Iterator elements() {
		return value.iterator();
	}
	
	public boolean isElement(Expression z) {
		for(Iterator i1 = value.iterator(); i1.hasNext();) if(z == i1.next()) return true;
		return false;
	}
	
	public String write() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("#(");
		for(Iterator i1 = value.iterator(); i1.hasNext();){
			Expression cur = (Expression)i1.next();
			sb.append(cur.write() + " ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public Expression convertToList() throws FnException {
		int s = value.size();
		if(s == 0) return Empty.EMPTY;
		Expression retVal = new Pair((Expression)value.elementAt(0));
		for(int i = 1; i < s; i++) retVal = retVal.append(new Pair((Expression)value.elementAt(i)));
		return retVal.append(Empty.EMPTY);
	}
	
	/*
	public ZPair toList(){
		ZPair retVal = null;
		ZPair cur = null;
		for(Enumeration e1 = m_value.elements(); e1.hasMoreElements();){
			ZExpression z = (ZExpression)e1.nextElement();
			if(cur == null){
				cur = new ZPair(z);
				retVal = cur;
			}else{
				ZPair temp = new ZPair(z);
				cur.rplcd(temp);
				cur = temp;
			}
		}
		return retVal;
	}
	*/
	public int length()	{
		return value.size();
	}
	
	public Expression elementAt(int index) {
		return (Expression)value.elementAt(index);
	}
	
	public void setElementAt(Expression z, int index) {
		value.setElementAt(z, index);
	}
	
	public Expression fill(Expression z) {
		for(int i = 0; i < value.size(); i++) value.setElementAt(z, i);
		return Void.VOID;
	}
	
	// JAVA support

	public String toString(){
		StringBuffer sb = new StringBuffer(1024);
		sb.append("[Ve=");
		for(Iterator i1 = value.iterator(); i1.hasNext();) sb.append(i1.next());
		sb.append("]");
		return sb.toString();
	}
	
}