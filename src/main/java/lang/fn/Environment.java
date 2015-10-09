package lang.fn;

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
	
	public Environment(Environment prev, Expression par, Expression arg) throws FnException {
		this(prev);
		Expression curArg = arg;
		Expression curPar = par;
		for(; curPar.isPair(); curPar = ((Pair)curPar).rest()){
			if (curArg == Empty.EMPTY) throw new FnException(Error.WRONG_NB_ARGS, arg);
			this.define((Symbol)((Pair)curPar).first(), ((Pair)curArg).first());
			curArg = ((Pair)curArg).rest();
		}
		if (curArg != Empty.EMPTY) {
			if (curPar != Empty.EMPTY) {
				define((Symbol)curPar, curArg);
			} else {
				throw new FnException(Error.WRONG_NB_ARGS, arg);
			}
		}
	}

	public Expression define(Expression sym, Expression value) throws FnException {
		if(!sym.isSymbol()) throw new FnException(Error.SYMBOL_EXPECTED, sym);
		bindings.put(sym, value);
		return Void.VOID;
	}

	public boolean bind(Expression sym, Expression value) throws FnException {
		if(!sym.isSymbol()) throw new FnException(Error.SYMBOL_EXPECTED, sym);
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

	public Expression lookup(Symbol sym) throws FnException {
		if(!sym.isSymbol()) throw new FnException(Error.SYMBOL_EXPECTED, sym);
		Environment cur = this;
		while(cur != null){
			Expression z = (Expression)cur.bindings.get(sym);
			if(z != null) return z;
			cur = cur.prev;
		}
		return Void.VOID ;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(16384);
		for(Iterator i = bindings.keySet().iterator(); i.hasNext();){
			Symbol sym = (Symbol)i.next();
			try{
				sb.append("  " + sym.write() + " -> " + this.lookup(sym).write() + "\n");
			}catch(FnException ex){
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}