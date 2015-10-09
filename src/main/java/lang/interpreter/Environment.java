package lang.interpreter;

import java.util.HashMap;
import java.util.Iterator;

public class Environment {
	private Environment prev;
	private HashMap bindings;
	
	public Environment(){
		this(null);
	}
	
	public Environment(Environment prev){
		this.prev = prev;
		bindings = new HashMap();
	}
	
	public Environment(Environment prev, Expression par, Expression arg) throws InterpreterException {
		this(prev);
		Expression curArg = arg;
		Expression curPar = par;
		for(; curPar instanceof Pair; curPar = ((Pair)curPar).rest()){
			if (curArg == Nil.NIL) throw new InterpreterException(Error.WRONG_NB_ARGS, arg);
			this.define((Symbol)((Pair)curPar).first(), ((Pair)curArg).first());
			curArg = ((Pair)curArg).rest();
		}
		if (curArg != Nil.NIL) {
			if (curPar != Nil.NIL) {
				define((Symbol)curPar, curArg);
			} else {
				throw new InterpreterException(Error.WRONG_NB_ARGS, arg);
			}
		}
	}
	
	public Expression define(Expression v, Expression value) throws InterpreterException {
        Symbol sym = (Symbol)v;
		bindings.put(sym, value);
		return null;
	}
	
	public boolean bind(Expression v, Expression value) throws InterpreterException {
        Symbol sym = (Symbol)v;
		Environment cur = this;
		while(cur != null){
			Expression z = (Expression)cur.bindings.get(sym);
			if(z != null){
				cur.bindings.put(sym, value);
				return true;
			}
			cur = cur.prev;
		}
		return false;
	}
	
	public Expression lookup(Expression v) throws InterpreterException {
        Symbol sym = (Symbol)v;
		Environment cur = this;
		while(cur != null){
			Expression z = (Expression)cur.bindings.get(sym);
			if(z != null) return z;
			cur = cur.prev;
		}
		return null;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(16384);
		for(Iterator i = bindings.keySet().iterator(); i.hasNext();){
			Symbol sym = (Symbol)i.next();
			try{
				sb.append("  " + sym.write() + " -> " + this.lookup(sym).write() + "\n");
			}catch(InterpreterException ex){
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}