package lang.fn;

import java.util.Iterator;
import java.util.Vector;

public abstract class Pack extends Expression {
	private Vector v;

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
	public boolean isPromise() { return false; }
	public boolean isEOF() { return false; }

	public boolean isList(){ return false; }

	public Pack(){
		v = new Vector();
	}

	public void addFunction(int index, String name, int minArgNb, int maxArgNb){
		v.add(new Primitive(this, index, name, minArgNb, maxArgNb));
	}

	public void addFunction(int index, String name, int argNb){
		addFunction(index, name, argNb, argNb);
	}

	public void addFunction(int index, String name){
		addFunction(index, name, 0, Integer.MAX_VALUE);
	}
	
	public abstract Expression f(int index, Interpreter ip, Expression arg, Environment env) throws FnException;

	public void bind(Parser p, Environment env) throws FnException {
		for(Iterator i = v.iterator(); i.hasNext();){
			Primitive zp = (Primitive)i.next();
			Symbol sym = p.recSymbol(zp.getName());
			env.define(sym, zp);
		}
	}

	public String toString(){
		return "#<" + this.getClass().getName() + ":package>";
	}

	public String write(){
		return this.toString();
	}
	
}