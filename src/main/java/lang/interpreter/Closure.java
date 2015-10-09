package lang.interpreter;

public class Closure extends Procedure {
	private Expression params;
	private Expression body;
	private Environment env;
	
	public Closure(Expression params, Expression body, Environment env) throws InterpreterException {
		this.params = params;
		this.env = env;
		this.body = (body.rest() == Nil.NIL) ? body.first() : Symbol.BEGIN.cons(body);
	}

	public Expression apply(Interpreter ip, Expression arg, Environment dummy) throws InterpreterException{
		return ip.eval(body, new Environment(env, params, arg));
	}
	
	public Expression getBody(){
		return body;
	}
	
	public Expression getParameters(){
		return params;
	}
	
	public Environment getEnvironment() {
		return(env);
	}
		
}