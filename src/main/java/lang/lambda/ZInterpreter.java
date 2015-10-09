package lang.lambda;

public class ZInterpreter {
	
	private static int offset = 0;
	
	private void enter(ZExpression z){
		for(int i = 0; i < offset; i++) System.out.print(' ');
		System.out.println("Entering eval, form=" + z.write());
		offset++;
	}
	
	private void exit(ZExpression z){
		offset--;
		for(int i = 0; i < offset; i++) System.out.print(' ');
		System.out.println("Exiting eval, ret=" + ((z == null) ? "<nothing>" : z.write()));
	}
	
	public ZExpression eval(ZExpression form, ZEnvironment env) throws ZException {
		// this.enter(form);
		while(true){
			// System.out.println("-----> form=" + form.write());
			if(form.isSymbol()){
				ZSymbol sym = (ZSymbol)form;
				ZExpression z = env.lookup(sym);
				if(z == ZEmpty.VOID ) throw new ZException(ZError.UNBOUND_SYMBOL, sym);
				// this.exit(z);
				return z;
			}else if(!form.isPair()){
				// this.exit(form);
				return form;
			}else{
				ZExpression op = form.first();
				ZExpression args = form.rest();
				if(op == ZSymbol.QUOTE){
					return args.first();
				}else if(op == ZSymbol.BEGIN){
					form = this.prog(args, env);
				}else if(op == ZSymbol.DEFINE){
					return this.define_(args, env);
				}else if(op == ZSymbol.SET_EXCLAMATION){
					return this.set_exclamation_(args, env);
				}else if(op == ZSymbol.LAMBDA){
					return new ZClosure(args.first(), args.rest(), env);
				}else if(op == ZSymbol.IF){
					form = (this.eval(args.first(), env) != ZBoolean.FALSE) ? args.second() : args.third();
				}else if(op == ZSymbol.COND){
					form = this.cond_(args, env);
				}else if(op == ZSymbol.CASE){
					form = this.case_(args, env);
				}else if(op == ZSymbol.DO){
					env = this.parInit(args.first(), env);
					while(this.eval(args.second().first(), env) == ZBoolean.FALSE){
						for(ZExpression cur = args.rest().rest(); cur.isPair(); cur = cur.rest()) this.eval(cur.first(), env);
						env = this.parStep(args.first(), env);
					}
					form = this.prog(args.second().rest(), env);
				}else if(op == ZSymbol.AND){
					form = this.and_(args, env);
				}else if(op == ZSymbol.OR){
					form = this.or_(args, env);
				}else if(op == ZSymbol.LET){
					if(args.first().isPair()){
						env = this.parInit(args.first(), env);
						form = this.prog(args.rest(), env);
					}else{
						env = this.parInit(args.second(), env);
						this.define_(args.first().cons(this.firsts(args.second())).cons(args.rest().rest()), env);
						form = this.prog(args.rest().rest(), env);
					}
				}else if(op == ZSymbol.LET_STAR){
					env = this.seqInit(args.first(), env);
					form = this.prog(args.rest(), env);
				}else if(op == ZSymbol.LETREC){
					env = this.recInit(args.first(), env);
					form = this.prog(args.rest(), env);
				}else if(op == ZSymbol.QUASIQUOTE){
					return this.quasiquote_(op.cons(args), env, 0);
				}else{
					op = this.eval(op, env);
					if(!op.isProcedure()) throw new ZException(ZError.PROCEDURE_EXPECTED, op);
					ZProcedure proc = (ZProcedure)op;
					if(proc.isClosure()){
						ZClosure clos = (ZClosure)proc;
						form = clos.getBody();
						env = new ZEnvironment(clos.getEnvironment(), clos.getParameters(), this.evalArgl(args, env));
					}else if(proc.isPrimitive()){
						ZPrimitive f = (ZPrimitive)proc;
						return f.apply(this, this.evalArgl(args, env));
					}
				}
			}
		}
	}
			
	// support
	
	private ZExpression evalArgl(ZExpression arg, ZEnvironment env) throws ZException{
		if(!arg.isPair()){
			if(arg == ZEmpty.EMPTY) return arg;
			throw new ZException(ZError.LIST_EXPECTED, arg);
		}
		ZPair argl = (ZPair)arg;
		return this.eval(argl.first(), env).cons(this.evalArgl(argl.rest(), env));
	}
	
	private ZExpression prog(ZExpression arg, ZEnvironment env) throws ZException{
		if(!arg.isPair()) return arg;
		ZPair curl = (ZPair)arg;
		while(curl.rest().isPair()){
			this.eval(curl.first(), env);
			curl = (ZPair)curl.rest();
		}
		return curl.first();
	}
	
