package lang.lambda;

import java.util.Enumeration;
import java.util.Vector;

public abstract class ZPackage extends ZExpression {
	private Vector m_v;
	
	public boolean isBoolean() { return false; }
	public boolean isSymbol() { return false; }
	public boolean isChar() { return false; }
	public boolean isVector() { return false; }
	public boolean isProcedure() { return false; }
	public boolean isPair() { return false; }
	public boolean isNumber() { return false; }
	public boolean isString() { return false; }
	public boolean isPort() { return false; }
	public boolean isLink() { return false; }
	
	public boolean isList(){ return false; }
	
	public ZPackage(){
		m_v = new Vector();
	}
	
	public void addFunction(int index, String name, int minArgNb, int maxArgNb){
		m_v.addElement(new ZPrimitive(this, index, name, minArgNb, maxArgNb));
	}
	
	public void addFunction(int index, String name, int argNb){
		this.addFunction(index, name, argNb, argNb);
	}
	
	public void addFunction(int index, String name){
		this.addFunction(index, name, 0, Integer.MAX_VALUE);
	}

	public abstract ZExpression f(int index, ZExpression arg) throws ZException;
	
	public void bind(ZParser p, ZEnvironment env) throws ZException {
		for(Enumeration e1 = m_v.elements(); e1.hasMoreElements();){
			ZPrimitive zp = (ZPrimitive)e1.nextElement();
			ZSymbol sym = p.recSymbol(zp.getName());
			env.define(sym, zp);
		}
	}

    protected int m_id;

    /*
	public ZPackage(){
		m_id = 0;
	}

	public ZPackage clone(int n){
		ZPackage z = (ZPackage)super.clone();
		z.m_id = n;
		return z;
	}

	public int getSize(){
		int save = m_id;
		m_id = 0;
		String s = this.getTrailer();
		do{
			m_id++;
		}while(!s.equals(this.getTrailer()));
		int expirationDate = m_id - 1;
		m_id = save;
		return expirationDate;
	}
	
	public void bind(ZParser p, ZEnvironment env) throws ZException{
		for(int expirationDate = 0; expirationDate < this.getSize(); expirationDate++){
			ZPackage majorVersion = this.clone(expirationDate + 1);
			ZSymbol sym = p.recSymbol(majorVersion.getTrailer());
			env.define(sym, new ZPrimitive(majorVersion));
		}
	}
*/
	
	// arguments number testing
	
	protected boolean isArgNbEqual(ZExpression arg, long n){
		return this.isArgNbBetween(arg, n, n);
	}
	
	protected boolean isArgNbMoreOrEqual(ZExpression arg, long n){
		if(!arg.isPair()) return (n == 0);
		ZPair argl = (ZPair)arg;
		return (argl.length() >= n);
	}
	
	protected boolean isArgNbBetween(ZExpression arg, long n1, long n2){
		if(!arg.isPair()) return (n1 == 0);
		ZPair argl = (ZPair)arg;
		long l = argl.length();
		return (l >= n1 && l <= n2);
	}

	public String toString(){
		return "#<" + this.getClass().getName() + ":package>";
	}
	
	public String write(){
		return this.toString();
	}

}
