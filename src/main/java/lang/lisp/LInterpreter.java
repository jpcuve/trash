package lang.lisp;

public class LInterpreter extends LPackage {
	
	private class LReturn extends LException {
		public LReturn(LExpression l){
			super(LError.UNEXPECTED_RESERVED, l);
		}
	}

	private static String AND = "and";
	private static String APPEND = "append";
	private static String APPLY = "apply";
	private static String ASSOC = "assoc";
	private static String ATOM = "atom";
	private static String CASE = "case";
	private static String BUTLAST = "butlast";
	private static String COND = "cond";
	private static String CONS = "cons";
	private static String COUNTIF = "count-if";
	private static String DEFUN = "defun";
	private static String DIVIDE = "/";
	private static String DOLIST = "dolist";
	private static String DOTIMES = "dotimes";
	private static String ENDP = "endp";
	private static String EQ = "eq";
	private static String EQL = "eql";
	private static String EQUAL ="equal";
	private static String EQUALS = "=";
	private static String EVAL = "eval";
	private static String EVENP = "evenp";
	private static String FINDIF = "find-if";
	private static String FIRST = "first";
	private static String FLOAT = "float";
	private static String FUNCALL = "funcall";
	private static String FUNCTION = "function";
	private static String GET = "get";
	private static String IF = "if";
	private static String LAST = "last";
	private static String LABELS = "labels";
	private static String LAMBDA = "lambda";
	private static String LENGTH = "length";
	private static String LET = "let";
	private static String LETSTAR = "let*";
	private static String LESS = "<";
	private static String LIST = "list";
	private static String LISTP = "listp";
	private static String LOOP = "loop";
	private static String MAPCAR = "mapcar";
	private static String MAX = "max";
	private static String MEMBER = "member";
	private static String MIN = "min";
	private static String MINUS = "-";
	private static String MINUSP = "minusp";
	private static String MORE = ">";
	private static String NIL = "nil";
	private static String NOT = "not";
	private static String NTH = "nth";
	private static String NTHCDR = "nthcdr";
	private static String NULL = "null";
	private static String NUMBERP = "numberp";
	private static String ODDP = "oddp";
	private static String OR = "or";
	private static String OTHERWISE = "otherwise";
	private static String PLUS = "+";
	private static String PLUSP = "plusp";
	private static String PROG1 = "prog1";
	private static String PROGN = "progn";
	private static String QUOTE = "quote";
	private static String RANDOM = "random";
	private static String REM = "rem";
	private static String REMOVEIF = "remove-if";
	private static String REMOVEIFNOT = "remove-if-not";
	private static String REST = "rest";
	private static String RETURN = "return";
	private static String REVERSE = "reverse";
	private static String ROUND = "round";
	private static String SETF = "setf";
	private static String SETQ = "setq";
	private static String SYMBOLP = "symbolp";
	private static String T = "t";
	private static String TIMES = "*";
	private static String TRACE = "trace";
	private static String UNLESS = "unless";
	private static String UNTRACE = "untrace";
	private static String WHEN = "when";
	private static String ZEROP = "zerop";
	
		
	public String getInfo(){
		switch(this.fID){
		case 1: return APPLY;
		case 2: return CASE;
		case 3: return COND;
		case 4: return DEFUN;
		case 5: return FUNCTION;
		case 6: return GET;
		case 7: return IF;
		case 8: return LET;
		case 9: return LETSTAR;
		case 10: return MAPCAR;
		case 11: return QUOTE;
		case 12: return SETF;
		case 13: return SETQ;
		case 14: return UNLESS;
		case 15: return WHEN;
		case 16: return EVAL;
		case 17: return FUNCALL;
		case 18: return TRACE;
		case 19: return UNTRACE;
		case 20: return LABELS;
		case 21: return COUNTIF;
		case 22: return FINDIF;
		case 23: return REMOVEIF;
		case 24: return REMOVEIFNOT;
		case 25: return DOTIMES;
		case 26: return DOLIST;
		case 27: return PROG1;
		case 28: return PROGN;
		case 29: return RETURN;
		case 30: return LOOP;
		case 31: return APPEND;
		case 32: return ASSOC;
		case 33: return ATOM;
		case 34: return BUTLAST;
		case 35: return CONS;
		case 36: return DIVIDE;
		case 37: return ENDP;
		case 38: return EQ;
		case 39: return EQL;
		case 40: return EQUAL;
		case 41: return FIRST;
		case 42: return FLOAT;
		case 43: return LAST;
		case 44: return LENGTH;
		case 45: return LIST;
		case 46: return MAX;
		case 47: return MIN;
		case 48: return MINUS;
		case 49: return NTHCDR;
		case 50: return NULL;
		case 51: return PLUS;
		case 52: return REST;
		case 53: return REVERSE;
		case 54: return ROUND;
		case 55: return TIMES;
		case 56: return NTH;
		case 57: return MEMBER;
		case 58: return NUMBERP;
		case 59: return ZEROP;
		case 60: return PLUSP;
		case 61: return MINUSP;
		case 62: return EVENP;
		case 63: return ODDP;
		case 64: return SYMBOLP;
		case 65: return LISTP;
		case 66: return MORE;
		case 67: return LESS;
		case 68: return AND;
		case 69: return OR;
		case 70: return NOT;
		case 71: return REM;
		case 72: return RANDOM;
		case 73: return EQUALS;
		}
		return "microLisp interpreter 1.2";
	}
	
	public boolean getSpecial(){
		switch(this.fID){
		case 2: return true;
		case 3: return true;
		case 4: return true;
		case 5: return true;
		case 6: return true;
		case 7: return true;
		case 8: return true;
		case 9: return true;
		case 11: return true;
		case 12: return true;
		case 13: return true;
		case 14: return true;
		case 15: return true;
		case 16: return true;
		case 18: return true;
		case 19: return true;
		case 20: return true;
		case 25: return true;
		case 26: return true;
		case 27: return true;
		case 28: return true;
		case 30: return true;
		}
		return false;
	}
	
