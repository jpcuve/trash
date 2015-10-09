package lang.interpreter;

public class Interpreter {

	public Expression eval(Expression form, Environment env) throws InterpreterException {
		while(true){
			if(form instanceof Symbol){
				Symbol sym = (Symbol)form;
				Expression z = env.lookup(sym);
				if (z == null) throw new InterpreterException(Error.UNBOUND_SYMBOL, sym);
				return(z);
			} else if (!(form instanceof Pair)) {
				return(form);
			} else {
                Expression op = form.first();
                Expression args = form.rest();
                if (op == Symbol.QUOTE) {
                    return args.first();
                } else if (op == Symbol.BEGIN) {
                    form = prog(args, env);
                } else if (op == Symbol.DEFINE) {
                    return define_(args, env);
                } else if (op == Symbol.SETEXCLAMATION) {
                    return set_exclamation_(args, env);
                } else if (op == Symbol.LAMBDA) {
                    return new Closure(args.first(), args.rest(), env);
                } else if (op == Symbol.IF) {
                    form = (eval(args.first(), env) != Bool.FALSE) ? args.second() : args.third();
                } else if (op == Symbol.COND) {
                    form = cond_(args, env);
                } else if (op == Symbol.CASE) {
                    form = case_(args, env);
                } else if (op == Symbol.DO) {
                    env = parInit(args.first(), env);
                    while(eval(args.second().first(), env) == Bool.FALSE){
                        for(Expression cur = args.rest().rest(); cur instanceof Pair; cur = cur.rest()) eval(cur.first(), env);
                        env = parStep(args.first(), env);
                    }
                    form = prog(args.second().rest(), env);
                } else if (op == Symbol.AND) {
                    return and_(args, env);
                } else if (op == Symbol.OR) {
                    return or_(args, env);
                } else if (op == Symbol.LET) {
                    if(args.first() instanceof Pair){
                        env = parInit(args.first(), env);
                        form = prog(args.rest(), env);
                    }else{
                        env = parInit(args.second(), env);
                        define_(args.first().cons(firsts(args.second())).cons(args.rest().rest()), env);
                        form = prog(args.rest().rest(), env);
                    }
                } else if (op == Symbol.LETSTAR) {
                    env = seqInit(args.first(), env);
                    form = prog(args.rest(), env);
                } else if (op == Symbol.LETREC) {
                    env = recInit(args.first(), env);
                    form = prog(args.rest(), env);
                } else if (op == Symbol.QUASIQUOTE) {
                    return quasiquote_(op.cons(args), env, 0);
                } else {
                    op = eval(op, env);
                    if(!(op instanceof Procedure)) throw new InterpreterException(Error.PROCEDURE_EXPECTED, op);
                    Procedure proc = (Procedure)op;
                    Expression eargs = evalArgl(args, env);
                    if(proc instanceof Closure) {
                        Closure clos = (Closure)proc;
                        form = clos.getBody();
                        env = new Environment(clos.getEnvironment(), clos.getParameters(), eargs);
                    }else if(proc instanceof Primitive){
                        Primitive f = (Primitive)proc;
                        return(f.apply(this, eargs, env));
                    }
                }
            }
         }
	}

	private Expression evalArgl(Expression arg, Environment env) throws InterpreterException{
		if(!(arg instanceof Pair)){
			if(arg == Nil.NIL) return arg;
			throw new InterpreterException(Error.LIST_EXPECTED, arg);
		}
		Pair argl = (Pair)arg;
		return eval(argl.first(), env).cons(evalArgl(argl.rest(), env));
	}

	private Expression prog(Expression arg, Environment env) throws InterpreterException{
		if(!(arg instanceof Pair)) return arg;
		Pair curl = (Pair)arg;
		while(curl.rest() instanceof Pair){
			eval(curl.first(), env);
			curl = (Pair)curl.rest();
		}
		return curl.first();
	}

	private Expression firsts(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(arg.first().first().cons(firsts(arg.rest())));
	}

	private Expression rests(Expression arg) throws InterpreterException {
		if (arg == Nil.NIL) return(arg);
		return(arg.first().rest().cons(rests(arg.rest())));
	}

	private Environment parInit(Expression arg, Environment env) throws InterpreterException{
		Expression parl = Nil.NIL;
		Expression vall = Nil.NIL;
		for(Expression init = arg; init instanceof Pair; init = init.rest()){
			Expression pair = init.first();
			parl = pair.first().cons(parl);
			vall = eval(pair.second(), env).cons(vall);
		}
		return new Environment(env, parl, vall);
	}

	private Environment seqInit(Expression arg, Environment env) throws InterpreterException{
		Environment nenv = new Environment(env);
		for(Expression init = arg; init instanceof Pair; init = init.rest()){
			Expression pair = init.first();
			nenv.define(pair.first(), eval(pair.second(), nenv));
		}
		return nenv;
	}

	private Environment recInit(Expression arg, Environment env) throws InterpreterException{
		Environment nenv = new Environment(env);
		for(Expression init = arg; init instanceof Pair; init = init.rest()){
			Expression pair = init.first();
			nenv.define(pair.first(), Nil.NIL);
		}
		for(Expression init = arg; init instanceof Pair; init = init.rest()){
			Expression pair = init.first();
			nenv.bind(pair.first(), eval(pair.second(), nenv));
		}
		return nenv;
	}

