package lang.interpreter;

import java.util.Iterator;
import java.util.Vector;

public abstract class Pack extends Expression {
	private Vector v;

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
	
	public abstract Expression f(int index, Interpreter ip, Expression arg, Environment env) throws InterpreterException;

	public void bind(Parser p, Environment env) throws InterpreterException {
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