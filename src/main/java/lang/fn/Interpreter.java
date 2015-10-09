package lang.fn;

public class Interpreter {
	public static final Symbol[] BUILT_IN = {
		Symbol.QUOTE, //0
		Symbol.BEGIN, //1
		Symbol.DEFINE, //2
		Symbol.SET_EXCLAMATION, //3
		Symbol.LAMBDA, //4
		Symbol.IF, //5
		Symbol.COND, //6
		Symbol.CASE, //7
		Symbol.DO, //8
		Symbol.AND, //9
		Symbol.OR, //10
		Symbol.LET, //11
		Symbol.LET_STAR, //12
		Symbol.LETREC, //13
		Symbol.QUASIQUOTE, //14
		Symbol.DELAY, //15
		Symbol.ELSE,
		Symbol.RECEIPT
	};
	
	public Symbol[] getBuiltIns() {
		return(BUILT_IN);
	}

	private static int offset = 0;
	
	public Expression eval(Expression form, Environment env) throws FnException {
		while(true){
			if(form.isSymbol()){
				Symbol sym = (Symbol)form;
				Expression z = env.lookup(sym);
				if (z == Void.VOID)	throw new FnException(Error.UNBOUND_SYMBOL, sym);
				return(z);
			}
			if(!form.isPair()) {
				return(form);
			}
			Expression op = form.first();
			Expression args = form.rest();
			int fn;
			for (fn = 0; fn < BUILT_IN.length && op != BUILT_IN[fn]; fn++);
			if (fn < BUILT_IN.length) {
				switch (fn) {
					case 0: return(args.first());
					case 1:
						form = prog(args, env);
						break;
					case 2: return(define_(args, env));
					case 3: return(set_exclamation_(args, env));
					case 4: return(new Closure(args.first(), args.rest(), env));
					case 5:
						form = (eval(args.first(), env) != Bool.FALSE) ? args.second() : args.third();
						break;
					case 6:
						form = cond_(args, env);
						break;
					case 7:
						form = case_(args, env);
						break;
					case 8:
						env = parInit(args.first(), env);
						while(eval(args.second().first(), env) == Bool.FALSE){
							for(Expression cur = args.rest().rest(); cur.isPair(); cur = cur.rest()) eval(cur.first(), env);
							env = parStep(args.first(), env);
						}
						form = prog(args.second().rest(), env);
						break;
					case 9:
						return(and_(args, env));
					case 10:
						return(or_(args, env));
					case 11:
						if(args.first().isPair()){
							env = parInit(args.first(), env);
							form = prog(args.rest(), env);
						}else{
							env = parInit(args.second(), env);
							define_(args.first().cons(firsts(args.second())).cons(args.rest().rest()), env);
							form = prog(args.rest().rest(), env);
						}
						break;
					case 12:
						env = seqInit(args.first(), env);
						form = prog(args.rest(), env);
						break;
					case 13:
						env = recInit(args.first(), env);
						form = prog(args.rest(), env);
						break;
					case 14:
						return(quasiquote_(op.cons(args), env, 0));
					case 15:
						return(new Promise(args.first()));
					default:
						throw new FnException(Error.SYNTAX, form);
				}
			} else {
				op = eval(op, env);
				if(!op.isProcedure()) throw new FnException(Error.PROCEDURE_EXPECTED, op);
				Procedure proc = (Procedure)op;
				Expression eargs = evalArgl(args, env);
				if(proc.isClosure()) {
					Closure clos = (Closure)proc;
					form = clos.getBody();
					env = new Environment(clos.getEnvironment(), clos.getParameters(), eargs);
				}else if(proc.isPrimitive()){
					Primitive f = (Primitive)proc;
					return(f.apply(this, eargs, env));
				}
			}
		}
	}

	private Expression evalArgl(Expression arg, Environment env) throws FnException{
		if(!arg.isPair()){
			if(arg == Empty.EMPTY) return arg;
			throw new FnException(Error.LIST_EXPECTED, arg);
		}
		Pair argl = (Pair)arg;
		return eval(argl.first(), env).cons(evalArgl(argl.rest(), env));
	}

	private Expression prog(Expression arg, Environment env) throws FnException{
		if(!arg.isPair()) return arg;
		Pair curl = (Pair)arg;
		while(curl.rest().isPair()){
			eval(curl.first(), env);
			curl = (Pair)curl.rest();
		}
		return curl.first();
	}

	private Expression firsts(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(arg.first().first().cons(firsts(arg.rest())));
	}

	private Expression rests(Expression arg) throws FnException {
		if (arg == Empty.EMPTY) return(arg);
		return(arg.first().rest().cons(rests(arg.rest())));
	}

	private Environment parInit(Expression arg, Environment env) throws FnException{
		Expression parl = Empty.EMPTY;
		Expression vall = Empty.EMPTY;
		for(Expression init = arg; init.isPair(); init = init.rest()){
			Expression pair = init.first();
			parl = pair.first().cons(parl);
			vall = eval(pair.second(), env).cons(vall);
		}
		return new Environment(env, parl, vall);
	}

	private Environment seqInit(Expression arg, Environment env) throws FnException{
		Environment nenv = new Environment(env);
		for(Expression init = arg; init.isPair(); init = init.rest()){
			Expression pair = init.first();
			nenv.define(pair.first(), eval(pair.second(), nenv));
		}
		return nenv;
	}