	public LExpression f(LExpression arg, LEnvironment env) throws LException, LReturn {
		switch(this.fID){
		case 1: return this.apply_(arg, env);
		case 2: return this.case_(arg, env);
		case 3: return this.cond_(arg, env);
		case 4: return this.defun_(arg, env);
		case 5: return this.function_(arg, env);
		case 6: return this.get_(arg, env);
		case 7: return this.if_(arg, env);
		case 8: return this.let_(arg, env);
		case 9: return this.letstar_(arg, env);
		case 10: return this.mapcar_(arg, env);
		case 11: return this.quote_(arg, env);
		case 12: return this.setf_(arg, env);
		case 13: return this.setq_(arg, env);
		case 14: return this.unless_(arg, env);
		case 15: return this.when_(arg, env);
		case 16: return this.eval_(arg, env);
		case 17: return this.funcall_(arg, env);
		case 18: return this.trace_(arg, env);
		case 19: return this.untrace_(arg, env);
		case 20: return this.labels_(arg, env);
		case 21: return this.countif_(arg, env);
		case 22: return this.findif_(arg, env);
		case 23: return this.removeif_(arg, env);
		case 24: return this.removeifnot_(arg, env);
		case 25: return this.dotimes_(arg, env);
		case 26: return this.dolist_(arg, env);
		case 27: return this.prog1_(arg, env);
		case 28: return this.progn_(arg, env);
		case 29: return this.return_(arg, env);
		case 30: return this.loop_(arg, env);
		case 31: return this.append_(arg);
		case 32: return this.assoc_(arg);
		case 33: return this.atom_(arg);
		case 34: return this.butlast_(arg);
		case 35: return this.cons_(arg);
		case 36: return this.divide_(arg);
		case 37: return this.endp_(arg);
		case 38: return this.eq_(arg);
		case 39: return this.eql_(arg);
		case 40: return this.equal_(arg);
		case 41: return this.first_(arg);
		case 42: return this.float_(arg);
		case 43: return this.last_(arg);
		case 44: return this.length_(arg);
		case 45: return this.list_(arg);
		case 46: return this.max_(arg);
		case 47: return this.min_(arg);
		case 48: return this.minus_(arg);
		case 49: return this.nthcdr(arg);
		case 50: return this.null_(arg);
		case 51: return this.plus_(arg);
		case 52: return this.rest_(arg);
		case 53: return this.reverse_(arg);
		case 54: return this.round_(arg);
		case 55: return this.times_(arg);
		case 56: return this.nth_(arg);
		case 57: return this.member_(arg);
		case 58: return this.numberp_(arg);
		case 59: return this.zerop_(arg);
		case 60: return this.plusp_(arg);
		case 61: return this.minusp_(arg);
		case 62: return this.evenp_(arg);
		case 63: return this.oddp_(arg);
		case 64: return this.symbolp_(arg);
		case 65: return this.listp_(arg);
		case 66: return this.more_(arg);
		case 67: return this.less_(arg);
		case 68: return this.and_(arg);
		case 69: return this.or_(arg);
		case 70: return this.not_(arg);
		case 71: return this.rem_(arg);
		case 72: return this.random_(arg);
		case 73: return this.equals_(arg);
		}
		throw new LException(LError.PACKAGED_FUNCTION_NOT_FOUND, this);
	}
	
	public LExpression eval(LExpression form, LEnvironment env) throws LException {
		// System.out.println("Entering eval, arg=" + form);
		if(form.atomp()){
			if(form.numberp() ||  form == LSymbol.T || form == LSymbol.NIL) return form;
			if(form.symbolp()) return env.findValue((LSymbol)form);
		}
		LList fform = (LList)form;
		LExpression form1 = fform.first();
		if(!form1.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, form1);
		LSymbol op = (LSymbol)form1;
		LExpression f = env.findFunction(op);
		if(f.functionp()){
			if(!f.listp()){
				if(((LPackage)f).getSpecial()) return this.apply(f, fform.rest(), env); // do not evaluate all arguments if special form
			}
		}
		LExpression argl = this.evalArgl(fform.rest(), env);
		return this.apply(op, argl, env);
	}
	
	private LExpression apply(LExpression proc, LExpression args, LEnvironment env) throws LException{
		// System.out.println("Entering apply: proc=" + proc + ", arg=" + arg);
		LExpression retVal = null;
		if(proc.symbolp()){
			LSymbol sym = (LSymbol)proc;
			LExpression f = env.findFunction(sym);
			if(sym.isTrace()) System.out.println("Entering: " + sym + ", argument list: " + args);
			retVal =  this.apply(f, args, env);
			if(sym.isTrace()) System.out.println("Exiting: " + sym + ", value: " + retVal);
		}
		if(proc.functionp()){
			if(proc.listp()){
				LList lambda = (LList)proc;
				env.pushl();
				this.bindValues(this.bindVars(lambda.second(), args, env), env);
				retVal = this.progn(lambda.nthcdr(2), env);
				env.popl();
			}else{
				retVal = ((LPackage)proc).f(args, env);
			}
		}
		if(retVal == null) throw new LException(LError.FUNCTION_ARG_EXPECTED, proc);
		// System.out.println("Exiting apply: proc=" + proc +", ret=" + retVal);
		return retVal;
	}
	
	// interpreter support
	
	private LExpression assoc(LExpression key, LExpression l) throws LException{
		if(l.atomp()) return LSymbol.NIL;
		LList ll = (LList)l;
		LExpression ll1 = ll.first();
		if(ll1.atomp()) return LSymbol.NIL;
		LList ll1l = (LList)ll1;
		if(key.equal(ll1l.first())) return ll1l;
		return assoc(key, ll.rest());
	}
	
	private LExpression get(LExpression sym, LExpression attr, LEnvironment env) throws LException{
		if(!sym.symbolp() || !attr.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, sym);
		return env.findProperty((LSymbol)sym, (LSymbol)attr);
	}
	