	private Environment parStep(Expression arg, Environment env) throws InterpreterException{
		Expression parl = Nil.NIL;
		Expression vall = Nil.NIL;
		for(Expression init = arg; init instanceof Pair; init = init.rest()){
			Expression pair = init.first();
			parl = pair.first().cons(parl);
			vall = ((pair.third() != Nil.NIL) ? eval(pair.third(), env) : eval(pair.first(), env)).cons(vall);
		}
		return new Environment(env, parl, vall);
	}


	// language definition

	private Expression and_(Expression arg, Environment env) throws InterpreterException {
		Expression retVal = Bool.TRUE;
		if(arg instanceof Pair) {
			for(Expression cur = arg; cur instanceof Pair; cur = cur.rest()) {
				retVal = eval(cur.first(), env);
				if(retVal == Bool.FALSE) return Bool.FALSE;
			}
		}
		return retVal;
	}

	private Expression case_(Expression arg, Environment env) throws InterpreterException {
		Expression key = eval(arg.first(), env);
		for(Expression cur = arg.rest(); cur instanceof Pair; cur = cur.rest()) {
			Expression cur1 = cur.first();
			Expression k = cur1.first();
			if(cur.rest() == Nil.NIL && k == Symbol.ELSE) return prog(cur1.rest(), env);
			for( ; k instanceof Pair; k = k.rest()) if(k.first().isEqv(key)) return prog(cur1.rest(), env);
		}
		return null;
	}

	private Expression cond_(Expression arg, Environment env) throws InterpreterException{
		for(Expression cur = arg; cur instanceof Pair; cur = cur.rest()){
			Expression cur1 = cur.first();
			Expression test = (cur.rest() == Nil.NIL && cur1.first() == Symbol.ELSE) ? Bool.TRUE : eval(cur1.first(), env);
			if(test != Bool.FALSE){
				Expression retVal = Nil.NIL;
				Expression body = cur1.rest();
				if(body.first() == Symbol.RECEIPT){
					if(body.rest().rest() != Nil.NIL) throw new InterpreterException(Error.WRONG_NB_ARGS, Symbol.RECEIPT );
					retVal = body.second().cons(Symbol.QUOTE.cons(test.cons(Nil.NIL)).cons(Nil.NIL));
				}else{
					retVal = prog(body, env);
				}
				return retVal;
			}
		}
		return null;
	}

	private Expression define_(Expression arg, Environment env) throws InterpreterException{
		Expression arg1 = arg.first();
		if(arg1 instanceof Pair){
			return env.define(arg1.first(), new Closure(arg1.rest(), arg.rest(), env));
		}else{
			if(arg.rest().rest() != Nil.NIL) throw new InterpreterException(Error.WRONG_NB_ARGS, Symbol.DEFINE );
			return env.define(arg1, eval(arg.rest().first(), env));
		}
	}

	private Expression do_(Expression arg, Environment env) throws InterpreterException {
		Expression iter = arg.first();
		Expression test = arg.second();
		Expression body = arg.rest().rest();
		for(;;) {
			if(eval(test.first(), env) != Bool.FALSE) return prog(test.rest(), env);
		}
	}

    private static final Symbol TAG = new Symbol();

	private Expression quasiquote_(Expression arg, Environment env, int level) throws InterpreterException{
		if(!(arg instanceof Pair)) return arg;
		if(arg.first() == Symbol.QUASIQUOTE) return ((level == 0) ? quasiquote_(arg.second(), env, level + 1) : quasiquote_1(arg, env, level + 1));
		if(arg.first() == Symbol.UNQUOTE) return ((level == 1) ? eval(arg.second(), env) : quasiquote_1(arg, env, level - 1));
		if(arg.first() == Symbol.UNQUOTESPLICING) return ((level == 1) ? Pair.list(TAG , eval(arg.second(), env)) : quasiquote_1(arg, env, level - 1));
		return quasiquote_1(arg, env, level);
	}

	private Expression quasiquote_1(Expression arg, Environment env, int level) throws InterpreterException {
		Expression car = quasiquote_(arg.first(), env, level);
		Expression cdr = quasiquote_(arg.rest(), env, level);
		if(car instanceof Pair && car.first() == TAG ){
			return car.rest().first().append(cdr);
		}
		return car.cons(cdr);
	}

	private Expression or_(Expression arg, Environment env) throws InterpreterException {
		Expression retVal = Bool.FALSE;
		if (arg instanceof Pair) {
			for(Expression cur = arg; cur instanceof Pair; cur = cur.rest()){
				retVal = eval(cur.first(), env);
				if(retVal != Bool.FALSE) return retVal;
			}
		}
		return retVal;
	}

	private Expression set_exclamation_(Expression arg, Environment env) throws InterpreterException{
		if(arg.rest().rest() != Nil.NIL) throw new InterpreterException(Error.WRONG_NB_ARGS, Symbol.SETEXCLAMATION );
		Expression sym = arg.first();
		Expression value = eval(arg.rest().first(), env);
		if(!env.bind(sym, value)) throw new InterpreterException(Error.UNBOUND_SYMBOL, sym);
		return null;
	}
}
				
