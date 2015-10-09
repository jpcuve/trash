package lang.lambda;

import java.util.HashMap;
import java.util.Iterator;

public class ZEnvironment {
    private ZEnvironment m_prev;
    private HashMap m_bindings;

    public ZEnvironment(){
        this(null);
    }

    public ZEnvironment(ZEnvironment prev){
        m_prev = prev;
        m_bindings = new HashMap();
    }

    public ZEnvironment(ZEnvironment prev, ZExpression par, ZExpression arg) throws ZException {
        this(prev);
        ZExpression curArg = arg;
        ZExpression curPar = par;
        for(; curPar.isPair(); curPar = ((ZPair)curPar).rest()){
            if(!curArg.isPair()) throw new ZException(ZError.WRONG_NB_ARGS, arg);
            this.define((ZSymbol)((ZPair)curPar).first(), ((ZPair)curArg).first());
            curArg = ((ZPair)curArg).rest();
        }
        if(curArg.isPair()) throw new ZException(ZError.WRONG_NB_ARGS, arg);
        if(curPar != ZEmpty.EMPTY){
            if(curArg == ZEmpty.EMPTY) throw new ZException(ZError.WRONG_NB_ARGS, arg);
            this.define((ZSymbol)curPar, curArg);
        }
    }
    /*
     public ZExpression define(ZSymbol sym){
         this.define(sym, ZEmpty.NULL);
         return null;
     }
     */

    public ZExpression define(ZExpression sym, ZExpression value) throws ZException {
        if(!sym.isSymbol()) throw new ZException(ZError.SYMBOL_EXPECTED, sym);
        m_bindings.put(sym, value);
        return ZEmpty.VOID ;
    }

    public boolean bind(ZExpression sym, ZExpression value) throws ZException {
        if(!sym.isSymbol()) throw new ZException(ZError.SYMBOL_EXPECTED, sym);
        ZEnvironment cur = this;
        while(cur != null){
            ZExpression z = (ZExpression)cur.m_bindings.get(sym);
            if(z != null){
                cur.m_bindings.put(sym, value);
                return true;
            }
            cur = cur.m_prev;
        }
        return false;
    }

    public ZExpression lookup(ZSymbol sym) throws ZException {
        if(!sym.isSymbol()) throw new ZException(ZError.SYMBOL_EXPECTED, sym);
        ZEnvironment cur = this;
        while(cur != null){
            ZExpression z = (ZExpression)cur.m_bindings.get(sym);
            if(z != null) return z;
            cur = cur.m_prev;
        }
        return ZEmpty.VOID ;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(16384);
        for(Iterator i = m_bindings.keySet().iterator(); i.hasNext();){
            ZSymbol sym = (ZSymbol)i.next();
            try{
                sb.append("  " + sym.write() + " -> " + this.lookup(sym).write() + "\n");
            }catch(ZException ex){
            }
        }
        return sb.toString();
    }

}
