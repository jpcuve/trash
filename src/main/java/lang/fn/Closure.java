package lang.fn;

public class Closure extends Procedure {
	private Expression params;
	private Expression body;
	private Environment env;
	
	public Closure(Expression params, Expression body, Environment env) throws FnException {
		this.params = params;
		this.env = env;
		this.body = (body.rest() == Empty.EMPTY) ? body.first() : Symbol.BEGIN.cons(body);
		// System.out.println("Closure created: params=" + params.write() + ", body=" + body.write());
	}

	public boolean isPrimitive(){ return false; }
	public boolean isClosure(){ return true; }
	public boolean isSpecial(){ return false; }
	public boolean isTail(){ return false; }
	
	public Expression apply(Interpreter ip, Expression arg, Environment dummy) throws FnException{
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