	public void bindValues(LExpression alist, LEnvironment env) throws LException{
		// System.out.println("Entering bindValuesSerial=" + alist);
		if(alist != LSymbol.NIL && alist.listp()){
			LExpression cur = alist;
			while(cur != LSymbol.NIL){
				LList curl = (LList)cur;
				LList pair = (LList)((curl).first());
				LSymbol sym = (LSymbol)pair.first();
				LExpression val = pair.second();
				env.bindValueLexical(sym, val);
				cur = curl.rest();
			}
		}
		// System.out.println("Environment updated=" + env);
	}
	
	public void bindFunctions(LExpression alist, LEnvironment env) throws LException{
		// System.out.println("Entering bindValuesSerial=" + alist);
		if(alist != LSymbol.NIL && alist.listp()){
			LExpression cur = alist;
			while(cur != LSymbol.NIL){
				LList curl = (LList)cur;
				LList pair = (LList)((curl).first());
				LSymbol sym = (LSymbol)pair.first();
				LExpression lambda = pair.rest();
				env.bindFunctionLexical(sym, lambda);
				cur = curl.rest();
			}
		}
		// System.out.println("Environment updated=" + env);
	}
	
	private LExpression bindVars(LExpression vars, LExpression vals, LEnvironment env){
		LExpression retVal = LSymbol.NIL;
		while(!vars.atomp() && !vals.atomp()){
			LList varsl = (LList)vars;
			LList valsl = (LList)vals;
			LExpression var1 = varsl.first();
			LExpression val1 = valsl.first();
			retVal = var1.cons(val1.cons(LSymbol.NIL)).cons(retVal);
			vars = varsl.rest();
			vals = valsl.rest();
		}
		return retVal;
	}
	
	public LExpression evalArgl(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering evalArgl: arg=" + arg);
		if(arg.atomp()) return LSymbol.NIL;
		LList argl = (LList)arg;
		LList retVal = this.eval(argl.first(), env).cons(LSymbol.NIL);
		LExpression cur = argl.rest();
		while(cur.listp()){
			LList curl = (LList)cur;
			retVal.append(this.eval(curl.first(), env));
			cur = curl.rest();
		}
		// System.out.println("Exiting evalArgl: ret=" + retVal);
		return retVal;
	}
	
	public LExpression progn(LExpression arg, LEnvironment env) throws LException{
		LExpression retVal = LSymbol.NIL;
		while(!arg.atomp()){
			LList argl = (LList)arg;
			retVal = this.eval(argl.first(), env);
			arg = argl.rest();
		}
		if(arg != LSymbol.NIL) retVal = this.eval(arg, env);
		return retVal;
	}
	
	public LExpression prog1(LExpression arg, LEnvironment env) throws LException{
		LExpression retVal = LSymbol.NIL;
		if(!arg.atomp()){
			LList argl = (LList)arg;
			retVal = this.eval(argl.first(), env);
			arg = argl.rest();
		}
		while(!arg.atomp()){
			LList argl = (LList)arg;
			this.eval(argl.first(), env);
			arg = argl.rest();
		}
		if(arg != LSymbol.NIL) this.eval(arg, env);
		return retVal;
	}
	