	private ZExpression firsts(ZExpression arg) throws ZException {
		if(arg == ZEmpty.EMPTY) return arg;
		return arg.first().first().cons(this.firsts(arg.rest()));
	}

	private ZEnvironment parInit(ZExpression arg, ZEnvironment env) throws ZException{
		ZExpression parl = ZEmpty.EMPTY;
		ZExpression vall = ZEmpty.EMPTY;
		for(ZExpression init = arg; init.isPair(); init = init.rest()){
			ZExpression pair = init.first();
			parl = pair.first().cons(parl);
			vall = this.eval(pair.second(), env).cons(vall);
		}
		return new ZEnvironment(env, parl, vall);
	}
	
	private ZEnvironment seqInit(ZExpression arg, ZEnvironment env) throws ZException{
		ZEnvironment nenv = new ZEnvironment(env);
		for(ZExpression init = arg; init.isPair(); init = init.rest()){
			ZExpression pair = init.first();
			nenv.define(pair.first(), this.eval(pair.second(), nenv));
		}
		return nenv;
	}
	
	private ZEnvironment recInit(ZExpression arg, ZEnvironment env) throws ZException{
		ZEnvironment nenv = new ZEnvironment(env);
		for(ZExpression init = arg; init.isPair(); init = init.rest()){
			ZExpression pair = init.first();
			nenv.define(pair.first(), ZEmpty.VOID);
		}
		for(ZExpression init = arg; init.isPair(); init = init.rest()){
			ZExpression pair = init.first();
			nenv.bind(pair.first(), this.eval(pair.second(), nenv));
		}
		return nenv;
	}
	
	private ZEnvironment parStep(ZExpression arg, ZEnvironment env) throws ZException{
		ZExpression parl = ZEmpty.EMPTY;
		ZExpression vall = ZEmpty.EMPTY;
		for(ZExpression init = arg; init.isPair(); init = init.rest()){
			ZExpression pair = init.first();
			parl = pair.first().cons(parl);
			vall = ((pair.third() != ZEmpty.EMPTY) ? this.eval(pair.third(), env) : pair.first()).cons(vall);
		}
		return new ZEnvironment(env, parl, vall);
	}
	
	
	// language definition
	
	private ZExpression and_(ZExpression arg, ZEnvironment env) throws ZException{
		if(!arg.isPair()) return ZBoolean.TRUE;
		ZExpression cur = arg;
		for(; cur.rest().isPair(); cur = cur.rest()){
			if(this.eval(cur.first(), env) == ZBoolean.FALSE) return ZBoolean.FALSE;
		}
		return cur.first();
	}
	
	private ZExpression case_(ZExpression arg, ZEnvironment env) throws ZException{
		ZExpression key = this.eval(arg.first(), env);
		for(ZExpression cur = arg.rest(); cur.isPair(); cur = cur.rest()){
			ZExpression cur1 = cur.first();
			ZExpression k = cur1.first();
			if(cur.rest() == ZEmpty.EMPTY && k == ZSymbol.ELSE) return this.prog(cur1.rest(), env);
			for( ; k.isPair(); k = k.rest()) if(k.first().isEqv(key)) return this.prog(cur1.rest(), env);
		}
		return ZEmpty.VOID;
	}
	
	private ZExpression cond_(ZExpression arg, ZEnvironment env) throws ZException{
		for(ZExpression cur = arg; cur.isPair(); cur = cur.rest()){
			ZExpression cur1 = cur.first();
			ZExpression test = (cur.rest() == ZEmpty.EMPTY && cur1.first() == ZSymbol.ELSE) ? ZBoolean.TRUE : this.eval(cur1.first(), env);
			if(test != ZBoolean.FALSE){
				ZExpression retVal = null;
				ZExpression body = cur1.rest();
				if(body.first() == ZSymbol.RECEIPT){
					if(body.rest().rest() != ZEmpty.EMPTY ) throw new ZException(ZError.WRONG_NB_ARGS, ZSymbol.RECEIPT );
					retVal = body.second().cons(ZSymbol.QUOTE.cons(test.cons(ZEmpty.EMPTY)).cons(ZEmpty.EMPTY));
				}else{
					retVal = this.prog(body, env);
				}
				return retVal;
			}
		}
		return ZEmpty.VOID;
	}
	
	private ZExpression define_(ZExpression arg, ZEnvironment env) throws ZException{
		ZExpression arg1 = arg.first();
		if(arg1.isPair()){
			return env.define(arg1.first(), new ZClosure(arg1.rest(), arg.rest(), env));
		}else{
			if(arg.rest().rest() != ZEmpty.EMPTY) throw new ZException(ZError.WRONG_NB_ARGS, ZSymbol.DEFINE );
			return env.define(arg1, this.eval(arg.rest().first(), env));
		}
	}
	