	private Environment recInit(Expression arg, Environment env) throws FnException{
		Environment nenv = new Environment(env);
		for(Expression init = arg; init.isPair(); init = init.rest()){
			Expression pair = init.first();
			nenv.define(pair.first(), Void.VOID);
		}
		for(Expression init = arg; init.isPair(); init = init.rest()){
			Expression pair = init.first();
			nenv.bind(pair.first(), eval(pair.second(), nenv));
		}
		return nenv;
	}

	private Environment parStep(Expression arg, Environment env) throws FnException{
		Expression parl = Empty.EMPTY;
		Expression vall = Empty.EMPTY;
		for(Expression init = arg; init.isPair(); init = init.rest()){
			Expression pair = init.first();
			parl = pair.first().cons(parl);
			vall = ((pair.third() != Empty.EMPTY) ? eval(pair.third(), env) : eval(pair.first(), env)).cons(vall);
		}
		return new Environment(env, parl, vall);
	}


	// language definition

	private Expression and_(Expression arg, Environment env) throws FnException {
		Expression retVal = Bool.TRUE;
		if(arg.isPair()) {
			for(Expression cur = arg; cur.isPair(); cur = cur.rest()) {
				retVal = eval(cur.first(), env);
				if(retVal == Bool.FALSE) return Bool.FALSE;
			}
		}
		return retVal;
	}

	private Expression case_(Expression arg, Environment env) throws FnException {
		Expression key = eval(arg.first(), env);
		for(Expression cur = arg.rest(); cur.isPair(); cur = cur.rest()) {
			Expression cur1 = cur.first();
			Expression k = cur1.first();
			if(cur.rest() == Empty.EMPTY && k == Symbol.ELSE) return prog(cur1.rest(), env);
			for( ; k.isPair(); k = k.rest()) if(k.first().isEqv(key)) return prog(cur1.rest(), env);
		}
		return Void.VOID;
	}

	private Expression cond_(Expression arg, Environment env) throws FnException{
		for(Expression cur = arg; cur.isPair(); cur = cur.rest()){
			Expression cur1 = cur.first();
			Expression test = (cur.rest() == Empty.EMPTY && cur1.first() == Symbol.ELSE) ? Bool.TRUE : eval(cur1.first(), env);
			if(test != Bool.FALSE){
				Expression retVal = null;
				Expression body = cur1.rest();
				if(body.first() == Symbol.RECEIPT){
					if(body.rest().rest() != Empty.EMPTY ) throw new FnException(Error.WRONG_NB_ARGS, Symbol.RECEIPT );
					retVal = body.second().cons(Symbol.QUOTE.cons(test.cons(Empty.EMPTY)).cons(Empty.EMPTY));
				}else{
					retVal = prog(body, env);
				}
				return retVal;
			}
		}
		return Void.VOID;
	}

	private Expression define_(Expression arg, Environment env) throws FnException{
		Expression arg1 = arg.first();
		if(arg1.isPair()){
			return env.define(arg1.first(), new Closure(arg1.rest(), arg.rest(), env));
		}else{
			if(arg.rest().rest() != Empty.EMPTY) throw new FnException(Error.WRONG_NB_ARGS, Symbol.DEFINE );
			return env.define(arg1, eval(arg.rest().first(), env));
		}
	}


	/*
	private Babel delay_(Babel arg) throws FnException {
		throw new FnException(Error.UNIMPLEMENTED_FEATURE, DELAY);
	}
	*/

	private Expression do_(Expression arg, Environment env) throws FnException {
		Expression iter = arg.first();
		Expression test = arg.second();
		Expression body = arg.rest().rest();
		for(;;) {
			if(eval(test.first(), env) != Bool.FALSE) return prog(test.rest(), env);
		}
	}

	private Expression quasiquote_(Expression arg, Environment env, int level) throws FnException{
		if(!arg.isPair()) return arg;
		if(arg.first() == Symbol.QUASIQUOTE) return ((level == 0) ? quasiquote_(arg.second(), env, level + 1) : quasiquote_1(arg, env, level + 1));
		if(arg.first() == Symbol.UNQUOTE) return ((level == 1) ? eval(arg.second(), env) : quasiquote_1(arg, env, level - 1));
		if(arg.first() == Symbol.UNQUOTE_SPLICING) return ((level == 1) ? Pair.list(Void.VOID , eval(arg.second(), env)) : quasiquote_1(arg, env, level - 1));
		return quasiquote_1(arg, env, level);
	}

	private Expression quasiquote_1(Expression arg, Environment env, int level) throws FnException {
		Expression car = quasiquote_(arg.first(), env, level);
		Expression cdr = quasiquote_(arg.rest(), env, level);
		if(car.isPair() && car.first() == Void.VOID ){
			return car.rest().first().append(cdr);
		}
		return car.cons(cdr);
	}

	private Expression or_(Expression arg, Environment env) throws FnException {
		Expression retVal = Bool.FALSE;
		if (arg.isPair()) {
			for(Expression cur = arg; cur.isPair(); cur = cur.rest()){
				retVal = eval(cur.first(), env);
				if(retVal != Bool.FALSE) return retVal;
			}
		}
		return retVal;
	}

	private Expression set_exclamation_(Expression arg, Environment env) throws FnException{
		if(arg.rest().rest() != Empty.EMPTY) throw new FnException(Error.WRONG_NB_ARGS, Symbol.SET_EXCLAMATION );
		Expression sym = arg.first();
		Expression value = eval(arg.rest().first(), env);
		if(!env.bind(sym, value)) throw new FnException(Error.UNBOUND_SYMBOL, sym);
		return Void.VOID;
	}
}
				