	/**apply
	 */
	private LExpression apply_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(APPLY, arg, 2);
		LList argl = (LList)arg;
		LExpression op = argl.first();
		LExpression args = argl.second();
		return this.apply(op, args, env);
	}
	
	/**case
	 */
	private LExpression case_(LExpression arg, LEnvironment env) throws LException{
		if(arg.atomp()) return LSymbol.NIL;
		LList argl = (LList)arg;
		LExpression key = this.eval(argl.first(), env);
		LExpression cur = argl.rest();
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression cur1 = curl.first();
			if(!cur1.listp()) throw new LException(LError.LIST_ARG_EXPECTED, COND);
			LList cur1l = (LList)cur1;
			LExpression k = cur1l.first();
			if(k.eql(key)) return this.progn(cur1l.rest(), env);
			cur = curl.rest();
			if(cur.atomp() && (k.eq(LSymbol.T) || k.eq(env.recSymbol(OTHERWISE)))) return this.progn(cur1l.rest(), env);
		}
		return LSymbol.NIL;
	}
	
	/**cond
	 */
	private LExpression cond_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering cond_, arg=" + arg);
		LExpression cur = arg;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression cur1 = curl.first();
			if(!cur1.listp()) throw new LException(LError.LIST_ARG_EXPECTED, COND);
			LList cur1l = (LList)cur1;
			if(!this.eval(cur1l.first(), env).eq(LSymbol.NIL)) return this.progn(cur1l.rest(), env);
			cur = curl.rest();
		}
		return LSymbol.NIL;
	}
	
	/**count-if
	 */
	public LExpression countif_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(COUNTIF, arg, 2);
		LList argl = (LList)arg;
		LExpression f = argl.first();
		if(!f.functionp()) throw new LException(LError.FUNCTION_ARG_EXPECTED, f);
		LExpression cur = argl.second();
		if(!cur.listp()) throw new LException(LError.LIST_ARG_EXPECTED, cur);
		long count = this.countif_1(f, cur, env);
		return new LInteger(Long.toString(count));
	}
	
	public long countif_1(LExpression f, LExpression cur, LEnvironment env) throws LException {
		if(cur.atomp()) return 0;
		LList curl = (LList)cur;
		LExpression cur1 = curl.first();
		LExpression retVal = this.apply(f, cur1.cons(LSymbol.NIL), env);
		if(!retVal.eq(LSymbol.NIL)) return this.countif_1(f, curl.rest(), env) + 1;
		return this.countif_1(f, curl.rest(), env);
	}
		
	/**defun
	 */
	public LExpression defun_(LExpression arg, LEnvironment env) throws LException{
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, DEFUN );
		LList argl = (LList)arg;
		LExpression fName = argl.first();
		if(!fName.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, fName);
		LSymbol sym = (LSymbol)fName;
		env.bindFunctionGlobal(sym, env.recSymbol(LAMBDA).cons(argl.rest()));
		return sym;						  
	}
	
	/**dolist
	 */
	public LExpression dolist_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberMoreOrEqual(DOLIST, arg, 1);
		LList argl = (LList)arg;
		LExpression bd = argl.first();
		if(bd.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, bd);
		LList bdl = (LList)bd;
		LExpression var = bdl.first();
		if(!var.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, var);
		LSymbol sym = (LSymbol)var;
		LExpression cur = this.eval(bdl.second(), env);
		LExpression retVal = null;
		env.pushu();
		try{
			while(cur.listp()){
				LList curl = (LList)cur;
				env.bindValueLexical(sym, curl.first());
				this.progn(argl.rest(), env);
				cur = curl.rest();
			}
			retVal = this.eval(bdl.third(), env);
		}catch(LReturn retEx){
			retVal = retEx.getValue();
		}finally{
			env.popu();
			return retVal;
		}
	}
	
	/**dotimes
	 */
	public LExpression dotimes_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberMoreOrEqual(DOTIMES, arg, 1);
		LList argl = (LList)arg;
		LExpression bd = argl.first();
		if(bd.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, bd);
		LList bdl = (LList)bd;
		LExpression var = bdl.first();
		if(!var.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, var);
		LSymbol sym = (LSymbol)var;
		LExpression up = this.eval(bdl.second(), env);
		if(!up.numberp()) throw new LException(LError.INTEGER_ARG_EXPECTED, up);
		LNumber upbound = (LNumber)up;
		if(!upbound.integerp()) throw new LException(LError.INTEGER_ARG_EXPECTED, up);
		LNumber count = LNumber.zero();
		LExpression retVal = null;
		env.pushu();
		try{
			while(count.compareTo(upbound) == -1){
				env.bindValueLexical(sym, count);
				this.progn(argl.rest(), env);
				count = count.add(LNumber.one());
			}
			retVal = this.eval(bdl.third(), env);
		}catch(LReturn retEx){
			retVal = retEx.getValue();
		}finally{
			env.popu();
			return retVal;
		}
	}
	
	/**eval
	 */
	public LExpression eval_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(EVAL, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		return this.eval(this.eval(arg1, env), env);
	}
	
	/**find-if
	 */
	public LExpression findif_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(FINDIF, arg, 2);
		LList argl = (LList)arg;
		LExpression f = argl.first();
		if(!f.functionp()) throw new LException(LError.FUNCTION_ARG_EXPECTED, f);
		LExpression cur = argl.second();
		if(!cur.listp()) throw new LException(LError.LIST_ARG_EXPECTED, cur);
		return this.findif_1(f, cur, env);
	}
	
	public LExpression findif_1(LExpression f, LExpression cur, LEnvironment env) throws LException {
		if(cur.atomp()) return LSymbol.NIL;
		LList curl = (LList)cur;
		LExpression cur1 = curl.first();
		LExpression retVal = this.apply(f, cur1.cons(LSymbol.NIL), env);
		if(!retVal.eq(LSymbol.NIL)) return cur1;
		return this.findif_1(f, curl.rest(), env);
	}
		
	/**funcall
	 */
	public LExpression funcall_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberMoreOrEqual(FUNCALL, arg, 1);
		LList argl = (LList)arg;
		// System.out.println("Entering funcall_, arg=" + arg);
		return this.apply(argl.first(), argl.rest(), env);
	}
	
	/**function
	  */
	public LExpression function_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering function_, arg=" + arg);
		this.isArgNumberEqual(FUNCTION, arg, 1);
		LExpression retVal = null;
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1.atomp()){
			if(!arg1.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, FUNCTION );
			LSymbol sym = (LSymbol)arg1;
			retVal = env.findFunction(sym);
		}else{
			LList arg1l = (LList)arg1;
			if(!arg1l.first().eq(env.recSymbol(LAMBDA))) throw new LException(LError.FUNCTION_ARG_EXPECTED, arg1);
			retVal = arg1l;
		}
		return retVal;
	}
	
	/**get
	 */
	private LExpression get_(LExpression arg, LEnvironment env) throws LException{
		LList argl = (LList)arg;
		return this.get(this.eval(argl.first(), env), this.eval(argl.second(), env), env);
	}
	
	/**if
	 */
	private LExpression if_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering if_, arg=" + arg);
		LList argl = (LList)arg;
		return (!this.eval(argl.first(), env).eq(LSymbol.NIL)) ? this.eval(argl.second(), env) : this.eval(argl.third(), env);
	}
	
	/**labels
	 */
	public LExpression labels_(LExpression arg, LEnvironment env) throws LException{
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, LABELS );
		LList argl = (LList)arg;
		env.pushu();
		LExpression init = argl.first();
		if(init.listp()){
			LExpression alist = LSymbol.NIL;
			LExpression cur = init;
			while(!cur.atomp()){
				LList curl = (LList)cur;
				LExpression def = curl.first();
				if(def.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, def);
				LList defl = (LList)def;
				System.out.println(defl);
				LExpression fName = defl.first();
				if(!fName.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, fName);
				LSymbol sym = (LSymbol)fName;
				LExpression lambda = env.recSymbol(LAMBDA).cons(defl.rest());
				System.out.println(lambda);
				alist = sym.cons(lambda).cons(alist);
				cur = curl.rest();
			}
			this.bindFunctions(alist, env);
		}
		LExpression retVal = this.progn(argl.rest(), env);
		env.popu();
		return retVal;
	}
	
	/**let
	 */
	public LExpression let_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering let: arg=" + arg);
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, LET );
		LList argl = (LList)arg;
		env.pushu();
		LExpression init = argl.first();
		if(init.listp()){
			LExpression alist = LSymbol.NIL;
			LExpression cur = init;
			while(!cur.atomp()){
				LList curl = (LList)cur;
				LExpression pair = curl.first();
				LExpression sym = null;
				LExpression val = null;
				if(pair.atomp()){
					sym = pair;
					val = LSymbol.NIL;
				}else{
					LList pairl = (LList)pair;
					sym = pairl.first();
					val = this.eval(pairl.second(), env);
				}
				if(!sym.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, sym);
				alist = sym.cons(val).cons(alist);
				cur = curl.rest();
			}
			this.bindValues(alist, env);
		}
		LExpression retVal = this.progn(argl.rest(), env);
		env.popu();
		return retVal;
	}
	
	/**letstar
	 */
	public LExpression letstar_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering let: arg=" + arg);
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, LETSTAR );
		LList argl = (LList)arg;
		env.pushu();
		LExpression init = argl.first();
		if(init.listp()){
			LExpression cur = init;
			while(!cur.atomp()){
				LList curl = (LList)cur;
				LExpression pair = curl.first();
				LExpression sym = null;
				LExpression val = null;
				if(pair.atomp()){
					sym = pair;
					val = LSymbol.NIL;
				}else{
					LList pairl = (LList)pair;
					sym = pairl.first();
					val = this.eval(pairl.second(), env);
				}
				if(!sym.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, sym);
				env.bindValueLexical((LSymbol)sym, val);
				cur = curl.rest();
			}
		}
		LExpression retVal = this.progn(argl.rest(), env);
		env.popu();
		return retVal;
	}
	
	/**loop
	 */
	private LExpression loop_(LExpression arg, LEnvironment env) throws LException{
		LExpression retVal = LSymbol.NIL;
		try{
			while(true){
				this.progn(arg, env);
			}
		}catch(LReturn retEx){
			retVal = retEx.getValue();
		}finally{
			return retVal;
		}
	}
	
	/**mapcar
	 */	
	private LExpression mapcar_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(MAPCAR, arg, 2);
		LList argl = (LList)arg;
		LExpression f = argl.first();
		if(!f.functionp()) throw new LException(LError.FUNCTION_ARG_EXPECTED, f);
		LExpression cur = argl.second();
		if(!cur.listp()) throw new LException(LError.LIST_ARG_EXPECTED, cur);
		return this.mapcar_1(f, cur, env);
	}
	
	private LExpression mapcar_1(LExpression f, LExpression cur, LEnvironment env) throws LException{
		if(cur.atomp()) return LSymbol.NIL;
		LList curl = (LList)cur;
		LExpression cur1 = curl.first();
		LExpression retVal = this.apply(f, cur1.cons(LSymbol.NIL), env);
		return retVal.cons(this.mapcar_1(f, curl.rest(), env));
	}
	
	/**prog1
	 */
	private LExpression prog1_(LExpression arg, LEnvironment env) throws LException{
		return this.prog1(arg, env);
	}
	
	/**progn
	 */
	private LExpression progn_(LExpression arg, LEnvironment env) throws LException{
		return this.progn(arg, env);
	}
	
	/**quote
	 */
	private LExpression quote_(LExpression arg, LEnvironment env) throws LException{
		LList argl = (LList)arg;
		return argl.first();
	}
	
	/**remove-if
	 */
	public LExpression removeif_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(REMOVEIF, arg, 2);
		LList argl = (LList)arg;
		LExpression f = argl.first();
		if(!f.functionp()) throw new LException(LError.FUNCTION_ARG_EXPECTED, f);
		LExpression cur = argl.second();
		if(!cur.listp()) throw new LException(LError.LIST_ARG_EXPECTED, cur);
		return this.removeif_1(f, cur, env);
	}
	
	public LExpression removeif_1(LExpression f, LExpression cur, LEnvironment env) throws LException {
		if(cur.atomp()) return LSymbol.NIL;
		LList curl = (LList)cur;
		LExpression cur1 = curl.first();
		LExpression retVal = this.apply(f, cur1.cons(LSymbol.NIL), env);
		if(retVal.eq(LSymbol.NIL)) return cur1.cons(this.removeif_1(f, curl.rest(), env));
		return this.removeif_1(f, curl.rest(), env);
	}
		
	/**remove-if-not
	 */
	public LExpression removeifnot_(LExpression arg, LEnvironment env) throws LException{
		this.isArgNumberEqual(REMOVEIFNOT, arg, 2);
		LList argl = (LList)arg;
		LExpression f = argl.first();
		if(!f.functionp()) throw new LException(LError.FUNCTION_ARG_EXPECTED, f);
		LExpression cur = argl.second();
		if(!cur.listp()) throw new LException(LError.LIST_ARG_EXPECTED, cur);
		return this.removeifnot_1(f, cur, env);
	}
	
	public LExpression removeifnot_1(LExpression f, LExpression cur, LEnvironment env) throws LException {
		if(cur.atomp()) return LSymbol.NIL;
		LList curl = (LList)cur;
		LExpression cur1 = curl.first();
		LExpression retVal = this.apply(f, cur1.cons(LSymbol.NIL), env);
		if(!retVal.eq(LSymbol.NIL)) return cur1.cons(this.removeifnot_1(f, curl.rest(), env));
		return this.removeifnot_1(f, curl.rest(), env);
	}
	
	/**return
	 */
	public LExpression return_(LExpression arg, LEnvironment env) throws LException {
		this.isArgNumberEqual(RETURN, arg, 1);
		LList argl = (LList)arg;
		throw new LReturn(argl.first());
	}
			
	/**setf
	 */
	public LExpression setf_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering setf: arg=" + arg);
		LExpression cur = arg;
		LExpression val = LSymbol.NIL;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression key = curl.first();
			val = this.eval(curl.second(), env);
			if(key.symbolp()){
				env.bindValue((LSymbol)key, val);
			}else{
				LList keyl = (LList)key;
				if(keyl.first() != env.recSymbol(GET)) throw new LException(LError.SYMB_ARG_EXPECTED, keyl);
				LExpression symb = this.eval(keyl.second(), env);
				LExpression prop = this.eval(keyl.third(), env);
				env.bindProperty((LSymbol)symb, (LSymbol)prop, val);
			}
			cur = curl.rest();
			if(cur.atomp()) break;
			cur = ((LList)cur).rest();
			if(cur.atomp()) break;
		}
		// System.out.println("Environment updated=" + env);
		return val;
	}
	
	/**setq
	 */
	public LExpression setq_(LExpression arg, LEnvironment env) throws LException{
		// System.out.println("Entering setq: arg=" + arg);
		LExpression cur = arg;
		LExpression val = LSymbol.NIL;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression key = curl.first();
			if(!key.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, key);
			val = this.eval(curl.second(), env);
			env.bindValue((LSymbol)key, val);
			cur = curl.rest();
			if(cur.atomp()) break;
			cur = ((LList)cur).rest();
			if(cur.atomp()) break;
		}
		// System.out.println("Environment updated=" + env);
		return val;
	}
	
	/**trace
	 */
	public LExpression trace_(LExpression arg, LEnvironment env) throws LException{
		LExpression cur = arg;
		if(cur.atomp()) return LSymbol.NIL;
		while(cur.listp()){
			LList curl = (LList)cur;
			LExpression cur1 = curl.first();
			if(!cur1.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, cur1);
			LSymbol sym = (LSymbol)cur1;
			LExpression f = env.findFunction(sym);
			sym.setTrace();
			cur = curl.rest();
		}
		return LSymbol.T;
	}
	
	/**unless
	 */
	public LExpression unless_(LExpression arg, LEnvironment env) throws LException{
		if(arg.atomp()) return LSymbol.NIL;
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(this.eval(arg1, env) != LSymbol.NIL) return LSymbol.NIL;
		return this.progn(argl.rest(), env);
	}
	
	/**untrace
	 */
	public LExpression untrace_(LExpression arg, LEnvironment env) throws LException{
		LExpression cur = arg;
		LExpression retVal = LSymbol.NIL;
		while(cur.listp()){
			LList curl = (LList)cur;
			LExpression cur1 = curl.first();
			if(!cur1.symbolp()) throw new LException(LError.SYMB_ARG_EXPECTED, cur1);
			LSymbol sym = (LSymbol)cur1;
			LExpression f = env.findFunction(sym);
			sym.clearTrace();
			retVal = sym.cons(retVal);
			cur = curl.rest();
		}
		return retVal;
	}
		
	/**when
	 */
	public LExpression when_(LExpression arg, LEnvironment env) throws LException{
		if(arg.atomp()) return LSymbol.NIL;
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(this.eval(arg1, env) == LSymbol.NIL) return LSymbol.NIL;
		return this.progn(argl.rest(), env);
	}
	
	/**and
	 */
	public LExpression and_(LExpression arg) throws LException{
		if(arg.atomp()) return LSymbol.T;
		LExpression retVal = null;
		LExpression cur = arg;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			retVal = curl.first();
			if(retVal.eq(LSymbol.NIL)) return LSymbol.NIL;
			cur = curl.rest();
		}
		return retVal;
	}

	/**append
	 */
	public LExpression append_(LExpression arg) throws LException{
		if(arg== LSymbol.NIL) return LSymbol.NIL;
		LList argl = (LList)arg;
		return this.append_1(argl.first(), argl.rest());
	}
	
	private LExpression append_1(LExpression arg1l, LExpression argl) throws LException {
		if(argl.atomp()) return arg1l;
		if(arg1l.atomp() && arg1l != LSymbol.NIL) throw new LException(LError.APPEND_TO_ATOM, arg1l);
		LList argll = (LList)argl;
		if(arg1l== LSymbol.NIL) return this.append_1(argll.first(), argll.rest());
		LList arg1ll = (LList)arg1l;
		return arg1ll.first().cons(this.append_1(arg1ll.rest(), argl));
	}
	
	/**assoc
	 */
	public LExpression assoc_(LExpression arg) throws LException{
		this.isArgNumberEqual(ASSOC, arg, 2);
		LList argl = (LList)arg;
		LExpression key = argl.first();
		LExpression pairs = argl.second();
		while(!pairs.atomp()){
			LList pairsl = (LList)pairs;
			LExpression arg1 = pairsl.first();
			if(!arg1.atomp()){
				LList arg1l = (LList)arg1;
				if(arg1l.first().eq(key)) return arg1l;
			}
			pairs = pairsl.rest();
		}
		return LSymbol.NIL;
	}
	
	/**atom
	 */
	public LExpression atom_(LExpression arg) throws LException{
		this.isArgNumberEqual(ATOM, arg, 1);
		LList argl = (LList)arg;
		if(argl.first().atomp()) return LSymbol.T;
		else return LSymbol.NIL;
	}
	
	/**butlast
	 */
	public LExpression butlast_(LExpression arg) throws LException{
		this.isArgNumberBetween(BUTLAST, arg, 1, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1.atomp()) return LSymbol.NIL;
		LList arg1l = (LList)arg1;
		long n = arg1l.length();
		long i = 1;
		if(argl.length() == 2){
			LExpression arg2 = argl.second();
			if(!arg2.numberp()) return LSymbol.NIL;
			if(!((LInteger)arg2).integerp()) return LSymbol.NIL;
			i = ((LInteger)arg2).value.longValue();
		}
		if(i < 0 || i >= n) return LSymbol.NIL;
		return this.butLast_1(arg1l, n - i);
	}
	
	public LExpression butLast_1(LExpression l, long n){
		if(n == 0) return LSymbol.NIL;
		LList ll = (LList)l;
		return ll.first().cons(this.butLast_1(ll.rest(), n - 1));
	}
		
	/**cons
	 */
	public LExpression cons_(LExpression arg) throws LException{
		this.isArgNumberEqual(CONS, arg, 2);
		LList argl = (LList)arg;
		return argl.first().cons(argl.second());
	}
	
	
	/**divide
	 */
	public LExpression divide_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(DIVIDE, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression n = curl.first();
		if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
		cur = curl.rest();
		LNumber quot = (!cur.atomp()) ? (LNumber)n : LNumber.one().div((LNumber)n);
		while(!cur.atomp()){
			curl = (LList)cur;
			n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			quot = quot.div((LNumber)n);
			cur = curl.rest();
		}
		return quot;
	}
	
	/**endp
	 */
	public LExpression endp_(LExpression arg) throws LException{
		this.isArgNumberEqual(ENDP, arg, 1);
		LList argl = (LList)arg;
		LExpression l = argl.first();
		if(l == LSymbol.NIL) return LSymbol.T;
		if(l.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, l);
		return LSymbol.NIL;
	}
	
	/**eq
	 */
	public LExpression eq_(LExpression arg) throws LException {
		this.isArgNumberEqual(EQ, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression arg2 = argl.second();
		return (arg1.eq(arg2)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**eql
	 */
	public LExpression eql_(LExpression arg) throws LException {
		this.isArgNumberEqual(EQL, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression arg2 = argl.second();
		return (arg1.eql(arg2)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**equal
	 */
	public LExpression equal_(LExpression arg) throws LException {
		this.isArgNumberEqual(EQUAL, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression arg2 = argl.second();
		return (arg1.equal(arg2)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**equals (=)
	 */
	public LExpression equals_(LExpression arg) throws LException{
		this.isArgNumberEqual(EQUALS, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression arg2 = argl.second();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		if(!arg2.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg2);
		return (((LNumber)arg1).compareTo((LNumber)arg2) == 0) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**evenp
	 */
	public LExpression evenp_(LExpression arg) throws LException{
		this.isArgNumberEqual(EVENP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.INTEGER_ARG_EXPECTED, arg1);
		if(!((LNumber)arg1).integerp()) throw new LException(LError.INTEGER_ARG_EXPECTED, arg1);
		long l = ((LInteger)arg1).value.longValue();
		return (l == ((l / 2) * 2)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**first
	 */
	public LExpression first_(LExpression arg) throws LException{
		this.isArgNumberEqual(FIRST, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1 == LSymbol.NIL) return LSymbol.NIL;
		if(arg1.atomp()) throw new LException(LError.CAR_CDR_NON_LIST, arg1);
		return ((LList)arg1).first();
	}
	
	/**float
	 */
	public LExpression float_(LExpression arg) throws LException{
		this.isArgNumberEqual(FLOAT, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		return ((LNumber)arg1).floaT();
	}
	
	/**last
	 */
	public LExpression last_(LExpression arg) throws LException{
		this.isArgNumberEqual(LAST, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1.atomp()) return arg1;
		LList arg1l = (LList)arg1;
		while(!arg1l.rest().atomp()) arg1l = (LList)arg1l.rest();
		return arg1l;
	}
	
	/**length
	 */
	public LExpression length_(LExpression arg) throws LException{
		this.isArgNumberEqual(LENGTH, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1.eq(LSymbol.NIL)) return new LInteger("0");
		if(arg1.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, LENGTH);
		return new LInteger(Long.toString(((LList)arg1).length()));
	}
	
	/**less
	 */
	public LExpression less_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(LESS, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression n1 = curl.first();
		if(!n1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n1);
		cur = curl.rest();
		while(!cur.atomp()){
			curl = (LList)cur;
			LExpression n2 = curl.first();
			if(!n2.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n2);
			if(((LNumber)n1).compareTo((LNumber)n2) > -1) return LSymbol.NIL;
			n1 = n2;
			cur = curl.rest();
		}
		return LSymbol.T;
	}

	/**list
	 */
	public LExpression list_(LExpression arg) throws LException{
		return arg;
	}
	
	/**listp
	 */
	public LExpression listp_(LExpression arg) throws LException{
		this.isArgNumberEqual(LISTP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		return (arg1.listp() || arg1.eq(LSymbol.NIL)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**max
	 */
	public LExpression max_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(MAX, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression retVal = curl.first();
		if(!retVal.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, retVal);
		cur = curl.rest();
		while(!cur.atomp()){
			curl = (LList)cur;
			LExpression n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			retVal = ((LNumber)retVal).max((LNumber)n);
			cur = curl.rest();
		}
		return retVal;
	}
	
	/**member
	 */
	public LExpression member_(LExpression arg) throws LException{
		this.isArgNumberEqual(MEMBER, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression arg2 = argl.second();
		while(!arg2.atomp()){
			LList arg2l = (LList)arg2;
			if(arg2l.first().eql(arg1)) return arg2l;
			arg2 = arg2l.rest();
		}
		return LSymbol.NIL;
	}
	
	/**min
	 */
	public LExpression min_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(MIN, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression retVal = curl.first();
		if(!retVal.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, retVal);
		cur = curl.rest();
		while(!cur.atomp()){
			curl = (LList)cur;
			LExpression n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			retVal = ((LNumber)retVal).min((LNumber)n);
			cur = curl.rest();
		}
		return retVal;
	}
	
		
	/**minus
	 */
	public LExpression minus_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(MINUS, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression n = curl.first();
		if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
		cur = curl.rest();
		LNumber diff = (!cur.atomp()) ? (LNumber)n : LNumber.zero().sub((LNumber)n);
		while(!cur.atomp()){
			curl = (LList)cur;
			n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			diff = diff.sub((LNumber)n);
			cur = curl.rest();
		}
		return diff;
	}
	
	/**minusp
	 */
	public LExpression minusp_(LExpression arg) throws LException{
		this.isArgNumberEqual(MINUSP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		LNumber n = (LNumber)arg1;
		int sign = 0;
		if(n.integerp()) sign = ((LInteger)arg1).value.signum();
		if(n.floatp()) sign = ((LFloatingPoint)arg1).value.signum();
		return (sign < 0) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**more
	 */
	public LExpression more_(LExpression arg) throws LException{
		this.isArgNumberMoreOrEqual(MORE, arg, 1);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression n1 = curl.first();
		if(!n1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n1);
		cur = curl.rest();
		while(!cur.atomp()){
			curl = (LList)cur;
			LExpression n2 = curl.first();
			if(!n2.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n2);
			if(((LNumber)n1).compareTo((LNumber)n2) < 1) return LSymbol.NIL;
			n1 = n2;
			cur = curl.rest();
		}
		return LSymbol.T;
	}
	
	/**not
	 */
	public LExpression not_(LExpression arg) throws LException{
		this.isArgNumberEqual(NOT, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.eq(LSymbol.NIL)) return LSymbol.NIL;
		return LSymbol.T;
	}
	
	/**nth
	 */
	public LExpression nth_(LExpression arg) throws LException{
		this.isArgNumberEqual(NTH, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression argr = argl.rest();
		if(argr== LSymbol.NIL) return LSymbol.NIL;
		LExpression l = ((LList)argr).first();
		if(l.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, l);
		if(!arg1.numberp()) return LSymbol.NIL;
		if(!((LInteger)arg1).integerp()) return LSymbol.NIL;
		return ((LList)l).nth(((LInteger)arg1).value.longValue());		
	}
	
	/**nthcdr
	 */
	public LExpression nthcdr(LExpression arg) throws LException{
		this.isArgNumberEqual(NTHCDR, arg, 2);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		LExpression argr = argl.rest();
		if(argr== LSymbol.NIL) return LSymbol.NIL;
		LExpression l = ((LList)argr).first();
		if(l.atomp()) throw new LException(LError.LIST_ARG_EXPECTED, l);
		if(!arg1.numberp()) return LSymbol.NIL;
		if(!((LInteger)arg1).integerp()) return LSymbol.NIL;
		return ((LList)l).nthcdr(((LInteger)arg1).value.longValue());		
	}
	
	/**null
	 */
	public LExpression null_(LExpression arg) throws LException{
		this.isArgNumberEqual(NULL, arg, 1);
		LList argl = (LList)arg;
		if(argl.first().eq(LSymbol.NIL)) return LSymbol.T;
		else return LSymbol.NIL;
	}
	
	/**numberp
	 */
	public LExpression numberp_(LExpression arg) throws LException{
		this.isArgNumberEqual(NUMBERP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		return (arg1.numberp()) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**oddp
	 */
	public LExpression oddp_(LExpression arg) throws LException{
		this.isArgNumberEqual(EVENP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.INTEGER_ARG_EXPECTED, arg1);
		if(!((LNumber)arg1).integerp()) throw new LException(LError.INTEGER_ARG_EXPECTED, arg1);
		long l = ((LInteger)arg1).value.longValue();
		return (l != ((l / 2) * 2)) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**or
	 */
	public LExpression or_(LExpression arg) throws LException{
		if(arg.atomp()) return LSymbol.NIL;
		LExpression cur = arg;
		LExpression retVal = null;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			retVal = curl.first();
			if(!retVal.eq(LSymbol.NIL)) return retVal;
			cur = curl.rest();
		}
		return LSymbol.NIL;
	}

	/**plus
	 */
	public LExpression plus_(LExpression arg) throws LException{
		LNumber sum = LNumber.zero();
		LExpression cur = arg;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			sum = sum.add((LNumber)n);
			cur = curl.rest();
		}
		return sum;
	}
		
	/**plusp
	 */
	public LExpression plusp_(LExpression arg) throws LException{
		this.isArgNumberEqual(PLUSP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		LNumber n = (LNumber)arg1;
		int sign = 0;
		if(n.integerp()) sign = ((LInteger)arg1).value.signum();
		if(n.floatp()) sign = ((LFloatingPoint)arg1).value.signum();
		return (sign > 0) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**random
	 */
	public LExpression random_(LExpression arg) throws LException{
		this.isArgNumberEqual(RANDOM, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		LNumber n = (LNumber)arg1;
		return n.random();
	}
	
	/**rem
	 */
	public LExpression rem_(LExpression arg) throws LException{
		this.isArgNumberEqual(REM, arg, 2);
		LExpression cur = arg;
		LList curl = (LList)cur;
		LExpression n1 = curl.first();
		LExpression n2 = curl.second();
		if(!n1.numberp()) throw new LException(LError.INTEGER_ARG_EXPECTED, n1);
		if(!n2.numberp()) throw new LException(LError.INTEGER_ARG_EXPECTED, n2);
		if(!((LNumber)n1).integerp()) throw new LException(LError.INTEGER_ARG_EXPECTED, n1);
		if(!((LNumber)n2).integerp()) throw new LException(LError.INTEGER_ARG_EXPECTED, n2);
		return ((LInteger)n1).rem((LInteger)n2);
	}

	/**rest
	 */
	public LExpression rest_(LExpression arg) throws LException{
		this.isArgNumberEqual(REST, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(arg1 == LSymbol.NIL) return LSymbol.NIL;
		if(arg1.atomp()) throw new LException(LError.CAR_CDR_NON_LIST, arg1);
		return ((LList)arg1).rest();
	}
	
	/*
	public LExpression reverse(LExpression arg) throws LException{
		if(arg.atomp()) throw new LException(LError.WRONG_NB_ARGS, "reverse");
		LList argl = (LList)arg;
		if(argl.length() != 1) throw new LException(LError.WRONG_NB_ARGS, "reverse");
		LExpression arg1 = argl.first();
		if(arg1.atomp()) return arg1;
		LList arg1l = (LList)arg1;
		this.reverse(arg1l);
		return arg1l;
	}
	
	public void reverse1(LExpression arg){
		LList argl = (LList)arg;
		if(argl.cdr().atomp()) return;
		((LList)this.reverse1(argl.rest())).append(argl.first());
	}
	*/
	//* define reverse
	public LExpression reverse_(LExpression arg) throws LException{
		return LSymbol.NIL;
	}
	
	/**round
	 */
	public LExpression round_(LExpression arg) throws LException{
		this.isArgNumberEqual(ROUND, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		return ((LNumber)arg1).round();
	}
	
	/**symbolp
	 */
	public LExpression symbolp_(LExpression arg) throws LException{
		this.isArgNumberEqual(SYMBOLP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		return (arg1.symbolp()) ? LSymbol.T : LSymbol.NIL;
	}
	
	/**times
	 */
	public LExpression times_(LExpression arg) throws LException{
		LNumber product = LNumber.one();
		LExpression cur = arg;
		while(!cur.atomp()){
			LList curl = (LList)cur;
			LExpression n = curl.first();
			if(!n.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, n);
			product = product.mul((LNumber)n);
			cur = curl.rest();
		}
		return product;
	}
	
	/**zerop
	 */
	public LExpression zerop_(LExpression arg) throws LException{
		this.isArgNumberEqual(NUMBERP, arg, 1);
		LList argl = (LList)arg;
		LExpression arg1 = argl.first();
		if(!arg1.numberp()) throw new LException(LError.NUMBER_ARG_EXPECTED, arg1);
		LNumber n = (LNumber)arg1;
		int sign = n.signum();
		return (sign == 0) ? LSymbol.T : LSymbol.NIL;
	}
	
	
}