	/*	
	private ZExpression define_syntax_() throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, DEFINE_SYNTAX);
	}
	
	private ZExpression delay_(ZExpression arg) throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, DELAY);
	}
	*/
	
	private ZExpression do_(ZExpression arg, ZEnvironment env) throws ZException{
		ZExpression iter = arg.first();
		ZExpression test = arg.second();
		ZExpression body = arg.rest().rest();
		for(;;){
			if(this.eval(test.first(), env) != ZBoolean.FALSE) return this.prog(test.rest(), env);
			
		}
	}
	
	/*	
	private ZExpression let_syntax_() throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, LET_SYNTAX);
	}
	
	private ZExpression letrec_syntax_() throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, LETREC_SYNTAX);
	}
	*/

	private ZExpression quasiquote_(ZExpression arg, ZEnvironment env, int level) throws ZException{
		// if (arg.isVector()) ...
		if(!arg.isPair()) return arg;
		if(arg.first() == ZSymbol.QUASIQUOTE) return ((level == 0) ? this.quasiquote_(arg.second(), env, level + 1) : this.quasiquote_1(arg, env, level + 1));
		if(arg.first() == ZSymbol.UNQUOTE) return ((level == 1) ? this.eval(arg.second(), env) : this.quasiquote_1(arg, env, level - 1));
		if(arg.first() == ZSymbol.UNQUOTE_SPLICING) return ((level == 1) ? ZPair.list(ZEmpty.VOID , this.eval(arg.second(), env)) : this.quasiquote_1(arg, env, level - 1));
		return this.quasiquote_1(arg, env, level);		
	}
	
	private ZExpression quasiquote_1(ZExpression arg, ZEnvironment env, int level) throws ZException {
		ZExpression car = this.quasiquote_(arg.first(), env, level);
		ZExpression cdr = this.quasiquote_(arg.rest(), env, level);
		if(car.isPair() && car.first() == ZEmpty.VOID ){
			return car.rest().first().append(cdr);
		}
		return car.cons(cdr);
	}
	
	private ZExpression or_(ZExpression arg, ZEnvironment env) throws ZException{
		if(!arg.isPair()) return ZBoolean.FALSE;
		ZExpression cur = arg;
		for(; cur.rest().isPair(); cur = cur.rest()){
			ZExpression retVal = this.eval(cur.first(), env);
			if(retVal != ZBoolean.FALSE) return retVal;
		}
		return cur.first();
	}
	
	private ZExpression set_exclamation_(ZExpression arg, ZEnvironment env) throws ZException{
		if(arg.rest().rest() != ZEmpty.EMPTY) throw new ZException(ZError.WRONG_NB_ARGS, ZSymbol.SET_EXCLAMATION );
		ZExpression sym = arg.first();
		ZExpression value = this.eval(arg.rest().first(), env);
		if(!env.bind(sym, value)) throw new ZException(ZError.UNBOUND_SYMBOL, sym);
		return ZEmpty.VOID;
	}

	private ZExpression syntax_rules_() throws ZException {
		throw new ZException(ZError.UNIMPLEMENTED_FEATURE, ZSymbol.SYNTAX_RULES);
	}
	
	/*
	private ZExpression qqCarCdr(ZExpression z, ZEnvironment env, int level) throws ZException {
		ZExpression car = this.qqExpander(z.first(), env, level);
		ZExpression cdr = this.qqExpander(z.rest(), env, level);
		if(car.isPair() && car.first() == ZEmpty.VOID ){
			return car.rest().first().append(cdr);
		}
		return car.cons(cdr);
	}
	
	private ZExpression qqExpander(ZExpression exp, ZEnvironment env, int level) throws ZException {
		// System.out.println("Entering expander=" + exp.write() + ", level=" + level);
		// if(exp.isVector()) return this.qqExpander(((ZVector)exp).toList());
		if(!exp.isPair()) return exp;
		ZExpression op = exp.first();
		if(op == ZSymbol.QUASIQUOTE){
			level++;
			ZExpression retVal = (level == 1) ? this.qqExpander(exp.second(), env, level) : this.qqCarCdr(exp, env, level);
			level--;
			return retVal;
		}
		if(op == ZSymbol.UNQUOTE || op == ZSymbol.UNQUOTE_SPLICING){
			level--;
			ZExpression retVal =	(level == 0) 
									? ((op == ZSymbol.UNQUOTE_SPLICING) ?  : this.eval(exp.second(), env)) 
									: this.qqCarCdr(exp, env, level);
			level++;
			return retVal;
		}
		return this.qqCarCdr(exp, env, level);
	}
	*/

	
}
