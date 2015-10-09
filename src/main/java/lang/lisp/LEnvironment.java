package lang.lisp;

import java.util.HashMap;
import java.util.Iterator;

public class LEnvironment{

    private class LBinding{
        private LExpression value;	// getLicenseTypeString value
        private LExpression function;	// getLicenseTypeString function description (lambda(arg1 arg2 ...)(function)
        private HashMap props;	// an getLicenseTypeString-list ((prop1 val1)(prop2 val2)...)

        public LBinding(){
            this(null, null);
        }

        public LBinding(LExpression v, LExpression f){
            this.setValue(v);
            this.setFunction(f);
            this.props = new HashMap();
        }

        public void setValue(LExpression l){
            this.value = l;
        }

        public LExpression getValue(){
            return this.value;
        }

        public void setFunction(LExpression l){
            this.function = l;
        }

        public LExpression getFunction(){
            return this.function;
        }

        public void addProperty(LSymbol prop, LExpression val){
            this.props.put(prop, val);
        }

        public LExpression getProperty(LSymbol prop){
            return (LExpression)this.props.get(prop);
        }

        public Iterator properties(){
            return this.props.keySet().iterator();
        }

        public int getPropertiesSize(){
            return this.props.size();
        }

    }

    private class LFence{
        private HashMap bindings;
        private LFence parent;
        private LFence next;

        public LFence(LFence next, LFence parent){
            this.bindings = new HashMap();
            this.next = next;
            this.parent = parent;
        }

        public void bind(LSymbol sym, LBinding bind){
            this.bindings.put(sym, bind);
        }

        public LBinding unbind(LSymbol sym){
            return (LBinding)this.bindings.get(sym);
        }

        public Iterator iterator(){
            return this.bindings.keySet().iterator();
        }

        public LFence getNext(){
            return this.next;
        }

        public LFence getParent(){
            return this.next;
        }
    }

    private LFence lexical;
    private LFence global;
    private String user;
    private int level;

    private HashMap symbols;

    public LEnvironment(String user, LInterpreter lint){
        this.reset();
        this.global = new LFence(null, null);
        this.user = user;
        this.symbols = new HashMap();
        this.symbols.put(LSymbol.NIL.getName(), LSymbol.NIL);
        this.symbols.put(LSymbol.T.getName(), LSymbol.T);
        this.symbols.put(LSymbol.LAMBDA.getName(), LSymbol.LAMBDA);
        this.symbols.put(LSymbol.OTHERWISE.getName(), LSymbol.OTHERWISE);
        this.addPackage(lint);
    }

    public LSymbol recSymbol(String s){
        LSymbol sym = (LSymbol)this.symbols.get(s );
        if(sym == null){
            sym = new LSymbol(s );
            this.symbols.put(s , sym);
        }
        return sym;
    }

    public void addPackage(LPackage l){
        for(int i = 0; i < l.getSize(); i++){
            LPackage f = l.clone(i + 1);
            this.bindFunctionGlobal(this.recSymbol(f.getInfo()), f);
        }
    }

    public void pushu(){
        this.lexical = new LFence(this.lexical, this.lexical);
        this.level++;
    }

    public void pushl(){
        LFence parent = (this.lexical == null)  ? null : this.lexical.getParent();
        this.lexical = new LFence(this.lexical, parent);
    }

    public void popu(){
        if(this.lexical != null){
            this.lexical = this.lexical.getNext();
            this.level--;
        }
    }

    public void popl(){
        if(this.lexical != null){
            this.lexical = this.lexical.getNext();
        }
    }

    public void reset(){
        this.lexical = null;
        this.level = 0;
    }

    public String getPrompt(){
        return this.user + ((this.level == 0) ? "*" : (this.level + ">"));
    }

    public LBinding findBinding(LSymbol sym){
        LFence cur = this.lexical;
        LBinding b = null;
        while(cur != null && b == null){
            b = cur.unbind(sym);
            cur = cur.getParent();
        }
        if(b == null) b = this.global.unbind(sym);
        return b;
    }

    public void bindValueGlobal(LSymbol sym, LExpression l){
        LBinding b = this.global.unbind(sym);
        if(b == null){
            b = new LBinding();
            this.global.bind(sym, b);
        }
        b.setValue(l);
    }

    public void bindValueLexical(LSymbol sym, LExpression l){
        LBinding b = this.lexical.unbind(sym);
        if(b == null){
            b = new LBinding();
            this.lexical.bind(sym, b);
        }
        b.setValue(l);
    }

    public void bindValue(LSymbol sym, LExpression l){
        LBinding b = this.findBinding(sym);
        if(b == null){
            b = new LBinding();
            this.global.bind(sym, b);
        }
        b.setValue(l);
    }

    public void bindFunctionGlobal(LSymbol sym, LExpression f){
        LBinding b = this.global.unbind(sym);
        if(b == null){
            b = new LBinding();
            this.global.bind(sym, b);
        }
        b.setFunction(f);
    }

    public void bindFunctionLexical(LSymbol sym, LExpression f){
        LBinding b = this.lexical.unbind(sym);
        if(b == null){
            b = new LBinding();
            this.lexical.bind(sym, b);
        }
        b.setFunction(f);
    }

    public void bindProperty(LSymbol sym, LSymbol prop, LExpression l){
        LBinding b = this.findBinding(sym);
        if(b == null){
            b = new LBinding();
            this.global.bind(sym, b);
        }
        b.addProperty(prop, l);
    }

    public LExpression findValue(LSymbol sym) throws LException{
        LBinding b = this.findBinding(sym);
        if(b == null) throw new LException(LError.UNBOUND_VARIABLE, sym);
        if(b.getValue() == null) throw new LException(LError.UNBOUND_VARIABLE, sym);
        return b.getValue();
    }

    public LExpression findFunction(LSymbol sym) throws LException{
        LBinding b = this.findBinding(sym);
        if(b == null) throw new LException(LError.UNDEFINED_FUNCTION, sym);
        if(b.getFunction() == null) throw new LException(LError.UNDEFINED_FUNCTION, sym);
        return b.getFunction();
    }

    //* must be rewritten
    public LExpression findProperty(LSymbol sym, LSymbol prop){
        LFence cur = this.lexical;
        LExpression retVal = LSymbol.NIL;
        while(cur != null){
            LBinding b = cur.unbind(sym);
            if(b != null){
                if(b.getPropertiesSize() != 0){
                    retVal = b.getProperty(prop);
                    return (retVal == null) ? LSymbol.NIL : retVal;
                }
            }
            cur = cur.getParent();
        }
        return LSymbol.NIL;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(1024);
        LFence cur = null;
        if(this.lexical == null){
            sb.append("Global");
            cur = this.global;
        }else{
            sb.append("Lexical level=" + this.level);
            cur = this.lexical;
        }
        for(Iterator i = cur.iterator(); i.hasNext();){
            LSymbol sym = (LSymbol)i.next();
            LBinding b = cur.unbind(sym);
            LExpression f = b.getFunction();
            sb.append("\n  (" + sym.toString());
            sb.append("(val=" + b.getValue() + ")");
            sb.append("(fct=" + b.getFunction() + ")");
            sb.append("(props=(");
            for(Iterator j = b.properties(); j.hasNext();){
                LSymbol prop = (LSymbol)j.next();
                LExpression val = b.getProperty(prop);
                sb.append("(" + prop + " " + val + ")");
            }
            sb.append("))");
        }
        return sb.toString();
    }

}
